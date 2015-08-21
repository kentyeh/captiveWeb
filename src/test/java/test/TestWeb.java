package test;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.web.context.WebApplicationContext;
import org.testng.annotations.BeforeClass;

/**
 *
 * @author Kent Yeh
 */
@WebAppConfiguration
@ContextConfiguration("classpath:testContext.xml")
public class TestWeb extends AbstractTestNGSpringContextTests {

    private static final Logger logger = LogManager.getLogger(TestWeb.class);
    @Autowired
    WebApplicationContext wac;
    private MockMvc mockMvc;

    @BeforeClass
    public void setup() {
        this.mockMvc = webAppContextSetup(this.wac).alwaysExpect(status().isOk()).build();
    }

    @Test
    public void testEcho1() throws Exception {
        mockMvc.perform(get("/echo/{msg}", "Hello World")).andDo(print())
                .andExpect(view().name("echo"))
                .andExpect(model().attribute("msg", is(equalTo("Hello World"))));
    }

    @Test
    public void testEcho2() throws Exception {
        mockMvc.perform(get("/echom").param("msg", "Hello World")).andDo(print())
                .andExpect(view().name("echo"))
                .andExpect(model().attribute("msg", is(equalTo("Hello World"))));
    }
}
