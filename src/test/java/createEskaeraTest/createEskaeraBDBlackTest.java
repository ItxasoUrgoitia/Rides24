package createEskaeraTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import dataAccess.DataAccess;
import domain.Bidaiari;
import domain.Driver;
import domain.Ride;
import domain.Eskaera;
import exceptions.RequestAlreadyExistException;

import java.sql.Date;

public class createEskaeraBDBlackTest {
	private EntityManagerFactory emf;
	private EntityManager em;
	private DataAccess sut;

	@Before
	public void setUp() {
		emf = Persistence.createEntityManagerFactory("objectdb:db/testDB.odb");
		em = emf.createEntityManager();
		sut = new DataAccess(em);
		em.getTransaction().begin();
		em.createQuery("DELETE FROM Alerta").executeUpdate();
		em.createQuery("DELETE FROM Eskaera").executeUpdate();
		em.createQuery("DELETE FROM Ride").executeUpdate();
		em.createQuery("DELETE FROM Bidaiari").executeUpdate();
		em.createQuery("DELETE FROM Driver").executeUpdate();
		em.getTransaction().commit();
	}

	@After
	public void tearDown() {
		if (em.getTransaction().isActive())
			em.getTransaction().rollback();
		em.close();
		emf.close();
	}

	@Test
	public void testCreateEskaeraBlackBox() {
		try {
			em.getTransaction().begin();
			Driver driver = new Driver("Driver Test", "pasahitza1", "driver1@gmail.com", "12345678A");
			Bidaiari bidaiari = new Bidaiari("Bidaiari Test", "1234", "bidaiari1@gmail.com", "12345678A");
			Ride ride = new Ride("Hasiera", "Helmuga", Date.valueOf("2025-10-10"), 5, 10.0f, driver);
			em.persist(driver);
			em.persist(bidaiari);
			em.persist(ride);
			em.getTransaction().commit();

			Eskaera result = sut.createEskaera(bidaiari, ride, 2);

			Eskaera dbEskaera = em.find(Eskaera.class, result.getEskaeraNumber());
			assertEquals(Eskaera.EskaeraEgoera.PENDING, dbEskaera.getEgoera());
			assertEquals(2, dbEskaera.getNPlaces());
			assertEquals(ride.getRideNumber(), dbEskaera.getRide().getRideNumber());
			assertEquals(bidaiari.getEmail(), dbEskaera.getBidaiari().getEmail());
		} catch (Exception e) {
			fail("Ez litzateke salbuespena jaurti behar");
		}
	}

	@Test
	public void testCreateEskaeraExistitzenDa() {
		try {
			em.getTransaction().begin();
			Driver driver = new Driver("driver2@gmail.com", "Driver Test");
			Bidaiari bidaiari = new Bidaiari("Bidaiari Test", "1234", "bidaiari2@gmail.com", "12345678B");
			Ride ride = new Ride("Hasiera", "Helmuga", Date.valueOf("2025-10-10"), 5, 10.0f, driver);
			Eskaera eskaera = new Eskaera(Eskaera.EskaeraEgoera.PENDING, 2, ride, bidaiari);
			em.persist(driver);
			em.persist(bidaiari);
			em.persist(ride);
			em.persist(eskaera);
			em.getTransaction().commit();

			sut.createEskaera(bidaiari, ride, 3);
			fail("RequestAlreadyExistException jaurti behar zuen");
		} catch (RequestAlreadyExistException e) {
		} catch (Exception e) {
			fail("RequestAlreadyExistException jaurti behar zuen");
		}
	}
}
