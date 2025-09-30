package acceptErrekTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import dataAccess.DataAccess;
import domain.Bidaiari;
import domain.Driver;
import domain.Erreklamazioa;
import domain.Erreklamazioa.ErrekLarri;
import domain.Erreklamazioa.ErrekMota;
import domain.Eskaera.EskaeraEgoera;
import testOperations.TestDataAccess;
import domain.Eskaera;
import domain.Movement;
import domain.Ride;

import java.util.List;

public class acceptErrekBDWhiteTest {

	 //sut:system under test
	 static DataAccess sut=new DataAccess();
	 
	 //additional operations needed to execute the test 
	 static TestDataAccess testDA=new TestDataAccess();

	 private Driver driver;
	 private Bidaiari bidaiari;
	 private Eskaera eskaera;
	 @Test
	 
	 public void test1() {
	     try {
	         driver = testDA.createDriver("driver1@gmail.com", "Driver Test"); 
	  
	         bidaiari = testDA.createBidaiari("Bidaiari Test", "123", "bidaiari1@gmail.com", "12345678A");
	         
	         
	         
	         eskaera = testDA.createEskaera(EskaeraEgoera.PENDING, 4, ride, bidaiari);

	         Erreklamazioa errekl = testDA.addErreklamazioa(driver, bidaiari, eskaera, ErrekLarri.TXIKIA);

	         
	         sut.acceptErrek(errekl);

	         
	         assertEquals(ErrekMota.ACCEPTED, errekl.getMota());
	         List<Movement> bidMovs = sut.getUserMugimenduak(bidaiari);
	         List<Movement> driMovs = sut.getUserMugimenduak(driver);

	         assertTrue(bidMovs.stream().anyMatch(m -> m.getMota().equals("-")));
	         assertTrue(driMovs.stream().anyMatch(m -> m.getMota().equals("+")));

	     } finally {
	         
	         testDA.removeErreklamazioak();
	         testDA.removeUsers();
	     }
	 }

	 
	 
	 
	 
	 
	 

}