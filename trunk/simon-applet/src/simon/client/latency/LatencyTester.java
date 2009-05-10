package simon.client.latency;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

import javax.swing.table.TableModel;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;


public class LatencyTester extends Thread {
	//static Logger log = Logger.getLogger(LatencyTester.class);
	
	ArrayList<TestPoint> testPoints = new ArrayList<TestPoint>();
	LatencyTableModel latencyTableModel;
	Country country; 
	Graph graph;
	int countrynumber;
	int noLocation;
	int nsamples;
	Applet applet;
	public LatencyTester(Applet applet, Country country, int countrynumber, Graph graph, int nsamples) {
		this.country = country;
		this.latencyTableModel = new LatencyTableModel(this);
		this.graph = graph;
		this.countrynumber = countrynumber;
		this.nsamples = nsamples;
		this.applet = applet;
		noLocation = 0;
	}

	void add(TestPoint site) {
		testPoints.add(site);
		noLocation++;
	}
	
	ArrayList<TestPoint> getSamples() {
		return testPoints;
	}

	public void run() {
		AppletTester[] testPointThreads = new AppletTester[this.testPoints.size()];
		int i=0;
		for(TestPoint testPoint:this.testPoints) {
			    testPointThreads[i] = new AppletTester(this.applet, this, testPoint, nsamples);
			    testPointThreads[i].start();
			    i++;
		}
		// Wait all finish
		for(AppletTester testPointThread:testPointThreads) {
			try {
				testPointThread.join();
			} catch (InterruptedException e) {}
		}
		// Post
		try {
			CentralServer.postResults(this);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	TestPoint getLatency(TestPoint testPoint) throws IOException, InterruptedException {
		if (testPoint.testPointType== TestPointType.NTP)
			return getUDPLatency(testPoint);
		if (testPoint.testPointType== TestPointType.TCP)
			return getTcpLatency(testPoint);
		
		// Unknown protocol
		return null;
	}
	private TestPoint getTcpLatency(TestPoint testPoint) throws IOException {
		InetAddress ip = testPoint.ip;
		SocketAddress address = new InetSocketAddress(ip, 80);
		//log.debug("address=" + address);
		long ti=System.currentTimeMillis();
		Socket socket = new Socket();
		socket.connect(address);
		long dt=System.currentTimeMillis() - ti;
		socket.close();

		testPoint.addSample(dt);
		return testPoint;	
	}
	
	private TestPoint getUDPLatency(TestPoint testPoint) throws IOException, InterruptedException {
		// wait a bit (the same amount between tests)...
		// I think it could help not to create local buffers for the sent packets,
		// that could cause some delay and imprecision
		// It should give the user the feeling of a continuous testing...
		int time = 1 + (int)(Math.random() * 16000);
		Thread.sleep(time); 
		// Send request
		long rtt;
		DatagramSocket socket = new DatagramSocket();
		InetAddress address = testPoint.ip;
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
		   testPoint.addLost();
		}  
		finally {
			socket.close();
		}
		if (rtt != -1) {
			testPoint.addSample(rtt);
			graph.addSample(countrynumber, rtt);
		}
		return testPoint;	      
	}

	public long getNumLocations() {
		return noLocation;
	}
	
	public long getMin() {
        if (noLocation==0) return -1;
        else {
            long min=9999;
        	for (int i=0; i<noLocation; i++) {
			   if ((testPoints.get(i).getMinimum()<min) & (testPoints.get(i).getMinimum()!=-1)) min=testPoints.get(i).getMinimum();
        	}
        return min;	
        }	
	}
	
	public long getMax() {
        if (noLocation==0) return -1;
        else {
            long max=0;
        	for (int i=0; i<noLocation; i++) {
			   if (testPoints.get(i).getMaximum()>max) max=testPoints.get(i).getMaximum();
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
			   sum += ((testPoints.get(i).getAverage())*(testPoints.get(i).getNumSamples()));
			   samples += testPoints.get(i).getNumSamples();
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
			   samples += testPoints.get(i).getNumSamples();
        	}
        return samples;	
        }	
	}
	
	public long getLost() {
        if (noLocation==0) return -1;
        else {
        	long losts = 0;
            for (int i=0; i<noLocation; i++) {
			   losts += testPoints.get(i).getLost();
        	}
        return losts;	
        }	
	}
	
	public String getTotal() {
	    if (noLocation==0) return "0/0";
	    else return Long.toString((this.getNumSamples())+(this.getLost()))+"/"+Long.toString(noLocation*nsamples);
	}
	
	
	public String toString() {
		return country.countryCode + " -> Min: " + getMin() + "ms; Average: " + getAverage() + "ms; Max: " + getMax() + "ms.";
	}

	public Object getData() {
		return this.country.countryCode +  ", " + this.getMin()  + ", " + this.getAverage()+ ", " + this.getMax()  + ", " + getNumSamples() + "\n";
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
		if (columnIndex==0) return this.country.countryName;
		if (columnIndex==1) return this.getMin();
		if (columnIndex==2) return this.getAverage();
		if (columnIndex==3) return this.getMax();
		if (columnIndex==4) return this.getNumSamples();
		if (columnIndex==5) return this.getLost();
		if (columnIndex==6) return this.getTotal();
		return "?";
	}
	

	TableModel getTableModel() {
		return latencyTableModel;
	}
	
}
