package createEskaeraTest;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.After;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import dataAccess.DataAccess;
import domain.*;
import exceptions.RequestAlreadyExistException;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.ArrayList;

public class createEskaeraMockWhiteTest {

    private DataAccess sut;

    @Mock
    private EntityManager db;

    @Mock
    private EntityTransaction et;

    @Mock
    private Bidaiari bidaiari;

    @Mock
    private Ride ride;

    @Mock
    private Driver driver;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Mock tx
        when(db.getTransaction()).thenReturn(et);

        // Mock listas vacías
        when(bidaiari.getEskaerak()).thenReturn(new ArrayList<>());
        when(ride.getEskaerenList()).thenReturn(new ArrayList<>());
        when(ride.getDriver()).thenReturn(driver);

        sut = new DataAccess(db);
    }

    @After
    public void tearDown() {
        // Nada especial
    }

    @Test
    public void testCreateEskaeraWhiteBox() {
        try {
            Eskaera result = sut.createEskaera(bidaiari, ride, 2);

            assertEquals(Eskaera.EskaeraEgoera.PENDING, result.getEgoera());
            assertEquals(2, result.getNPlaces());
            assertEquals(ride, result.getRide());
            assertEquals(bidaiari, result.getBidaiari());

            verify(db).persist(any(Alerta.class));
            verify(et).commit();

        } catch (Exception e) {
            fail("Ez litzateke salbuespena jaurti behar");
        }
    }

    @Test
    public void testCreateEskaeraExistitzenDaWhiteBox() {
        try {
            // Lista con Eskaera existente
            ArrayList<Eskaera> existing = new ArrayList<>();
            existing.add(new Eskaera(Eskaera.EskaeraEgoera.PENDING, 2, ride, bidaiari));
            when(bidaiari.getEskaerak()).thenReturn(existing);

            sut.createEskaera(bidaiari, ride, 3);
            fail("RequestAlreadyExistException jaurti behar zuen");
        } catch (RequestAlreadyExistException e) {
            verify(et).rollback();
        } catch (Exception e) {
            fail("RequestAlreadyExistException jaurti behar zuen");
        }
    }
}
