package com.payment.entity.model;

import com.payment.entity.base.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "PARAMETER")
@EqualsAndHashCode(callSuper = true)
public class Parameter extends BaseEntity implements Serializable {

    @Column(name = "key")
    private String key;
    @Column(name = "value")
    private String value;

}
