package simon.client.latency;

import java.awt.Container;
import java.awt.Font;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;

import org.apache.commons.httpclient.HttpException;
import org.apache.log4j.Logger;
import java.awt.*;
import javax.swing.*;

public class Applet extends JApplet implements ActionListener{
	//static Logger log = Logger.getLogger(Applet.class);
	
	int WIDTH = 320;
	int HEIGHT = 480;
	
	JTextArea logArea;
    JButton startButton;
    JTable table;
    String[] sites = {"www.arin.net", "www.nic.br", "www.nic.cl", "www.nic.pe", "www.nic.bo", "www.nic.ar", "www.nic.uy", "www.nic.pa", "www.nic.co", "www.nic.ve", "www.nic.ec"};
    LatencyTester latencyTester;
    int numSamples = 10;
    
    public void init() {
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                public void run() {
                    createGUI();
                    initTets();
                }
            });
        } catch (Exception e) {
            System.err.println("createGUI didn't successfully complete");
            e.printStackTrace();
        }

    }

    private void createGUI() {    
    	// Sets a BoxLayout
    	Container contentPane = getContentPane();
    	contentPane.setLayout (new BoxLayout (contentPane, BoxLayout.Y_AXIS));

        
    	resize(WIDTH, HEIGHT);
    	
    	// Label
    	Label label = new Label("Simon-Tester");
    	label.setFont(new Font(null, Font.BOLD, 18));
    	add(label);
    	
    	//Add the text field to the applet.
    	startButton = new JButton("Start");
        startButton.addActionListener(this);
        add(startButton);
    	
    	// Table
    	table = new JTable();
    	table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
    	JScrollPane scrollPane = new JScrollPane(table);
    	add(scrollPane);
    	
    	// Logs
        logArea = new JTextArea();
       // add(logArea);

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
                    remove(logArea);
                }
            });
        } catch (Exception e) {
            System.err.println("cleanUp didn't successfully complete");
        }
        logArea = null;
    }
    
	public void actionPerformed(ActionEvent e) {
		
		if (startButton.equals(e.getSource())) {
			startButton.disable();
			startButton.setText("Cancel");
			for(LatencyLocation sample:latencyTester.getSamples()) {
				AppletTester appletTester = new AppletTester(this, this.latencyTester, sample, numSamples);
				appletTester.start();
			}			
			startButton.setText("Again");
			startButton.enable();
		}
	}
	
	private void add2logArea(String newWord) {
        logArea.append(newWord + "\n");
        logArea.repaint();
    }	
	
	public void initTets() {
		if (latencyTester==null) {
			latencyTester= new LatencyTester(sites);
			table.setModel(latencyTester.getTableModel());
			
			for(String site:sites) {
				latencyTester.add(new LatencyLocation(site));
			}
		}
	}
	
	public void endTest() {
		// Post results
		try {
			LatencyTester.postResults("http://127.0.0.1/~jmguzman/tests/getresults.php", latencyTester.getSamples());
		} catch (HttpException e) {
			System.err.println("Error during results sending: " +e );
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("Error during results sending: " +e );
			e.printStackTrace();
		}
	}

	
}
