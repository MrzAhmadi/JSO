package ir.mrzahmadi.jso.api;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user/")
public class UserController {

    @PostMapping("test")
    public @ResponseBody
    String testApi() {
        return "User service is up";
    }

}
