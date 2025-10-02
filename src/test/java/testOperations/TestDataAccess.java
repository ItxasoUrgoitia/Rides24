package testOperations;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import domain.Erreklamazioa.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import configuration.ConfigXML;
import domain.Alerta;
import domain.Bidaiari;
import domain.Driver;
import domain.Erreklamazioa;
import domain.Eskaera;
import domain.Eskaera.EskaeraEgoera;
import domain.Movement;
import domain.Ride;
import domain.User;
import exceptions.RequestAlreadyExistException;

public class TestDataAccess {
	protected EntityManager db;
	protected EntityManagerFactory emf;
	ConfigXML c = ConfigXML.getInstance();

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
	public boolean existDriver(String email) {
		 return  db.find(Driver.class, email)!=null;
		 

	}
	public boolean existBidaiari(String email) {
		 return  db.find(Bidaiari.class, email)!=null;
		 

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


	public Bidaiari sortuBidaiari(String name, String pasahitza, String email, String nanZbk) {
		Bidaiari bidaiari = null;
		db.getTransaction().begin();
		try {
			bidaiari = new Bidaiari(name, pasahitza, email, nanZbk);
			db.persist(bidaiari);
			db.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bidaiari;
	}

	public Ride sortuRide(String from, String to, Date date, int nPlaces, double price, Driver driver) {
		Ride ride = null;
		db.getTransaction().begin();
		try {
			driver = db.find(Driver.class, driver.getEmail());
			if (driver != null) {
				ride = driver.addRide(from, to, date, nPlaces, (float) price);
				db.persist(driver);
			}
			db.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ride;
	}

	public Eskaera sortuEskaera(EskaeraEgoera egoera, int nPlaces, Ride ride, Bidaiari bidaiari) {
		Eskaera eskaera = null;
		db.getTransaction().begin();
		try {
			eskaera = new Eskaera(egoera, nPlaces, ride, bidaiari);
			db.persist(eskaera);
			db.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return eskaera;
	}

	public void ezabatuEskaerak() {
		db.getTransaction().begin();
		db.createQuery("DELETE FROM Eskaera").executeUpdate();
		db.getTransaction().commit();
	}

	public void ezabatuErabiltzaileak() {
		db.getTransaction().begin();
		db.createQuery("DELETE FROM Bidaiari").executeUpdate();
		db.createQuery("DELETE FROM Driver").executeUpdate();
		db.getTransaction().commit();
	}

	public List<Eskaera> getBidaiariEskaerak(Bidaiari bidaiari) {
		db.getTransaction().begin();
		try {
			Bidaiari refreshedBidaiari = db.find(Bidaiari.class, bidaiari.getEmail());
			List<Eskaera> eskaerak = refreshedBidaiari.getEskaerak();
			db.getTransaction().commit();
			return eskaerak;
		} catch (Exception e) {
			db.getTransaction().rollback();
			throw e;
		}
	}

	public List<Eskaera> getRideEskaerak(Ride ride) {
		db.getTransaction().begin();
		try {
			Ride refreshedRide = db.find(Ride.class, ride.getRideNumber());
			List<Eskaera> eskaerak = refreshedRide.getEskaerenList();
			db.getTransaction().commit();
			return eskaerak;
		} catch (Exception e) {
			db.getTransaction().rollback();
			throw e;
		}
	}

	public List<Alerta> getDriverAlertak(Driver driver) {
		db.getTransaction().begin();
		try {
			Driver refreshedDriver = db.find(Driver.class, driver.getEmail());
			List<Alerta> alertak = refreshedDriver.getAlertak();
			db.getTransaction().commit();
			return alertak;
		} catch (Exception e) {
			db.getTransaction().rollback();
			throw e;
		}
	}

	public void removeAlertak() {
		db.getTransaction().begin();
		db.createQuery("DELETE FROM Alerta").executeUpdate();
		db.getTransaction().commit();
	}

	public void removeRides() {
		db.getTransaction().begin();
		db.createQuery("DELETE FROM Ride").executeUpdate();
		db.getTransaction().commit();
	}

	

	public Bidaiari createBidaiari(String name, String pasahitza, String email, String nanZbk) {
		return sortuBidaiari(name, pasahitza, email, nanZbk);
	}

	public Ride createRide(String from, String to, java.sql.Date date, int nPlaces, double price, Driver driver) {
		return sortuRide(from, to, date, nPlaces, price, driver);
	}

	public Eskaera createEskaera(Eskaera.EskaeraEgoera egoera, int nPlaces, Ride ride, Bidaiari bidaiari) {
		return sortuEskaera(egoera, nPlaces, ride, bidaiari);
	}

	public void removeUsers() {
		ezabatuErabiltzaileak();
	}

	public void removeEskaerak() {
		ezabatuEskaerak();
	}
	
	public Erreklamazioa addErreklamazio(User userJarri, User userJaso, Eskaera eskaera, String texto, float coste, ErrekLarri larritasuna) {
	    if(userJarri == null || userJaso == null || eskaera == null || texto == null || texto.isEmpty() || larritasuna == null) {
	        throw new NullPointerException("Parametro nulo en addErreklamazio");
	    }

	    Erreklamazioa erreklamazioa = new Erreklamazioa(userJarri, userJaso, eskaera, texto, coste, larritasuna);

	    db.getTransaction().begin();
	    db.persist(erreklamazioa);
	    db.getTransaction().commit();

	    return erreklamazioa;
	}
	
	public void addErreklamazio2(User userJarri, User userJaso, Eskaera eskaera, String text, float diruKop, ErrekLarri larritasuna) {
	    db.getTransaction().begin();
	    try {
	        User uJarri = db.find(User.class, userJarri.getEmail());
	        User uJaso = db.find(User.class, userJaso.getEmail());
	        Eskaera esk = db.find(Eskaera.class, eskaera.getEskaeraNumber());

	        if (uJarri == null || uJaso == null || esk == null) {
	            throw new IllegalArgumentException("User o Eskaera no encontrado");
	        }

	        Erreklamazioa erreklamazioa = new Erreklamazioa(uJarri, uJaso, esk, text, diruKop, larritasuna);
	        db.persist(erreklamazioa);
	        db.getTransaction().commit();
	    } catch (Exception e) {
	        db.getTransaction().rollback();
	        throw e;
	    }
	}
	
	/*Nik gehitu dut hau (createBidaiari)
	public Bidaiari createBidaiari(String name, String pasahitza, String email, String nanZbk) {
		System.out.println(">> TestDataAccess: createBidaiari");
		Bidaiari bidaiari = null;
		db.getTransaction().begin();
		try {
			bidaiari = new Bidaiari(name, pasahitza, email, nanZbk);
			db.persist(bidaiari);
			db.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bidaiari;
	}

	// Nik gehitu dut hau (createEskaera)
	public Eskaera createEskaera(EskaeraEgoera egoera, int nPlaces, Ride ride, Bidaiari bidaiari) {
		System.out.println(">> TestDataAccess: createEskaera");
		Eskaera eskaera = null;
		db.getTransaction().begin();
		try {
			eskaera = new Eskaera(egoera, nPlaces, ride, bidaiari);
			db.persist(eskaera);
			db.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return eskaera;
	}*/
	public boolean removeBidaiari(String bidaiariEmail) {
		System.out.println(">> TestDataAccess: removeBidaiari");
		Bidaiari b = db.find(Bidaiari.class, bidaiariEmail);
		if (b!=null) {
			db.getTransaction().begin();
			db.remove(b);
			db.getTransaction().commit();
			return true;
		} else 
		return false;
    }
	
	public List<Movement> getDriverMove(Driver driver) {
		db.getTransaction().begin();
		try {
			Driver refreshedDriver = db.find(Driver.class, driver.getEmail());
			List<Movement> Movements = refreshedDriver.getMugimenduak();
			db.getTransaction().commit();
			return Movements;
		} catch (Exception e) {
			db.getTransaction().rollback();
			throw e;
		}
	}
	public List<Movement> getBidaiariMove(Bidaiari bidaiari) {
		db.getTransaction().begin();
		try {
			Bidaiari refreshedBidaiari = db.find(Bidaiari.class, bidaiari.getEmail());
			List<Movement> Movements = refreshedBidaiari.getMugimenduak();
			db.getTransaction().commit();
			return Movements;
		} catch (Exception e) {
			db.getTransaction().rollback();
			throw e;
		}
	}
}
