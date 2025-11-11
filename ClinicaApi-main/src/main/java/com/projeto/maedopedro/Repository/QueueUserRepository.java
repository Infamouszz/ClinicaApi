package com.projeto.maedopedro.Repository;


import com.projeto.maedopedro.Model.QueueUserModel.QueueUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QueueUserRepository extends JpaRepository<QueueUser, Long>, JpaSpecificationExecutor<QueueUser> {
    Optional<QueueUser> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByPhoneNumber(String phoneNumber);

    @Modifying
    @Query(value = "UPDATE QueueUser u SET u.queuePosition = u.queuePosition - 1 WHERE u.queuePosition > :positionOfRemovedUser")
    void decrementQueuePositionAfter(@Param("positionOfRemovedUser") Long positionOfRemovedUser);

    List<QueueUser> findByFirstNameAndLastName(String firstName, String lastName);
}


