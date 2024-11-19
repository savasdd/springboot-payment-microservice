package com.payment.notification.entity.model;

import com.payment.notification.entity.base.BaseEntity;
import com.payment.notification.entity.dto.FirebaseDto;
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
@Table(name = "FIREBASE_TOKEN")
public class FirebaseToken extends BaseEntity implements Serializable {

    @Column(name = "senderId")
    private String senderId;

    @Column(name = "token")
    private String token;

}
