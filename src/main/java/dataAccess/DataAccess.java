package dataAccess;

import java.io.File;


import java.net.NoRouteToHostException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.jws.WebMethod;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import configuration.ConfigXML;
import configuration.UtilDate;
import domain.Driver;
import domain.Erreklamazioa;
import domain.Erreklamazioa.ErrekLarri;
import domain.Erreklamazioa.ErrekMota;
import domain.Eskaera;
import domain.Eskaera.EskaeraEgoera;
import domain.Ride.RideEgoera;
import domain.Movement;
import domain.Ride;
import domain.User;
import domain.Admin;
import domain.Alerta;
import domain.Balorazio;
import domain.Alerta.AlertMota;
import domain.Bidaiari;
import domain.Car;
import exceptions.*;


/**
 * It implements the data access to the objectDb database
 */

//jfg
public class DataAccess {
	private EntityManager db;
	private EntityManagerFactory emf;
	private static final String err = "Errorea: ";

	ConfigXML c = ConfigXML.getInstance();

	public DataAccess() {
		/*
		 * if (c.isDatabaseInitialized()) { String fileName=c.getDbFilename();
		 * 
		 * File fileToDelete= new File(fileName); if(fileToDelete.delete()){ File
		 * fileToDeleteTemp= new File(fileName+"$"); fileToDeleteTemp.delete();
		 * 
		 * System.out.println("File deleted"); } else {
		 * System.out.println("Operation failed"); } }
		 */
		open();
		if (c.isDatabaseInitialized())
			initializeDB();

		System.out.println("DataAccess created => isDatabaseLocal: " + c.isDatabaseLocal() + " isDatabaseInitialized: "
				+ c.isDatabaseInitialized());

		close();

	}

	public DataAccess(EntityManager db) {
		this.db = db;
	}

