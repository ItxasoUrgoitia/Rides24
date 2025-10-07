package addErrekTest;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import dataAccess.DataAccess;
import domain.*;
import domain.User;
import domain.Erreklamazioa.ErrekLarri;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.fail;

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
    private Integer eskaeraId; // Guardar el ID real de Eskaera

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
        Driver bidaiari = new Driver("Bidaiari", "pasahitza1", "bidaiari@ex.com", "12345678A");
		em.persist(bidaiari);
        em.persist(new Driver("bidaiari@ex.com", "Bidaiari", "20", "100f"));
        em.persist(new Bidaiari("admin@ex.com", "Admin", "30", "200f"));
        em.persist(new Bidaiari("userjaso@ex.com", "User", "25", "150f"));
        Eskaera esk = new Eskaera();
        em.persist(esk);
        em.getTransaction().commit();
        eskaeraId = esk.getEskaeraNumber(); // Guardar el ID real
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
        Eskaera esk = em.find(Eskaera.class, eskaeraId); // Usar el ID real
        assertThrows(NullPointerException.class, () -> sut.addErreklamazio(null, userJaso, esk, "Queja", 100f, ErrekLarri.TXIKIA));
        }

    @Test
    public void testNullUserJaso() {
        User userJarri = em.find(User.class, "bidaiari@ex.com");
        Eskaera esk = em.find(Eskaera.class, eskaeraId); // Usar el ID real
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
        Eskaera esk = em.find(Eskaera.class, eskaeraId); // Usar el ID real
        assertThrows(IllegalArgumentException.class, () -> sut.addErreklamazio(userJarri, userJaso, esk, null, 100f, ErrekLarri.TXIKIA));
    }

    @Test
    public void testEmptyTexto() {
        User userJarri = em.find(User.class, "bidaiari@ex.com");
        User userJaso = em.find(User.class, "userjaso@ex.com");
        Eskaera esk = em.find(Eskaera.class, eskaeraId); // Usar el ID real
        assertThrows(IllegalArgumentException.class, () -> sut.addErreklamazio(userJarri, userJaso, esk, "", 100f, ErrekLarri.TXIKIA));
    }
    @Test
    public void testAddErreklamazioSuccess() {
        // 1. VERIFICAR que los datos existen
        User userJarri = em.find(User.class, "bidaiari@ex.com");
        User userJaso = em.find(User.class, "userjaso@ex.com");
        Eskaera esk = em.find(Eskaera.class, eskaeraId);
        
        assertNotNull("User Jarri no debería ser null", userJarri);
        assertNotNull("User Jaso no debería ser null", userJaso);
        assertNotNull("Eskaera no debería ser null", esk);
        
        // 2. VERIFICAR que Eskaera tiene precio
        assertNotNull("Eskaera debería tener precio", esk.getPrez());
        
        // 3. CAPTURAR la excepción real para debuggear
        try {
            sut.addErreklamazio(userJarri, userJaso, esk, "Queja válida", 100f, ErrekLarri.TXIKIA);
            // Si llega aquí, el test pasa ✅
            System.out.println("✅ Test pasó correctamente");
            
        } catch (Exception e) {
            // Debug: imprimir la excepción real
            System.err.println("❌ Excepción capturada: " + e.getClass().getName());
            System.err.println("❌ Mensaje: " + e.getMessage());
            e.printStackTrace();
            
            // Fallar el test con información útil
            fail("No debería lanzar excepción con parámetros válidos. Error: " + e.getMessage());
        }
    }
}