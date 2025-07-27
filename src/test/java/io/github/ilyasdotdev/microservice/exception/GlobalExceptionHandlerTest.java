package io.github.ilyasdotdev.microservice.exception;

import io.github.ilyasdotdev.microservice.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/**
 * @author Muhammad Ilyas (m.ilyas@live.com)
 * This class tests the global exception handler for validation errors and unhandled exceptions.
 */
class GlobalExceptionHandlerTest extends BaseTest {

    /**
     * Tests the response when validation fails for the 'name' field.
     * @throws Exception
     */
    @Test
    void testValidationFailedResponseForName() throws Exception {

        var request = """
                {"name": "", "email": "email@domain.com"}
                """;

        mockMvc.perform(
                        post("/api/test")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(request)
                ).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.fields.name").value("Name must be between 3 and 50 characters"));
    }

    /**
     * Tests the response when validation fails for the 'email' field.
     * @throws Exception
     */
    @Test
    void testSuccessfulResponse() throws Exception {

        var request = """
                {"name": "john", "email": "email@domain.com"}
                """;

        mockMvc.perform(post("/api/test")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request)
        ).andExpect(status().isOk());

    }

    /**
     * Tests the response when an unhandled exception occurs.
     * @throws Exception
     */
    @Test
    void testUnhandledExceptionResponse() throws Exception {

        mockMvc.perform(post("/api/test/unhandled"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("Something happened that we didn't expect"));
    }
}