package test;

import captive.context.FakeDNS;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.xbill.DNS.Lookup;
import org.xbill.DNS.SimpleResolver;
import org.xbill.DNS.TextParseException;
import org.xbill.DNS.Type;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 *
 * @author Kent Yeh
 */
@ContextConfiguration("classpath:testContext.xml")
@Test(groups = {"unit"})
public class TestDns extends AbstractTestNGSpringContextTests {

    private static final Logger logger = LoggerFactory.getLogger(TestDns.class);
    private final SimpleResolver sr = null;
    private final AtomicInteger aidx = new AtomicInteger(0);
    private String dnsHost = null;
    @Autowired
    private FakeDNS dns;

    @BeforeClass
    public void setup() throws UnknownHostException {
        dnsHost = InetAddress.getLocalHost().getHostAddress();
    }

    @Test(invocationCount = 20, threadPoolSize = 10, successPercentage = 100)
    public void testDns() throws UnknownHostException, TextParseException {
        int idx = aidx.addAndGet(1);
        Lookup l = new Lookup(String.format("www%d.unknown.com.tw", idx), Type.A);
        l.setResolver(new SimpleResolver(dnsHost));
        l.run();
        assertThat("Failed to query DNS", l.getResult(), is(equalTo(Lookup.SUCCESSFUL)));
        assertThat("Failed to query DNS", l.getAnswers()[0].rdataToString(), is(equalTo(dnsHost)));
    }
}
