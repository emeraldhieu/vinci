package com.emeraldhieu.vinci.order.logic;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends ListCrudRepository<Order, Long>,
    PagingAndSortingRepository<Order, Long>,
    JpaSpecificationExecutor<Order> {

    Optional<Order> findByExternalId(String externalId);

    void deleteByExternalId(String externalId);
}
