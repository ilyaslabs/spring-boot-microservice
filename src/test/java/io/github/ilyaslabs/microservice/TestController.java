package io.github.ilyaslabs.microservice;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Muhammad Ilyas (m.ilyas@live.com)
 */
@RestController
@RequestMapping("/api/test")
class TestController {

    record Request(
            @NotNull
            @Size(min = 3, max = 50, message = "Name must be between 3 and 50 characters")
            String name,

            @NotBlank(message = "Email must not be blank")
            @Email(message = "Email must be valid")
            String email
    ) {
    }

    @PostMapping
    ResponseEntity<Void> test(@Valid @RequestBody Request request) {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/unhandled")
    void unhandled() {
        // This endpoint is intentionally left unhandled to demonstrate exception handling
        throw new RuntimeException("This is an unhandled exception");
    }

    @GetMapping("/param/{id}")
    ResponseEntity<Void> param(@PathVariable @Size(message = "Id must be at least 4 characters", min = 4) String id) {
        return ResponseEntity.ok().build();
    }
}
