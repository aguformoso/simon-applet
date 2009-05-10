package simon.client.latency;

import java.awt.Container;
import java.awt.Font;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.UnsupportedLookAndFeelException;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.table.TableColumn;

import org.apache.commons.httpclient.HttpException;

public class Applet extends JApplet implements ActionListener{
	
	private static final long serialVersionUID = 1L;
	//static Logger log = Logger.getLogger(Applet.class);
	
	int WIDTH = 720;
	int HEIGHT = 480;
	
	JTextArea logArea;
    JButton startButton;
    JTable tables[];
    JScrollPane scrollPanels[];
    JTable table_all;

    // Countries
    Country[] countries = new Country[]{
    		// Continents (pseudo-countries)
   // 		new Country("na","North America"),
   // 		new Country("eu","Europe"),
   // 		new Country("as","Asia"),
    		// Countries
    		new Country("AR","Argentina"),
    		new Country("BO","Bolivia"),
    		new Country("BR","Brasil"),
    		new Country("CL","Chile"),
    		new Country("CO","Colombia"),
    		new Country("CR","Costa Rica"),
    		new Country("DO","Argentina"),
    		new Country("AR","Republica Dominicana"),
    		new Country("EC","Ecuador"),
    		new Country("SV","El Salvador"),
    		new Country("GT","Guatemala"),
    		new Country("HN","Honduras"),
    		new Country("MX","Mexico"),
    		new Country("NI","Nicaragua"),
    		new Country("PA","Panama"),
    		new Country("PY","Paraguay"),
    		new Country("PE","Peru"),
    		new Country("UY","Uruguay"),
    		new Country("VE","Venezuela"),
    };
    
