package com.payment.user.entity.model;

import com.payment.user.entity.base.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.io.PipedReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "CITY")
public class City extends BaseEntity implements Serializable {

    @Column(name = "name")
    private String name;
    @Column(name = "code")
    private String code;
}
