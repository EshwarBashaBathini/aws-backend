package com.accesshr.emsbackend.EmployeeController.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**") // Adjust the mapping as needed
                .allowedOrigins("http://localhost:3000","https://company-product-frontend.azurewebsites.net", "http://localhost:5173","https://endless-memento-464105-u1.uc.r.appspot.com", "http://3.82.125.93", "https://react-frontend-492553906336.europe-west1.run.app") // Your frontend URL
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Allowed methods
                .allowedHeaders("*") // Allow all headers
                .allowCredentials(true); // Allow credentials (cookies, authorization headers, etc.)
    }
}
