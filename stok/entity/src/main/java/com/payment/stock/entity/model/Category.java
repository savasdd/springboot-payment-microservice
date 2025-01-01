package com.payment.stock.entity.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.payment.stock.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Data
@Entity
@Table(name = "CATEGORY")
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Category extends BaseEntity implements Serializable {

    @Column(name = "categoryName", nullable = false)
    private String categoryName;

    @Column(name = "description", length = 2000)
    private String description;

    @ToString.Exclude
    @ManyToMany(mappedBy = "categoryList",cascade = CascadeType.ALL)
    @JsonIgnoreProperties("categoryList")
    private List<Stock> stockList;

}
