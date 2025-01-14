package programmermuda.spring.product.controller.auth;

import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import programmermuda.spring.product.dto.LoginRequest;
import programmermuda.spring.product.dto.LoginResponse;
import programmermuda.spring.product.dto.RegisterRequest;
import programmermuda.spring.product.dto.TokenResponse;

@Controller
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    @Value("${auth.url}")
    private String backendUrl;

    private final RestTemplate restTemplate;

    public AuthController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/login")
    public String loginPage(){
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest loginRequest, Model model){
        String loginUrl = backendUrl + "/auth/login";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");

        HttpEntity<LoginRequest> requestHttpEntity = new HttpEntity<>(loginRequest, headers);

        ResponseEntity<LoginResponse> responseEntity =
                restTemplate.exchange(loginUrl, HttpMethod.POST, requestHttpEntity, LoginResponse.class);

        if (responseEntity.getStatusCode().is2xxSuccessful()){
            LoginResponse loginResponse = responseEntity.getBody();

            model.addAttribute("accessToken", loginResponse.getToken());
            model.addAttribute("expiresToken", loginResponse.getExpiresIn());

            return "home";
        }else {
            model.addAttribute("error", "Login failed");
            return "login";
        }
    }


}
