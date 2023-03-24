package com.emeraldhieu.vinci.shipping.logic;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShippingRepository extends ListCrudRepository<Shipping, Long>,
    PagingAndSortingRepository<Shipping, Long>,
    JpaSpecificationExecutor<Shipping> {

    Optional<Shipping> findByExternalId(String externalId);

    void deleteByExternalId(String externalId);
}