    /*
    int ncountries = 21;
    String[] countries = {"North America", "Europe", "Asia", 
    		"Argentina", "Bolivia", "Brasil", "Chile",
    		"Colombia", "Costa Rica", "Republica Dominicana", 
    		"Equador", "El Salvador", "Guatemala", "Haiti",
    		"Mexico", "Nicaragua", "Panama", "Paraguay",
    		"Peru", "Uruguay", "Venezuela" };
    String[] country_codes = {"na","eu","as",
    		"AR","BO","BR","CL",
    		"CO","CR","DO",
    		"EC","SV","GT","HN",
    		"MX","NI","PA","PY",
    		"PE","UY","VE" };   
    // NTP servers
    // Stratum 1, 2 or 3 NTP public servers 
    String[][] ntp = {
    // 00 North America - na (RIPE TTM Boxes and others)
    {"192.122.183.212", "194.25.6.14", "216.98.125.4", "64.90.182.55",
     "128.118.25.5", "209.51.161.238", "68.248.203.43", "140.142.16.34",
     "128.233.154.245", "136.159.2.9", "199.212.17.22", "142.3.100.15"},   
    // 01 Europe - eu (RIPE TTM Boxes and others)
    {"193.0.0.228", "194.25.0.198", "89.186.245.200", "195.66.248.74",
     "93.189.33.204", "212.95.210.35", "194.117.9.136", "213.203.238.82",
     "145.238.203.10", "69.46.228.178", "62.48.35.100", "87.239.10.190"},   
    // 02 Asia - as (TTM Boxes and others)
    {"192.107.171.131", "210.130.188.44", "203.133.248.3", "128.250.80.34",
     "133.243.238.244", "58.73.137.250", "81.91.129.95", "120.88.47.10",
     "202.71.97.92", "202.134.1.10", "114.80.81.1", "61.129.66.79"},      
    // 03 Argentina - AR
    {"200.10.140.2", "216.244.192.3", "201.234.131.34", "190.2.0.201",
     "168.96.6.8", "157.92.44.101", "200.47.134.57", "200.80.18.131",
     "24.232.1.116", "201.252.250.9", "170.210.195.129", "209.99.224.17" },
    // 04 Bolivia - BO
    {"200.105.128.43", "200.112.193.1", "200.87.120.147", "190.129.11.76",
     "200.58.164.1", "200.58.164.252", "201.222.126.142", "200.105.211.12",
     "200.58.163.51", "200.105.181.118", "200.87.127.27", "200.87.128.100"},
    // 05 Brasil - BR
    {"201.63.254.68", "200.144.121.33", "200.246.239.26", "200.132.0.131",
     "200.160.0.8", "200.189.40.8", "200.192.232.8", "146.164.130.3",
     "200.169.224.10", "200.141.76.210", "200.160.7.165", "200.19.119.120"},           
    // 06 Chile - CL
    {"200.54.149.19", "146.83.183.179", "200.27.113.179", "200.111.152.86",
     "190.196.65.26", "190.208.1.6", "200.68.53.130", "200.83.4.28",
     "200.1.19.4", "200.0.144.2", "200.119.246.28", "201.238.196.194" },
    // 07 Colombia - CO
    {"206.49.176.206", "200.93.143.170", "190.146.68.210", "200.47.157.3",
     "190.90.1.34", "63.245.96.1", "201.234.186.196", "216.241.2.18",
     "201.236.221.69", "190.24.138.7", "64.76.177.102", "206.49.176.34" },
    // 08 Costa Rica - CR
    {"200.91.84.137", "201.198.75.18", "200.91.67.110", "196.40.24.161",
     "201.198.0.74", "200.122.159.230", "196.40.17.13", "201.201.58.162",
     "200.91.87.211", "196.40.2.121", "196.40.39.70", "190.241.38.51" },
    // 09 Republica Dominicana - DO
    {"201.229.190.10", "200.88.115.104", "190.8.43.150", "190.0.64.10",
     "200.26.171.149", "200.88.115.142", "190.166.42.76", "200.58.240.6",
     "200.42.206.189", "190.80.242.155", "200.88.156.66", "200.88.217.123"},    
    // 10 Ecuador - EC
    {"200.1.6.52", "157.100.162.190", "200.93.230.24", "157.100.84.154",
     "157.100.132.22", "190.11.15.36", "190.11.15.35", "157.100.115.117",
     "200.93.220.18", "200.32.72.82", "201.234.214.35", "200.31.31.44"},
    // 11 El Salvador - SV
    {"168.243.72.14", "190.57.64.1", "190.57.64.3", "168.243.237.176",
     "66.119.94.170", "168.243.203.226", "168.243.229.3", "168.243.80.234",
     "168.243.253.225", "216.184.102.1", "190.86.172.42", "201.247.147.201" },
    // 12 Guatemala - GT
    {"200.35.169.245", "200.30.144.180", "168.234.200.81", "216.230.145.87",
     "200.81.49.114", "200.110.240.201", "200.110.240.14", "168.234.200.122",
     "200.49.167.249", "200.30.154.74", "200.49.180.130", "190.4.2.69" },
    // 13 Haiti - HN
    {"190.6.196.206", "200.30.134.22", "201.220.137.232",
    "201.220.128.51", "200.14.59.68" },    
    // 14 Mexico - MX
    {"148.234.7.30", "200.23.51.205", "201.147.143.213", "148.244.114.148",
     "200.57.210.90", "201.155.229.129", "200.76.252.249", "207.248.227.154",
     "200.76.98.228", "132.248.81.29", "131.178.53.72", "158.97.97.251"},
    // 15 Nicaragua - NI
    {"209.124.105.66", "200.30.177.116", "200.30.128.100", "190.184.35.15",
     "200.30.165.140", "190.184.69.73", "165.98.16.19", "165.98.132.13",
     "200.62.94.12"},
    // 16 Panama - PA
    {"201.226.65.74", "200.115.131.134",
     "200.46.34.68", "200.3.200.55", "200.63.44.5", "200.46.243.29",
     "190.35.191.85", "201.227.246.77", "66.115.176.46", "200.115.174.28" },
    // 17 Paraguay - PY
    {"200.85.42.142", "200.85.32.19", "201.217.17.110", "200.85.32.34",
     "201.217.5.2", "200.61.224.170", "200.85.39.66" },
    // 18 Peru - PE
    {"161.132.252.2", "64.76.79.201", "200.60.94.225", "161.132.0.14",
     "64.76.93.99", "190.187.16.34", "200.60.200.161", "64.76.79.254",
     "190.41.144.187", "161.132.0.12", "201.234.49.34", "161.132.0.3" },
    // 19 Uruguay - UY
    {"200.40.209.98", "164.73.47.242", "200.93.243.2", "200.93.243.16",
     "200.93.240.3", "200.93.242.17", "200.93.242.16", "200.93.241.1",
     "164.73.83.55", "164.73.83.116", "201.221.29.28", "200.40.51.194"},
    // 20 Venezuela - VE
     {"200.44.153.90", "200.62.25.242", "150.188.30.6", "190.9.128.107",
      "150.187.178.3", "200.11.196.243", "200.44.153.154", "200.90.21.194",
      "200.11.190.2", "200.47.215.3", "200.82.166.170", "159.90.100.201"}};
	*/
   // LatencyTester latencyTester;
    LatencyTester countryLatencyTesters[];
    
    Graph graph;
    
    // with something about 200 servers, 5 samples
    // appear to be a very reasonable amount...
    // we are getting a sample each 16s per server,
    // what gives us a total time of 1.5 min.
    // it is ok, isn't it?
    
