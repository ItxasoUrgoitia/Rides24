package gui;

import java.awt.Color;
import java.net.URL;
import java.util.Locale;

import javax.swing.UIManager;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import configuration.ConfigXML;
import configuration.UtilDate;
import dataAccess.DataAccess;
import domain.Driver;
import businessLogic.BLFacade;
import businessLogic.BLFacadeImplementation;
import businessLogic.BLFactory;
import businessLogic.DriverTable;
import businessLogic.ExtendedIterator;

public class ApplicationLauncher { 
	
	
	
	public static void main(String[] args) {

	/*	ConfigXML c=ConfigXML.getInstance();
	
		System.out.println(c.getLocale());
		
		Locale.setDefault(new Locale(c.getLocale()));
		
		System.out.println("Locale: "+Locale.getDefault());
		
		
		// System.setProperty("objectdb.conf", "/Users/oihane/git/Rides24ok/objectdb.conf");

		
	   //  Driver driver=new Driver("driver3@gmail.com", "Test Driver", "0","0");
	    
	 **/
		boolean	isLocal	= true;	
			
		MainGUI a=new MainGUI();
	    //MainGidariGUI a = new MainGidariGUI(driver);
		a.setVisible(true);


		try {
			
			UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");

       
            //BLFacade appFacadeInterface = BLFactory.createBLFacade();
			
			BLFacade	blFacade	=	new	BLFactory().getBusinessLogicFactory(isLocal);
			
			
            MainGUI.setBussinessLogic(blFacade);
			 
			
			MainGUI.setBussinessLogic(blFacade);

			ExtendedIterator<String> i = blFacade.getDepartingCitiesIterator();
			String c;
			System.out.println("_____________________");
			System.out.println("FROM	LAST	TO	FIRST");
			i.goLast(); // Go to last element
			while (i.hasPrevious()) {
				c = i.previous();
				System.out.println(c);
			}
			System.out.println();
			System.out.println("_____________________");
			System.out.println("FROM	FIRST	TO	LAST");
			i.goFirst(); // Go to first element
			while (i.hasNext()) {
				c = i.next();
				System.out.println(c);
			}	
			
			
			
			 Driver d = new Driver("eNEKO rUIZ", "1", "d92@gmail.com", "7342S");
		        // -------------------- RIDES DRIVERRERAKO --------------------
		        d.addRide("Donostia", "Bilbo", UtilDate.newDate(2025, 1, 6), 4, 8f);
		        d.addRide("Bilbo", "Donostia", UtilDate.newDate(2025, 2, 15), 4, 10f);
		        d.addRide("Donostia", "Gasteiz", UtilDate.newDate(2025, 3, 25), 3, 12f);
		        d.addRide("Bilbo", "Iruña", UtilDate.newDate(2025, 4, 28), 2, 15f);

		      
		        DriverTable	dt=new	DriverTable(d);
		        dt.setVisible(true);	
			

			
		}catch (Exception e) {
			a.jLabelSelectOption.setText("Error: "+e.toString());
			a.jLabelSelectOption.setForeground(Color.RED);	
			
			System.out.println("Error in ApplicationLauncher: "+e.toString());
		}
		//a.pack();


	}

}
