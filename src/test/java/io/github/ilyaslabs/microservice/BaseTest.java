package io.github.ilyaslabs.microservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
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
