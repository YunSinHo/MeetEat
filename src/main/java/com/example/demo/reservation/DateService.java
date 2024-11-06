package com.example.demo.reservation;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.demo.owner.store.Store;
import com.example.demo.owner.store.StoreCombineDTO;
import com.example.demo.owner.store.StoreService;
import com.example.demo.owner.store.management.ManagementService;
import com.example.demo.owner.store.management.StoreBasic;
import com.example.demo.owner.store.management.StoreTable;

import jakarta.annotation.PostConstruct;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class DateService {

    private final SRDRepository srdRepository;
    private final StoreService storeService;
    private final ReservationService reservationService;
    private final ManagementService managementService;

    public DateService(SRDRepository srdRepository, StoreService storeService, ReservationService reservationService,
            ManagementService managementService) {
        this.srdRepository = srdRepository;
        this.storeService = storeService;
        this.reservationService = reservationService;
        this.managementService = managementService;

    }

    // 12시가 지나면 날짜 업데이트
    @Scheduled(cron = "0 0 0 * * *")
    public void updateDates() {
        List<Store> stores = storeService.findAll();

        LocalDate today = LocalDate.now();
        LocalDate threeWeeksLater = today.plusWeeks(3);

        // 어제 날짜 삭제 시 다른 조건이 없을 때만 삭제
        LocalDate yesterday = today.minusDays(1);
        srdRepository.deleteAllByDate(yesterday);

        for (Store store : stores) {
            StoreCombineDTO storeDto = reservationService.getStoreInformation(store.getStoreId());
            List<String> reservationTimes = reservationService.getReservationTime(storeDto.getStartTime(),
                    storeDto.getEndTime());
            StoreTable table = managementService.getTable(store.getStoreId());
            for (String time : reservationTimes) {
                srdRepository.save(new StoreReservationDate(threeWeeksLater, store.getStoreId(), time,
                        table.getOneTable(), table.getTwoTable(), table.getFourTable(), table.getPartyTable()));
            }

        }

    }

    @PostConstruct
    public void initializeDates() {
        LocalDate today = LocalDate.now();
        LocalDate endDate = today.plusWeeks(3);

        List<Store> stores = storeService.findAll();
        // 이미 있는 날짜는 제외하고 새로운 날짜만 추가
        System.out.println("가게수 : " + stores.size());

        List<StoreReservationDate> dateEntitiesToAdd = new ArrayList<>();
        for (Store store : stores) {
            // 3주 후 날짜 추가, 이미 있는 경우는 추가하지 않음
            StoreCombineDTO storeDto = reservationService.getStoreInformation(store.getStoreId());
            List<String> reservationTimes = reservationService.getReservationTime(storeDto.getStartTime(),
                    storeDto.getEndTime());
            StoreTable table = managementService.getTable(store.getStoreId());
            for (String time : reservationTimes) {
                for (LocalDate date = today; !date.isAfter(endDate); date = date.plusDays(1)) {
                    if (!srdRepository.existsByDateAndStoreId(date, store.getStoreId())) {
                        dateEntitiesToAdd.add(new StoreReservationDate(date, store.getStoreId(), time,
                                table.getOneTable(), table.getTwoTable(), table.getFourTable(), table.getPartyTable()));
                    }
                }
            }

        }

        srdRepository.saveAll(dateEntitiesToAdd);
    }

    public Map<String, Integer> getTableInformation(String storeId, String date, String time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-M-d");
        LocalDate date2 = LocalDate.parse(date, formatter);
       
        StoreReservationDate SRD = srdRepository.
        findByIdAndDateAndTime(Long.parseLong(storeId), date2, time).orElse(new StoreReservationDate());

        Map<String, Integer> timeMap = new HashMap<>();
        timeMap.put("oneTable", SRD.getOneTable());
        timeMap.put("twoTable", SRD.getTwoTable());
        timeMap.put("fourTable", SRD.getFourTable());
        timeMap.put("partyTable", SRD.getPartyTable());

        return timeMap;



    }
}
