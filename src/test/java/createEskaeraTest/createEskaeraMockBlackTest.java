package createEskaeraTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import dataAccess.DataAccess;
import domain.Bidaiari;
import domain.Driver;
import domain.Ride;
import domain.Eskaera;
import domain.Alerta;
import exceptions.RequestAlreadyExistException;

import java.util.ArrayList;

public class createEskaeraMockBlackTest {
	static DataAccess sut;
	protected MockedStatic<Persistence> persistenceMock;

	@Mock
	protected EntityManagerFactory entityManagerFactory;
	@Mock
	protected EntityManager db;
	@Mock
	protected EntityTransaction et;

	@Before
	public void init() {
		MockitoAnnotations.openMocks(this);
		persistenceMock = Mockito.mockStatic(Persistence.class);
		persistenceMock.when(() -> Persistence.createEntityManagerFactory(Mockito.any()))
				.thenReturn(entityManagerFactory);
		Mockito.doReturn(db).when(entityManagerFactory).createEntityManager();
		Mockito.doReturn(et).when(db).getTransaction();
		sut = new DataAccess(db);
	}

	@After
	public void tearDown() {
		persistenceMock.close();
	}

	@Test
	public void testCreateEskaeraReturnsEskaera() {
		try {
			Driver driver = new Driver("driver1@gmail.com", "Driver Test");
			Bidaiari bidaiari = new Bidaiari("Bidaiari Test", "1234", "bidaiari1@gmail.com", "12345678A");
			Ride ride = new Ride("Hasiera", "Helmuga", java.sql.Date.valueOf("2025-10-10"), 5, 10.0f, driver);
			when(db.find(Bidaiari.class, bidaiari.getEmail())).thenReturn(bidaiari);
			when(db.find(Ride.class, ride.getRideNumber())).thenReturn(ride);
			when(bidaiari.getEskaerak()).thenReturn(new ArrayList<>());
			when(ride.getEskaerenList()).thenReturn(new ArrayList<>());

			Eskaera result = sut.createEskaera(bidaiari, ride, 2);

			assertEquals(Eskaera.EskaeraEgoera.PENDING, result.getEgoera());
			assertEquals(2, result.getNPlaces());
			assertEquals(ride, result.getRide());
			assertEquals(bidaiari, result.getBidaiari());
			verify(db).persist(Mockito.any(Alerta.class));
			verify(et).commit();
		} catch (Exception e) {
			fail("Ez litzateke salbuespena jaurti behar");
		}
	}

	@Test
	public void testCreateEskaeraExistitzenDa() {
		try {
			// Objekto benetakoak
            Driver driver = new Driver("driver2@gmail.com", "Driver Test");
            Bidaiari bidaiari = new Bidaiari("Bidaiari Test", "1234", "bidaiari2@gmail.com", "12345678B");
            Ride ride = new Ride("Hasiera", "Helmuga", java.sql.Date.valueOf("2025-10-10"), 5, 10.0f, driver);

            // Eskaera existitzen da
            Eskaera existEskaera = new Eskaera(Eskaera.EskaeraEgoera.PENDING, 2, ride, bidaiari);
            bidaiari.getEskaerak().add(existEskaera);
            ride.getEskaerenList().add(existEskaera);

            // Mock bakarrik EntityManager-rentzat
            when(db.find(Bidaiari.class, bidaiari.getEmail())).thenReturn(bidaiari);
            when(db.find(Ride.class, ride.getRideNumber())).thenReturn(ride);

			sut.createEskaera(bidaiari, ride, 3);
			fail("RequestAlreadyExistException jaurti behar zuen");
		} catch (RequestAlreadyExistException e) {
			verify(et).rollback();
		} catch (Exception e) {
			fail("RequestAlreadyExistException jaurti behar zuen");
		}
	}
}
