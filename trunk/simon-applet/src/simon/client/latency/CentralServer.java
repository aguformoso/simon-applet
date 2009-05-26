package simon.client.latency;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.TimeZone;

import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.util.EntityUtils;
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
	
	// HTTP Client
	
	//static HttpConnectionManagerParams httpConnectionManagerParams;
	//static MultiThreadedHttpConnectionManager httpConnectionManager;
	static HttpClient httpClient;
	
	// Parametros
	static String postUrl;
	static Country localCountry;
	
	static {
		// Create and initialize HTTP parameters
        HttpParams params = new BasicHttpParams();
        ConnManagerParams.setMaxTotalConnections(params, 100);
        ConnManagerParams.setMaxConnectionsPerRoute(params, new ConnPerRouteBean(100) );
        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
        
		// Create and initialize scheme registry 
        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        schemeRegistry.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));
        
        // Create an HttpClient with the ThreadSafeClientConnManager.
        // This connection manager must be used if more than one thread will
        // be using the HttpClient.
        ClientConnectionManager cm = new ThreadSafeClientConnManager(params, schemeRegistry);
        httpClient = new DefaultHttpClient(cm, params);

        /*
		// Initialize a multithreaded http client
		httpConnectionManagerParams = new HttpConnectionManagerParams();
		httpConnectionManager = new MultiThreadedHttpConnectionManager();
		httpConnectionManagerParams = httpConnectionManager.getParams();
		httpConnectionManagerParams.setDefaultMaxConnectionsPerHost(100);
		
		httpConnectionManagerParams.setMaxTotalConnections(100);		
		httpConnectionManagerParams.setConnectionTimeout(8000);
		httpClient = new HttpClient(httpConnectionManager);
		*/
	}
	
	static void retrieveTestoPoints(String parametersUrl) {
		log.info("Retrieving test points");
		//HttpClient client = new HttpClient();
		HttpGet getMethod = new HttpGet(parametersUrl);
        
        try {
        	String line;
			//int statusCode = client.executeMethod(getMethod);
			HttpResponse response = httpClient.execute(getMethod, new BasicHttpContext());
			//System.out.println("statusCode:"+statusCode);
			HttpEntity entity = response.getEntity();
			//InputStream is= getMethod.getResponseBodyAsStream();
			BufferedReader in = new BufferedReader(new StringReader(EntityUtils.toString(entity)));
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
			//getMethod.releaseConnection();
			ArrayList<TestPoint> tps = testPointsByContryCode.get("CL");
			try {
				tps.add(new TestPoint("999,Chile,tcp_web,200.1.123.3,CL,2009-05-08 00:00:00"));
				tps.add(new TestPoint("999,Chile,tcp_web,146.82.90.31,CL,2009-05-08 00:00:00"));
				tps.add(new TestPoint("999,Chile,tcp_dns,200.1.123.4,CL,2009-05-08 00:00:00"));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		//} catch (HttpException e) {
		//	log.error("Error Connecting Server:" + e);
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
	
	
	private static void postResults(String url,String data) throws Exception {
		log.info("Opening conection");
		
		//HttpMethodParams httpMethodParams = new HttpMethodParams();
		//httpMethodParams.setVersion(HttpVersion.HTTP_1_0);
		
		
		long ti = System.currentTimeMillis();
	/*
        PostMethod postMethod = new PostMethod(url);
       // postMethod.getParams()
        //System.out.println("Uploading:\n" + data);
        //httpClient.setConnectionTimeout(8000);
        postMethod.setRequestBody(data);
       // postMethod.getParams().setVersion(HttpVersion.HTTP_1_0);
        postMethod.setRequestHeader("Content-type","text/xml; charset=ISO-8859-1");
        //postMethod.getHostConfiguration()
		httpConnectionManagerParams.setMaxConnectionsPerHost(httpClient.getHostConfiguration(), 100);
		
		 log.info("Sending");
        log.debug("POST\n"+ data);
        int statusCode = 0;
        
        try {
        	statusCode = httpClient.executeMethod(postMethod);
        } catch (SocketException e) {
        	// Si hay timeout, trata de nuevo
        	log.warn(e.getMessage() + ": reintentando..");
        	statusCode = httpClient.executeMethod(postMethod);
        }
        if (statusCode!=200) {
        	long dt=System.currentTimeMillis()-ti;
        	log.warn("Status Code:"+ statusCode + " in " + dt +" ms");
        	postMethod.releaseConnection();
			throw new Exception("Status Code:"+ statusCode);
        }
        log.info("Reading");
          InputStream is= postMethod.getResponseBodyAsStream();
		BufferedReader in = new BufferedReader(new InputStreamReader(is));

	 */
		HttpPost postMethod = new HttpPost(url);
		StringEntity reqEntity = new StringEntity(data);
		reqEntity.setContentType("text/xml; charset=ISO-8859-1");
		postMethod.setEntity(reqEntity);
		HttpResponse response = httpClient.execute(postMethod, new BasicHttpContext());
		HttpEntity entity = response.getEntity();
		BufferedReader in = new BufferedReader(new StringReader(EntityUtils.toString(entity)));
		
		

        // Results
      
		String result = "";
		String line;
		boolean ok=false;
		while( (line=in.readLine())!=null) {
			result += line+"\n";
			if (line !=null && line.startsWith("END")) {
				log.info("END received");
				ok=true;
			}
		}
        long dt=System.currentTimeMillis()-ti;

		if (ok) {
			log.info("Post accepted in " + dt + " ms");
			//postMethod.releaseConnection();
			return;
		} else {
			log.error("Post NOT accepted in " + dt + " ms!" );
			log.debug("Returned:"+ result);
			//postMethod.releaseConnection();
			throw new Exception("POST not accepted");
		}
	}
	
	static void postXmlResults(String url, LatencyTester latencyTester, Country country) throws Exception {
		postXmlResults(url, latencyTester.testPoints, country);
	}

	static void postXmlResults(String url, ArrayList<TestPoint> testPoints, Country country) throws Exception {
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
		
		for(TestPoint testPoint:testPoints) {
			if (testPoint.ip != null) {
				if (testPoint.isOk()) {
					data.append("<test>\n");
					data.append("<destination_ip>" + testPoint.ip.getHostAddress() + "</destination_ip>\n");
					data.append("<testtype>" + testPoint.testPointType + "</testtype>\n");
					data.append("<number_probes>" + testPoint.getNumSamples() + "</number_probes>\n");
					data.append("<min_rtt>" + testPoint.getMinimum() + "</min_rtt>\n");
					data.append("<max_rtt>" + testPoint.getMaximum() + "</max_rtt>\n");
					data.append("<ave_rtt>" + testPoint.getAverage() + "</ave_rtt>\n");
					data.append("<dev_rtt>" + testPoint.getStdDev() + "</dev_rtt>\n");
					data.append("<median_rtt>" + testPoint.getMedian() + "</median_rtt>\n");
					data.append("<packet_loss>" + 0 +  "</packet_loss>\n");
					data.append("</test>\n");			
				}
			}
		}
		data.append("</simon>\n\n");
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
	
	public static void main(String[] args) throws Exception {
		String xml = "";
		
		

		//CentralServer.postXmlResults("http://10.0.0.72:1234/cgi-bin/simonpost.cgi", testPoints, new Country("XX","test"));
		for (int i=0;i<10;i++) {
			Thread test = new Thread() {
				public void run() {
					try {
						ArrayList<TestPoint> testPoints = new ArrayList<TestPoint>();
						for (int i=0;i<12;i++) 
						{
							TestPoint testPoint = new TestPoint("999,Chile,tcp_web,200.1.123."+i+",CL,2009-05-08 00:00:00");
							testPoint.addSample(10);
							testPoint.addSample(20);
							testPoint.addSample(30);
							testPoints.add(testPoint);
						}
						CentralServer.postXmlResults("http://simon.lacnic.net/cgi-bin/simonpost.cgi", testPoints, new Country("XX","test"));
					} catch (Exception e) {}
					
				}
			};
			test.start();
		}
		
	}
}
