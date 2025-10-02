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

public class createEskaeraMockWhiteTest {
	static DataAccess sut;
	protected MockedStatic<Persistence> persistenceMock;

	@Mock
	protected EntityManagerFactory entityManagerFactory;
	@Mock
	protected EntityManager db;
	@Mock
	protected EntityTransaction et;
	@Mock
	protected Bidaiari bidaiari;
	@Mock
	protected Ride ride;
	@Mock
	protected Driver driver;

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
	public void testCreateEskaeraWhiteBox() {
		try {
			when(bidaiari.getEmail()).thenReturn("bidaiari1@gmail.com");
			when(ride.getRideNumber()).thenReturn(1);
			when(ride.getDriver()).thenReturn(driver);
			ArrayList<Eskaera> bidaiariEskaerak = new ArrayList<>();
			ArrayList<Eskaera> rideEskaerak = new ArrayList<>();
			when(db.find(Bidaiari.class, "bidaiari1@gmail.com")).thenReturn(bidaiari);
			when(db.find(Ride.class, 1)).thenReturn(ride);
			when(bidaiari.getEskaerak()).thenReturn(bidaiariEskaerak);
			when(ride.getEskaerenList()).thenReturn(rideEskaerak);

			Eskaera result = sut.createEskaera(bidaiari, ride, 2);

			assertEquals(Eskaera.EskaeraEgoera.PENDING, result.getEgoera());
			assertEquals(2, result.getNPlaces());
			assertEquals(ride, result.getRide());
			assertEquals(bidaiari, result.getBidaiari());
			verify(bidaiari).getEskaerak();
			verify(ride).getEskaerenList();
			verify(db).persist(Mockito.any(Alerta.class));
			verify(et).commit();
		} catch (Exception e) {
			fail("Ez litzateke salbuespena jaurti behar");
		}
	}

	@Test
	public void testCreateEskaeraExistitzenDaWhiteBox() {
		try {
			when(bidaiari.getEmail()).thenReturn("bidaiari2@gmail.com");
			when(ride.getRideNumber()).thenReturn(2);
			ArrayList<Eskaera> bidaiariEskaerak = new ArrayList<>();
			Eskaera existEskaera = new Eskaera(Eskaera.EskaeraEgoera.PENDING, 2, ride, bidaiari);
			bidaiariEskaerak.add(existEskaera);
			when(db.find(Bidaiari.class, "bidaiari2@gmail.com")).thenReturn(bidaiari);
			when(db.find(Ride.class, 2)).thenReturn(ride);
			when(bidaiari.getEskaerak()).thenReturn(bidaiariEskaerak);

			sut.createEskaera(bidaiari, ride, 3);
			fail("RequestAlreadyExistException jaurti behar zuen");
		} catch (RequestAlreadyExistException e) {
			verify(et).rollback();
		} catch (Exception e) {
			fail("RequestAlreadyExistException jaurti behar zuen");
		}
	}
}
