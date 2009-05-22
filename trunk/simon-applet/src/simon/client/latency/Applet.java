package simon.client.latency;

import java.awt.Container;
import java.awt.Font;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JPanel;
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
import org.apache.log4j.Logger;

public class Applet extends JApplet implements ActionListener{
	
	private static final long serialVersionUID = 1L;
	static Logger log = Logger.getLogger(Applet.class);
	
	int WIDTH = 640;
	int HEIGHT = 480;
	int NUM_SAMPLES = 5;
	JTextArea logArea;
    JButton startButton;
    JComboBox countrySelectionBox;
    JTabbedPane testPanel;
    //Graph graph;
    
    JTable tables[];
    JScrollPane scrollPanels[];
    JTable table_all;
    JTable table_simple;

    // Countries
    Country[] countries = new Country[]{
    		// Continents (pseudo-countries)
    		new Country("US","United States"),
    		//new Country("EU","Europe"),
    		//new Country("AS","Asia"),
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
    	
    	Container contentPane = getContentPane();
    	contentPane.setLayout (new BoxLayout (contentPane, BoxLayout.Y_AXIS));
    	contentPane.setBackground(Color.white);
    	
    	// Country Selection
    	JPanel userPanel = new JPanel();
    	userPanel.setBackground(Color.white);
    	{
    		Label label = new Label("Select the country where you are now:");
        	userPanel.add(label);
        	
        	Country[] countryNames = new Country[1+countries.length];
        	countryNames[0]=null;
        	for(int c=0; c<countries.length; c++) {
        		countryNames[c+1]=countries[c];
        	}
        	countrySelectionBox = new JComboBox(countryNames);
            countrySelectionBox.setEnabled(true);
            countrySelectionBox.addActionListener(this);
            userPanel.add(countrySelectionBox);
            
        	startButton = new JButton("Start the tests");
            startButton.addActionListener(this);
            userPanel.add(startButton);
    	}
        contentPane.add(userPanel);

        // Test
    	testPanel = new JTabbedPane();
    	testPanel.setEnabled(false);
    	{
    		// EASY
        	JPanel easyPane = new JPanel();
        	easyPane.setBackground(Color.white);
        	easyPane.setLayout (new GridLayout(1,0)); // para grafico
        	easyPane.setPreferredSize(new Dimension(WIDTH-50, HEIGHT-100));
        	{
        		//JPanel graphPanel = new JPanel();
    			//graph = new Graph(countries);
    			//graph.repaint();
    			//graphPanel.add(graph);
    			//easyPane.add(graphPanel);
    			
    			table_simple = new JTable();
    			table_simple.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
    			table_simple.setBackground(Color.white);
    			table_simple.setFont(new Font(null, Font.PLAIN, 10));
    			easyPane.add(new JScrollPane(table_simple));
    		}
        	
        	
        	testPanel.addTab("Easy", new JScrollPane(easyPane));
        	
        	// ADVANCED
    		JTabbedPane advancedPane = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);
    		//advancedPane.setFont(new Font(null, Font.BOLD, 11));
    		advancedPane.setBackground(Color.yellow);
    		advancedPane.setPreferredSize(new Dimension(WIDTH-50, HEIGHT-100));
    		{
    			// A tab and a table for the summary
    			tables = new JTable[countries.length];
    			scrollPanels = new JScrollPane[countries.length];

    			/*table_simple = new JTable();
    			table_simple.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
    			table_simple.setBackground(Color.white);
    			table_simple.setFont(new Font(null, Font.PLAIN, 10));
    			JScrollPane scrollPane_all = new JScrollPane(table_simple);
    			advancedPane.add("SUMMARY", scrollPane_all);
    			 */
    			// Sets a Tab and a JTable per country
    			for (int i = 0; i < countries.length; i++) {
    				tables[i] = new JTable();
    				tables[i].setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
    				tables[i].setFont(new Font(null, Font.PLAIN, 10));
    				scrollPanels[i] = new JScrollPane(tables[i]);
    				advancedPane.addTab(countries[i].countryCode, null, scrollPanels[i], countries[i].countryName);
    			}
    			testPanel.addTab("Advanced", new JScrollPane(advancedPane));
    		}
    		
    	
    		// Details
    		JPanel detailsPane = new JPanel();
    		//detailsPane.setPreferredSize(new Dimension(WIDTH-50, HEIGHT-100));
    		{
    			logArea = new JTextArea();
    			detailsPane.add(logArea);
    			
    			Logger.getRootLogger().addAppender(new AppletLogAppender(logArea));
    			log.fatal("Details...");
    			log.fatal("Test...");
    			testPanel.addTab("Details", new JScrollPane(detailsPane));
    		}
    	}

