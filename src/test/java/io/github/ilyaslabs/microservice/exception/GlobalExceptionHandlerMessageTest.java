package io.github.ilyaslabs.microservice.exception;

import io.github.ilyaslabs.microservice.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.TestPropertySource;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * This class tests the global exception handler for unhandled exceptions.
 * @author Muhammad Ilyas (m.ilyas@live.com)
 */
@TestPropertySource(
        properties = {
                "io.github.ilyaslabs.microservice.exception.unhandledExceptionMessage=Test Exception Message"
        }
)
class GlobalExceptionHandlerMessageTest extends BaseTest {

    /**
     * Tests the response when validation fails for the 'name' field.
     * @throws Exception
     */
    @Test
    void testUnhandledExceptionResponse() throws Exception {
        mockMvc.perform(post("/api/test/unhandled"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("Test Exception Message"));
    }
}
