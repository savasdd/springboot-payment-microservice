package com.payment.stock.entity.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.payment.stock.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "CATEGORY")
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Category extends BaseEntity implements Serializable {

    @Column(name = "categoryName", nullable = false)
    private String categoryName;

    @Column(name = "description", length = 2000)
    private String description;

    @JsonIgnore
    @ManyToMany(mappedBy = "categoryList", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<Stock> stockList;

}
