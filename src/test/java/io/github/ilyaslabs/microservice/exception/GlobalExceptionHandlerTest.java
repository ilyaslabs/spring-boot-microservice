package io.github.ilyaslabs.microservice.exception;

import io.github.ilyaslabs.microservice.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

        mockMvc.perform(get("/api/test/unhandled"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("Something happened that we didn't expect"));
    }

    /**
     * Test the response when an invalid path is accessed.
     * @throws Exception
     */
    @Test
    void testNotFoundResponse() throws Exception {
        mockMvc.perform(get("/api/invalid-path"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("404 Not Found"));
    }

    @Test
    void testPathParamValidation() throws Exception {
        mockMvc.perform(get("/api/test/param/123"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.fields.id").value("Id must be at least 4 characters"));
    }
}