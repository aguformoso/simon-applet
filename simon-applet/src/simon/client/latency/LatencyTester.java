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
import java.util.Collections;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.table.TableModel;


import org.apache.log4j.Logger;

import simon.client.latency.testers.NtpTester;
import simon.client.latency.testers.TcpTester;
import simon.client.latency.testers.Tester;


public class LatencyTester extends Thread {
	static Logger log = Logger.getLogger(LatencyTester.class);
	
	ArrayList<TestPoint> testPoints = new ArrayList<TestPoint>();
	
	public ArrayList<Integer> countrySamples = new ArrayList<Integer>();
		
	LatencyTableModel latencyTableModel;
	Country country; 
	//Graph graph;
	int countrynumber;
	int numTestPoints;
	int nsamples;
	Applet applet;
	boolean finished=false;
	
	int expectedSamples;
	int capturedSamples = 0;
	int lostSamples = 0;
	
	enum Status { NOSTARTED, TESTING, POSTING, DONE, ERROR };
	
	Status status = Status.NOSTARTED;
	
	public LatencyTester(Applet applet, Country country, int countrynumber, int nsamples) {
		this.setName("tester(" + country.countryCode + ")");
		this.country = country;
		this.latencyTableModel = new LatencyTableModel(this);
		//this.graph = graph;
		this.countrynumber = countrynumber;
		this.nsamples = nsamples;
		this.applet = applet;
		numTestPoints = 0;
	}

	void add(TestPoint site) {
		testPoints.add(site);
		numTestPoints++;
		this.expectedSamples += nsamples;
	}
	
	ArrayList<TestPoint> getSamples() {
		return testPoints;
	}

	public void run() {
		log.info("Starting");
		Tester[] testPointThreads = new Tester[this.testPoints.size()];
		int i=0;
		this.status = Status.TESTING;
		
		for(TestPoint testPoint:this.testPoints) {
				if (testPoint.testPointType==TestPointType.ntp) {
					testPointThreads[i] = new NtpTester(this.applet, this, testPoint,nsamples);
				}
				if (testPoint.testPointType==TestPointType.tcp_dns) {
					testPointThreads[i] = new TcpTester(this.applet, this, testPoint, nsamples);
				}
				if (testPoint.testPointType==TestPointType.tcp_web) {
					testPointThreads[i] = new TcpTester(this.applet, this, testPoint,nsamples);
				}
			    // Inicia
			    if ( testPointThreads[i] != null) {
			    	testPointThreads[i].start();
			    }    
			    i++;
		}
		
		// Wait all finish
		log.info("Running tests..");
		for(Tester testPointThread:testPointThreads) {
			try {
				if (testPointThread!=null) 
					testPointThread.join();
			} catch (InterruptedException e) {}
		}
		
		// Post
		log.info("Posting results..");
		this.status = Status.POSTING;
		try {
			CentralServer.postResults(this);
			this.status = Status.DONE;
		} catch (Exception e) {
			log.error("Error during post: " + e,e);
			this.status = Status.ERROR;
		}
		//this.applet.repaint();
		log.info("Finishing...");
		this.finished=true;
		// Notifies that have finished
		this.applet.finishedTesterCallbak(this);
		
	}
	public boolean isFinished() {
		return this.finished;
	}
	
	public long getNumLocations() {
		return numTestPoints;
	}
	
	public long getMin() {
        if (numTestPoints==0) return -1;
        else {
            long min=9999;
        	for (int i=0; i<numTestPoints; i++) {
			   if ((testPoints.get(i).getMinimum()<min) & (testPoints.get(i).getMinimum()!=-1)) min=testPoints.get(i).getMinimum();
        	}
        return min;	
        }	
	}
	
	public long getMax() {
        if (numTestPoints==0) return -1;
        else {
            long max=0;
        	for (int i=0; i<numTestPoints; i++) {
			   if (testPoints.get(i).getMaximum()>max) max=testPoints.get(i).getMaximum();
        	}
        return max;	
        }	
	}
	
	public long getAverage() {
        if (numTestPoints==0) return -1;
        else {
        	long sum = 0;
        	long samples = 0;
            for (int i=0; i<numTestPoints; i++) {
			   sum += ((testPoints.get(i).getAverage())*(testPoints.get(i).getNumSamples()));
			   samples += testPoints.get(i).getNumSamples();
        	}
        if (samples==0) return -1;
        else return sum/samples;	
        }	
	}
	public long getMedian() {
		if (numTestPoints==0) return -1;	
		ArrayList<Long> myArray=new ArrayList<Long>();
		for (int i=0; i<numTestPoints; i++) {
			if ((testPoints.get(i).getMedian())>0) myArray.add(testPoints.get(i).getMedian());
		}
        Collections.sort(myArray);
        int arrayLength = 0;
        long arrayMedian = 0;
        int currentIndex = 0;
        arrayLength = myArray.size();
        if (arrayLength==0) return -1;
        if(arrayLength % 2 != 0) {    
        	currentIndex = ((arrayLength / 2) + 1);
            arrayMedian = myArray.get(currentIndex - 1);
        }
        else {    
        	int indexOne = (arrayLength / 2);
            int indexTwo = arrayLength / 2 + 1;
            long arraysSum = myArray.get(indexOne - 1) + myArray.get(indexTwo - 1);
            arrayMedian = arraysSum / 2;
        }
        return arrayMedian;
    }
	
