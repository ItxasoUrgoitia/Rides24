package addErrekTest;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import dataAccess.DataAccess;
import domain.*;
import domain.Erreklamazioa.ErrekLarri;
import testOperations.TestDataAccess;

import static org.junit.Assert.fail;

public class AddErreklamazioBDWhiteTest {

    private TestDataAccess tda;       // Test-eko datu baseko laguntzailea
    private DataAccess sut;            // System Under Test (DataAccess)
    private Integer eskaeraId;         // Eskaera baten ID
    private Bidaiari bidaiari;         // Bidaiari jartzailea
    private Driver gidaria;            // Driver jartzailea
    private Bidaiari userJaso;         // Bidaiari jasotzailea

    @Before
    public void setUp() {
        tda = new TestDataAccess();
        tda.open(); // DB ireki

        // DB garbitu testak hasi aurretik
        tda.removeUsers();
        tda.removeEskaerak();
        tda.removeRides();
        tda.removeAlertak();

        sut = new DataAccess(tda.db);

        // Erabiltzaileak sortu
        gidaria = tda.createDriver("driver@ex.com", "Gidaria");
        bidaiari = tda.createBidaiari("Bidaiari", "20", "bidaiari@ex.com", "100f");
        userJaso = tda.createBidaiari("Erabiltzaile", "25", "userjaso@ex.com", "150f");

        // Ride bat sortu gidariaren eskutik
        java.sql.Date data = new java.sql.Date(System.currentTimeMillis());
        Ride ride = tda.createRide("Bilbao", "Donostia", data, 3, 50.0, gidaria);

        // Eskaera bat sortu ride horrekin eta bidaiariarentzat
        Eskaera esk = tda.createEskaera(Eskaera.EskaeraEgoera.PENDING, 1, ride, bidaiari);
        eskaeraId = esk.getEskaeraNumber();
    }

    @After
    public void tearDown() {
        // Testak bukatu ondoren, DB garbitu
        tda.db.getTransaction().begin();
        tda.db.createQuery("DELETE FROM Erreklamazioa").executeUpdate();
        tda.db.createQuery("DELETE FROM User").executeUpdate();
        tda.db.createQuery("DELETE FROM Eskaera").executeUpdate();
        tda.db.getTransaction().commit();
        tda.close();
    }

    @Test
    public void testAddErreklamazioBaliozkoBidaiari() {
        // Eskaera aurkitu DB-tik
        Eskaera esk = tda.db.find(Eskaera.class, eskaeraId);
        try {
            // Erreklamazioa gehitu bidaiariak jarriko duena
            sut.addErreklamazio(bidaiari, userJaso, esk, "Testu baliozkoa", 100f, ErrekLarri.TXIKIA);
        } catch (Exception e) {
            fail("Ez luke salbuespenik sortu behar kasu baliozkoan");
        }
    }

    @Test
    public void testAddErreklamazioBaliozkoGidaria() {
        // Eskaera aurkitu DB-tik
        Eskaera esk = tda.db.find(Eskaera.class, eskaeraId);
        try {
            // Erreklamazioa gehitu gidariak jarriko duena
            sut.addErreklamazio(gidaria, userJaso, esk, "Testu baliozkoa", 100f, ErrekLarri.ERTAINA);
        } catch (Exception e) {
            fail("Ez luke salbuespenik sortu behar kasu baliozkoan");
        }
    }
}
