package ir.mrzahmadi.jso.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Entity(name = "users_tbl")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "phone_number", unique = true, nullable = false)
    private String phoneNumber;

    @Column(name = "toke_expiration_date")
    private long tokeExpirationDate;

    private String otp;

    public User() {

    }

    public User(String phoneNumber, long tokeExpirationDate, String otp) {
        this.phoneNumber = phoneNumber;
        this.tokeExpirationDate = tokeExpirationDate;
        this.otp = otp;
    }

    public Long getId() {
        return id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public long getTokeExpirationDate() {
        return tokeExpirationDate;
    }

    public void setTokeExpirationDate(long tokeExpirationDate) {
        this.tokeExpirationDate = tokeExpirationDate;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new ArrayList<>();
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return getPhoneNumber();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
