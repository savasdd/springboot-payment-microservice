package com.payment.notification.entity.model;

import com.payment.notification.entity.dto.FirebaseDto;
import com.payment.notification.entity.base.BaseEntity;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.*;
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
@Table(name = "FIREBASE_DATA")
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class FirebaseData extends BaseEntity implements Serializable {

    @Column(name = "senderId")
    private String senderId;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private FirebaseDto data;

}
