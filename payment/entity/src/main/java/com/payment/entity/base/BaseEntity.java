package com.payment.entity.base;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.payment.common.enums.RecordStatus;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.Date;


@Getter
@Setter
@MappedSuperclass
public abstract class BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1656703245648711747L;

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(generator = "uuid-hibernate-generator")
    @GenericGenerator(name = "uuid-hibernate-generator", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    @Column(name = "CR_DATE", updatable = false)
    private Date createdDate;

    @Column(name = "UP_DATE")
    private Date updatedDate;

    @Column(name = "status", nullable = false)
    private RecordStatus recordStatus;

    @JsonIgnore
    @Version
    private Long version;

    @JsonIgnore
    @CreatedBy
    @Column(name = "CR_BY")
    private String createdBy;

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