package com.Inventory.demo.dto;

public class LoginDto {
    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

//package com.Inventory.demo.dto;

//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//import javax.validation.constraints.NotBlank;
//
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//public class LoginDto {
//    @NotBlank(message = "Username is required")
//    private String username;
//
//    @NotBlank(message = "Password is required")
//    private String password;
//}
