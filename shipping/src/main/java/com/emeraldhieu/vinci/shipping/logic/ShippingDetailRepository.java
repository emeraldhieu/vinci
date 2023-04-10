package com.emeraldhieu.vinci.shipping.logic;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShippingDetailRepository extends ListCrudRepository<ShippingDetail, Long>,
    PagingAndSortingRepository<ShippingDetail, Long>,
    JpaSpecificationExecutor<ShippingDetail> {

    Optional<ShippingDetail> findByExternalId(String externalId);

    /**
     * Join fetch with pagination;
     * Join fetch is used to avoid N+1 problem.
     * See https://vladmihalcea.com/join-fetch-pagination-spring/#Using_JOIN_FETCH_with_Spring_Pagination
     */
    @Query(value = """
            SELECT sd
            FROM ShippingDetail sd
            JOIN FETCH sd.shipping
        """,
        countQuery = """
                SELECT COUNT(1) 
                FROM ShippingDetail
            """)
    Page<ShippingDetail> getShippings(Pageable pageable);
}
