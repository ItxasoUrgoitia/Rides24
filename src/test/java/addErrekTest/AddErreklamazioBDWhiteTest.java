package addErrekTest;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import dataAccess.DataAccess;
import domain.*;
import domain.Erreklamazioa.ErrekLarri;
import javax.persistence.*;
import static org.junit.Assert.fail;

public class AddErreklamazioBDWhiteTest {
    private static EntityManagerFactory emf;
    private EntityManager em;
    private DataAccess sut;
    private Integer eskaeraId;

    @BeforeClass
    public static void init() {
        emf = Persistence.createEntityManagerFactory("objectdb:db/testDB.odb");
    }

    @AfterClass
    public static void close() {
        emf.close();
    }

    @Before
    public void setUp() {
        em = emf.createEntityManager();
        sut = new DataAccess(em);
        em.getTransaction().begin();
        em.persist(new Driver("driver@ex.com", "Driver", "22", "120f"));
        em.persist(new Bidaiari("bidaiari@ex.com", "Bidaiari", "20", "100f"));
        em.persist(new Bidaiari("userjaso@ex.com", "User", "25", "150f"));
        Eskaera esk = new Eskaera();
        em.persist(esk);
        em.getTransaction().commit();
        eskaeraId = esk.getEskaeraNumber();
    }

    @After
    public void tearDown() {
        em.getTransaction().begin();
        em.createQuery("DELETE FROM Erreklamazioa").executeUpdate();
        em.createQuery("DELETE FROM User").executeUpdate();
        em.createQuery("DELETE FROM Eskaera").executeUpdate();
        em.getTransaction().commit();
        em.close();
    }

    @Test
    public void testAddErreklamazioValidoBidaiari() {
        User userJarri = em.find(User.class, "bidaiari@ex.com");
        User userJaso = em.find(User.class, "userjaso@ex.com");
        Eskaera esk = em.find(Eskaera.class, eskaeraId);
        try {
            sut.addErreklamazio(userJarri, userJaso, esk, "Texto válido", 100f, ErrekLarri.TXIKIA);
        } catch (Exception e) {
            fail("No debería lanzar excepción en caso válido");
        }
    }

    @Test
    public void testAddErreklamazioValidoDriver() {
        User userJarri = em.find(User.class, "driver@ex.com");
        User userJaso = em.find(User.class, "userjaso@ex.com");
        Eskaera esk = em.find(Eskaera.class, eskaeraId);
        try {
            sut.addErreklamazio(userJarri, userJaso, esk, "Texto válido", 100f, ErrekLarri.ERTAINA);
        } catch (Exception e) {
            fail("No debería lanzar excepción en caso válido");
        }
    }
}
