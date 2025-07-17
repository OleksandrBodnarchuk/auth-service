package pl.alex.auth.ui;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatusController {

    @GetMapping("/status/check")
    public String status() {
        return "auth-service is working";
    }
}
