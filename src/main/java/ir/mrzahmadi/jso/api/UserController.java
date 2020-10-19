package ir.mrzahmadi.jso.api;

import ir.mrzahmadi.jso.Utils.jwt.JwtUtil;
import ir.mrzahmadi.jso.model.Response.DetailsResponse;
import ir.mrzahmadi.jso.model.User;
import ir.mrzahmadi.jso.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user/")
public class UserController {

    UserService userService;
    JwtUtil jwtUtil;

    @Autowired
    public UserController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("details")
    public ResponseEntity<DetailsResponse> getDetails(@RequestHeader("Authorization") String authorization) {
        String phoneNumber = jwtUtil.getPhoneNumber(authorization);
        User user = userService.findByPhoneNumber(phoneNumber);
        return new ResponseEntity<>(new DetailsResponse(user.getId(), user.getPhoneNumber()), HttpStatus.OK);
    }

}
