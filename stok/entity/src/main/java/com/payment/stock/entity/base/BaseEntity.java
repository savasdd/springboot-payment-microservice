package com.payment.stock.entity.base;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.payment.stock.common.enums.RecordStatus;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.Date;


@Data
@NoArgsConstructor
@MappedSuperclass
@SuperBuilder(toBuilder = true)
public abstract class BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1656703245648711747L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Basic
    @Column(name = "CR_DATE", updatable = false)
    private Date createdDate;

    @Basic
    @Column(name = "UP_DATE")
    private Date updatedDate;

    @Basic
    @Column(name = "status", nullable = false)
    private RecordStatus recordStatus;

    @Basic
    @JsonIgnore
    @Version
    private Long version;

    @Basic
    @JsonIgnore
    @CreatedBy
    @Column(name = "CR_BY")
    private String createdBy;

    @Basic
    @JsonIgnore
    @LastModifiedDate
    @Column(name = "UP_BY")
    private String updatedBy;

    @PreUpdate
    public void setPreUpdate() {
        this.updatedDate = new Date();
    }

    @PrePersist
    public void setPrePersist() {
        this.createdDate = new Date();
        this.recordStatus = RecordStatus.ACTIVE;
    }

}