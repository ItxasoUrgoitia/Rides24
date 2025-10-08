package addErrekTest;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import dataAccess.DataAccess;
import domain.*;
import domain.Erreklamazioa.ErrekLarri;

import static org.junit.Assert.*;

import javax.persistence.*;

/**
 * 🧪 AddErreklamazio metodoaren KUTXA BELTZEKO testak datu-base errealarekin.
 * 
 * Balio probatuak (balio baliodunak eta muturrak):
 * - userJarriDB: Bidaiari / Admin / null
 * - userJasoDB: User / null
 * - eskSelect: baliozkoa / null
 * - sartutakoTXT: testu baliozkoa / null / ""
 * - lar: TXIKIA / ERTAINA / HANDIA
 */
public class AddErreklamazioBDBlackTest {

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

        Bidaiari bidaiari = new Bidaiari("bidaiari@ex.com", "Bidaiari", "20", "100f");
        Bidaiari admin = new Bidaiari("admin@ex.com", "Admin", "30", "200f");
        Bidaiari userJaso = new Bidaiari("userjaso@ex.com", "User", "25", "150f");
        Driver driver = new Driver("driver@ex.com", "Driver", "35", "300f");

        em.persist(bidaiari);
        em.persist(admin);
        em.persist(userJaso);
        em.persist(driver);

        Eskaera esk = new Eskaera();
        esk.setBidaiari(bidaiari);
        esk.setPrez(100f);
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

    /*
    /** userJarri null denean, NullPointerException espero da */
    /**
    @Test
    public void testNullUserJarri() {
        User userJaso = em.find(User.class, "userjaso@ex.com");
        Eskaera esk = em.find(Eskaera.class, eskaeraId);
        assertThrows(NullPointerException.class, 
            () -> sut.addErreklamazio(null, userJaso, esk, "Queja", 100f, ErrekLarri.TXIKIA));
    }
    
    
    */
    
    /** userJaso null denean, NullPointerException espero da */
    @Test
    public void testNullUserJaso() {
        User userJarri = em.find(User.class, "bidaiari@ex.com");
        Eskaera esk = em.find(Eskaera.class, eskaeraId);
        assertThrows(NullPointerException.class, 
            () -> sut.addErreklamazio(userJarri, null, esk, "Queja", 100f, ErrekLarri.TXIKIA));
    }

    /** eskaera null denean, NullPointerException espero da */
    @Test
    public void testNullEskaera() {
        User userJarri = em.find(User.class, "bidaiari@ex.com");
        User userJaso = em.find(User.class, "userjaso@ex.com");
        assertThrows(NullPointerException.class, () -> sut.addErreklamazio(userJarri, userJaso, null, "Queja", 100f, ErrekLarri.TXIKIA));
    }

    /** testua null denean, IllegalArgumentException espero da */
    @Test
    public void testNullTexto() {
        User userJarri = em.find(User.class, "bidaiari@ex.com");
        User userJaso = em.find(User.class, "userjaso@ex.com");
        Eskaera esk = em.find(Eskaera.class, eskaeraId);

        assertThrows(IllegalArgumentException.class, 
            () -> sut.addErreklamazio(userJarri, userJaso, esk, null, 100f, ErrekLarri.TXIKIA));
    }

    /** testua hutsik dagoenean, IllegalArgumentException espero da */
    @Test
    public void testEmptyTexto() {
        User userJarri = em.find(User.class, "bidaiari@ex.com");
        User userJaso = em.find(User.class, "userjaso@ex.com");
        Eskaera esk = em.find(Eskaera.class, eskaeraId);

        assertThrows(IllegalArgumentException.class, 
            () -> sut.addErreklamazio(userJarri, userJaso, esk, "", 100f, ErrekLarri.TXIKIA));
    }

   
}
