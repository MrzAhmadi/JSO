package ir.mrzahmadi.jso.api;

import ir.mrzahmadi.jso.Utils.jwt.JwtUtil;
import ir.mrzahmadi.jso.model.Request.LoginRequest;
import ir.mrzahmadi.jso.model.Request.VerifyOTPRequest;
import ir.mrzahmadi.jso.model.Response.BaseResponse;
import ir.mrzahmadi.jso.model.Response.ErrorResponse;
import ir.mrzahmadi.jso.model.Response.VerifyOTPResponse;
import ir.mrzahmadi.jso.model.Token;
import ir.mrzahmadi.jso.model.User;
import ir.mrzahmadi.jso.service.TokenService;
import ir.mrzahmadi.jso.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Random;

import static ir.mrzahmadi.jso.Utils.Const.PASSWORD_ENCRYPT;

@RestController
@RequestMapping("/api/auth/")
public class AuthController {

    private UserService userService;
    private TokenService tokenService;
    private JwtUtil jwtUtil;
    private AuthenticationManager authenticationManager;

    Random random = new Random();

    @Autowired
    public AuthController(UserService userService, TokenService tokenService, JwtUtil jwtUtil, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.tokenService = tokenService;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("login")
    public @ResponseBody
    ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        User user = userService.findByPhoneNumber(loginRequest.getPhoneNumber());
        if (user == null) {
            user = new User(loginRequest.getPhoneNumber());
            user = userService.registerUser(user);
            int otp = random.nextInt(99999);
            Token token = new Token(user, String.valueOf(otp), jwtUtil.generateToken(loginRequest.getPhoneNumber()));
            tokenService.addToken(token);
            System.out.println("This code send by sms/message: " + otp);
        } else {
            tokenService.expireAll(user.getId());
            int otp = random.nextInt(99999);
            Token token = new Token(user, String.valueOf(otp), jwtUtil.generateToken(loginRequest.getPhoneNumber()));
            tokenService.addToken(token);
            System.out.println("This code send by sms/message: " + otp);
        }
        return new ResponseEntity<>(new BaseResponse("otp send"), HttpStatus.OK);
    }

    @PostMapping("verify-otp")
    public @ResponseBody
    ResponseEntity<?> verifyOTP(@RequestBody VerifyOTPRequest verifyOTPRequest) throws Exception {
        User user = userService.findByPhoneNumber(verifyOTPRequest.getPhoneNumber());
        if (user == null) {
            ErrorResponse errorResponse = new ErrorResponse(
                    HttpServletResponse.SC_NOT_FOUND,
                    "user not found!"
            );
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        } else {
            Token token = tokenService.findByUserAndOtp(user, verifyOTPRequest.getOtp());
            if (token == null || token.isExpired()) {
                ErrorResponse errorResponse = new ErrorResponse(
                        HttpServletResponse.SC_FORBIDDEN,
                        "Invalid otp!"
                );
                return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
            } else {
                authenticate(verifyOTPRequest.getPhoneNumber(), PASSWORD_ENCRYPT);
                return new ResponseEntity<>(new VerifyOTPResponse(token.getToken()), HttpStatus.OK);
            }
        }
    }

    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }


}
