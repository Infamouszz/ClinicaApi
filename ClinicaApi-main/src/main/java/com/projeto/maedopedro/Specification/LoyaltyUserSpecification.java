package com.projeto.maedopedro.Specification;

import com.projeto.maedopedro.Model.LolyaltUsersModel.LoyaltyUser;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class LoyaltyUserSpecification {
    public static Specification<LoyaltyUser> hasCpf(final String cpf) {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.get("cpf"), cpf);
        };
    }
    public static Specification<LoyaltyUser> hasFirstName(String firstName){
        return (root, query, criteriaBuilder) ->{
            return criteriaBuilder.like(root.get("firstName"), "%"+firstName+"%");
        };
    }
    public static Specification<LoyaltyUser> hasLastName(String lastName){
        return(root, query, criteriaBuilder) -> {
            return criteriaBuilder.like(root.get("lastName"), "%"+lastName+"%");
        };
    }
    public static Specification<LoyaltyUser> hasEmail(String email){
        return(root, query, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.get("email"), email);
        };
    }
    public static Specification<LoyaltyUser> hasPhone(String phoneNumber){
        return(root, query, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.get("phone"), phoneNumber);
        };
    }
    public static Specification<LoyaltyUser> hasDateOfBirth(LocalDate dateOfBirth){
        return(root, query, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.get("dateOfBirth"), dateOfBirth);
        };
    }
    public static Specification<LoyaltyUser> hasMotherName(String motherName){
        return(root, query, criteriaBuilder) -> {
            return criteriaBuilder.like(root.get("motherName"), "%"+motherName+"%");
        };
    }
    public static Specification<LoyaltyUser> hasFatherName(String fatherName){
        return(root, query, criteriaBuilder) -> {
            return criteriaBuilder.like(root.get("fatherName"), "%"+fatherName+"%");
        };
    }
}
