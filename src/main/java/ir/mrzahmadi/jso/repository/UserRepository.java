package ir.mrzahmadi.jso.repository;

import ir.mrzahmadi.jso.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {

    User findByPhoneNumber(String phoneNumber);

}
