package com.example.demo.user.join;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JoinRequestRepo extends JpaRepository<JoinRequest, Long>{

    List<JoinRequest> findAllByUserId(Long userId);

    List<JoinRequest> findAllByJoinId(Long joinId);

    List<JoinRequest> findAllByOtherId(Long userId);

    JoinRequest findByJoinId(Long joinId);

    List<JoinRequest> findAllByJoinIdAndIsAcceptAndIsRefused(Long joinId, boolean b, boolean c);
    

}
