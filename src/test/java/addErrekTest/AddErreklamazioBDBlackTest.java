package addErrekTest;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import dataAccess.DataAccess;
import domain.*;
import domain.User;

import static org.junit.Assert.assertThrows;
import javax.persistence.*;

/**
 * Kutxa beltzeko frogak DB errealarekin addErreklamazio metodoarentzat.
 * Clases de equivalencia y valores límite probados:
 * - userJarriDB: Bidaiari / Admin / null
 * - userJasoDB: User / null
 * - eskSelect: válido / null
 * - sartutakoTXT: texto válido / null / ""
 * - lar: TXIKIA / ERTAINA / HANDIA
 */
public class AddErreklamazioBDBlackTest {
    private static EntityManagerFactory emf;
    private EntityManager em;
    private DataAccess sut;

    @BeforeClass
    public static void init() {
        emf = Persistence.createEntityManagerFactory("objectdb:test.odb");
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
        em.persist(new Driver("bidaiari@ex.com", "Bidaiari", "20", "100f"));
        em.persist(new Bidaiari("admin@ex.com", "Admin", "30", "200f"));
        em.persist(new Bidaiari("userjaso@ex.com", "User", "25", "150f"));
        em.persist(new Eskaera());
        em.getTransaction().commit();
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
    public void testNullUserJarri() {
        User userJaso = em.find(User.class, "userjaso@ex.com");
        Eskaera esk = em.find(Eskaera.class, 1);
        assertThrows(NullPointerException.class, () -> sut.addErreklamazio(null, userJaso, esk, "Queja", 100f, ErrekLarri.TXIKIA));
    }

    @Test
    public void testNullUserJaso() {
        User userJarri = em.find(User.class, "bidaiari@ex.com");
        Eskaera esk = em.find(Eskaera.class, 1);
        assertThrows(NullPointerException.class, () -> sut.addErreklamazio(userJarri, null, esk, "Queja", 100f, ErrekLarri.TXIKIA));
    }

    @Test
    public void testNullEskaera() {
        User userJarri = em.find(User.class, "bidaiari@ex.com");
        User userJaso = em.find(User.class, "userjaso@ex.com");
        assertThrows(NullPointerException.class, () -> sut.addErreklamazio(userJarri, userJaso, null, "Queja", 100f, ErrekLarri.TXIKIA));
    }

    @Test
    public void testNullTexto() {
        User userJarri = em.find(User.class, "bidaiari@ex.com");
        User userJaso = em.find(User.class, "userjaso@ex.com");
        Eskaera esk = em.find(Eskaera.class, 1);
        assertThrows(NullPointerException.class, () -> sut.addErreklamazio(userJarri, userJaso, esk, null, 100f, ErrekLarri.TXIKIA));
    }

    @Test
    public void testEmptyTexto() {
        User userJarri = em.find(User.class, "bidaiari@ex.com");
        User userJaso = em.find(User.class, "userjaso@ex.com");
        Eskaera esk = em.find(Eskaera.class, 1);
        assertThrows(NullPointerException.class, () -> sut.addErreklamazio(userJarri, userJaso, esk, "", 100f, ErrekLarri.TXIKIA));
    }
}

