package ir.mrzahmadi.jso.model;

import javax.persistence.*;

@Entity(name = "tokens_tbl")
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    private String otp;

    private String token;

    @Column(name = "is_expired", columnDefinition = "boolean default false")
    private boolean isExpired;

    public Token() {
    }

    public Token(User user, String otp, String token) {
        this.user = user;
        this.otp = otp;
        this.token = token;
    }

    public Long getId() {
        return id;
    }

    public String getOtp() {
        return otp;
    }

    public String getToken() {
        return token;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
