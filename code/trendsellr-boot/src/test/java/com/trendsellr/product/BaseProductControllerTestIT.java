package com.trendsellr.product;

import com.trendsellr.BaseTestIT;
import com.trendsellr.domain.model.user.Role;
import com.trendsellr.domain.model.user.User;
import com.trendsellr.domain.repository.ProductRepository;
import com.trendsellr.domain.repository.UserRepository;
import com.trendsellr.domain.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Set;
import java.util.UUID;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
public abstract class BaseProductControllerTestIT extends BaseTestIT {

    protected final ProductRepository productRepository;

    protected final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    protected final WebApplicationContext context;

    private final String testApiKey;

    protected MockMvc insecureMockMvc;

    protected MockMvc secureMockMvc;

    @BeforeEach
    void setUp() {
        this.insecureMockMvc = MockMvcBuilders
                .webAppContextSetup(this.context)
                .apply(springSecurity())
                .build();
        this.productRepository.deleteAll();
        this.userRepository.deleteAll();

        final User testUser = this.userRepository.save(
                User.builder()
                        .id(UUID.randomUUID().toString())
                        .email("test@user.com")
                        .password(this.passwordEncoder.encode("password"))
                        .roles(Set.of(Role.USER))
                        .build()
        );

        final String jwtToken = this.jwtService.generateToken(testUser);

        this.insecureMockMvc = MockMvcBuilders
                .webAppContextSetup(this.context)
                .apply(springSecurity())
                .build();

        this.secureMockMvc = MockMvcBuilders
                .webAppContextSetup(this.context)
                .apply(springSecurity())
                .defaultRequest(
                        get("/")
                                .header("X-API-KEY", this.testApiKey)
                                .header("Authorization", "Bearer " + jwtToken)
                )
                .build();
    }
}
