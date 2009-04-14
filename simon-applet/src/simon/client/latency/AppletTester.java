package simon.client.latency;

import java.io.IOException;

import javax.swing.SwingUtilities;

public class AppletTester extends Thread {
	LatencyLocation sample;
	LatencyTester latencyTester;
	Applet applet;
	AppletTester(Applet applet, LatencyTester latencyTester, LatencyLocation sample) {
		this.applet = applet;
		this.sample = sample;
		this.latencyTester = latencyTester;
	}
	
	public void run() {
		System.out.println("Starting test " + sample);
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
				public void run() {
					try {		
						sample = latencyTester.getTcpLatency(sample);
						System.out.println(sample);
						applet.repaint();
					} catch (IOException e) {
						System.err.println("Error during test: " +e );
					}
					
				}
            });
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Finish test " + sample);
    }
}
