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
import programmermuda.spring.product.dto.RegisterRequest;
import programmermuda.spring.product.dto.TokenResponse;

@Controller
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    @Value("${backend.url}")
    private String backendUrl;

    private final RestTemplate restTemplate;

    public AuthController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/login")
    public String showLoginPage(Model model, @RequestParam(required = false) String error) {
        model.addAttribute("loginRequest", new LoginRequest());
        model.addAttribute("error", error); // Tampilkan error jika ada
        return "login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute LoginRequest loginRequest, Model model, HttpSession session) {
        String loginUrl = backendUrl + "/api/login";
        try {
            ResponseEntity<TokenResponse> response = restTemplate.postForEntity(loginUrl, loginRequest, TokenResponse.class);
            TokenResponse tokenResponse = response.getBody();

            if (tokenResponse != null) {
                session.setAttribute("accessToken", tokenResponse.getAccessToken());
                return "redirect:/home";
            }
        } catch (HttpClientErrorException ex) {
            log.error("Login failed: {}", ex.getMessage());
            return "redirect:/login?error=Invalid username or password";
        }
        return "redirect:/login?error=Unexpected error occurred";
    }

    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("registerRequest", new RegisterRequest());
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute RegisterRequest registerRequest, Model model) {
        String registerUrl = backendUrl + "/auth/register";
        try {
            restTemplate.postForEntity(registerUrl, registerRequest, Void.class);
            return "redirect:/login?success=true";
        } catch (HttpClientErrorException ex) {
            log.error("Registration failed: {}", ex.getMessage());
            model.addAttribute("error", "Failed to register. Please try again.");
        }
        return "register";
    }

    @GetMapping("/home")
    public String showHome(Model model, HttpSession session) {
        String token = (String) session.getAttribute("accessToken");

        if (token != null) {
            // Ambil data pengguna atau informasi lainnya dari backend dengan menggunakan token
            String apiUrl = backendUrl + "/api/home"; // Misalnya, endpoint di backend yang menyediakan data untuk home
            try {
                HttpHeaders headers = new HttpHeaders();
                headers.set("Authorization", "Bearer " + token);
                HttpEntity<String> entity = new HttpEntity<>(headers);

                ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.GET, entity, String.class);
                String userData = response.getBody(); // Sesuaikan dengan respons yang diterima dari backend

                model.addAttribute("userData", userData);
                return "home"; // Menampilkan halaman home dengan data pengguna
            } catch (HttpClientErrorException ex) {
                // Jika token tidak valid atau ada masalah saat mengambil data dari backend
                model.addAttribute("error", "Failed to load data from backend.");
                return "login"; // Mengarahkan ulang ke halaman login jika terjadi kesalahan
            }
        }

        // Jika tidak ada token atau pengguna belum login, arahkan ke login
        return "redirect:/login";
    }
}
