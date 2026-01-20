package com.projeto.clinicaapi.Repository;

import com.projeto.clinicaapi.Model.LolyaltUsersModel.LoyaltyUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LoyaltyUserRepository extends JpaRepository<LoyaltyUser, Long>, JpaSpecificationExecutor<LoyaltyUser> {

    boolean existsLoyaltyUserByPhoneNumber(String phoneNumber);

    boolean existsLoyaltyUserByEmail(String email);

    boolean existsLoyaltyUserByCpf(String cpf);

    Optional<LoyaltyUser> findByCpf(String cpf);

    List<LoyaltyUser> findByFirstNameAndLastName(String firstName, String lastName);

    boolean existsByPhoneNumber(String phoneNumber);

    boolean existsByEmail(String email);
}
