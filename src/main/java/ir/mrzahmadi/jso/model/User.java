package ir.mrzahmadi.jso.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity(name = "users_tbl")
public class User {

    @Id
    @GeneratedValue
    private long id;

    @Column(name = "phone_number", unique = true)
    private String phoneNumber;

    private String password;

    public long getId() {
        return id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
