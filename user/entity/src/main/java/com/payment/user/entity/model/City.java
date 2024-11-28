package com.payment.user.entity.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.payment.user.entity.base.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.io.PipedReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Builder
@Entity
@Table(name = "CITY")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class City extends BaseEntity implements Serializable {

    @Column(name = "name")
    private String name;
    @Column(name = "code")
    private String code;

    @JsonIgnore
    @OneToMany(mappedBy = "city")
    private Set<User> users = new HashSet<>();
}
