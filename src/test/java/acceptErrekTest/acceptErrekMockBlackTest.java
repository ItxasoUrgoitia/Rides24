package acceptErrekTest;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

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

public class acceptErrekMockBlackTest {
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
    public void testAcceptErrekReturnsAccepted() {
        Driver driver = new Driver("driver1@gmail.com", "Driver Test");
        Bidaiari bidaiari = new Bidaiari("Bidaiari Test", "1234", "bidaiari1@gmail.com", "12345678A");
        Eskaera eskaera = new Eskaera();
        eskaera.setPrez(100f);
        Erreklamazioa errekl = new Erreklamazioa();
        errekl.setId(1);
        errekl.setErrekJarri(driver);
        errekl.setErrekJaso(bidaiari);
        errekl.setEskaera(eskaera);
        errekl.setLarri(ErrekLarri.TXIKIA);
        when(db.find(Erreklamazioa.class, 1)).thenReturn(errekl);
        when(db.find(Driver.class, driver.getEmail())).thenReturn(driver);
        when(db.find(Bidaiari.class, bidaiari.getEmail())).thenReturn(bidaiari);
        sut.acceptErrek(errekl);
        assertEquals(ErrekMota.ACCEPTED, errekl.getMota());
    }

    @Test
    public void testAcceptErrekWithDifferentSeverity() {
        Driver driver = new Driver("driver2@gmail.com", "Driver Test");
        Bidaiari bidaiari = new Bidaiari("Bidaiari Test", "1234", "bidaiari2@gmail.com", "12345678B");
        Eskaera eskaera = new Eskaera();
        eskaera.setPrez(200f);
        Erreklamazioa errekl = new Erreklamazioa();
        errekl.setId(2);
        errekl.setErrekJarri(driver);
        errekl.setErrekJaso(bidaiari);
        errekl.setEskaera(eskaera);
        errekl.setLarri(ErrekLarri.HANDIA);
        when(db.find(Erreklamazioa.class, 2)).thenReturn(errekl);
        when(db.find(Driver.class, driver.getEmail())).thenReturn(driver);
        when(db.find(Bidaiari.class, bidaiari.getEmail())).thenReturn(bidaiari);
        sut.acceptErrek(errekl);
        assertEquals(ErrekMota.ACCEPTED, errekl.getMota());
    }
}