package ir.mrzahmadi.jso.api;

import ir.mrzahmadi.jso.Utils.jwt.JwtUtil;
import ir.mrzahmadi.jso.model.Request.LoginRequest;
import ir.mrzahmadi.jso.model.Request.VerifyOTPRequest;
import ir.mrzahmadi.jso.model.Response.BaseResponse;
import ir.mrzahmadi.jso.model.Response.ErrorResponse;
import ir.mrzahmadi.jso.model.Response.VerifyOTPResponse;
import ir.mrzahmadi.jso.model.User;
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
    private JwtUtil jwtUtil;
    private AuthenticationManager authenticationManager;

    Random random = new Random();

    @Autowired
    public AuthController(UserService userService, JwtUtil jwtUtil, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("login")
    public @ResponseBody
    ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        User user = userService.findByPhoneNumber(loginRequest.getPhoneNumber());
        if (user == null) {
            long expirationDate = jwtUtil.generateExpirationDate();
            String token = jwtUtil.generateToken(loginRequest.getPhoneNumber(),expirationDate);
            expirationDate = jwtUtil.getExpirationByTime(token);
            int otp = random.nextInt(99999);
            user = new User(loginRequest.getPhoneNumber(),expirationDate,String.valueOf(otp));
            userService.registerUser(user);
            System.out.println("This code send by sms/message: " + otp);
        } else {
            long expirationDate = jwtUtil.generateExpirationDate();
            String token = jwtUtil.generateToken(loginRequest.getPhoneNumber(),expirationDate);
            expirationDate = jwtUtil.getExpirationByTime(token);
            int otp = random.nextInt(99999);
            user.setTokeExpirationDate(expirationDate);
            user.setOtp(String.valueOf(otp));
            userService.registerUser(user);
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
            if (user.getOtp() == null || !user.getOtp().equals(verifyOTPRequest.getOtp())) {
                ErrorResponse errorResponse = new ErrorResponse(
                        HttpServletResponse.SC_FORBIDDEN,
                        "Invalid otp!"
                );
                return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
            } else {
                String token = jwtUtil.generateToken(verifyOTPRequest.getPhoneNumber(),user.getTokeExpirationDate());
                authenticate(verifyOTPRequest.getPhoneNumber(), PASSWORD_ENCRYPT);
                return new ResponseEntity<>(new VerifyOTPResponse(token), HttpStatus.OK);
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
