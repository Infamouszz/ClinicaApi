package com.projeto.maedopedro.Specification;

import com.projeto.maedopedro.Model.QueueUserModel.QueueUser;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class QueueUserSpecification {
    public static Specification<QueueUser> hasFirstName(String firstName){
        return (root, query, criteriaBuilder) ->{
            return criteriaBuilder.like(root.get("firstName"), "%"+firstName+"%");
        };
    }
    public static Specification<QueueUser> hasLastName(String lastName){
        return(root, query, criteriaBuilder) -> {
            return criteriaBuilder.like(root.get("lastName"), "%"+lastName+"%");
        };
    }
    public static Specification<QueueUser> hasEmail(String email){
        return(root, query, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.get("email"), email);
        };
    }
    public static Specification<QueueUser> hasPhone(String phoneNumber){
        return(root, query, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.get("phoneNumber"), phoneNumber);
        };
    }
    public static Specification<QueueUser> hasQueuePosition(Long position) {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.get("queuePosition"), position);
        };
    }
}
