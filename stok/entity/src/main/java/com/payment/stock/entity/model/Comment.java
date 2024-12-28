package com.payment.stock.entity.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.payment.stock.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "STOCK_COMMENT")
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Comment extends BaseEntity implements Serializable {

    @Column(name = "comment",length = 4000)
    private String comment;

    @Column(name = "language")
    private String language;

    @ManyToOne
    @JoinColumn(name = "STOCK_ID", referencedColumnName = "ID")
    @ToString.Exclude
    private Stock stock;
}