	/**
	 * This is the data access method that initializes the database with some events
	 * and questions. This method is invoked by the business logic (constructor of
	 * BLFacadeImplementation) when the option "initialize" is declared in the tag
	 * dataBaseOpenMode of resources/config.xml file
	 */
	public void initializeDB() {

		db.getTransaction().begin();

		try {

			Calendar today = Calendar.getInstance();

			int month = today.get(Calendar.MONTH);
			if (month == 12) {
				month = 1;
			}

			
			 //Create drivers Driver driver1=new
			//Driver driver1 = new Driver("Aitor Fernandez", "1", "d1@gmail.com", "7342S");

			  //Create rides driver1.addRide("Donostia", "Bilbo",
			 // UtilDate.newDate(year,month,15); driver1.addRide("Donostia",
			  //"Gasteiz", UtilDate.newDate(year,month,6), 4, 8); driver1.addRide("Bilbo",
			  //"Donostia", UtilDate.newDate(year,month,25), 4, 4);
			  
			  //driver1.addRide("Donostia", "Iruña", UtilDate.newDate(year,month,7), 4, 8);
			  
			 // driver3.addRide("Bilbo", "Donostia", UtilDate.newDate(year,month,14), 1, 3);
			  
			  
			 
			 // db.persist(driver1); db.persist(driver2); db.persist(driver3);
			 
			Admin admin=new Admin("a@gmail.com", "admin", "0","0");
			System.out.println("Admin creado" + admin);
		    db.persist(admin);
		    System.out.println("añadido" + admin);
			db.getTransaction().commit();
			System.out.println("comit" + admin);
			System.out.println("Db initialized");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method returns all the cities where rides depart
	 * 
	 * @return collection of cities
	 */
	public List<String> getDepartCities() {
		TypedQuery<String> query = db.createQuery("SELECT DISTINCT r.from FROM Ride r ORDER BY r.from", String.class);
		List<String> cities = query.getResultList();
		return cities;

	}

	/**
	 * This method returns all the arrival destinations, from all rides that depart
	 * from a given city
	 * 
	 * @param from the depart location of a ride
	 * @return all the arrival destinations
	 */
	public List<String> getArrivalCities(String from) {
		TypedQuery<String> query = db.createQuery("SELECT DISTINCT r.to FROM Ride r WHERE r.from=?1 ORDER BY r.to",
				String.class);
		query.setParameter(1, from);
		List<String> arrivingCities = query.getResultList();
		return arrivingCities;

	}

	/**
	 * This method creates a ride for a driver
	 * 
	 * @param from        the origin location of a ride
	 * @param to          the destination location of a ride
	 * @param date        the date of the ride
	 * @param nPlaces     available seats
	 * @param driverEmail to which ride is added
	 * 
	 * @return the created ride, or null, or an exception
	 * @throws RideMustBeLaterThanTodayException if the ride date is before today
	 * @throws RideAlreadyExistException         if the same ride already exists for
	 *                                           the driver
	 */
	public Ride createRide(String from, String to, Date date, int nPlaces, float price, String driverEmail)
			throws RideAlreadyExistException, RideMustBeLaterThanTodayException {
		System.out.println(">> DataAccess: createRide=> from= " + from + " to= " + to + " driver=" + driverEmail
				+ " date " + date);
		try {
			if (new Date().compareTo(date) > 0) {
				throw new RideMustBeLaterThanTodayException(
						ResourceBundle.getBundle("Etiquetas").getString("CreateRideGUI.ErrorRideMustBeLaterThanToday"));
			}
			db.getTransaction().begin();

			Driver driver = db.find(Driver.class, driverEmail);
			if (driver.doesRideExists(from, to, date)) {
				db.getTransaction().commit();
				throw new RideAlreadyExistException(
						ResourceBundle.getBundle("Etiquetas").getString("DataAccess.RideAlreadyExist"));
			}
			Ride ride = driver.addRide(from, to, date, nPlaces, price);
			db.persist(driver);
			db.getTransaction().commit();

			return ride;
		} catch (NullPointerException e) {
			// TODO Auto-generated catch block
			db.getTransaction().commit();
			return null;
		}
	}
	
	public void addMovement(Movement mov) {
		User usr = mov.getUsr();
		if(mov.getMota().equals("+")) {
			createAlert(usr, AlertMota.DIRUA_SARTU);
		}else {
			createAlert(usr, AlertMota.DIRUA_ATERA);
		}
		usr.addMovement(mov);
	}

/*
 * public RideRequest requestRide(User user, Ride ride, int seatQuantity) throws RequestAlreadyExistsException {
		open();
		db.getTransaction().begin();
		ArrayList<RideRequest> rRs = ((Traveler)user).getRideRequests();
		RideRequest newRequest = new RideRequest(RideRequest.RequestState.PENDING, seatQuantity, ride, (Traveler)user);;
		
		boolean alreadyHasReq = false;
		
		for (RideRequest rr: rRs) {
			if (rr.getRide().equals(ride)) {
				alreadyHasReq = true;
				break;
			}
		}
		if (alreadyHasReq == false) {
			db.persist(newRequest);

			Ride rideDB = db.find(Ride.class, ride.getRideNumber());
			rideDB.addRequest(newRequest);
			
			System.out.println(newRequest);
			
			Alert alert = new Alert("Traveler batek bidaia eskatu du"+user.getEmail(),"",rideDB.getDriver());
			db.persist(alert);
			registerAlert(alert);

		} else throw new RequestAlreadyExistsException();

		db.getTransaction().commit();
		close();
		
		return newRequest;

	}

 */

	public Eskaera createEskaera(User user, Ride ride, int nPlaces)throws RequestAlreadyExistException{
		db.getTransaction().begin();
		User userDB = db.find(User.class, user.getEmail());
		Ride rideDB = db.find(Ride.class, ride.getRideNumber());
		Bidaiari bidaiariDB = ((Bidaiari)userDB);
		Eskaera eskBerr = new Eskaera(Eskaera.EskaeraEgoera.PENDING, nPlaces, rideDB, bidaiariDB);
		ArrayList<Eskaera> eskaeraListDB = bidaiariDB.getEskaerak();
		boolean exisDa = false;
		for (Eskaera eskDB : eskaeraListDB) {
			if (eskDB.getRide().equals(rideDB)) {
				exisDa=true;
				break;
			}
		}
		if (!exisDa) {
			eskaeraListDB.add(eskBerr);
		} else {
			db.getTransaction().rollback();
			throw new RequestAlreadyExistException();
		}
		ArrayList<Eskaera> rideEskaeraListDB = rideDB.getEskaerenList();
		rideEskaeraListDB.add(eskBerr);
		
		
		
		createAlert(rideDB.getDriver(), AlertMota.ESKAERA_EGIN);
		db.getTransaction().commit();
			
		return eskBerr;
	}


	

	/*
	 * public boolean storeRider (Bidaiari rider) { try {
	 * db.getTransaction().begin(); Bidaiari existingRider = db.find(Bidaiari.class,
	 * rider.getEmail()); if (existingRider == null) { db.persist(rider);
	 * db.getTransaction().commit();
	 * System.out.println("Rider stored successfully: " + rider); return true; }
	 * else { db.getTransaction().rollback();
	 * System.out.println("Rider already exists with email: " + rider.getEmail());
	 * return false; } } catch (Exception e) { db.getTransaction().rollback();
	 * System.err.println("Error storing rider: " + e.getMessage()); return false; }
	 */
	/*
	 * try { db.getTransaction().begin(); db.persist(rider);
	 * db.getTransaction().commit();
	 * System.out.println("Rider stored successfully: " + rider); } catch (Exception
	 * e) { db.getTransaction().rollback();
	 * System.err.println("Error storing rider: " + e.getMessage()); }
	 */
	// }

	/*
	 * public boolean storeDriver (Driver driver) {
	 * 
	 * try { db.getTransaction().begin(); Driver existingDriver =
	 * db.find(Driver.class, driver.getEmail()); if (existingDriver == null) {
	 * db.persist(driver); db.getTransaction().commit();
	 * System.out.println("Driver stored successfully: " + driver); return true; }
	 * else { db.getTransaction().rollback();
	 * System.out.println("Driver already exists with email: " + driver.getEmail());
	 * return false; } } catch (Exception e) { db.getTransaction().rollback();
	 * System.err.println("Error storing driver: " + e.getMessage()); return false;
	 * }
	 */

	/*
	 * try { db.getTransaction().begin(); db.persist(driver);
	 * db.getTransaction().commit();
	 * System.out.println("Driver stored successfully: " + driver); } catch
	 * (Exception e) { db.getTransaction().rollback();
	 * System.err.println("Error storing driver: " + e.getMessage()); }
	 */
	// }
	public User isRegistered(String email, String password) {
		try {
			db.getTransaction().begin();
			User existingUser = db.find(User.class, email);
			if (existingUser == null) { // Ez dago erregistratuta email hori duen inor
				db.getTransaction().rollback();// para deshacer una transacción en una base de datos
				System.out.println("You are not registered yet");
				return null;
			} else {
				if (password.equals(existingUser.getPasahitza())) {// berdinak badira pasahitzak
					System.out.println("Good");
					db.getTransaction().commit();
					return existingUser;
				} else {
					
					System.out.println("Your password is not correct");
					db.getTransaction().rollback();
					return null;
				}
			}
		} catch (Exception e) {
			db.getTransaction().rollback();
			System.out.println("Error while checking registration: " + e.getMessage());
			return null;
		}
	}

	public boolean diruaSartu(User user, float diru) {
		try {
			db.getTransaction().begin();
			User existingUser = db.find(User.class, user.getEmail());
			float diruBerr = existingUser.getDirua() + diru;
			if (diruBerr>0) {
				existingUser.setDirua(diruBerr);
				createMov(existingUser, diru, "+");
				db.getTransaction().commit();
				return true;
			}else {
				db.getTransaction().rollback();
				return false;
			}
			
		} catch (Exception e) {
			db.getTransaction().rollback();
			System.err.println("Errorea dirua sartzean: " + e.getMessage());
			return false;
		}
	}
	
	

	public List<Bidaiari> getAllBidaiari() {
		db.getTransaction().begin();
		TypedQuery<Bidaiari> query = db.createQuery("SELECT b FROM Bidaiari b", Bidaiari.class);
		db.getTransaction().commit();
		return query.getResultList();
		
	}

	public List<Eskaera> getAllEskaera() {
		db.getTransaction().begin();
		TypedQuery<Eskaera> query = db.createQuery("SELECT b FROM Eskaera b", Eskaera.class);
		db.getTransaction().commit();
		return query.getResultList();
	}
	
	public List<Ride> getAllRides(){
		db.getTransaction().begin();
		TypedQuery<Ride> query = db.createQuery("SELECT b FROM Ride b", Ride.class);
		db.getTransaction().commit();
		return query.getResultList();
	}
	public List<Movement> getUserMugimenduak(User user) {
		try {
			db.getTransaction().begin();
			User existingUser = db.find(User.class, user.getEmail());
			db.getTransaction().commit();
			return existingUser.getMugimenduak();
		} catch (Exception e) {
			db.getTransaction().rollback();
			System.err.println(err + e.getMessage());
			return null;
		}
	}
	/*public boolean jarri(boolean jarri, Eskaera eskaera) {
		try {
			db.getTransaction().begin();
			eskaera.setBaieztatuta(jarri);
			db.merge(eskaera);
			db.getTransaction().commit();
			return true;
		}catch (Exception e) {
			db.getTransaction().rollback();
			System.err.println("Error eskaera onartzean: " + e.getMessage());
			return false;
		}
	}*/

	public List<Ride> getDriverRides(Driver driver) {
		db.getTransaction().begin();
		Driver existingDriver = db.find(Driver.class, driver.getEmail());
		db.getTransaction().commit();
		return existingDriver.getRides();
	}
	public List<Car> getDriverCars(Driver driver) {
		db.getTransaction().begin();
		Driver existingDriver = db.find(Driver.class, driver.getEmail());
		db.getTransaction().commit();
		return existingDriver.getCars();
	}
	public void kantzelatuRide(Ride ride) {
		db.getTransaction().begin();
		Ride rideDB = db.find(Ride.class, ride.getRideNumber());
		rideDB.setEgoera(RideEgoera.CANCELLED);
		for(Eskaera esk: rideDB.getEskaerenList()) {
			if (esk.getEgoera()==EskaeraEgoera.ACCEPTED) {
				esk.getBidaiari().setDirua(esk.getBidaiari().getDirua()+esk.getPrez());
				
				createMov(esk.getBidaiari(),esk.getPrez(), "+");
				createAlert(esk.getBidaiari(), AlertMota.BIDAIA_KANTZELATU);
				
				esk.setEgoera(EskaeraEgoera.CANCELLED);
			}
		}
		db.getTransaction().commit();
		
	}
	
	
	public void amaituRide(Ride ride) {
		db.getTransaction().begin();
		Ride rideDB = db.find(Ride.class, ride.getRideNumber());
		rideDB.setEgoera(RideEgoera.FINISHED);
		for(Eskaera esk: rideDB.getEskaerenList()) {
			if(esk.getEgoera()==EskaeraEgoera.ACCEPTED) {
				esk.setEgoera(EskaeraEgoera.FINISHED);
				
				createAlert(esk.getBidaiari(), AlertMota.BIDAIA_AMAITUTA);
				
			}
		}
		
		db.getTransaction().commit();
	}
	
	public void ezabatuAlertakUser(User user) {
		db.getTransaction().begin();
		User userDB = db.find(User.class, user.getEmail());
		userDB.ezabatuAlertakUser();
		db.getTransaction().commit();
		
	}

	/*public boolean ezabatuEskaera(Eskaera esk) {
		try {
			db.getTransaction().begin();
			if(esk.getBidaiari() !=null) {
				Bidaiari bidaiari = esk.getBidaiari();
				bidaiari.getEskaerak().remove(esk);
				esk.setBidaiari(null);
				db.merge(bidaiari);
			}
			db.remove(db.contains(esk) ? esk : db.merge(esk));
			db.getTransaction().commit();
			return true;
		} catch (Exception e) {
			db.getTransaction().rollback();
			System.err.println("Error deleting ride: " + e.getMessage());
			return false;
		}
	}*/

	public boolean storeUser(User user) {
		try {
			db.getTransaction().begin();
			User existingUser = db.find(User.class, user.getEmail());
			if (existingUser == null) {
				db.persist(user);
				db.getTransaction().commit();
				System.out.println("User stored successfully: " + user);
				return true;
			} else {
				db.getTransaction().rollback();
				System.out.println("User already exists with email: " + user.getEmail());
				return false;
			}
		} catch (Exception e) {
			db.getTransaction().rollback();
			System.err.println("Error storing user: " + e.getMessage());
			return false;
		}

	}

	/**
	 * This method retrieves the rides from two locations on a given date
	 * 
	 * @param from the origin location of a ride
	 * @param to   the destination location of a ride
	 * @param date the date of the ride
	 * @return collection of rides
	 */
	public List<Ride> getRides(String from, String to, Date date) {
		System.out.println(">> DataAccess: getRides=> from= " + from + " to= " + to + " date " + date);

		List<Ride> res = new ArrayList<>();
		TypedQuery<Ride> query = db.createQuery("SELECT r FROM Ride r WHERE r.from=?1 AND r.to=?2 AND r.date=?3",
				Ride.class);
		query.setParameter(1, from);
		query.setParameter(2, to);
		query.setParameter(3, date);
		List<Ride> rides = query.getResultList();
		for (Ride ride : rides) {
			res.add(ride);
		}
		return res;
	}

	public List<String> getEmails() {
		List<String> emails = new ArrayList<String>();
		emails = db.createQuery("SELECT d.email FROM Driver d", String.class).getResultList();
		return emails;
	}

	/**
	 * This method retrieves from the database the dates a month for which there are
	 * events
	 * 
	 * @param from the origin location of a ride
	 * @param to   the destination location of a ride
	 * @param date of the month for which days with rides want to be retrieved
	 * @return collection of rides
	 */
	public List<Date> getThisMonthDatesWithRides(String from, String to, Date date) {
		System.out.println(">> DataAccess: getEventsMonth");
		List<Date> res = new ArrayList<>();

		Date firstDayMonthDate = UtilDate.firstDayMonth(date);
		Date lastDayMonthDate = UtilDate.lastDayMonth(date);

		TypedQuery<Date> query = db.createQuery(
				"SELECT DISTINCT r.date FROM Ride r WHERE r.from=?1 AND r.to=?2 AND r.date BETWEEN ?3 and ?4",
				Date.class);

		query.setParameter(1, from);
		query.setParameter(2, to);
		query.setParameter(3, firstDayMonthDate);
		query.setParameter(4, lastDayMonthDate);
		List<Date> dates = query.getResultList();
		for (Date d : dates) {
			res.add(d);
		}
		return res;
	}

	public boolean addCar(String licensePlate, int places, String model, String color, String driverEmail) {
	
			db.getTransaction().begin();
			Driver driver = db.find(Driver.class, driverEmail);
			if(driver!=null) {
				if (driver.doesCarExist(licensePlate)) {
					db.getTransaction().commit();
					System.out.println("car already exists");
					return false;
				}
				System.out.println(licensePlate);
				Car car = driver.addCar(licensePlate,  places,  model,  color);
				System.out.println(car.toString());

				//db.persist(driver);
				db.getTransaction().commit();
				return true;
			}else {
				db.getTransaction().rollback();
				return false;
			}
			
		
	}

	
	public void open() {

		String fileName = c.getDbFilename();
		if (c.isDatabaseLocal()) {
			emf = Persistence.createEntityManagerFactory("objectdb:" + fileName);
			db = emf.createEntityManager();
		} else {
			Map<String, String> properties = new HashMap<>();
			properties.put("javax.persistence.jdbc.user", c.getUser());
			properties.put("javax.persistence.jdbc.password", c.getPassword());

			emf = Persistence.createEntityManagerFactory(
					"objectdb://" + c.getDatabaseNode() + ":" + c.getDatabasePort() + "/" + fileName, properties);
			db = emf.createEntityManager();
		}
		System.out.println("DataAccess opened => isDatabaseLocal: " + c.isDatabaseLocal());

	}

	public void close() {
		db.close();
		System.out.println("DataAcess closed");
	}
	
	
	public List<Eskaera> getEskaerakRide(Ride ride) {
		
		db.getTransaction().begin();
	    Ride rideDB = db.find(Ride.class, ride.getRideNumber());
	    //TypedQuery<Eskaera> query = db.createQuery("SELECT DISTINCT esk FROM Eskaera esk WHERE esk.ride = :ride", Eskaera.class);
	    //query.setParameter("ride", rideDB);
	    //List<Eskaera> eskaerak = query.getResultList();
	    List<Eskaera> eskaerak = rideDB.getEskaerenList();
	    db.getTransaction().commit();
	    return eskaerak;
	}
	
	
	/*public void acceptEskaera(Eskaera eskaera) throws NotEnoughPlacesException, NotEnoughMoneyException{
		db.getTransaction().begin();

		Eskaera eskaeraDB = db.find(Eskaera.class, eskaera.getEskaeraNumber());
		Bidaiari bidaiariDB = eskaeraDB.getBidaiari();
		Ride ride = eskaeraDB.getRide();
		int lekuLibre = ride.getnPlaces();
		int lekuEskatu = eskaeraDB.getNPlaces();
		float daukanDiru = bidaiariDB.getDirua();
		float balioDuena = ride.getPrice();
		
		if(daukanDiru<balioDuena*1.5) {
			db.getTransaction().rollback();
			throw new NotEnoughMoneyException();
		}
		
		db.persist(bidaiariDB);

		if(lekuLibre<lekuEskatu) {
			db.getTransaction().rollback();
			throw new NotEnoughPlacesException();
		}
		
		eskaeraDB.acceptRequest();
		ride.setnPlaces(lekuLibre-lekuEskatu);
		Movement mov = new Movement(bidaiariDB, eskaeraDB.getPrez(), "-");
		addMovement(mov);
		db.persist(mov);
		
		Alerta alert = new Alerta(bidaiariDB, AlertMota.ESKAERA_ONARTU);
		
		addAlert(alert);
		db.persist(alert);
		db.getTransaction().commit();
	}*/
	
	public void acceptEskaera(Eskaera eskaera) throws NotEnoughPlacesException, NotEnoughMoneyException {
	    
	    db.getTransaction().begin();
	    Eskaera eskaeraDB = db.find(Eskaera.class, eskaera.getEskaeraNumber());
	    Bidaiari bidaiariDB = eskaeraDB.getBidaiari();
	    Ride ride = eskaeraDB.getRide();

	    verifyMoney(bidaiariDB, ride);
	    verifyPlaces(ride, eskaeraDB);

	    processAcceptance(eskaeraDB, bidaiariDB, ride);
	    db.getTransaction().commit();
	}
	
	private void verifyMoney(Bidaiari bidaiari, Ride ride) throws NotEnoughMoneyException {
	    if (bidaiari.getDirua() < ride.getPrice() * 1.5) {
	        db.getTransaction().rollback();
	        throw new NotEnoughMoneyException();
	    }
	    db.persist(bidaiari);
	}
	
	private void verifyPlaces(Ride ride, Eskaera eskaera) throws NotEnoughPlacesException {
	    if (ride.getnPlaces() < eskaera.getNPlaces()) {
	        db.getTransaction().rollback();
	        throw new NotEnoughPlacesException();
	    }
	}
	
	private void processAcceptance(Eskaera eskaera, Bidaiari bidaiari, Ride ride) {
	    eskaera.acceptRequest();
	    ride.setnPlaces(ride.getnPlaces() - eskaera.getNPlaces());

	    
	    createMov(bidaiari, eskaera.getPrez(), "-");
	    
	    
	    createAlert(bidaiari, AlertMota.ESKAERA_ONARTU);
	}



	
	public void ezOnartuEskaera(Eskaera eskaera) {
		db.getTransaction().begin();
		Eskaera eskaeraDB = db.find(Eskaera.class, eskaera.getEskaeraNumber());
		eskaeraDB.ezeztatuEskaera();
		createAlert(eskaeraDB.getBidaiari(), AlertMota.ESKAERA_EZONARTUA);
		db.getTransaction().commit();
		
		
	}
	public void konfirmatuEskaera(Eskaera eskaera) {
		db.getTransaction().begin();
		Eskaera eskaeraDB = db.find(Eskaera.class, eskaera.getEskaeraNumber());
		eskaeraDB.konfirmatuEskaera();
		eskaeraDB.getRide().getDriver().diruSartuDri(eskaeraDB.getNPlaces()*eskaeraDB.getRide().getPrice());
		
		createMov(eskaeraDB.getRide().getDriver(),eskaeraDB.getRide().getPrice(), "+");
		
		db.getTransaction().commit();
	}
	
	public List<Eskaera> getEskaerakBidaiari(Bidaiari bidaiari){
		db.getTransaction().begin();
		Bidaiari bidaiariDB = db.find(Bidaiari.class, bidaiari.getEmail());
		db.getTransaction().commit();
		return bidaiariDB.getEskaerak();
	}
	
	public void kantzelatuEskaera(Eskaera eskaera) {
		db.getTransaction().begin();
		Eskaera eskaeraDB = db.find(Eskaera.class, eskaera.getEskaeraNumber());
		if (eskaeraDB.getEgoera() == EskaeraEgoera.PENDING) {
			eskaeraDB.setEgoera(EskaeraEgoera.CANCELLED);
		}else if (eskaeraDB.getEgoera() == EskaeraEgoera.ACCEPTED) {
			eskaeraDB.getBidaiari().diruSartuBid(eskaeraDB.getPrez());
			
			
			createMov(eskaeraDB.getBidaiari(),eskaeraDB.getPrez(), "+");
			int lekuLibre = eskaeraDB.getRide().getnPlaces();
			int eskatutakoak = eskaeraDB.getNPlaces();
			eskaeraDB.getRide().setnPlaces(lekuLibre+eskatutakoak);
			eskaeraDB.setEgoera(EskaeraEgoera.CANCELLED);	
		}
		
		
		createAlert(eskaeraDB.getRide().getDriver(), AlertMota.ESKAERA_KANTZELATU);
		db.getTransaction().commit();
	}
	
	/*public boolean ezabatuUser(User user) {
		db.getTransaction().begin();
		User ezabUserDB = bilatuUserEmail(user.getEmail());
		if (ezabUserDB instanceof Driver) {
			Driver ezabDriverDB = (Driver) ezabUserDB;
			List<Ride> rideList = ezabDriverDB.getRides();
			for(Ride ride : rideList) {
				//kantzelatuRide(ride);
				Ride rideDB = db.find(Ride.class, ride.getRideNumber());
				rideDB.setEgoera(RideEgoera.CANCELLED);
				for(Eskaera esk: rideDB.getEskaerenList()) {
					if (esk.getEgoera()==EskaeraEgoera.ACCEPTED) {
						esk.getBidaiari().setDirua(esk.getBidaiari().getDirua()+esk.getPrez());
						Movement mov = new Movement(esk.getBidaiari(),esk.getPrez(), "+");
						addMovement(mov);
						db.persist(mov);
						Alerta alert = new Alerta(esk.getBidaiari(), AlertMota.BIDAIA_KANTZELATU);
						
						addAlert(alert);
						db.persist(alert);
						esk.setEgoera(EskaeraEgoera.CANCELLED);
					}
				}
			}
		}else {
			Bidaiari ezabBidDB = (Bidaiari) ezabUserDB;
			List<Eskaera> eskList = ezabBidDB.getEskaerak();
			for(Eskaera esk : eskList) {
				if(esk.getEgoera() == EskaeraEgoera.FINISHED) {
					return false;
				}else {
					//kantzelatuEskaera(esk);
					Eskaera eskaeraDB = db.find(Eskaera.class, esk.getEskaeraNumber());
					if (eskaeraDB.getEgoera() == EskaeraEgoera.PENDING) {
						eskaeraDB.setEgoera(EskaeraEgoera.CANCELLED);
					}else if (eskaeraDB.getEgoera() == EskaeraEgoera.ACCEPTED) {
						eskaeraDB.getBidaiari().diruSartuBid(eskaeraDB.getPrez());
						Movement mov = new Movement( eskaeraDB.getBidaiari(),eskaeraDB.getPrez(), "+");
						addMovement(mov);
						db.persist(mov);
						int lekuLibre = eskaeraDB.getRide().getnPlaces();
						int eskatutakoak = eskaeraDB.getNPlaces();
						eskaeraDB.getRide().setnPlaces(lekuLibre+eskatutakoak);
						eskaeraDB.setEgoera(EskaeraEgoera.CANCELLED);	
					}
					Alerta alert = new Alerta(eskaeraDB.getRide().getDriver(), AlertMota.ESKAERA_KANTZELATU);
					
					addAlert(alert);
					db.persist(alert);
				}
			}
		}
		 db.remove(ezabUserDB);
		db.getTransaction().commit();
		return true;
	}*/
	
	public boolean ezabatuUser(User user) {
	    db.getTransaction().begin();
	    User ezabUserDB = bilatuUserEmail(user.getEmail());

	    boolean result;

	    if (ezabUserDB instanceof Driver) {
	        result = ezabatuDriver((Driver) ezabUserDB);
	    } else {
	        result = ezabatuBidaiari((Bidaiari) ezabUserDB);
	    }

	    if (result) {
	        db.remove(ezabUserDB);
	        db.getTransaction().commit();
	    } else {
	        db.getTransaction().rollback();
	    }

	    return result;
	}
	
	private boolean ezabatuDriver(Driver driver) {
	    for (Ride ride : driver.getRides()) {
	        Ride rideDB = db.find(Ride.class, ride.getRideNumber());
	        rideDB.setEgoera(RideEgoera.CANCELLED);
	        kantzelatuEskaerakRide(rideDB);
	    }
	    return true;
	}

	private boolean ezabatuBidaiari(Bidaiari bidaiari) {
	    for (Eskaera esk : bidaiari.getEskaerak()) {
	        if (esk.getEgoera() == EskaeraEgoera.FINISHED) {
	            return false;
	        }
	        kantzelatuEskaera(esk);
	    }
	    return true;
	}

	private void kantzelatuEskaerakRide(Ride rideDB) {
	    for (Eskaera esk : rideDB.getEskaerenList()) {
	        if (esk.getEgoera() == EskaeraEgoera.ACCEPTED) {
	            esk.getBidaiari().diruSartuBid(esk.getPrez());;
	        }
	        esk.setEgoera(EskaeraEgoera.CANCELLED);
	    }
	}

	
	public User bilatuUserEmail(String email) {
		User user = db.find(User.class, email);
		if (user != null) {
			return user;
		}else{
			return null;
		}
	}
	
	public List<Alerta> getUserAlertak(User user){
		try {
			db.getTransaction().begin();
			User existingUser = db.find(User.class, user.getEmail());
			db.getTransaction().commit();
			return existingUser.getAlertak();
		} catch (Exception e) {
			db.getTransaction().rollback();
			System.err.println(err + e.getMessage());
			return null;
		}
	}
	
	public void addAlert(Alerta alert) {
			User usr = alert.getUser();
			System.out.println("Data Acces-eko addAlert metodora iritsi da");
			usr.addAlert(alert);
	}
	public void createAlert(User user, AlertMota mota) {
		Alerta alert = new Alerta(user, mota);
		addAlert(alert); 
		db.persist(alert);
	}
	
	public void createMov(User user, float diru, String mota) {
		Movement mov = new Movement(user,  diru,  mota);
		addMovement(mov);
		db.persist(mov);
	}
	
	
	/*public void addB(Balorazio b) {
		User usr = b.getUserJaso();
		System.out.println("Data Acces-eko addAlert metodora iritsi da");
		usr.addBalorazioa(b);
	}*/
	
	public void addBalorazioa(User userJaso, User userJarri, String deskribapena, Integer nota, Eskaera eskaera) {
		
		db.getTransaction().begin();
		Balorazio balorazio = new Balorazio(userJarri, userJaso, deskribapena, nota, eskaera);
		System.out.println("Hau addBalorazioa egin baino lehen");
		System.out.println(balorazio);
		System.out.println("BALORAZIOA:");
		//System.out.println("Jarri: " + balorazio.getUserJarri());
		System.out.println("Jaso: " + balorazio.getUserJaso());
		System.out.println("Deskribapena: " + balorazio.getDeskribapena());
		System.out.println("Nota: " + balorazio.getNota());
		System.out.println("Eskaera: " + balorazio.getEskaera());
		db.persist(balorazio);
		//Alerta alert = new Alerta(balorazio.getUserJaso(), AlertMota.BALORATUTA);
		//addAlert(alert);
		//db.persist(alert);
		//balorazio.getEskaera().setEgoera(EskaeraEgoera.VALUED);
		db.getTransaction().commit();
		/*try {
			System.out.println("DAO BALORAZIOA:");
			System.out.println("Jarri: " + balorazio.getUserJarri());
			System.out.println("Jaso: " + balorazio.getUserJaso());
			System.out.println("Data accesen addBalorazioa-ren barruan");
			System.out.println(balorazio);
			
			db.getTransaction().begin();
			System.out.println(balorazio);
			System.out.println("Trantsakzioa hasi ondoren");
			System.out.println(balorazio);
			//db.persist(balorazio);
			//Balorazio bDB = db.find(Balorazio.class, balorazio.getID());
			//System.out.println(bDB.getUserJaso());//null
			//System.out.println("-------------");
			//System.out.println(bDB.getUserJaso().getEmail());
			//User existingUser = db.find(User.class, bDB.getUserJaso().getEmail());
			System.out.println("userreko metodoa baino lehen");
			Balorazio balorazioGehitutakoa = existingUser.addBalorazioa(bDB);
			db.persist(balorazioGehitutakoa);
			System.out.println("egin dau persist");
			Eskaera eskDB = db.find(Eskaera.class, balorazioGehitutakoa.getEskaera());
			eskDB.setEgoera(EskaeraEgoera.VALUED);
			System.out.println("eskaera valued jarri");
			Alerta alert = new Alerta(existingUser, AlertMota.BALORATUTA);
			addAlert(alert);
			db.persist(alert);
			db.getTransaction().commit();
		} catch (NullPointerException e) {
			db.getTransaction().rollback();
		} catch (Exception e) {
	        e.printStackTrace();
	        db.getTransaction().rollback();
	    }*/
	}
	
	public List<Balorazio> getUserBalorazioa(User user){
		try {
			db.getTransaction().begin();
			User existingUser = db.find(User.class, user.getEmail());
			db.getTransaction().commit();
			return existingUser.getBalorazioak();
		} catch (Exception e) {
			db.getTransaction().rollback();
			System.err.println(err + e.getMessage());
			return null;
		}
	}
	
	public List<Erreklamazioa> getUserErrek(User user){
		try {
			db.getTransaction().begin();
			User existingUser = db.find(User.class, user.getEmail());
			db.getTransaction().commit();
			return existingUser.getErrek();
		} catch (Exception e) {
			db.getTransaction().rollback();
			System.err.println(err + e.getMessage());
			return null;
		}
	}
	
	public ArrayList<Erreklamazioa> getAllErrek() {
		db.getTransaction().begin();
		ArrayList<Erreklamazioa> errek = new ArrayList<>();	
		TypedQuery<Erreklamazioa> query = db.createQuery("SELECT c FROM Erreklamazioa c",Erreklamazioa.class);   
		List<Erreklamazioa> errekList = query.getResultList();
		for (Erreklamazioa e :errekList){
			errek.add(e);
		}
		
		db.getTransaction().commit();
		return errek;
	}
	
	public void acceptErrek(Erreklamazioa selectRk) {
        db.getTransaction().begin();
//try {
                      Erreklamazioa erreklDB = db.find(Erreklamazioa.class, selectRk.getId());
        
        if(erreklDB.getErrekJaso() instanceof Bidaiari) {
                      //Txikia --> *1,1
                      //Ertaina --> *1,3
                      //Handia --> *1,5
                      //Bidaiaria kendu diruA gidariari gehitu
                      Driver driver = (Driver) erreklDB.getErrekJarri();
                      Driver driverDB = db.find(Driver.class, driver.getEmail());
                      
                      Bidaiari bidaiari = (Bidaiari) erreklDB.getErrekJaso();
                      Bidaiari bidaiariDB = db.find(Bidaiari.class, bidaiari.getEmail());
                      
                      
                      float dirua;
/*2*/    if(erreklDB.getLarri().equals(ErrekLarri.TXIKIA)) {
                                     dirua = erreklDB.getEskaera().getPrez() * (1.1f);
                      }else if(erreklDB.getLarri().equals(ErrekLarri.ERTAINA)) {
                                     dirua = erreklDB.getEskaera().getPrez() * (1.3f);
                      }else {
                                     dirua = erreklDB.getEskaera().getPrez() * (1.5f);
                      }
                      bidaiariDB.diruSartuBid((dirua*(-1)));
                      
                      createMov( bidaiariDB,dirua, "-");
                      
                      createAlert(bidaiariDB, AlertMota.DIRUA_ATERA);
                      
                      driverDB.diruSartuDri(dirua);
                      
                      createMov(driverDB, dirua, "+");
                      createAlert(driverDB, AlertMota.DIRUA_SARTU);
                      
        }else {
                      //Bidaiariari gehitu dirua besteari kendu
                      Driver driver = (Driver) erreklDB.getErrekJaso();
                      Driver driverDB = db.find(Driver.class, driver.getEmail());
                      
                      Bidaiari bidaiari = (Bidaiari) erreklDB.getErrekJarri();
                      Bidaiari bidaiariDB = db.find(Bidaiari.class, bidaiari.getEmail());
                      
                      
                      bidaiariDB.diruSartuBid(erreklDB.getEskaera().getPrez());
                      
                      createMov(bidaiariDB, erreklDB.getEskaera().getPrez(), "+");
                      createAlert(bidaiariDB, AlertMota.DIRUA_SARTU);
                      
                      
                      
                      driverDB.diruSartuDri(erreklDB.getEskaera().getPrez()*(-1));
                      
                      createMov(driverDB,erreklDB.getEskaera().getPrez(), "-");
                      createAlert(driverDB, AlertMota.DIRUA_ATERA);
                      
        }
        erreklDB.setMota(ErrekMota.ACCEPTED);
        
        createAlert(erreklDB.getErrekJarri(), AlertMota.ERREKLAMAZIOA_ONARTUTA);
        
        db.getTransaction().commit();
        
        /*}catch(NullPointerException e ) {
                      db.getTransaction().rollback(); 
        }*/
}

	
	 public void rejectErrekUser(Erreklamazioa selectRk) {
		 db.getTransaction().begin();
		 Erreklamazioa errekDB = db.find(Erreklamazioa.class, selectRk.getId());
		 addAdminErrek(errekDB);
		 errekDB.setMota(ErrekMota.ADMIN);
		
		 
		 createAlert(errekDB.getErrekJarri(), AlertMota.ERREKLAMAZIOA_DEUSESTATUTA);
		 
		 db.getTransaction().commit();
	 }
	
	 public void addAdminErrek(Erreklamazioa selectRk) {
		 try {
			
			User existingUser = db.find(User.class, "a@gmail.com");
			Erreklamazioa gehitutakoa = existingUser.addErrek(selectRk);
			db.persist(gehitutakoa);
			
		} catch (NullPointerException e) {
			db.getTransaction().rollback();
		}
	 }
	 
	 public void rejectErrekAdmin(Erreklamazioa selectRk) {
		 db.getTransaction().begin();
		 Erreklamazioa errekDB = db.find(Erreklamazioa.class, selectRk.getId());
		 errekDB.setMota(ErrekMota.REJECTED);
		 
		 createAlert(errekDB.getErrekJarri(), AlertMota.ADMINAK_ERREKLAMAZIOA_DEUSESTATU);
		 
		 db.getTransaction().commit();
	 }
	 /*public void AcceptWithChange(Erreklamazioa selectRk, int num) {
		 db.getTransaction().begin();
		 Erreklamazioa erreklDB = db.find(Erreklamazioa.class, selectRk.getId());
		 
		 Driver driver = (Driver) erreklDB.getErrekJarri();
		 Driver driverDB = db.find(Driver.class, driver.getEmail());
			
		 Bidaiari bidaiari = (Bidaiari) erreklDB.getErrekJaso();
		 Bidaiari bidaiariDB = db.find(Bidaiari.class, bidaiari.getEmail());
		 
		 float dirua;
		 
		 if(num==1) {
			 erreklDB.setLarri(ErrekLarri.TXIKIA);
			 dirua = erreklDB.getEskaera().getPrez() * (1.1f);
		 }else if(num==2) {
			 erreklDB.setLarri(ErrekLarri.ERTAINA);
			 dirua = erreklDB.getEskaera().getPrez() * (1.3f);
		 }else {
			 erreklDB.setLarri(ErrekLarri.HANDIA);
			 dirua = erreklDB.getEskaera().getPrez() * (1.5f);
		 }
		  
		 bidaiariDB.diruSartuBid((dirua*(-1)));
		 Movement mov = new Movement(bidaiariDB,dirua, "-" );
		 addMovement(mov);
		 db.persist(mov);
		 Alerta alert = new Alerta(bidaiariDB, AlertMota.DIRUA_ATERA);
		 
		 addAlert(alert); 
		 db.persist(alert);
			
		 driverDB.diruSartuDri(dirua);
		 Movement mov2 = new Movement(driverDB,dirua, "+" );
		 addMovement(mov2);
		 db.persist(mov2);
		 Alerta alert2 = new Alerta(driverDB, AlertMota.DIRUA_SARTU);
		 
		 addAlert(alert2);
		 db.persist(alert2);
		 
		 erreklDB.setMota(ErrekMota.ACCEPTED);
		 Alerta alert3 = new Alerta(erreklDB.getErrekJarri(), AlertMota.ADMINAK_ERREKLAMAZIOA_ONARTU);
		 
		 addAlert(alert3);
		 db.persist(alert3);
		 db.getTransaction().commit();
		 
	 }*/
	 
	 public void AcceptWithChange(Erreklamazioa selectRk, int num) {
		    db.getTransaction().begin();
		    Erreklamazioa erreklDB = db.find(Erreklamazioa.class, selectRk.getId());

		    Driver driverDB = getDriver(erreklDB);
		    Bidaiari bidaiariDB = getBidaiari(erreklDB);

		    float dirua = calculateAmount(erreklDB, num);
		    processBidaiari(bidaiariDB, dirua);
		    processDriver(driverDB, dirua);
		    erreklDB.setMota(ErrekMota.ACCEPTED);
			
			
			createAlert(erreklDB.getErrekJarri(), AlertMota.ADMINAK_ERREKLAMAZIOA_ONARTU);
			
		    db.getTransaction().commit();
		}
	 
	 private Driver getDriver(Erreklamazioa erreklDB) {
		    Driver driver = (Driver) erreklDB.getErrekJarri();
		    return db.find(Driver.class, driver.getEmail());
		}

		private Bidaiari getBidaiari(Erreklamazioa erreklDB) {
		    Bidaiari bidaiari = (Bidaiari) erreklDB.getErrekJaso();
		    return db.find(Bidaiari.class, bidaiari.getEmail());
		}
		
		private float calculateAmount(Erreklamazioa erreklDB, int num) {
		    float prez = erreklDB.getEskaera().getPrez();
		    if (num == 1) {
		        erreklDB.setLarri(ErrekLarri.TXIKIA);
		        return prez * 1.1f;
		    } else if (num == 2) {
		        erreklDB.setLarri(ErrekLarri.ERTAINA);
		        return prez * 1.3f;
		    } else {
		        erreklDB.setLarri(ErrekLarri.HANDIA);
		        return prez * 1.5f;
		    }
		}

		private void processBidaiari(Bidaiari bidaiari, float dirua) {
		    bidaiari.diruSartuBid(dirua*(-1));
		    
			 createMov(bidaiari,dirua, "-" );
			
			 
			 createAlert(bidaiari, AlertMota.DIRUA_ATERA);
		}

		private void processDriver(Driver driver, float dirua) {
		    driver.diruSartuDri(dirua);
		    
			 createMov(driver,dirua, "+" );
			 
			 createAlert(driver, AlertMota.DIRUA_SARTU);
		}

	 
	 
		public void addErreklamazio(User userJarri, User userJaso, Eskaera eskSelect, String sartutakoTxt, float prez, ErrekLarri lar) {
		    System.out.println(">>> ENTRA en addErreklamazio <<<");
		    System.out.println(">>> Entrando a addErreklamazio <<<");
		    System.out.println("userJarri: " + userJarri);
		    System.out.println("userJaso: " + userJaso);
		    System.out.println("eskSelect: " + eskSelect);
		    System.out.println("db: " + db);
/*
		    if(sartutakoTxt==null || sartutakoTxt.trim().isEmpty()) {
		    	System.out.println(">>> sartutakoTxt es NULL <<<");
		        throw new NullPointerException("Deskribapena ezin da null izan");
		    }
		    
		    if (userJaso == null) {
		        System.out.println(">>> userJaso es NULL <<<");
		        throw new NullPointerException("User Jaso ezin da null izan");
		    }

		    if (userJarri == null) {
		        System.out.println(">>> userJarri es NULL <<<");
		        throw new NullPointerException("User Jarri ezin da null izan");
		    }
		    if (eskSelect == null) {
		        System.out.println(">>> eskSelect es NULL <<<");
		        throw new NullPointerException("Eskaera ezin da null izan");
		    }
		    
		    
*/
		    System.out.println(">>> userJarri y userJaso no son null <<<");
		    System.out.println(">>> eskSelect: " + eskSelect + ", lar: " + lar + " <<<");

		    try {
		        db.getTransaction().begin();
		        System.out.println(">>> Transacción iniciada <<<");

		        User userJarriDB = db.find(User.class, userJarri.getEmail());
		        User userJasoDB = db.find(User.class, userJaso.getEmail());
		        System.out.println(">>> find hechos: jarriDB=" + userJarriDB + ", jasoDB=" + userJasoDB);

		        Erreklamazioa errekJarri;
		        if (userJarriDB instanceof Bidaiari) {
		            System.out.println(">>> userJarriDB es Bidaiari <<<");
		            errekJarri = new Erreklamazioa(userJarriDB, userJasoDB, eskSelect, sartutakoTxt, eskSelect.getPrez());
		        } else {
		            System.out.println(">>> userJarriDB es Driver <<<");
		            errekJarri = new Erreklamazioa(userJarriDB, userJasoDB, eskSelect, sartutakoTxt, eskSelect.getPrez(), lar);
		        }

		        db.persist(errekJarri);
		        System.out.println(">>> persist llamado correctamente <<<");

		        db.getTransaction().commit();
		        System.out.println(">>> commit hecho <<<");

		    } catch (Exception e) {
		        System.out.println(">>> EXCEPCIÓN dentro de addErreklamazio: " + e);
		        db.getTransaction().rollback();
		        throw e;
		    }
		}

	 
	 public Driver getDriverOfRide(Ride ride) {
		 db.getTransaction().begin();
		 Ride rDB = db.find(Ride.class, ride.getRideNumber());
		 db.getTransaction().commit();
		 return rDB.getDriver();
	 }
}
