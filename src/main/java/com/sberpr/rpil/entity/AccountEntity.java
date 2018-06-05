package com.sberpr.rpil.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Min;

/**
 * Created by defabey on 24-Mar-18.
 */
@Entity
@Data
public class AccountEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
        name = "UUID",
        strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(updatable = false,
        nullable = false)
    private UUID id;

    @Min(value = 0)
    @Column(nullable = false)
    private Integer balance = 0;
}
