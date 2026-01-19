package fpt.com.ecommerce.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI ecommerceApi() {

        return new OpenAPI()
                .info(new Info()
                        .title("E-commerce Backend API")
                        .description("Phase 1 - Headless E-commerce Backend Application")
                        .version("1.0")
                );
    }
}
