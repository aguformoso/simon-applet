package simon.client.latency;


import java.util.ArrayList;

public class LatencyLocation {
	String destination;
	ArrayList<Long> samples = new ArrayList<Long> ();
	//long devLatency;
	//long avgLatency;
	
	static String [] columnNames = { "Destination", "Latency", "Samples", "stdDev"};
	
	public int getNumSamples() {
		return this.samples.size();
	}
	
	public long getAverage() {
		if (getNumSamples()==0) return 0;	
		long accum = 0;
		for(Long sample:samples) {
			accum+= (long)sample;
		}
		return (accum/getNumSamples());
	}
	
	public double getStdDev() {
		if (getNumSamples()==0) return 0;
		long avg = getAverage();
		double S2 = 0;
		for(Long sample:samples) {
			S2 += (sample-avg)*(sample-avg);
		}
		return  Math.sqrt(S2/getNumSamples());
	}
	
	void addSample(long sample) {
		samples.add(sample);
	}
	public LatencyLocation(String site) {
		this.destination = site;
	}

	public String toString() {
		return destination + " = " + getAverage() + " ms";
	}

	public Object getData() {
		return this.destination +  ", " + this.getAverage()  + ", " + this.getStdDev() + ", " + getNumSamples() + " " + this.samples + "\n";
	}

	static public Class<?> getColumnClass(int columnIndex) {
		if (columnIndex==0) return String.class;
		if (columnIndex==1) return Long.class;
		if (columnIndex==2) return Integer.class;
		if (columnIndex==3) return Double.class;
		return String.class;
	}
	
	public Object getColumn(int columnIndex) {
		if (columnIndex==0) return this.destination;
		if (columnIndex==1) return this.getAverage();
		if (columnIndex==2) return this.getNumSamples();
		if (columnIndex==3) return this.getStdDev();
		return "?";
	}
}


