package com.example.demo.reservation;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "store_reservation_menu")
@Getter
@Setter
public class StoreReservationMenu {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "menu_id")
    private Long menuId;

    @Column(name = "info_id")
    private Long infoId;

    @Column(name = "menu_name")
    private String menuName;

    @Column(name = "quantity")
    private String quantity;

    public StoreReservationMenu(){}

}
