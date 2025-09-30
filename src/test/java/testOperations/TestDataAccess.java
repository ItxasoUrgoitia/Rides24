package testOperations;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import configuration.ConfigXML;
import domain.Bidaiari;
import domain.Driver;
import domain.Eskaera;
import domain.Ride;
import domain.Eskaera.EskaeraEgoera;


public class TestDataAccess {
	protected  EntityManager  db;
	protected  EntityManagerFactory emf;

	ConfigXML  c=ConfigXML.getInstance();


	public TestDataAccess()  {
		
		System.out.println("TestDataAccess created");

		//open();
		
	}

	
	public void open(){
		

		String fileName=c.getDbFilename();
		
		if (c.isDatabaseLocal()) {
			  emf = Persistence.createEntityManagerFactory("objectdb:"+fileName);
			  db = emf.createEntityManager();
		} else {
			Map<String, String> properties = new HashMap<String, String>();
			  properties.put("javax.persistence.jdbc.user", c.getUser());
			  properties.put("javax.persistence.jdbc.password", c.getPassword());

			  emf = Persistence.createEntityManagerFactory("objectdb://"+c.getDatabaseNode()+":"+c.getDatabasePort()+"/"+fileName, properties);

			  db = emf.createEntityManager();
    	   }
		System.out.println("TestDataAccess opened");

		
	}
	public void close(){
		db.close();
		System.out.println("TestDataAccess closed");
	}

	public boolean removeDriver(String driverEmail) {
		System.out.println(">> TestDataAccess: removeRide");
		Driver d = db.find(Driver.class, driverEmail);
		if (d!=null) {
			db.getTransaction().begin();
			db.remove(d);
			db.getTransaction().commit();
			return true;
		} else 
		return false;
    }
	
	public Driver getDriver(String driverEmail) {
		System.out.println(">> TestDataAccess: getDriver "+driverEmail);
		Driver d = db.find(Driver.class, driverEmail);
		
		return d;
    }
	public Driver createDriver(String email, String name) {
		System.out.println(">> TestDataAccess: createDriver");
		Driver driver=null;
			db.getTransaction().begin();
			try {
			    driver=new Driver(email,name);;
				db.persist(driver);
				db.getTransaction().commit();
			}
			catch (Exception e){
				e.printStackTrace();
			}
			return driver;
    }
	//Nik gehitu dut hau (createBidaiari)
	public Bidaiari createBidaiari(String name, String pasahitza, String email, String nanZbk) {
		System.out.println(">> TestDataAccess: createBidaiari");
		Bidaiari bidaiari=null;
			db.getTransaction().begin();
			try {
			    bidaiari=new Bidaiari(name,pasahitza, email, nanZbk);
				db.persist(bidaiari);
				db.getTransaction().commit();
			}
			catch (Exception e){
				e.printStackTrace();
			}
			return bidaiari;
    }
	//Nik gehitu dut hau (createEskaera)
	public Eskaera createEskaera(EskaeraEgoera egoera, int nPlaces, Ride ride, Bidaiari bidaiari) {
		System.out.println(">> TestDataAccess: createEskaera");
		Eskaera eskaera=null;
			db.getTransaction().begin();
			try {
			    eskaera=new Eskaera(egoera, nPlaces, ride, bidaiari);
				db.persist(eskaera);
				db.getTransaction().commit();
			}
			catch (Exception e){
				e.printStackTrace();
			}
			return eskaera;
    }
	
	public boolean existDriver(String email) {
		 return  db.find(Driver.class, email)!=null;
		 

	}
		
		public Driver addDriverWithRide(String email, String name, String from, String to,  Date date, int nPlaces, float price) {
			System.out.println(">> TestDataAccess: addDriverWithRide");
				Driver driver=null;
				db.getTransaction().begin();
				try {
					 driver = db.find(Driver.class, email);
					if (driver==null)
						driver=new Driver(email,name);
				    driver.addRide(from, to, date, nPlaces, price);
				    db.persist(driver);
				    System.out.println("Stored: "+driver);
					db.getTransaction().commit();
					return driver;
					
				}
				catch (Exception e){
					e.printStackTrace();
				}
				return driver;
	    }
		
		
		public boolean existRide(String email, String from, String to, Date date) {
			System.out.println(">> TestDataAccess: existRide");
			Driver d = db.find(Driver.class, email);
			if (d!=null) {
				return d.doesRideExists(from, to, date);
			} else 
			return false;
		}
		public Ride removeRide(String email, String from, String to, Date date ) {
			System.out.println(">> TestDataAccess: removeRide");
			Driver d = db.find(Driver.class, email);
			if (d!=null) {
				db.getTransaction().begin();
				Ride r= d.removeRide(from, to, date);
				db.getTransaction().commit();
				return r;

			} else 
			return null;

		}


		
}


