package simon.client.latency;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

import javax.swing.table.TableModel;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;


public class LatencyTester {
	//static Logger log = Logger.getLogger(LatencyTester.class);
	
	ArrayList<LatencyLocation> location = new ArrayList<LatencyLocation>();
	LatencyTableModel latencyTableModel;
	String country; 
	Graph graph;
	int countrynumber;
	int noLocation;
	int nsamples;
	
	public LatencyTester(String country, int countrynumber, Graph graph, int nsamples) {
		this.country = country;
		this.latencyTableModel = new LatencyTableModel(this);
		this.graph = graph;
		this.countrynumber = countrynumber;
		this.nsamples = nsamples;
		noLocation = 0;
	}

	void add(LatencyLocation site) {
		location.add(site);
		noLocation++;
	}
	
	ArrayList<LatencyLocation> getSamples() {
		return location;
	}

	LatencyLocation getUDPLatency(LatencyLocation stats) throws IOException, InterruptedException {
		// wait a bit (the same amount between tests)...
		// I think it could help not to create local buffers for the sent packets,
		// that could cause some delay and imprecision
		// It should give the user the feeling of a continuous testing...
		int time = 1 + (int)(Math.random() * 16000);
		Thread.sleep(time); 
		// Send request
		long rtt;
		DatagramSocket socket = new DatagramSocket();
		InetAddress address = InetAddress.getByName(stats.destination);
		byte[] buf = new NtpMessage().toByteArray();
		DatagramPacket packet = new DatagramPacket(buf, buf.length, address, 123);	
		NtpMessage.encodeTimestamp(packet.getData(), 40, (System.nanoTime()/1000000000.0) + 2208988800.0);		
		socket.send(packet);
		// Get response
		packet = new DatagramPacket(buf, buf.length);
		socket.setSoTimeout(2000); // wait (max) two seconds for a response
		try {
		   socket.receive(packet);		
		   double destinationTimestamp = (System.nanoTime()/1000000000.0) + 2208988800.0;	
		   // Process response
		   // We only need to calculate RTT...
		   // The local time not need to be accurate to do it!
		   NtpMessage msg = new NtpMessage(packet.getData());
		   Double dt = 1000*((destinationTimestamp-msg.originateTimestamp) - (msg.transmitTimestamp-msg.receiveTimestamp));
		   rtt = dt.longValue();		   
		}
		catch (SocketTimeoutException e) {
		   rtt = -1;
		   System.out.println("socket timeout");
		   stats.addLost();
		}  
		finally {
			socket.close();
		}
		if (rtt != -1) {
			stats.addSample(rtt);
			graph.addSample(countrynumber, rtt);
		}
		return stats;	      
	}

	public long getNumLocations() {
		return noLocation;
	}
	
	public long getMin() {
        if (noLocation==0) return -1;
        else {
            long min=9999;
        	for (int i=0; i<noLocation; i++) {
			   if ((location.get(i).getMin()<min) & (location.get(i).getMin()!=-1)) min=location.get(i).getMin();
        	}
        return min;	
        }	
	}
	
	public long getMax() {
        if (noLocation==0) return -1;
        else {
            long max=0;
        	for (int i=0; i<noLocation; i++) {
			   if (location.get(i).getMax()>max) max=location.get(i).getMax();
        	}
        return max;	
        }	
	}
	
	public long getAverage() {
        if (noLocation==0) return -1;
        else {
        	long sum = 0;
        	long samples = 0;
            for (int i=0; i<noLocation; i++) {
			   sum += ((location.get(i).getAverage())*(location.get(i).getNumSamples()));
			   samples += location.get(i).getNumSamples();
        	}
        if (samples==0) return -1;
        else return sum/samples;	
        }	
	}
	
	public long getNumSamples() {
        if (noLocation==0) return -1;
        else {
        	long samples = 0;
            for (int i=0; i<noLocation; i++) {
			   samples += location.get(i).getNumSamples();
        	}
        return samples;	
        }	
	}
	
	public long getLost() {
        if (noLocation==0) return -1;
        else {
        	long losts = 0;
            for (int i=0; i<noLocation; i++) {
			   losts += location.get(i).getLost();
        	}
        return losts;	
        }	
	}
	
	public String getTotal() {
	    if (noLocation==0) return "0/0";
	    else return Long.toString((this.getNumSamples())+(this.getLost()))+"/"+Long.toString(noLocation*nsamples);
	}
	
	
	public String toString() {
		return country + " -> Min: " + getMin() + "ms; Average: " + getAverage() + "ms; Max: " + getMax() + "ms.";
	}

	public Object getData() {
		return this.country +  ", " + this.getMin()  + ", " + this.getAverage()+ ", " + this.getMax()  + ", " + getNumSamples() + "\n";
	}
	
	static String [] columnNames = { "Region", "min", "avg", "max", "samples", "losts", "total"};
	
	static public Class<?> getColumnClass(int columnIndex) {
		if (columnIndex==0) return String.class;
		if (columnIndex==1) return Long.class;
		if (columnIndex==2) return Long.class;
		if (columnIndex==3) return Long.class;
		if (columnIndex==4) return Long.class;
		if (columnIndex==5) return Long.class;
		if (columnIndex==6) return String.class;
		return String.class;
	}
	
	public Object getColumn(int columnIndex) {
		if (columnIndex==0) return this.country;
		if (columnIndex==1) return this.getMin();
		if (columnIndex==2) return this.getAverage();
		if (columnIndex==3) return this.getMax();
		if (columnIndex==4) return this.getNumSamples();
		if (columnIndex==5) return this.getLost();
		if (columnIndex==6) return this.getTotal();
		return "?";
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
	
	TableModel getTableModel() {
		return latencyTableModel;
	}
	
}
