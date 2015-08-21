package test;

import captive.context.EchoController;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.web.servlet.HandlerAdapter;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ModelAndView;
import org.testng.annotations.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 *
 * @author Kent Yeh
 */
@ContextConfiguration("classpath:testContext.xml")
public class TestWeb extends AbstractTestNGSpringContextTests {

    private static Logger logger = LoggerFactory.getLogger(TestWeb.class);
    @Autowired
    @Qualifier("handlerAdapter")
    private HandlerAdapter handlerAdapter;
    @Autowired
    @Qualifier("handlerMapping")
    private HandlerMapping handlerMapping;
    @Autowired
    private EchoController echoController;

    private ModelAndView handleController(HttpServletRequest request, HttpServletResponse response, Object controller) throws Exception {
        HandlerInterceptor[] interceptors = handlerMapping.getHandler(request).getInterceptors();
        for (HandlerInterceptor interceptor : interceptors) {
            interceptor.preHandle(request, response, controller);
        }
        ModelAndView mv = handlerAdapter.handle(request, response, controller);
        for (HandlerInterceptor interceptor : interceptors) {
            interceptor.postHandle(request, response, controller, mv);
        }
        return mv;
    }

    @Test
    public void testEcho1() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        request.setMethod("GET");
        request.setRequestURI("/echo/Hello+World");
        HashMap<String, String> pathvars = new HashMap<String, String>();
        pathvars.put("msg", "Hello World");
        request.setAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE, pathvars);
        ModelAndView mv = handleController(request, response, echoController);
        assertThat("Failed echo message", mv.getViewName(), is(equalTo("echo")));
        assertThat("Failed echo message", mv.getModel().get("msg").toString(), is(equalTo("Hello World")));
    }

    @Test
    public void testEcho2() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        request.setMethod("GET");
        request.setRequestURI("/echo");
        request.setParameter("msg", "Hello World");
        ModelAndView mv = handleController(request, response, echoController);
        assertThat("Failed echo message", mv.getViewName(), is(equalTo("echo")));
        assertThat("Failed echo message", mv.getModel().get("msg").toString(), is(equalTo("Hello World")));
    }
}
