package ir.mrzahmadi.jso.model;

import javax.persistence.*;

@Entity(name = "tokens_tbl")
public class Token {

    @Id
    @GeneratedValue
    private String id;

    @ManyToOne(targetEntity = User.class , fetch = FetchType.EAGER)
    @Column(name = "user_id")
    private long userId;

    private String otp;

    private String token;

}
