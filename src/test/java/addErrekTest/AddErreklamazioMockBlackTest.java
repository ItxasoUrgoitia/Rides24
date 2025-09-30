package addErrekTest;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import dataAccess.DataAccess;
import domain.*;
import domain.Erreklamazioa.ErrekLarri;
import javax.persistence.EntityManager;
import static org.junit.Assert.*;

public class AddErreklamazioMockBlackTest {
    private DataAccess sut;
    private EntityManager em;

    @Before
    public void setUp() {
        em = Mockito.mock(EntityManager.class);
        sut = new DataAccess(em);
    }

    @Test
    public void testNullUserJarri() {
        User userJaso = new Bidaiari("userjaso@ex.com", "User", "25", "150f");
        Eskaera esk = new Eskaera();
        Mockito.when(em.find(User.class, "userjaso@ex.com")).thenReturn(userJaso);
        Mockito.when(em.find(Eskaera.class, esk.getEskaeraNumber())).thenReturn(esk);
        assertThrows(NullPointerException.class, () -> sut.addErreklamazio(null, userJaso, esk, "Queja", 100f, ErrekLarri.TXIKIA));
    }

    @Test
    public void testNullUserJaso() {
        User userJarri = new Bidaiari("bidaiari@ex.com", "Bidaiari", "20", "100f");
        Eskaera esk = new Eskaera();
        Mockito.when(em.find(User.class, "bidaiari@ex.com")).thenReturn(userJarri);
        Mockito.when(em.find(Eskaera.class, esk.getEskaeraNumber())).thenReturn(esk);
        assertThrows(NullPointerException.class, () -> sut.addErreklamazio(userJarri, null, esk, "Queja", 100f, ErrekLarri.TXIKIA));
    }

    @Test
    public void testNullEskaera() {
        User userJarri = new Bidaiari("bidaiari@ex.com", "Bidaiari", "20", "100f");
        User userJaso = new Bidaiari("userjaso@ex.com", "User", "25", "150f");
        Mockito.when(em.find(User.class, "bidaiari@ex.com")).thenReturn(userJarri);
        Mockito.when(em.find(User.class, "userjaso@ex.com")).thenReturn(userJaso);
        assertThrows(NullPointerException.class, () -> sut.addErreklamazio(userJarri, userJaso, null, "Queja", 100f, ErrekLarri.TXIKIA));
    }

    @Test
    public void testNullTexto() {
        User userJarri = new Bidaiari("bidaiari@ex.com", "Bidaiari", "20", "100f");
        User userJaso = new Bidaiari("userjaso@ex.com", "User", "25", "150f");
        Eskaera esk = new Eskaera();
        Mockito.when(em.find(User.class, "bidaiari@ex.com")).thenReturn(userJarri);
        Mockito.when(em.find(User.class, "userjaso@ex.com")).thenReturn(userJaso);
        Mockito.when(em.find(Eskaera.class, esk.getEskaeraNumber())).thenReturn(esk);
        assertThrows(NullPointerException.class, () -> sut.addErreklamazio(userJarri, userJaso, esk, null, 100f, ErrekLarri.TXIKIA));
    }

    @Test
    public void testEmptyTexto() {
        User userJarri = new Bidaiari("bidaiari@ex.com", "Bidaiari", "20", "100f");
        User userJaso = new Bidaiari("userjaso@ex.com", "User", "25", "150f");
        Eskaera esk = new Eskaera();
        Mockito.when(em.find(User.class, "bidaiari@ex.com")).thenReturn(userJarri);
        Mockito.when(em.find(User.class, "userjaso@ex.com")).thenReturn(userJaso);
        Mockito.when(em.find(Eskaera.class, esk.getEskaeraNumber())).thenReturn(esk);
        assertThrows(NullPointerException.class, () -> sut.addErreklamazio(userJarri, userJaso, esk, "", 100f, ErrekLarri.TXIKIA));
    }
}
