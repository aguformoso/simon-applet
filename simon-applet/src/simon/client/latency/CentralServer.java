package simon.client.latency;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.TimeZone;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.log4j.Logger;

public class CentralServer {
	static Logger log = Logger.getLogger(CentralServer.class);
	static SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
	static SimpleDateFormat timeFormatter = new SimpleDateFormat("hh:mm:ss");
	// Global Test Points
	static ArrayList<TestPoint> testPoints = new ArrayList<TestPoint>();
	
	// Test Points per Country
	static Hashtable<String,ArrayList<TestPoint> > testPointsByContryCode = 
		new Hashtable<String,ArrayList<TestPoint> >();
	
	// Parametros
	static String postUrl;
	static Country localCountry;
	
	static void retrieveTestoPoints(String parametersUrl) {
		log.info("Retrieving test points");
		HttpClient client = new HttpClient();
        GetMethod getMethod = new GetMethod(parametersUrl);
        
        try {
        	String line;
			int statusCode = client.executeMethod(getMethod);
			//System.out.println("statusCode:"+statusCode);
			InputStream is= getMethod.getResponseBodyAsStream();
			BufferedReader in = new BufferedReader(new InputStreamReader(is));
			while( (line=in.readLine()) != null) {
				//System.out.println("line:"+line);
				try {
					TestPoint testPoint = new TestPoint(line);
					// add it to the global list
					testPoints.add(testPoint);
					// add it to the country list
					ArrayList<TestPoint> tps = testPointsByContryCode.get(testPoint.countryCode);
					if (tps==null) {
						// if list for country does not exist, create it
						tps = new ArrayList<TestPoint>();
						testPointsByContryCode.put(testPoint.countryCode, tps);
					}
					tps.add(testPoint);
				} catch (Exception e) {
					System.err.println("Error:" + e);
				}
			}
			getMethod.releaseConnection();
			ArrayList<TestPoint> tps = testPointsByContryCode.get("CL");
			try {
				tps.add(new TestPoint("999,Chile,tcp_web,200.1.123.3,CL,2009-05-08 00:00:00"));
				tps.add(new TestPoint("999,Chile,tcp_web,146.82.90.31,CL,2009-05-08 00:00:00"));
				tps.add(new TestPoint("999,Chile,tcp_dns,200.1.123.4,CL,2009-05-08 00:00:00"));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (HttpException e) {
			log.error("Error Connecting Server:" + e);
		} catch (IOException e) {
			log.error("Error Connecting Server:" + e);
		}   
		
		// Experimental
		
	}
	
	static public Enumeration<String> getContryCodes() {
		return testPointsByContryCode.keys();
	}
	
	static public List<TestPoint> getTestPointsByCountry(Country country) {
		if (country==null || country.countryCode==null) 
			return new ArrayList<TestPoint>();
		
		List<TestPoint> testPointsForCountry = testPointsByContryCode.get(country.countryCode);
		if (testPointsForCountry==null) 
			return new ArrayList<TestPoint>();
		return testPointsForCountry;
	}
	
	
	private static synchronized void postResults(String url,String data) throws Exception {
		log.info("Opening conection");
		HttpClient client = new HttpClient();
        PostMethod postMethod = new PostMethod(url);
        //System.out.println("Uploading:\n" + data);
        //client.setConnectionTimeout(8000);
        postMethod.setRequestBody(data);
        postMethod.setRequestHeader("Content-type","text/xml; charset=ISO-8859-1");
        log.info("Sending");
        log.debug("POST\n"+ data);
        int statusCode = client.executeMethod(postMethod);
       
        // Results
        InputStream is= postMethod.getResponseBodyAsStream();
		BufferedReader in = new BufferedReader(new InputStreamReader(is));
		String resultLine = in.readLine();
		log.info("Returned:"+ resultLine);
		String result = resultLine+"\n";
		String line;
		while( (line=in.readLine())!=null) {
			result += line+"\n";
		}
		
		log.debug("REPLY\n" + result);
		// TODO: fix once roque send us just a error code
		//System.out.println("Result:" + line);
		if (line!=null && line.indexOf("Success") != -1) {
			log.info("Post accepted");
			postMethod.releaseConnection();
			return;
		} else {
			log.error("Server returned error: "  + resultLine);
			log.info("Returned:"+ resultLine);
			postMethod.releaseConnection();
			throw new Exception(resultLine);
		}
	}
	
	static void postXmlResults(String url, LatencyTester latencyTester, Country country) throws Exception {
		// I am doing this, this way (old school), to avoid include in the applet all the 
		// libs required to do it with the current implementations
		StringBuffer data = new StringBuffer();
		int zonehh=(TimeZone.getDefault().getRawOffset()/60000)/60;
		int zonemm=(TimeZone.getDefault().getRawOffset()/60000)%60;
		
		data.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		data.append("<simon xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n");
		data.append("<version>1</version>\n");
		data.append("<date>" + dateFormatter.format(System.currentTimeMillis()) + "</date>\n");
		data.append("<time>" + timeFormatter.format(System.currentTimeMillis()) + 
							   String.format("%+03d:%02d", zonehh, zonemm) +"</time>\n");
		data.append("<local_country>"+country.countryCode+"</local_country>\n");
		
		for(TestPoint testPoint:latencyTester.testPoints) {
			if (testPoint.ip != null) {
				//String testtype=null;
				//if (testPoint.testPointType==TestPointType.NTP) testtype="ntp";
				//if (testPoint.testPointType==TestPointType.TCP) testtype="tcp_connection";
				//if (testPoint.testPointType==TestPointType.ICMP) testtype="icmp_echo";
				if (testPoint.isOk()) {
					data.append("<test>\n");
					data.append("    <destination_ip>" + testPoint.ip.getHostAddress() + "</destination_ip>\n");
					data.append("    <testtype>" + testPoint.testPointType + "</testtype>\n");
					data.append("    <number_probes>" + testPoint.getNumSamples() + "</number_probes>\n");
					data.append("    <min_rtt>" + testPoint.getMinimum() + "</min_rtt>\n");
					data.append("    <max_rtt>" + testPoint.getMaximum() + "</max_rtt>\n");
					data.append("    <ave_rtt>" + testPoint.getAverage() + "</ave_rtt>\n");
					data.append("    <dev_rtt>" + testPoint.getStdDev() + "</dev_rtt>\n");
					data.append("    <median_rtt>" + testPoint.getMedian() + "</median_rtt>\n");
					data.append("    <packet_loss>" + 0 +  "</packet_loss>\n");
					data.append("</test>\n");			
				}
			}
		}
		data.append("</simon>\n");
		postResults(url, data.toString());
	}

	static void postResults(LatencyTester latencyTester) throws Exception {
		postXmlResults(postUrl,latencyTester, localCountry );
	}

	public static void setPostUrl(String postUrl) {
		CentralServer.postUrl = postUrl;
	}

	public static void setLocalCountry(Country localCountry) {
		CentralServer.localCountry = localCountry;
	}
}
