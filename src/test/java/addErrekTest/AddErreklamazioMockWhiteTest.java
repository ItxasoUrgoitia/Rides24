package addErrekTest;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import dataAccess.DataAccess;
import domain.*;
import domain.Erreklamazioa.ErrekLarri;
import javax.persistence.EntityManager;
import static org.junit.Assert.*;

public class AddErreklamazioMockWhiteTest {
    private DataAccess sut;
    private EntityManager em;

    @Before
    public void setUp() {
        em = Mockito.mock(EntityManager.class);
        sut = new DataAccess(em);
    }

    @Test
    public void testAddErreklamazioValidoBidaiari() {
        User userJarri = new Bidaiari("bidaiari@ex.com", "Bidaiari", "20", "100f");
        User userJaso = new Bidaiari("userjaso@ex.com", "User", "25", "150f");
        Eskaera esk = new Eskaera();
        Mockito.when(em.find(User.class, userJarri.getEmail())).thenReturn(userJarri);
        Mockito.when(em.find(User.class, userJaso.getEmail())).thenReturn(userJaso);
        Mockito.when(em.find(Eskaera.class, esk.getEskaeraNumber())).thenReturn(esk);
        try {
            sut.addErreklamazio(userJarri, userJaso, esk, "baliozkoa", 100f, ErrekLarri.TXIKIA);
        } catch (Exception e) {
            fail("Kasua baliozkoa da, ez luke salbuespenik bota behar");
        }
    }

    @Test
    public void testAddErreklamazioValidoDriver() {
        User userJarri = new Driver("driver@ex.com", "Driver", "22", "120f");
        User userJaso = new Bidaiari("userjaso@ex.com", "User", "25", "150f");
        Eskaera esk = new Eskaera();
        Mockito.when(em.find(User.class, userJarri.getEmail())).thenReturn(userJarri);
        Mockito.when(em.find(User.class, userJaso.getEmail())).thenReturn(userJaso);
        Mockito.when(em.find(Eskaera.class, esk.getEskaeraNumber())).thenReturn(esk);
        try {
            sut.addErreklamazio(userJarri, userJaso, esk, "baliozkoa", 100f, ErrekLarri.ERTAINA);
        } catch (Exception e) {
            fail("Kasua baliozkoa da, ez luke salbuespenik bota behar");
        }
    }
}
