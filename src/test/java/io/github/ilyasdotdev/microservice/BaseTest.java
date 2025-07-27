package io.github.ilyasdotdev.microservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

/**
 * @author Muhammad Ilyas (m.ilyas@live.com)
 */
@Import({
        TestController.class,
})
@AutoConfigureMockMvc
@SpringBootTest
public abstract class BaseTest {

    @Autowired
    public MockMvc mockMvc;
}
