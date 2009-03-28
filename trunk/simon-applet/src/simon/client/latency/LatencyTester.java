package simon.client.latency;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;

import org.apache.log4j.Logger;


public class LatencyTester {
	//static Logger log = Logger.getLogger(LatencyTester.class);
	/**
	 * @param args
	 * @throws IOException 
	 */
	
	static long getTcpLatency(String host) throws IOException {
		InetAddress ip = InetAddress.getByName(host);
		SocketAddress address = new InetSocketAddress(ip, 80);
		//log.debug("address=" + address);
		long ti=System.currentTimeMillis();
		Socket socket = new Socket();
		socket.connect(address);
		long dt=System.currentTimeMillis() - ti;
		socket.close();
		return dt;
		
	}
	public static void main(String[] args) throws IOException {	
		String[] sites = {"www.arin.net", "www.nic.br", "www.nic.cl", "www.nic.pe", "www.nic.bo", "www.nic.ar", "www.nic.uy", "www.nic.pa", "www.nic.co", "www.nic.ve", "www.nic.ec"};
		for(String site:sites) {
			//log.info("Latency to " + site + " is "+ getTcpLatency(site) + " ms");
			//log.info("Latency to " + site + " is "+ getTcpLatency(site) + " ms");
			//log.info("Latency to " + site + " is "+ getTcpLatency(site) + " ms");
		}
	}

}
