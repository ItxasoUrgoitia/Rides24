package addErrekTest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import dataAccess.DataAccess;
import domain.*;
import domain.Erreklamazioa.ErrekLarri;
import testOperations.TestDataAccess;

import static org.mockito.Mockito.*;
import static org.junit.Assert.fail;

@RunWith(MockitoJUnitRunner.class)
public class AddErreklamazioMockWhiteTest {

    @Mock
    private TestDataAccess tdaMock; // DB mock

    @Mock
    private Eskaera eskaeraMock;    // Eskaera mock

    @Mock
    private Bidaiari bidaiariMock;  // Bidaiari mock
    @Mock
    private Driver gidariaMock;     // Driver mock
    @Mock
    private Bidaiari userJasoMock;  // Bidaiari jasotzailea mock

    @InjectMocks
    private DataAccess sut;         // System under test, inyectado con mocks

    private Integer eskaeraId;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Comportamiento de los mocks
        when(tdaMock.createDriver("driver@ex.com", "Gidaria")).thenReturn(gidariaMock);
        when(tdaMock.createBidaiari("Bidaiari", "20", "bidaiari@ex.com", "100f")).thenReturn(bidaiariMock);
        when(tdaMock.createBidaiari("Erabiltzaile", "25", "userjaso@ex.com", "150f")).thenReturn(userJasoMock);
        when(tdaMock.createEskaera(any(), anyInt(), any(), any())).thenReturn(eskaeraMock);
        when(eskaeraMock.getEskaeraNumber()).thenReturn(1);

        eskaeraId = eskaeraMock.getEskaeraNumber();
    }

    @Test
    public void testAddErreklamazioBaliozkoBidaiari() {
        try {
            sut.addErreklamazio(bidaiariMock, userJasoMock, eskaeraMock, "Testu baliozkoa", 100f, ErrekLarri.TXIKIA);
        } catch (Exception e) {
            fail("Ez luke salbuespenik sortu behar kasu baliozkoan");
        }

        // Verificar que se llamó al método addAlert o equivalente
        verify(bidaiariMock, atLeastOnce()).addAlert(any());
    }

    @Test
    public void testAddErreklamazioBaliozkoGidaria() {
        try {
            sut.addErreklamazio(gidariaMock, userJasoMock, eskaeraMock, "Testu baliozkoa", 100f, ErrekLarri.ERTAINA);
        } catch (Exception e) {
            fail("Ez luke salbuespenik sortu behar kasu baliozkoan");
        }

        // Verificar que se llamó al método addAlert o equivalente
        verify(gidariaMock, atLeastOnce()).addAlert(any());
    }
}
