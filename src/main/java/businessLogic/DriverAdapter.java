package businessLogic;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import domain.Driver;
import domain.Ride;

public class DriverAdapter extends AbstractTableModel{
	private Driver driver;
    private String[] columnNames = { "From", "To", "Price" };
    private List<Ride> rides;

    public DriverAdapter(Driver driver) {
        this.driver = driver;
        this.rides = driver.getRides();  // suponiendo que existe getRides()
    }
	
	
		@Override
	    public int getRowCount() {
	        return rides != null ? rides.size() : 0;
	    }

	    @Override
	    public int getColumnCount() {
	        return columnNames.length;
	    }

	    @Override
	    public Object getValueAt(int rowIndex, int columnIndex) {
	        Ride ride = rides.get(rowIndex);
	        switch (columnIndex) {
	            case 0: return ride.getFrom();
	            case 1: return ride.getTo();
	            case 2: return ride.getPrice();
	            default: return null;
	        }
	    }

	    @Override
	    public String getColumnName(int column) {
	        return columnNames[column];
	    }

	
}
