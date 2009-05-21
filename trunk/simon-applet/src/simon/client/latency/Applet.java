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
    JComboBox countrySelectionBox;
    JPanel testPanel;
    
    JTable tables[];
    JScrollPane scrollPanels[];
    JTable table_all;
    JTable table_simple;

    // Countries
    Country[] countries = new Country[]{
    		// Continents (pseudo-countries)
    		new Country("US","North America"),
    		new Country("EU","Europe"),
    		new Country("AS","Asia"),
    		// Countries
    		new Country("AR","Argentina"),
    		new Country("BO","Bolivia"),
    		new Country("BR","Brasil"),
    		new Country("CL","Chile"),
    		new Country("CO","Colombia"),
    		new Country("CR","Costa Rica"),
    		new Country("DO","Republica Dominicana"),
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
    
    LatencyTester countryLatencyTesters[];
    Country localCountry=null;
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
    	contentPane.setLayout (new BoxLayout (contentPane, BoxLayout.Y_AXIS));
    	//contentPane.setLayout (null);
    	contentPane.setBackground(Color.white);
    	
    	// Country Selection
    	JPanel userFrame = new JPanel();
    	userFrame.setBackground(Color.white);
    	{
    		//userFrame.setBackground(Color.blue);
        	//userFrame.setOpaque(true);
        	// Label
        	Label label = new Label("Select Country where you are:");
        	//label.setFont(new Font(null, Font.BOLD, 18));
        	//label.setBounds(310,0,200,20);
        	userFrame.add(label);
        	
        	Country[] countryNames = new Country[1+countries.length];
        	countryNames[0]=null;
        	for(int c=0; c<countries.length; c++) {
        		countryNames[c+1]=countries[c];
        	}
        	countrySelectionBox = new JComboBox(countryNames);
            countrySelectionBox.setEnabled(true);
            countrySelectionBox.addActionListener(this);
            userFrame.add(countrySelectionBox);
            
          //Add the text field to the applet.
        	startButton = new JButton("Start the tests");
        	startButton.setFont(new Font(null, Font.BOLD, 18));
        	startButton.setBounds(440,20,180,50);
            startButton.addActionListener(this);
            userFrame.add(startButton);
    	}
        contentPane.add(userFrame);

        // Test
    	testPanel = new JPanel();
    	//testPanel.setLayout (new BoxLayout (testPanel, BoxLayout.X_AXIS));
    	testPanel.setLayout (new GridLayout(1,0));
    	testPanel.setBackground(Color.white);
    	testPanel.setVisible(false);
    	{
    		
            
        	// Sets the Tabs
        	JTabbedPane tab = new JTabbedPane();
        	//tab.setBounds(0,15,370,465);
            //tab.setBackground(Color.lightGray);
            tab.setFont(new Font(null, Font.BOLD, 11));
            
            testPanel.add(tab);
            
            // A tab and a table for the summary 
            tables = new JTable[countries.length];
            scrollPanels = new JScrollPane[countries.length];
            
            table_simple = new JTable();
            table_simple.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        	table_simple.setBackground(Color.white);
            table_simple.setFont(new Font(null, Font.PLAIN, 10));
            JScrollPane scrollPane_all = new JScrollPane(table_simple);
        	tab.add("SUMMARY", scrollPane_all);
        	
        	// Sets a Tab and a JTable per country        
            for(int i = 0; i < countries.length; i++) {
        	    tables[i] = new JTable();
        	   	tables[i].setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
            	tables[i].setFont(new Font(null, Font.PLAIN, 10));
            	tab.add(countries[i].countryName, scrollPanels[i]);
            	scrollPanels[i] = new JScrollPane(tables[i]);
        	}
            
         // Graph
        	graph = new Graph(countries);
        	// TODO: I think is better to put it as parameters/constants (jm)
        	//graph.setBounds(380,93,310,370);
        	graph.setSize(100,200);
        	graph.setBackground(Color.pink);
        	testPanel.add(graph);	
    	}
    	contentPane.add(testPanel);
    	resize(WIDTH, HEIGHT);
    	
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
		if (countrySelectionBox.equals(e.getSource())) {
			localCountry = (Country) countrySelectionBox.getSelectedItem();
		}
		if (startButton.equals(e.getSource())) {
			if (localCountry != null) {
				startButton.setEnabled(false);
				startButton.setText("Cancel");
				
                initTest();
                
				// Starts all the tester threads
				for(LatencyTester countryLatencyTester:countryLatencyTesters){
					countryLatencyTester.start();
				}
				startButton.setText("Again");
				startButton.setEnabled(true);
				testPanel.setVisible(true);
			}
			
		}
	}
	
	
	public void initTest() {
		CentralServer.retrieveTestoPoints("http://simon.lacnic.net/testpoints.txt");
		CentralServer.setPostUrl("http://simon.lacnic.net/cgi-bin/simonpost.cgi");
		CentralServer.setLocalCountry(localCountry);
		
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
		//table_all.setModel(tablemodel);
  	    //TableColumn col = (table_all.getColumnModel().getColumn(0));
	    //col.setPreferredWidth(180);
	    
   		SimpleTableModel simpletablemodel = new SimpleTableModel(countryLatencyTesters,countries.length);
		table_simple.setModel(simpletablemodel);
  	    //TableColumn simplecol = (table_simple.getColumnModel().getColumn(0));
	    //simplecol.setPreferredWidth(180);

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
