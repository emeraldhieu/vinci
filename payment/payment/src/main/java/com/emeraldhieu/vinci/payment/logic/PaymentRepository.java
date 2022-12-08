package com.emeraldhieu.vinci.payment.logic;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends ListCrudRepository<Payment, Long>,
    PagingAndSortingRepository<Payment, Long>,
    JpaSpecificationExecutor<Payment> {

}
