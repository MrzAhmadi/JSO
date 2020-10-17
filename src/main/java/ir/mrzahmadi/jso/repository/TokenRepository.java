package ir.mrzahmadi.jso.repository;

import ir.mrzahmadi.jso.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;

public interface TokenRepository extends JpaRepository<Token, Long> {

    Token findByUserId(long userId);

    @Transactional
    @Modifying
    @Query(value = "UPDATE tokens_tbl t SET t.isExpired=true WHERE t.isExpired=false AND t.user.id = :userId")
    void expireAll(@Param("userId") long userId);

}
