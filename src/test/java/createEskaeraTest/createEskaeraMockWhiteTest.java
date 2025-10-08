package createEskaeraTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import dataAccess.DataAccess;
import domain.Bidaiari;
import domain.Driver;
import domain.Ride;
import domain.User;
import domain.Eskaera;
import domain.Alerta;
import exceptions.RequestAlreadyExistException;

public class createEskaeraMockWhiteTest {

    static DataAccess sut; // Sistema bajo prueba (System Under Test)
    protected MockedStatic<Persistence> persistenceMock;

    @Mock
    protected EntityManagerFactory entityManagerFactory;
    @Mock
    protected EntityManager db;
    @Mock
    protected EntityTransaction et;

    @Before
    public void init() {
        MockitoAnnotations.openMocks(this);

        // Mockear Persistence y EntityManager
        persistenceMock = Mockito.mockStatic(Persistence.class);
        persistenceMock.when(() -> Persistence.createEntityManagerFactory(Mockito.any()))
                .thenReturn(entityManagerFactory);

        Mockito.doReturn(db).when(entityManagerFactory).createEntityManager();
        Mockito.doReturn(et).when(db).getTransaction();

        sut = new DataAccess(db);
    }

    @After
    public void tearDown() {
        if (persistenceMock != null)
            persistenceMock.close();
    }

    /**
     * ✅ Test white-box: creación correcta de una Eskaera nueva
     */
    @Test
    public void testCreateEskaeraWhiteBox() {
        try {
            Driver driver = new Driver("driver@ex.com", "Driver");
            Bidaiari bidaiari = new Bidaiari("Bidaiari", "1234", "bidaiari1@gmail.com", "111");

            Date fecha = new Date();
            Ride ride = new Ride("Bilbao", "Donostia", fecha, 3, 12.5f, driver);
            ride.setRideNumber(1); // Asignar ID para el mock

            bidaiari.setEskaerak(new ArrayList<>());
            ride.setEskaerenList(new ArrayList<>());

            // Mockear búsquedas
            when(db.find(User.class, "bidaiari1@gmail.com")).thenReturn(bidaiari);
            when(db.find(Ride.class, 1)).thenReturn(ride);

            // Ejecutar método
            Eskaera result = sut.createEskaera(bidaiari, ride, 2);

            // Verificar resultados
            assertEquals(Eskaera.EskaeraEgoera.PENDING, result.getEgoera());
            assertEquals(2, result.getNPlaces());
            assertEquals(ride, result.getRide());
            assertEquals(bidaiari, result.getBidaiari());

            verify(db).persist(Mockito.any(Alerta.class));
            verify(et).commit();

        } catch (Exception e) {
            e.printStackTrace();
            fail("Ez litzateke salbuespena jaurti behar");
        }
    }


    /**
     * ⚠️ Test white-box: cuando la Eskaera ya existe
     */
    @Test
    public void testCreateEskaeraExistitzenDaWhiteBox() throws Exception {
        Driver driver = new Driver("driver@ex.com", "Driver");
        Bidaiari bidaiari = new Bidaiari("Bidaiari", "1234", "bidaiari2@gmail.com", "111");

        Date fecha = new Date();
        Ride ride = new Ride("Bilbao", "Donostia", fecha, 3, 12.5f, driver);
        ride.setRideNumber(2);

        ArrayList<Eskaera> bidaiariEskaerak = new ArrayList<>();
        Eskaera existEskaera = new Eskaera(Eskaera.EskaeraEgoera.PENDING, 2, ride, bidaiari);
        bidaiariEskaerak.add(existEskaera);
        bidaiari.setEskaerak(bidaiariEskaerak);

        ride.setEskaerenList(new ArrayList<>());

        when(db.find(User.class, "bidaiari2@gmail.com")).thenReturn(bidaiari);
        when(db.find(Ride.class, 2)).thenReturn(ride);

        assertThrows(RequestAlreadyExistException.class, () -> sut.createEskaera(bidaiari, ride, 3)); // Si no lanza la excepción, JUnit falla automáticamente
    }

}
