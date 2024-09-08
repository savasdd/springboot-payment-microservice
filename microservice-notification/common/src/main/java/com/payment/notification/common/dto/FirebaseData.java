package com.payment.notification.common.dto;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
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
