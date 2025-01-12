package programmermuda.product.auth.dto;

public class LoginUserDto {

    private String email;

    private String password;

    public String getEmail() {
        return email;
    }

    public v setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
