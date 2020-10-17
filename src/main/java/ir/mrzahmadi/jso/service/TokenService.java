package ir.mrzahmadi.jso.service;

import ir.mrzahmadi.jso.model.Token;
import ir.mrzahmadi.jso.model.User;
import ir.mrzahmadi.jso.repository.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TokenService {

    TokenRepository tokenRepository;

    @Autowired
    public TokenService(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    public void expireAll(long userId) {
        tokenRepository.expireAll(userId);
    }

    public void addToken(Token token) {
        tokenRepository.save(token);
    }

    public Token findByUserAndOtp(User user, String otp){
        return tokenRepository.findByUserAndOtp(user,otp);
    }


}
