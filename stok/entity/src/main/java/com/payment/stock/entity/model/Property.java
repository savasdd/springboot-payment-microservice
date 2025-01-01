package com.payment.stock.entity.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.payment.stock.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "PROPERTY")
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Property extends BaseEntity implements Serializable {

    @Column(name = "property", length = 2000, nullable = false)
    private String property;

    @ToString.Exclude
    @ManyToMany(mappedBy = "propertyList",cascade = CascadeType.ALL)
    @JsonIgnoreProperties("propertyList")
    private List<Stock> stockList;
}
