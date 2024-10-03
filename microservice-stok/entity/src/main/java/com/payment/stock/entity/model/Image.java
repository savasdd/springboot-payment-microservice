package com.payment.stock.entity.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.payment.stock.entity.base.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "IMAGE")
@EqualsAndHashCode(callSuper = true)
public class Image extends BaseEntity implements Serializable {

    @Column(name = "name")
    private String name;

    @Column(name = "link", length = 500)
    private String link;

    @Column(name = "size")
    private Long size;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stockId", referencedColumnName = "ID")
    private Stock stock;

}
