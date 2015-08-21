package test;

import com.thoughtworks.selenium.DefaultSelenium;
import com.thoughtworks.selenium.SeleneseTestCase;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;                                   
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 *
 * @author Kent Yeh
 */                               
@Test(groups={"integrate"})
public class TestBrowser extends SeleneseTestCase {
    private static final Logger logger = LogManager.getLogger(TestBrowser.class);
    
    protected DefaultSelenium selenium;
    
    @BeforeClass
    @Parameters("browser")
    public void setup(@Optional("iexplore") String browser){
        selenium = new DefaultSelenium("localhost", 4444, "*"+browser, "http://localhost");
        selenium.start();
    }

    @AfterClass
    @Override
    public void tearDown() {
        if (selenium != null) {
            selenium.stop();
        }
    }
    @Test
    public void test404() {
        selenium.open("/unknownpath/404.html");
        selenium.waitForPageToLoad("10000");
        assertThat("Failed to redirect homepage when url error", selenium.getBodyText(), is(equalTo("Hello 丁丁")));
    }
}
