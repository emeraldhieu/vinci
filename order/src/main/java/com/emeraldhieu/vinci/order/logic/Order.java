package com.emeraldhieu.vinci.order.logic;

import com.vladmihalcea.hibernate.type.json.JsonType;
import jakarta.persistence.Column;
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
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Getter
@Setter
@EqualsAndHashCode
@EntityListeners(AuditingEntityListener.class)
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Type(JsonType.class)
    @Column(nullable = false)
    private List<String> products;

    @Column(nullable = false)
    @CreatedBy
    private Long createdBy;

    @Column(nullable = false)
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(nullable = false)
    @LastModifiedBy
    private Long updatedBy;

    @Column(nullable = false)
    @LastModifiedDate
    private LocalDateTime updatedAt;

    /**
     * Set default value before persisting.
     * See https://stackoverflow.com/questions/197045/setting-default-values-for-columns-in-jpa#13432234
     */
    @PrePersist
    void preInsert() {
        if (createdBy == null) {
            // TODO Set this value to the user who inserts the order.
            createdBy = 1L;
        }
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (updatedBy == null) {
            // TODO Set this value to the user who updates the order.
            updatedBy = 1L;
        }
        if (updatedAt == null) {
            updatedAt = LocalDateTime.now();
        }
    }
}