package simon.client.latency;


import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.swing.table.TableModel;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.log4j.Logger;


public class LatencyTester {
	//static Logger log = Logger.getLogger(LatencyTester.class);
	/**
	 * @param args
	 * @throws IOException 
	 */
	
	ArrayList<LatencyLocation> samples = new ArrayList<LatencyLocation>();
	
	LatencyTableModel latencyTableModel;
	String[] sites; 
	public LatencyTester(String[] sites) {
		this.sites = sites;
		this.latencyTableModel = new LatencyTableModel(this);
	}

	void add(LatencyLocation sample) {
		samples.add(sample);
	}
	ArrayList<LatencyLocation> getSamples() {
		return samples;
	}
	
	LatencyLocation getTcpLatency(LatencyLocation stats) throws IOException {
		InetAddress ip = InetAddress.getByName(stats.destination);
		SocketAddress address = new InetSocketAddress(ip, 80);
		//log.debug("address=" + address);
		long ti=System.currentTimeMillis();
		Socket socket = new Socket();
		socket.connect(address);
		long dt=System.currentTimeMillis() - ti;
		socket.close();

		stats.addSample(dt);
		return stats;	
	}
	
	static void postResults(String url,String data) throws HttpException, IOException {
		HttpClient client = new HttpClient();
        PostMethod postMethod = new PostMethod(url);

        client.setConnectionTimeout(8000);
        postMethod.setRequestBody(data);

        postMethod.setRequestHeader("Content-type","text/xml; charset=ISO-8859-1");

        int statusCode1 = client.executeMethod(postMethod);
        postMethod.releaseConnection();
        System.out.println("Uploaded");
	}
	
	static void postResults(String url, ArrayList<LatencyLocation> samples) throws HttpException, IOException {
		
		StringBuffer data = new StringBuffer();
		for(LatencyLocation sample:samples) {
			data.append(sample.getData());
		}
		
		postResults(url, data.toString());
	}
	
	public static void main(String[] args) throws IOException {	
		/*String[] sites = {"www.arin.net", "www.nic.br", "www.nic.cl", "www.nic.pe", "www.nic.bo", "www.nic.ar", "www.nic.uy", "www.nic.pa", "www.nic.co", "www.nic.ve", "www.nic.ec"};
		//String[] sites = {"www.arin.net"};
		LatencyTester latencyTester = new LatencyTester(sites);
		ArrayList<LatencySample> samples = new ArrayList<LatencySample>();
		
		for(String site:sites) {
			LatencySample sample = latencyTester.getTcpLatency(site) ;
			samples.add(sample);
			System.out.println(sample);
		}
		
		postResults("http://127.0.0.1/~jmguzman/tests/getresults.php", samples);
	*/	
	}

	TableModel getTableModel() {
		return latencyTableModel;
	}
	
}
