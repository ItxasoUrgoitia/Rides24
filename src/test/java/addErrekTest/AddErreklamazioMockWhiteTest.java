package addErrekTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.List;

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
import domain.Erreklamazioa;
import domain.Erreklamazioa.ErrekLarri;
import domain.Erreklamazioa.ErrekMota;
import domain.Eskaera;
import domain.Movement;

public class AddErreklamazioMockWhiteTest {
/*
    static DataAccess sut;

    @Mock
    protected EntityManagerFactory entityManagerFactory;
    @Mock
    protected EntityManager db;
    @Mock
    protected EntityTransaction et;

    protected MockedStatic<Persistence> persistenceMock;

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
    public void testAddErreklValidoBidaiari() {
        Driver driver = new Driver("driver@ex.com", "Driver");
        Bidaiari bidaiari = new Bidaiari("Bidaiari", "1234", "bidaiari@ex.com", "111");
        Bidaiari userJaso = new Bidaiari("User", "1234", "userjaso@ex.com", "222");

        Eskaera eskaera = new Eskaera();
        eskaera.setNPlaces(1);
        eskaera.setBidaiari(bidaiari);

        Erreklamazioa errekl = new Erreklamazioa();
        errekl.setId(1);
        errekl.setEskaera(eskaera);
        errekl.setErrekJarri(bidaiari);
        errekl.setErrekJaso(userJaso);
        errekl.setLarri(ErrekLarri.TXIKIA);

        when(db.find(Erreklamazioa.class, 1)).thenReturn(errekl);
        when(db.find(Driver.class, driver.getEmail())).thenReturn(driver);
        when(db.find(Bidaiari.class, bidaiari.getEmail())).thenReturn(bidaiari);
        when(db.find(Bidaiari.class, userJaso.getEmail())).thenReturn(userJaso);

        sut.addErreklamazio(bidaiari, userJaso, eskaera, "Test txosten", 50f, ErrekLarri.TXIKIA);
        
        assertEquals(errekl.getLarri(), ErrekLarri.TXIKIA);
        assertTrue(errekl.getErrekJarri().equals(bidaiari));
        assertTrue(errekl.getErrekJaso().equals(userJaso));
    }

    @Test
    public void testAddErreklDriverBaliozkoa() {
        Driver driver = new Driver("driver@ex.com", "Driver");
        Bidaiari bidaiari = new Bidaiari("Bidaiari", "1234", "bidaiari@ex.com", "111");
        Bidaiari userJaso = new Bidaiari("User", "1234", "userjaso@ex.com", "222");

        Eskaera eskaera = new Eskaera();
        eskaera.setNPlaces(1);
        eskaera.setBidaiari(bidaiari);


        Erreklamazioa errekl = new Erreklamazioa();
        errekl.setId(2);
        errekl.setEskaera(eskaera);
        errekl.setErrekJarri(driver);
        errekl.setErrekJaso(userJaso);
        errekl.setLarri(ErrekLarri.ERTAINA);

        when(db.find(Erreklamazioa.class, 2)).thenReturn(errekl);
        when(db.find(Driver.class, driver.getEmail())).thenReturn(driver);
        when(db.find(Bidaiari.class, bidaiari.getEmail())).thenReturn(bidaiari);
        when(db.find(Bidaiari.class, userJaso.getEmail())).thenReturn(userJaso);

        sut.addErreklamazio(driver, userJaso, eskaera, "Test txosten", 100f, ErrekLarri.ERTAINA);

        assertEquals(errekl.getLarri(), ErrekLarri.ERTAINA);
        assertTrue(errekl.getErrekJarri().equals(driver));
        assertTrue(errekl.getErrekJaso().equals(userJaso));
    }
    */
}
