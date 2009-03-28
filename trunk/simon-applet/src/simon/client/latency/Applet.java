package simon.client.latency;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JList;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;

import org.apache.log4j.Logger;

public class Applet extends JApplet implements ActionListener{
	//static Logger log = Logger.getLogger(Applet.class);
	
	JTextArea logArea;
    JButton startButton;
    
    public void init() {

        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                public void run() {
                    createGUI();
                }
            });
        } catch (Exception e) {
            System.err.println("createGUI didn't successfully complete");
        }
    }

    private void createGUI() {        
        //Create the text field and make it uneditable.
        logArea = new JTextArea();
        //field.setEditable(false);
        
        startButton = new JButton("Start");
        startButton.addActionListener(this);
        //Set the layout manager so that the text field will be
        //as wide as possible.
        setLayout(new java.awt.GridLayout(0,1));

        //Add the text field to the applet.
       // add(startButton);
        add(logArea);
    }

    public void start() {
    	performTests();
    }

    public void stop() {
    	
    }

    public void destroy() {
        cleanUp();
    }
    
    private void cleanUp() {
        //Execute a job on the event-dispatching thread:
        //taking the text field out of this applet.
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                public void run() {
                    remove(logArea);
                }
            });
        } catch (Exception e) {
            System.err.println("cleanUp didn't successfully complete");
        }
        logArea = null;
    }
/*
    private void addItem(boolean alreadyInEDT, String newWord) {
        if (alreadyInEDT) {
            addItem(newWord);
        } else {
            final String word = newWord;
            //Execute a job on the event-dispatching thread:
            //invoking addItem(newWord).
            try {
                SwingUtilities.invokeAndWait(new Runnable() {
                    public void run() {
                        addItem(word);
                    }
                });
            } catch (Exception e) {
                System.err.println("addItem didn't successfully complete");
            }
        }
    }
        
    //Invoke this method ONLY from the event-dispatching thread.
    private void addItem(String newWord) {
        //String t = field.getText();
        //System.out.println(newWord);
        //field.setText(t + newWord + ",\n");
        logArea.append(newWord + "\n");
    }
*/
	public void actionPerformed(ActionEvent e) {
		//log.info("ActionEvent " + e);
		if (startButton.equals(e.getSource())) {
			startButton.disable();
			//log.info("StartButton");
			performTests();
			startButton.enable();
		}
	}
	
	private void add2logArea(String newWord) {
        logArea.append(newWord + "\n");
        logArea.repaint();
    }	
	
	public void performTests() {
		String[] sites = {"www.arin.net", "www.nic.br", "www.nic.cl", "www.nic.pe", "www.nic.bo", "www.nic.ar", "www.nic.uy", "www.nic.pa", "www.nic.co", "www.nic.ve", "www.nic.ec"};
		
		for(String site:sites) {
			try {
				//log.info("Latency to " + site + " is "+ LatencyTester.getTcpLatency(site) + " ms");
				add2logArea("Latency to " + site + " is "+ LatencyTester.getTcpLatency(site) + " ms");
			} catch (IOException e) {
				System.err.println("Error during test: " +e );
			}
		}
	}

}
