package acceptErrekTest;

import static org.junit.Assert.assertEquals;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
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
import domain.Eskaera;

public class acceptErrekBDBlackTest {
    private EntityManagerFactory emf;
    private EntityManager em;
    private DataAccess sut;

    @Before
    public void setUp() {
        emf = Persistence.createEntityManagerFactory("objectdb:db/testDB.odb");
        em = emf.createEntityManager();
        sut = new DataAccess(em);
        em.getTransaction().begin();
        // Clean up previous test data
        em.createQuery("DELETE FROM Erreklamazioa").executeUpdate();
        em.createQuery("DELETE FROM Driver").executeUpdate();
        em.createQuery("DELETE FROM Bidaiari").executeUpdate();
        em.createQuery("DELETE FROM Eskaera").executeUpdate();
        em.getTransaction().commit();
    }

    @After
    public void tearDown() {
        if (em.getTransaction().isActive()) em.getTransaction().rollback();
        em.close();
        emf.close();
    }

    @Test
    public void testAcceptErrekBlackBox() {
        em.getTransaction().begin();
        Driver driver = new Driver("driver1@gmail.com", "Driver Test");
        Bidaiari bidaiari = new Bidaiari("Bidaiari Test", "1234", "bidaiari1@gmail.com", "12345678A");
        Eskaera eskaera = new Eskaera();
        eskaera.setPrez(100f);
        em.persist(driver);
        em.persist(bidaiari);
        em.persist(eskaera);
        Erreklamazioa errekl = new Erreklamazioa();
        errekl.setId(1);
        errekl.setErrekJarri(driver);
        errekl.setErrekJaso(bidaiari);
        errekl.setEskaera(eskaera);
        errekl.setLarri(ErrekLarri.TXIKIA);
        em.persist(errekl);
        em.getTransaction().commit();

        // Call acceptErrek
        sut.acceptErrek(errekl);

        // Reload from DB and check only the final outcome
        Erreklamazioa dbErrekl = em.find(Erreklamazioa.class, 1);
        assertEquals(ErrekMota.ACCEPTED, dbErrekl.getMota());
    }
}