	public long getNumSamples() {
        if (numTestPoints==0) return -1;
        else {
        	long samples = 0;
            for (int i=0; i<numTestPoints; i++) {
			   samples += testPoints.get(i).getNumSamples();
        	}
        return samples;	
        }	
	}
	
	public long getLost() {
        if (numTestPoints==0) return -1;
        else {
        	long losts = 0;
            for (int i=0; i<numTestPoints; i++) {
			   losts += testPoints.get(i).getLost();
        	}
        return losts;	
        }	
	}
	
	public String getTotal() {
	    if (numTestPoints==0) return "0/0";
	    else return Long.toString((this.getNumSamples())+(this.getLost()))+"/"+Long.toString(numTestPoints*nsamples);
	}
	
	int getPercent() {
		//return  (int) ((this.getNumSamples()+this.getLost())*100/(numTestPoints*nsamples));
		if (this.status==Status.POSTING) return 200;
		if (this.status==Status.DONE) return 300;
		if (this.status==Status.ERROR) return -1;
		
		//log.info("PERCENT: " + this.capturedSamples+ "," + this.lostSamples+ "," + this.expectedSamples);
		if (this.expectedSamples>0)
			return (100*(this.capturedSamples+this.lostSamples))/this.expectedSamples;
		
		return 0;
	}
	// replaced by a cell Renderer
	/*
	public String getComplete() {
	    if (noLocation==0) return ".................... 0%";
	    long percent = getPercent();
	    String sp = "....................";
	    if (percent>=05) sp="|...................";
	    if (percent>=10) sp="||..................";
	    if (percent>=15) sp="|||.................";
	    if (percent>=20) sp="||||................";
	    if (percent>=25) sp="|||||...............";
	    if (percent>=30) sp="||||||..............";
	    if (percent>=35) sp="|||||||.............";
	    if (percent>=40) sp="||||||||............";
	    if (percent>=45) sp="|||||||||...........";
	    if (percent>=50) sp="||||||||||..........";
	    if (percent>=55) sp="|||||||||||.........";
	    if (percent>=60) sp="||||||||||||........";
	    if (percent>=65) sp="|||||||||||||.......";
	    if (percent>=70) sp="||||||||||||||......";
	    if (percent>=75) sp="|||||||||||||||.....";
	    if (percent>=80) sp="||||||||||||||||....";
	    if (percent>=85) sp="|||||||||||||||||...";
	    if (percent>=90) sp="||||||||||||||||||..";
	    if (percent>=95) sp="|||||||||||||||||||.";
	    if (percent>=100) sp="||||||||||||||||||||";
	    return sp+" "+Long.toString(percent)+"%";
	}
	*/
	
	public String toString() {
		return country.countryCode + " -> Min: " + getMin() + "ms; Average: " + getAverage() + "ms; Max: " + getMax() + "ms.";
	}

	public Object getData() {
		return this.country.countryCode +  ", " + this.getMin()  + ", " + this.getAverage()+ ", " + this.getMax()  + ", " + getNumSamples() + "\n";
	}
	
	static String [] columnNamesDetailed = { "Region", "min", "avg", "max", "samples", "losts", "total"};
	static String [] columnNamesSimple = { "Country/Region", "latency (typ)", "% Test" ,"Dispersion"};
	
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
	
	static public Class<?> getColumnClassSimple(int columnIndex) {
		if (columnIndex==0) return String.class;
		if (columnIndex==1) return String.class;
		if (columnIndex==2) return Long.class;
		if (columnIndex==3) return (new ArrayList<Integer>()).getClass();
		return String.class;
	}
	
	public Object getColumnSimple(int columnIndex) {
		if (columnIndex==0) return this.country.countryName;
		if (columnIndex==1) return Long.toString(this.getMedian())+" ms ";
		if (columnIndex==2) return new Integer(getPercent());
		if (columnIndex==3) return countrySamples;
		return "?";
	}
	

	TableModel getTableModel() {
		return latencyTableModel;
	}

	public void addCountrySamples(Integer integer) {
		synchronized(countrySamples) {
			countrySamples.add(integer);
			this.capturedSamples++;
		}
		
	}
	public void addCountrySamples() {
		this.lostSamples++;
	}
}
