package captive.context;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Kent Yeh
 */
public class FackDNSImpl implements Runnable {

    private static final Logger logger = LogManager.getLogger(FackDNSImpl.class);
    private DatagramSocket server = null;
    private int port = 53;
    private String fakeIp = null;

    public FackDNSImpl(String fakeIp, int port) {
        this.port = port;
        this.fakeIp = fakeIp;
    }

    public void shutdown() throws IOException {
        if (this.server != null) {
            server.close();
        }
    }

    @Override
    public void run() {
        try {
            server = new DatagramSocket(port);
            logger.debug("DatagramSocket({})", port);
            while (true) {
                DatagramPacket packet = new DatagramPacket(new byte[512], 512);
                server.receive(packet);
                AnalizeDNS dns = new AnalizeDNS(packet, fakeIp);
                dns.parser();
                logger.info(String.format("Received data from: %s:%d with length: %d asking for:  %s", packet.getAddress().toString().substring(1), packet.getPort(), packet.getLength(), dns.getURL()));
                if ("github.com".equals(dns.getURL())) {
                    dns.poisoner(192, 30, 252, 128);
                } else {
                    dns.poisoner();
                }
                DatagramPacket packet_out = new DatagramPacket(new byte[dns.getpayloadLenght() + 62], dns.getpayloadLenght() + 62);
                packet_out.setAddress(packet.getAddress());
                packet_out.setPort(packet.getPort());
                packet_out.setData(dns.getpayload_FORGED());
                server.send(packet_out);
            }
        } catch (IOException ex) {
            logger.error(ex.getMessage(), ex);
        }
    }
}
