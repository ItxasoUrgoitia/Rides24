package testOperations;

import java.util.Date;
import java.util.List;

import configuration.ConfigXML;
import domain.*;
import domain.Eskaera.EskaeraEgoera;

public class TestBusinessLogic {
	TestDataAccess dbManagerTest;

	public TestBusinessLogic()  {
		
		System.out.println("Creating TestBusinessLogic instance");
		ConfigXML c=ConfigXML.getInstance();
		dbManagerTest=new TestDataAccess(); 
		dbManagerTest.close();
	}
	
	
	public boolean removeDriver(String driverEmail) {
		dbManagerTest.open();
		boolean b=dbManagerTest.removeDriver(driverEmail);
		dbManagerTest.close();
		return b;

	}
	
	public Driver createDriver(String email, String name) {
		dbManagerTest.open();
		Driver driver=dbManagerTest.createDriver(email, name);
		dbManagerTest.close();
		return driver;

	}
	
	public boolean existDriver(String email) {
		dbManagerTest.open();
		boolean existDriver=dbManagerTest.existDriver(email);
		dbManagerTest.close();
		return existDriver;

	}
	
	public Driver addDriverWithRide(String email, String name, String from, String to,  Date date, int nPlaces, float price) {
		dbManagerTest.open();
		Driver driver=dbManagerTest.addDriverWithRide(email, name, from, to, date, nPlaces, price);
		dbManagerTest.close();
		return driver;

	}
	public boolean existRide(String email, String from, String to, Date date) {
		dbManagerTest.open();
		boolean b=dbManagerTest.existRide(email, from, to, date);
		dbManagerTest.close();
		return b;
	}
	public Ride removeRide(String email,String from, String to, Date date ) {
		dbManagerTest.open();
		Ride r=dbManagerTest.removeRide( email, from,  to,  date );
		dbManagerTest.close();
		return r;
	}


	public Bidaiari sortuBidaiari(String name, String pasahitza, String email, String nanZbk) {
		dbManagerTest.open();
		Bidaiari bidaiari = dbManagerTest.sortuBidaiari(name, pasahitza, email, nanZbk);
		dbManagerTest.close();
		return bidaiari;
	}

	public Ride sortuRide(String from, String to, java.util.Date date, int nPlaces, double price, Driver driver) {
		dbManagerTest.open();
		Ride ride = dbManagerTest.sortuRide(from, to, date, nPlaces, price, driver);
		dbManagerTest.close();
		return ride;
	}

	public Eskaera sortuEskaera(Eskaera.EskaeraEgoera egoera, int nPlaces, Ride ride, Bidaiari bidaiari) {
		dbManagerTest.open();
		Eskaera eskaera = dbManagerTest.sortuEskaera(egoera, nPlaces, ride, bidaiari);
		dbManagerTest.close();
		return eskaera;
	}

	public void ezabatuEskaerak() {
		dbManagerTest.open();
		dbManagerTest.ezabatuEskaerak();
		dbManagerTest.close();
	}

	public void ezabatuErabiltzaileak() {
		dbManagerTest.open();
		dbManagerTest.ezabatuErabiltzaileak();
		dbManagerTest.close();
	}

// Metodo berriak
	public List<Eskaera> getBidaiariEskaerak(Bidaiari bidaiari) {
		dbManagerTest.open();
		List<Eskaera> eskaerak = dbManagerTest.getBidaiariEskaerak(bidaiari);
		dbManagerTest.close();
		return eskaerak;
	}

	public List<Eskaera> getRideEskaerak(Ride ride) {
		dbManagerTest.open();
		List<Eskaera> eskaerak = dbManagerTest.getRideEskaerak(ride);
		dbManagerTest.close();
		return eskaerak;
	}

	public List<Alerta> getDriverAlertak(Driver driver) {
		dbManagerTest.open();
		List<Alerta> alertak = dbManagerTest.getDriverAlertak(driver);
		dbManagerTest.close();
		return alertak;
	}

	public void removeAlertak() {
		dbManagerTest.open();
		dbManagerTest.removeAlertak();
		dbManagerTest.close();
	}

	public void removeRides() {
		dbManagerTest.open();
		dbManagerTest.removeRides();
		dbManagerTest.close();
	}


	public Bidaiari createBidaiari(String name, String pasahitza, String email, String nanZbk) {
		dbManagerTest.open();
		Bidaiari bidaiari = dbManagerTest.createBidaiari(name, pasahitza, email, nanZbk);
		dbManagerTest.close();
		return bidaiari;
	}

	public Ride createRide(String from, String to, java.sql.Date date, int nPlaces, double price, Driver driver) {
		dbManagerTest.open();
		Ride ride = dbManagerTest.createRide(from, to, date, nPlaces, price, driver);
		dbManagerTest.close();
		return ride;
	}

	public Eskaera createEskaera(Eskaera.EskaeraEgoera egoera, int nPlaces, Ride ride, Bidaiari bidaiari) {
		dbManagerTest.open();
		Eskaera eskaera = dbManagerTest.createEskaera(egoera, nPlaces, ride, bidaiari);
		dbManagerTest.close();
		return eskaera;
	}

	public void removeUsers() {
		dbManagerTest.open();
		dbManagerTest.removeUsers();
		dbManagerTest.close();
	}

	public void removeEskaerak() {
		dbManagerTest.open();
		dbManagerTest.removeEskaerak();
		dbManagerTest.close();
	}
}