    int numSamples = 5;
    
    
    public void init() {
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                public void run() {
                    createGUI();
                    initTest();
                }
            });
        } catch (Exception e) {
            System.err.println("createGUI didn't successfully complete");
            e.printStackTrace();
        }

    }

    private void createGUI() {  
      	// re-install the Metal Look and Feel
    	//try {
		//	UIManager.setLookAndFeel(new MetalLookAndFeel());
		//} catch (UnsupportedLookAndFeelException e) {
		//}
    	// Sets a BoxLayout
    	Container contentPane = getContentPane();
    	//contentPane.setLayout (new BoxLayout (contentPane, BoxLayout.Y_AXIS));
    	contentPane.setLayout (null);
    	contentPane.setBackground(Color.white);
    	// Label
    	//Label label = new Label("Simon");
    	//label.setFont(new Font(null, Font.BOLD, 18));
    	//label.setBounds(310,0,200,20);
    	//add(label);
    	// Graph
    	graph = new Graph(countries);
    	
    	// TODO: I think is better to put it as parameters/constants (jm)
    	graph.setBounds(380,93,310,370);
        add(graph);
        
    	// Sets the Tabs
    	JTabbedPane tab = new JTabbedPane();
    	tab.setBounds(0,15,370,465);
        contentPane.add(tab, BorderLayout.CENTER);
        tab.setBackground(Color.lightGray);
        tab.setFont(new Font(null, Font.BOLD, 11));
        
        //tab.add("TesteGraph",graph);
        
        // A tab and a table for the summary 
        tables = new JTable[countries.length];
        scrollPanels = new JScrollPane[countries.length];
        
        table_all = new JTable();
        table_all.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
    	JScrollPane scrollPane_all = new JScrollPane(table_all);
    	table_all.setBackground(Color.white);
        table_all.setFont(new Font(null, Font.PLAIN, 10));
    	tab.add("SUMMARY", scrollPane_all);
    	// Sets a Tab and a JTable per country        
        for(int i = 0; i < countries.length; i++) {
    	    tables[i] = new JTable();
    	   	tables[i].setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        	scrollPanels[i] = new JScrollPane(tables[i]);
        	tables[i].setFont(new Font(null, Font.PLAIN, 10));
        	tab.add(countries[i].countryName, scrollPanels[i]);
    	}
    	resize(WIDTH, HEIGHT);
    	//Add the text field to the applet.
    	startButton = new JButton("Start the tests");
    	startButton.setFont(new Font(null, Font.BOLD, 18));
    	startButton.setBounds(440,20,180,50);
        startButton.addActionListener(this);
        add(startButton);
    	// Logs
        //logArea = new JTextArea();
        //JScrollPane scrollPane_log = new JScrollPane(logArea);
        //tab.add("History",scrollPane_log);

    }

    public void start() {
    	
    }

    public void stop() {
    	
    }

    public void destroy() {
    	try {
            SwingUtilities.invokeAndWait(new Runnable() {
                public void run() {
                	endTest();
                    //remove(logArea);
                }
            });
        } catch (Exception e) {
            System.err.println("cleanUp didn't successfully complete");
        }
        logArea = null;
    }
    
	public void actionPerformed(ActionEvent e) {
		
		if (startButton.equals(e.getSource())) {
			startButton.setEnabled(false);
			startButton.setText("Cancel");
			// Starts all the tester threads
			for(LatencyTester countryLatencyTester:countryLatencyTesters){
				countryLatencyTester.start();
			}
			startButton.setText("Again");
			startButton.setEnabled(true);
		}
	}
	
	
	public void initTest() {
		CentralServer.retrieveTestoPoints("http://simon.lacnic.net/testpoints.txt");
		CentralServer.setPostUrl("http://simon.lacnic.net/cgi-bin/simonpost.cgi");
		CentralServer.setLocalCountry(new Country("XX","Undefined"));
		
		countryLatencyTesters = new LatencyTester[countries.length];
		for(int i = 0; i < countries.length; i++) {
    	   if (countryLatencyTesters[i] == null) {
    		   countryLatencyTesters[i] = new LatencyTester(this, countries[i], i, graph, numSamples);
    		   tables[i].setModel(countryLatencyTesters[i].getTableModel()); 
    		   // Change column width
         	   TableColumn col = (tables[i].getColumnModel().getColumn(0));
        	   col.setPreferredWidth(190);
    	   }
    	   List<TestPoint> testPointsForCountry = CentralServer.getTestPointsByCountry(countries[i]);
    	   //System.out.println(countries[i]);
    	   for(TestPoint testPoint:testPointsForCountry) {
    		  //System.out.println("\t" + testPoint);	      
    		  countryLatencyTesters[i].add(testPoint);
		   }
    	}
   		AllTableModel tablemodel = new AllTableModel(countryLatencyTesters,countries.length);
		table_all.setModel(tablemodel);
  	    TableColumn col = (table_all.getColumnModel().getColumn(0));
	    col.setPreferredWidth(180);
	}
	
	public void endTest() {
		// Post results
		/*
		try {
			LatencyTester.postResults("http://127.0.0.1/~jmguzman/tests/getresults.php", latencyTester.getSamples());
		} catch (HttpException e) {
			System.err.println("Error during results sending: " +e );
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("Error during results sending: " +e );
			e.printStackTrace();
		}
		*/
	}

	
}
