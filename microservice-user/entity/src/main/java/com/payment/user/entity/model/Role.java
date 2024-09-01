package com.payment.user.entity.model;

import com.payment.user.entity.base.BaseEntity;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@Builder
@Entity
@Table(name = "ROLES")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Role extends BaseEntity implements Serializable {

    @Column(name = "name")
    private String name;
}
