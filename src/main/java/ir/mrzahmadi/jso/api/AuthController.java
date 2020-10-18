package ir.mrzahmadi.jso.api;

import ir.mrzahmadi.jso.Utils.TextUtil;
import ir.mrzahmadi.jso.Utils.jwt.JwtUtil;
import ir.mrzahmadi.jso.model.Request.LoginRequest;
import ir.mrzahmadi.jso.model.Request.RefreshTokenRequest;
import ir.mrzahmadi.jso.model.Request.VerifyOTPRequest;
import ir.mrzahmadi.jso.model.Response.BaseResponse;
import ir.mrzahmadi.jso.model.Response.ErrorResponse;
import ir.mrzahmadi.jso.model.Response.VerifyOTPResponse;
import ir.mrzahmadi.jso.model.User;
import ir.mrzahmadi.jso.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Random;

@RestController
@RequestMapping("/api/auth/")
public class AuthController {

    private UserService userService;
    private JwtUtil jwtUtil;

    Random random = new Random();

    @Autowired
    public AuthController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("login")
    public @ResponseBody
    ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        User user = userService.findByPhoneNumber(loginRequest.getPhoneNumber());
        if (user == null) {
            long expirationDate = jwtUtil.generateAccessTokenExpirationDate();
            String token = jwtUtil.generateAccessToken(loginRequest.getPhoneNumber(), expirationDate);
            expirationDate = jwtUtil.getExpirationByTime(token);
            int otp = random.nextInt(99999);
            user = new User(loginRequest.getPhoneNumber(), expirationDate, String.valueOf(otp));
            userService.registerUser(user);
            System.out.println("This code send by sms/message: " + otp);
        } else {
            long expirationDate = jwtUtil.generateAccessTokenExpirationDate();
            String token = jwtUtil.generateAccessToken(loginRequest.getPhoneNumber(), expirationDate);
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
    ResponseEntity<?> verifyOTP(@RequestBody VerifyOTPRequest verifyOTPRequest) {
        User user = userService.findByPhoneNumber(verifyOTPRequest.getPhoneNumber());
        if (user == null) {
            ErrorResponse errorResponse = new ErrorResponse(
                    HttpStatus.NOT_FOUND.value(),
                    "user not found!"
            );
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        } else {
            if (user.getOtp() == null || !user.getOtp().equals(verifyOTPRequest.getOtp())) {
                ErrorResponse errorResponse = new ErrorResponse(
                        HttpStatus.FORBIDDEN.value(),
                        "Invalid otp!"
                );
                return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
            } else {
                String token = jwtUtil.generateAccessToken(verifyOTPRequest.getPhoneNumber(), user.getTokeExpirationDate());
                String refreshToken = jwtUtil.generateRefreshToken(verifyOTPRequest.getPhoneNumber(), user.getTokeExpirationDate());
                return new ResponseEntity<>(new VerifyOTPResponse(token, refreshToken), HttpStatus.OK);
            }
        }
    }

    @PostMapping("refresh-token")
    public @ResponseBody
    ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        if (TextUtil.isEmpty(refreshTokenRequest.getAccessToken()) || TextUtil.isEmpty(refreshTokenRequest.getRefreshToken())) {
            ErrorResponse errorResponse = new ErrorResponse(
                    HttpStatus.BAD_REQUEST.value(),
                    HttpStatus.BAD_REQUEST.getReasonPhrase()
            );
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        String phoneNumber = jwtUtil.getPhoneNumber(refreshTokenRequest.getAccessToken());

        User user = userService.findByPhoneNumber(phoneNumber);
        long expireTime = jwtUtil.getExpirationByTime(refreshTokenRequest.getAccessToken());
        if (user != null &&
                user.getTokeExpirationDate() == expireTime &&
                jwtUtil.validateRefreshToken(refreshTokenRequest.getRefreshToken(), user.getTokeExpirationDate())) {

            long expirationDate = jwtUtil.generateAccessTokenExpirationDate();
            String token = jwtUtil.generateAccessToken(phoneNumber, expirationDate);
            expirationDate = jwtUtil.getExpirationByTime(token);
            user.setTokeExpirationDate(expirationDate);
            userService.registerUser(user);

            String newToken = jwtUtil.generateAccessToken(phoneNumber, user.getTokeExpirationDate());
            String newRefreshToken = jwtUtil.generateRefreshToken(phoneNumber, user.getTokeExpirationDate());
            return new ResponseEntity<>(new VerifyOTPResponse(newToken, newRefreshToken), HttpStatus.OK);

        } else {
            ErrorResponse errorResponse = new ErrorResponse(
                    HttpStatus.FORBIDDEN.value(),
                    HttpStatus.FORBIDDEN.getReasonPhrase()
            );
            return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
        }

    }


}
