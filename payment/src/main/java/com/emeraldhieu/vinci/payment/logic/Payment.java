package com.emeraldhieu.vinci.payment.logic;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "payment")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Getter
@Setter
@EqualsAndHashCode
@EntityListeners(AuditingEntityListener.class)
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column(nullable = false)
    private String externalId;

    @Column(nullable = false)
    private String orderId;

    @Convert(converter = PaymentMethodConverter.class)
    @Column(nullable = false)
    private PaymentMethod paymentMethod;

    @Column(nullable = false)
    @CreatedBy
    private String createdBy;

    @Column(nullable = false)
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(nullable = false)
    @LastModifiedBy
    private String updatedBy;

    @Column(nullable = false)
    @LastModifiedDate
    private LocalDateTime updatedAt;

    /**
     * Set default value before persisting.
     * See https://stackoverflow.com/questions/197045/setting-default-values-for-columns-in-jpa#13432234
     */
    @PrePersist
    void preInsert() {
        if (externalId == null) {
            externalId = UUID.randomUUID().toString().replace("-", "");
        }
        if (paymentMethod == null) {
            paymentMethod = PaymentMethod.DEBIT;
        }
        if (createdBy == null) {
            // TODO Set this value to the user who creates the payment.
            createdBy = UUID.randomUUID().toString().replace("-", "");
        }
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (updatedBy == null) {
            // TODO Set this value to the user who updates the payment.
            updatedBy = UUID.randomUUID().toString().replace("-", "");
        }
        if (updatedAt == null) {
            updatedAt = LocalDateTime.now();
        }
    }
}