package simon.client.latency;

import java.awt.Component;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class ProgressCellRenderer implements TableCellRenderer {
	JProgressBar progressBar = new JProgressBar(0,100);
	
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		progressBar.setValue((Integer) value);
		return progressBar;
	}
}
