package fpt.com.ecommerce.auth.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginRequest {

    private String username;
    private String password;
}
