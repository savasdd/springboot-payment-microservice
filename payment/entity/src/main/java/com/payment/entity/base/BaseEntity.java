package com.payment.entity.base;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1656703245648711747L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "CR_DATE", updatable = false)
    private Date creDate;

    @Column(name = "UP_DATE")
    private Date modDate;

    @Column(name = "status", nullable = false)
    private RecordStatus recordStatus;

    @JsonIgnore
    @Version
    private Long version;

    @JsonIgnore
    @CreatedBy
    @Column(name = "CR_BY")
    private String creBy;

    @JsonIgnore
    @LastModifiedDate
    @Column(name = "UP_BY")
    private String modBy;

    @PreUpdate
    public void setPreUpdate() {
        this.modDate = new Date();
    }

    @PrePersist
    public void setPrePersist() {
        this.creDate = new Date();
        this.recordStatus = RecordStatus.ACTIVE;
        this.modDate = null;
        this.creBy = "Admin";
    }

}