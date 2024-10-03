package com.payment.stock.entity.model;

import com.payment.stock.entity.base.BaseEntity;
import com.payment.stock.entity.dto.CDNDto;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "CDN_DATA")
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class CDNData extends BaseEntity implements Serializable {

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private CDNDto data;

}
