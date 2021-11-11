package com.yeokeong.gonggang.model.entity;

import com.yeokeong.gonggang.common.UserStatus;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "user")
public class User extends AbstractTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    private String nickName;

    private String userName;

    private String pwd;

    @Enumerated(EnumType.STRING)
    private UserStatus status;
}
