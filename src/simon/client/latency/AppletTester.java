package simon.client.latency;

import java.io.IOException;

import javax.swing.SwingUtilities;

public class AppletTester extends Thread {
	LatencyLocation sample;
	LatencyTester latencyTester;
	Applet applet;
	int num;
	AppletTester(Applet applet, LatencyTester latencyTester, LatencyLocation sample, int num) {
		this.applet = applet;
		this.sample = sample;
		this.latencyTester = latencyTester;
		this.num = num;
	}
	
	public void run() {
		//System.out.println("Starting test " + sample);
        try {
        	for(int i=0; i<num; i++) {
        		sample = latencyTester.getTcpLatency(sample);
    			System.out.println(sample);
                SwingUtilities.invokeAndWait(new Runnable() {
    				public void run() {
    					applet.repaint();	
    				}
                });
        	}
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        //System.out.println("Finish test " + sample);
    }
}
