package captive.context;

import java.net.InetAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 *
 * @author Kent Yeh
 */
@Service
public class FakeDNS implements InitializingBean, DisposableBean {

    private static Logger logger = LoggerFactory.getLogger(FakeDNS.class);
    FackDNSImpl impl = null;
    private String fakeIp = null;
    private int port = 53;

    public String getFakeIp() {
        return fakeIp;
    }

    public void setFakeIp(String fakeIp) {
        this.fakeIp = fakeIp;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (!StringUtils.hasText(fakeIp)) {
            fakeIp = InetAddress.getLocalHost().getHostAddress();
            logger.info("Fake IP: {}", fakeIp);
        }
        draw_ASCII();
        impl = new FackDNSImpl(fakeIp, port);
        new Thread(impl).start();
    }

    @Override
    public void destroy() throws Exception {
        if (impl != null) {
            impl.shutdown();
        }
        impl = null;
    }

    public static void draw_ASCII() {
        System.out.println("          )))");
        System.out.println("         (o o)");
        System.out.println("-----ooO--(_)--Ooo--------------------------------------------------------------------");
        System.out.println("   ffffffffffffffff  DDDDDDDDDDDDD        NNNNNNNN        NNNNNNNN   SSSSSSSSSSSSSSS");
        System.out.println("   f::::::::::::::::f D::::::::::::DDD     N:::::::N       N::::::N SS:::::::::::::::S");
        System.out.println("  f::::::::::::::::::fD:::::::::::::::DD   N::::::::N      N::::::NS:::::SSSSSS::::::S");
        System.out.println("  f::::::fffffff:::::fDDD:::::DDDDD:::::D  N:::::::::N     N::::::NS:::::S     SSSSSSS");
        System.out.println("  f:::::f       ffffff  D:::::D    D:::::D N::::::::::N    N::::::NS:::::S");
        System.out.println("  f:::::f               D:::::D     D:::::DN:::::::::::N   N::::::NS:::::S");
        System.out.println(" f:::::::ffffff         D:::::D     D:::::DN:::::::N::::N  N::::::N S::::SSSS");
        System.out.println(" f::::::::::::f         D:::::D     D:::::DN::::::N N::::N N::::::N  SS::::::SSSSS");
        System.out.println(" f::::::::::::f         D:::::D     D:::::DN::::::N  N::::N:::::::N    SSS::::::::SS");
        System.out.println(" f:::::::ffffff         D:::::D     D:::::DN::::::N   N:::::::::::N       SSSSSS::::S");
        System.out.println("  f:::::f               D:::::D     D:::::DN::::::N    N::::::::::N            S:::::S");
        System.out.println("  f:::::f               D:::::D    D:::::D N::::::N     N:::::::::N            S:::::S");
        System.out.println(" f:::::::f            DDD:::::DDDDD:::::D  N::::::N      N::::::::NSSSSSSS     S:::::S");
        System.out.println(" f:::::::f            D:::::::::::::::DD   N::::::N       N:::::::NS::::::SSSSSS:::::S");
        System.out.println(" f:::::::f            D::::::::::::DDD     N::::::N        N::::::NS:::::::::::::::SS");
        System.out.println(" fffffffff            DDDDDDDDDDDDD        NNNNNNNN         NNNNNNN SSSSSSSSSSSSSSS");
        System.out.println("--------------------------------------------------------------------------------------");
        System.out.println("|  This program is part of DNSpenTest suite, a project hosted by sourcefoge.net and  |");
        System.out.println("|  developed by PerseoIIN and Fahrenheit84.                                          |");
        System.out.println("--------------------------------------------------------------------------------------");
        System.out.println("|  Web Site: http://dnspentest.sourceforge.net                                       |");
        System.out.println("|  Web CVS: http://dnspentest.cvs.sourceforge.net                                    |");
        System.out.println("|  Project's Home: http://sourceforge.net/projects/dnspentest/                       |");
        System.out.println("|  Email: perseoiin@users.sourceforge.net or fahrenheit84@users.sourceforge.net      |");
        System.out.println("--------------------------------------------------------------------------------------");
    }
}
