package com.payment.stock.entity.model;

import com.payment.stock.entity.base.BasicEntity;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "STOCK_DETAILS")
@EqualsAndHashCode(callSuper = true)
public class StockDetail extends BasicEntity implements Serializable {

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "language")
    private String language;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STOCK_ID", referencedColumnName = "ID")
    private Stock stock;
}
