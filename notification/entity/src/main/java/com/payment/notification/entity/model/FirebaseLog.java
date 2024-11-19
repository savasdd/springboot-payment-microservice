package com.payment.notification.entity.model;

import com.payment.notification.entity.base.BaseEntity;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "FIREBASE_LOG")
public class FirebaseLog extends BaseEntity implements Serializable {

    @Column(name = "userId")
    private String userId;
    @Column(name = "title")
    private String title;
    @Column(name = "body", length = 500)
    private String body;
    @Column(name = "topic")
    private String topic;
    @Column(name = "token")
    private String token;

}
