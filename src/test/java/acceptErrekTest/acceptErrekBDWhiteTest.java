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
import domain.User;

import java.sql.Date;
import java.util.List;

public class acceptErrekBDWhiteTest {

              //sut:system under test
              static DataAccess sut=new DataAccess();
              
               //additional operations needed to execute the test 
               static TestDataAccess testDA=new TestDataAccess();

              private Driver driver;
              private Bidaiari bidaiari;
              private Eskaera eskaera;
              private Ride ride;
              private Erreklamazioa errekl;
              
               @Test
                  // sut.acceptErrek: Bidaiari is receiver, severity = TXIKIA
                  public void test1() {
                      String driverEmail = "driver1@gmail.com";
                      String bidaiariEmail = "bid1@gmail.com";
                      boolean existDriver = false;
                      boolean existBidaiari = false;
                      
                      try {
                          testDA.open();
                          existDriver = testDA.existDriver(driverEmail);
                          existBidaiari = testDA.existBidaiari(bidaiariEmail);
                          driver = testDA.createDriver(driverEmail, "Driver Test");
                          bidaiari = testDA.createBidaiari("BidName", "123", bidaiariEmail, "12345678A");
                          ride = testDA.createRide("CityA", "CityB", Date.valueOf("2025-10-10"),5, 3.0, driver);
                          eskaera = testDA.createEskaera(EskaeraEgoera.PENDING, 1, ride, bidaiari);
                          errekl = testDA.addErreklamazio(driver, bidaiari, eskaera, "fls", eskaera.getPrez(), ErrekLarri.TXIKIA); 
                          testDA.close();

                          // invoke sut
                          sut.open();
                          sut.acceptErrek(errekl);
                          sut.close();
                          
                          // verify
                          testDA.open();
                          Erreklamazioa updated = testDA.existErreklamazio(errekl.getId());
                          assertEquals(ErrekMota.ACCEPTED, updated.getMota());
                          assertTrue(testDA.getBidaiariMove(bidaiari).stream().anyMatch(m -> m.getMota().equals("-")));
                          assertTrue(testDA.getDriverMove(driver).stream().anyMatch(m -> m.getMota().equals("+")));
                          testDA.close();
                          
                      } finally {
                          // cleanup
                          testDA.open();
                          if (!existDriver) testDA.removeDriver(driverEmail);
                          if (!existBidaiari) testDA.removeBidaiari(bidaiariEmail);
                          testDA.close();
                      }
                  }
              @Test
              // sut.acceptErrek: Bidaiari is receiver, severity = Esratina
                  public void test2() {
                      String driverEmail = "driver1@gmail.com";
                      String bidaiariEmail = "bid1@gmail.com";
                      boolean existDriver = false;
                      boolean existBidaiari = false;
                      
                      try {
                          testDA.open();
                          existDriver = testDA.existDriver(driverEmail);
                          existBidaiari = testDA.existBidaiari(bidaiariEmail);
                          driver = testDA.createDriver(driverEmail, "Driver Test");
                          bidaiari = testDA.createBidaiari("BidName", "123", bidaiariEmail, "12345678A");
                          ride = testDA.createRide("CityA", "CityB", Date.valueOf("2025-10-10"),5, 3.0, driver);
                          eskaera = testDA.createEskaera(EskaeraEgoera.PENDING, 1, ride, bidaiari);
                          errekl = testDA.addErreklamazio(driver, bidaiari, eskaera, "fls", eskaera.getPrez(), ErrekLarri.ERTAINA); 
                          testDA.close();

                          // invoke sut
                          sut.open();
                          sut.acceptErrek(errekl);
                          sut.close();

                          // verify
                          testDA.open();
                          Erreklamazioa updated = testDA.existErreklamazio(errekl.getId());
                          assertEquals(ErrekMota.ACCEPTED, updated.getMota());
                          assertTrue(testDA.getBidaiariMove(bidaiari).stream().anyMatch(m -> m.getMota().equals("-")));
                          assertTrue(testDA.getDriverMove(driver).stream().anyMatch(m -> m.getMota().equals("+")));
                          testDA.close();
                          
                      } finally {
                          // cleanup
                          testDA.open();
                          if (!existDriver) testDA.removeDriver(driverEmail);
                          if (!existBidaiari) testDA.removeBidaiari(bidaiariEmail);
                          testDA.close();
                      }
                  }
              @Test
              // sut.acceptErrek: Bidaiari is receiver, severity = HANDIA
                  public void test3() {
                      String driverEmail = "driver1@gmail.com";
                      String bidaiariEmail = "bid1@gmail.com";
                      boolean existDriver = false;
                      boolean existBidaiari = false;
                      
                      try {
                          testDA.open();
                          existDriver = testDA.existDriver(driverEmail);
                          existBidaiari = testDA.existBidaiari(bidaiariEmail);
                          driver = testDA.createDriver(driverEmail, "Driver Test");
                          bidaiari = testDA.createBidaiari("BidName", "123", bidaiariEmail, "12345678A");
                          ride = testDA.createRide("CityA", "CityB", Date.valueOf("2025-10-10"),5, 3.0, driver);
                          eskaera = testDA.createEskaera(EskaeraEgoera.PENDING, 1, ride, bidaiari);
                          errekl = testDA.addErreklamazio(driver, bidaiari, eskaera, "fls", eskaera.getPrez(), ErrekLarri.HANDIA); 
                          testDA.close();

                          // invoke sut
                          sut.open();
                          sut.acceptErrek(errekl);
                          sut.close();

                          // verify
                          testDA.open();
                          Erreklamazioa updated = testDA.existErreklamazio(errekl.getId());
                          assertEquals(ErrekMota.ACCEPTED, updated.getMota());
                          assertTrue(testDA.getBidaiariMove(bidaiari).stream().anyMatch(m -> m.getMota().equals("-")));
                          assertTrue(testDA.getDriverMove(driver).stream().anyMatch(m -> m.getMota().equals("+")));
                          testDA.close();
                          
                      } finally {
                          // cleanup
                          testDA.open();
                          if (!existDriver) testDA.removeDriver(driverEmail);
                          if (!existBidaiari) testDA.removeBidaiari(bidaiariEmail);
                          testDA.close();
                      }
                  }
              @Test
                  public void test4() {
                      String driverEmail = "driver4@gmail.com";
                      String bidaiariEmail = "bid4@gmail.com";
                      boolean existDriver = false;
                      boolean existBidaiari = false;
                      try {
                          testDA.open();
                          existDriver = testDA.existDriver(driverEmail);
                          existBidaiari = testDA.existBidaiari(bidaiariEmail);
                          driver = testDA.createDriver(driverEmail, "Driver Test");
                          bidaiari = testDA.createBidaiari("BidName", "123", bidaiariEmail, "12345678A");
                          ride = testDA.createRide("CityA", "CityB", Date.valueOf("2025-10-10"),5, 3.0, driver);
                          eskaera = testDA.createEskaera(EskaeraEgoera.PENDING, 1, ride, bidaiari);
                          // aquí el bidaiari pone la erreklamazioa contra el driver
                          errekl = testDA.addErreklamazio(bidaiari, driver, eskaera, "fls", eskaera.getPrez(), ErrekLarri.HANDIA); 
                          testDA.close();

                          sut.open();
                          sut.acceptErrek(errekl);
                          sut.close();
                          
                          testDA.open();
                          Erreklamazioa updated = testDA.existErreklamazio(errekl.getId());
                          assertEquals(ErrekMota.ACCEPTED, updated.getMota());
                          assertTrue(testDA.getBidaiariMove(bidaiari).stream().anyMatch(m -> m.getMota().equals("+")));
                          assertTrue(testDA.getDriverMove(driver).stream().anyMatch(m -> m.getMota().equals("-")));
                          testDA.close();

                      } finally {
                          testDA.open();
                          testDA.removeDriver(driverEmail);
                          testDA.removeBidaiari(bidaiariEmail);
                          testDA.close();
                      }
                  }
              
               

}