    	contentPane.add(testPanel);
    	resize(WIDTH, HEIGHT);
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
                }
            });
        } catch (Exception e) {
            System.err.println("cleanUp didn't successfully complete");
        }
    }
    
	public void actionPerformed(ActionEvent e) {
		if (countrySelectionBox.equals(e.getSource())) {
			localCountry = (Country) countrySelectionBox.getSelectedItem();
		}
		if (startButton.equals(e.getSource())) {
			if (localCountry != null) {
				if (JOptionPane.showConfirmDialog(null,
						"Please, confirm that you are in " + this.localCountry +".\nThis is very important for the test accuracy", 
						"Country Confirmation",
						JOptionPane.OK_CANCEL_OPTION) == JOptionPane.CANCEL_OPTION) {
					return;
				}
				testPanel.setEnabled(true);
				startButton.setEnabled(false);
				countrySelectionBox.setEnabled(false);
				startButton.setText("wait");
				
                initTest();
             
				// Starts all the tester threads
				for(LatencyTester countryLatencyTester:countryLatencyTesters){
					countryLatencyTester.start();
				}
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
    		   countryLatencyTesters[i] = new LatencyTester(this, countries[i], i, NUM_SAMPLES);
    		   tables[i].setModel(countryLatencyTesters[i].getTableModel()); 
    		   // Change column width
         	   TableColumn col = (tables[i].getColumnModel().getColumn(0));
        	   col.setPreferredWidth(190);   
    	   }
    	   List<TestPoint> testPointsForCountry = CentralServer.getTestPointsByCountry(countries[i]);
    	   for(TestPoint testPoint:testPointsForCountry) {      
    		  countryLatencyTesters[i].add(testPoint);
		   }
    	}
//   		AllTableModel tablemodel = new AllTableModel(countryLatencyTesters,countries.length);
	    
   		SimpleTableModel simpletablemodel = new SimpleTableModel(countryLatencyTesters,countries.length);
		table_simple.setModel(simpletablemodel);
		
		TableColumn colCountry = (table_simple.getColumnModel().getColumn(0));
		colCountry.setPreferredWidth(300);
		
		TableColumn colAvg= (table_simple.getColumnModel().getColumn(1));
		colAvg.setPreferredWidth(150);
		
		TableColumn colProg = (table_simple.getColumnModel().getColumn(2));
		colProg.setCellRenderer(new ProgressCellRenderer());
		
		TableColumn colRtt = (table_simple.getColumnModel().getColumn(3));
 	    colRtt.setCellRenderer(new LatencyCellRenderer(400));
 	    colRtt.setPreferredWidth(400);
 	    
 	    //table_simple.getTableHeader().getColumnModel().getColumn(3).setCellRenderer(new LatencyCellRenderer(400));
	}
	
	
	public void endTest() {
		
	}

	/**
	 * Testers call here when they finish
	 * @param LatencyTester
	 */
	public synchronized void  finishedTesterCallbak(LatencyTester latencyTester){
		log.debug("NOTIFYING " + latencyTester);
		for(LatencyTester countryLatencyTester:countryLatencyTesters){
			if (!countryLatencyTester.isFinished()) {
				log.debug(countryLatencyTester + " NOT finished");
				return;
			}
			log.debug(countryLatencyTester + " finished");
		}
		log.info("All testers finished");
		// re-enable for more tests
		startButton.setEnabled(true);
		countrySelectionBox.setEnabled(false);
		startButton.setText("Restart Tests");

	}
}
