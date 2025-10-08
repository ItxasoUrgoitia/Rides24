package createEskaeraTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import dataAccess.DataAccess;
import domain.Bidaiari;
import domain.Driver;
import domain.Ride;
import domain.Eskaera;
import domain.Alerta;
import exceptions.RequestAlreadyExistException;
import testOperations.TestDataAccess;

import java.sql.Date;
import java.util.List;

public class createEskaeraBDWhiteTest {
	static DataAccess sut; // = new DataAccess();
	static TestDataAccess testDA = new TestDataAccess();

	@Before
    public void setUp() {
        // **Konexioa ireki test bakoitzaren hasieran**
        testDA.open();
        // **sut-eri testDA-ren konexioa eman**
        sut = new DataAccess(testDA.db);
        
        // **Datu-basea garbitu**
        testDA.removeAlertak();
        testDA.removeEskaerak();
        testDA.removeRides();
        testDA.removeUsers();
    }

    @After
    public void tearDown() {
        // **Konexioa itxi test bakoitzaren bukaeran**
        testDA.close();
    }
	    
	@Test
	public void testCreateEskaeraWhiteBox() {
		try {
			Driver driver = testDA.createDriver("driver1@gmail.com", "Driver Test");
			Bidaiari bidaiari = testDA.createBidaiari("Bidaiari Test", "123", "bidaiari1@gmail.com", "12345678A");
			Date date = Date.valueOf("2025-10-10");
			Ride ride = testDA.createRide("Hasiera", "Helmuga", date, 5, 10.0, driver);

			Eskaera result = sut.createEskaera(bidaiari, ride, 2);

			assertEquals(Eskaera.EskaeraEgoera.PENDING, result.getEgoera());
			assertEquals(2, result.getNPlaces());
			assertEquals(ride, result.getRide());
			assertEquals(bidaiari, result.getBidaiari());

			List<Eskaera> bidaiariEskaerak = testDA.getBidaiariEskaerak(bidaiari);
			assertTrue(bidaiariEskaerak.contains(result));

			List<Eskaera> rideEskaerak = testDA.getRideEskaerak(ride);
			assertTrue(rideEskaerak.contains(result));

			List<Alerta> alertak = testDA.getDriverAlertak(driver);
			assertTrue(alertak.stream().anyMatch(a -> a.getMota().toString().equals("ESKAERA_EGIN")));

		} catch (RequestAlreadyExistException e) {
			fail("Ez litzateke RequestAlreadyExistException jaurti behar");
		} finally {
			testDA.removeAlertak();
			testDA.removeEskaerak();
			testDA.removeRides();
			testDA.removeUsers();
		}
	}

	@Test
	public void testCreateEskaeraExistitzenDaWhiteBox() {
		try {
			Driver driver = testDA.createDriver("driver2@gmail.com", "Driver Test");
			Bidaiari bidaiari = testDA.createBidaiari("Bidaiari Test", "123", "bidaiari2@gmail.com", "12345678B");
			Date date = Date.valueOf("2025-10-10");
			Ride ride = testDA.createRide("Hasiera", "Helmuga", date, 5, 10.0, driver);
			testDA.createEskaera(Eskaera.EskaeraEgoera.PENDING, 2, ride, bidaiari);

			sut.createEskaera(bidaiari, ride, 3);
			fail("RequestAlreadyExistException jaurti behar zuen");
		} catch (RequestAlreadyExistException e) {
		
			fail("RequestAlreadyExistException jaurti behar zuen");
		} finally {
			testDA.removeAlertak();
			testDA.removeEskaerak();
			testDA.removeRides();
			testDA.removeUsers();
		}
	}
}
