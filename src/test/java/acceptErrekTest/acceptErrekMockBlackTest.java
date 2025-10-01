package acceptErrekTest;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

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
import domain.Erreklamazioa;
import domain.Erreklamazioa.ErrekLarri;
import domain.Erreklamazioa.ErrekMota;
import domain.Eskaera;

public class acceptErrekMockBlackTest {
static DataAccess sut;
	
	protected MockedStatic <Persistence> persistenceMock;

	@Mock
	protected  EntityManagerFactory entityManagerFactory;
	@Mock
	protected  EntityManager db;
	@Mock
    protected  EntityTransaction  et;
	

	@Before
    public  void init() {
        MockitoAnnotations.openMocks(this);
        persistenceMock = Mockito.mockStatic(Persistence.class);
		persistenceMock.when(() -> Persistence.createEntityManagerFactory(Mockito.any()))
        .thenReturn(entityManagerFactory);
        
        Mockito.doReturn(db).when(entityManagerFactory).createEntityManager();
		Mockito.doReturn(et).when(db).getTransaction();
	    sut=new DataAccess(db);


		
    }
	@After
    public  void tearDown() {
		persistenceMock.close();


		
    }
	
	// acceptErrekMockBlackTest.java

	@Test
	public void test1() {
	    // Arrange
	    Bidaiari bidaiari = Mockito.mock(Bidaiari.class);
	    Driver driver = Mockito.mock(Driver.class);
	    Eskaera eskaera = Mockito.mock(Eskaera.class);
	    Erreklamazioa erreklamazioa = Mockito.mock(Erreklamazioa.class);

	    // Mock IDs and emails
	    Mockito.when(erreklamazioa.getId()).thenReturn(1);
	    Mockito.when(erreklamazioa.getErrekJaso()).thenReturn(bidaiari);
	    Mockito.when(erreklamazioa.getErrekJarri()).thenReturn(driver);
	    Mockito.when(erreklamazioa.getLarri()).thenReturn(Erreklamazioa.ErrekLarri.TXIKIA);
	    Mockito.when(erreklamazioa.getEskaera()).thenReturn(eskaera);
	    Mockito.when(eskaera.getPrez()).thenReturn(100f);

	    // Mock DB finds
	    Mockito.when(db.find(Erreklamazioa.class, 1)).thenReturn(erreklamazioa);
	    Mockito.when(db.find(Driver.class, driver.getEmail())).thenReturn(driver);
	    Mockito.when(db.find(Bidaiari.class, bidaiari.getEmail())).thenReturn(bidaiari);

	    // Act
	    sut.acceptErrek(erreklamazioa);

	    // Assert
	    // Bidaiari loses 110
	    Mockito.verify(bidaiari).diruSartuBid(-110f);
	    // Driver gains 110
	    Mockito.verify(driver).diruSartuDri(110f);
	    // Status set to ACCEPTED
	    Mockito.verify(erreklamazioa).setMota(Erreklamazioa.ErrekMota.ACCEPTED);
	    // Movements and alerts persisted
	    Mockito.verify(db, Mockito.atLeastOnce()).persist(Mockito.any());
	}
	
	// acceptErrekMockBlackTest.java

	@Test
	public void test2() {
	    // Arrange
	    Bidaiari bidaiari = Mockito.mock(Bidaiari.class);
	    Driver driver = Mockito.mock(Driver.class);
	    Eskaera eskaera = Mockito.mock(Eskaera.class);
	    Erreklamazioa erreklamazioa = Mockito.mock(Erreklamazioa.class);

	    // Mock IDs and emails
	    Mockito.when(erreklamazioa.getId()).thenReturn(2);
	    Mockito.when(erreklamazioa.getErrekJaso()).thenReturn(bidaiari);
	    Mockito.when(erreklamazioa.getErrekJarri()).thenReturn(driver);
	    Mockito.when(erreklamazioa.getLarri()).thenReturn(Erreklamazioa.ErrekLarri.ERTAINA);
	    Mockito.when(erreklamazioa.getEskaera()).thenReturn(eskaera);
	    Mockito.when(eskaera.getPrez()).thenReturn(100f);

	    // Mock DB finds
	    Mockito.when(db.find(Erreklamazioa.class, 2)).thenReturn(erreklamazioa);
	    Mockito.when(db.find(Driver.class, driver.getEmail())).thenReturn(driver);
	    Mockito.when(db.find(Bidaiari.class, bidaiari.getEmail())).thenReturn(bidaiari);

	    // Act
	    sut.acceptErrek(erreklamazioa);

	    // Assert
	    // Bidaiari loses 130
	    Mockito.verify(bidaiari).diruSartuBid(-130f);
	    // Driver gains 130
	    Mockito.verify(driver).diruSartuDri(130f);
	    // Status set to ACCEPTED
	    Mockito.verify(erreklamazioa).setMota(Erreklamazioa.ErrekMota.ACCEPTED);
	    // Movements and alerts persisted
	    Mockito.verify(db, Mockito.atLeastOnce()).persist(Mockito.any());
	}
	
	// acceptErrekMockBlackTest.java

	@Test
	public void test3() {
	    // Arrange
	    Bidaiari bidaiari = Mockito.mock(Bidaiari.class);
	    Driver driver = Mockito.mock(Driver.class);
	    Eskaera eskaera = Mockito.mock(Eskaera.class);
	    Erreklamazioa erreklamazioa = Mockito.mock(Erreklamazioa.class);

	    // Mock IDs and emails
	    Mockito.when(erreklamazioa.getId()).thenReturn(3);
	    Mockito.when(erreklamazioa.getErrekJaso()).thenReturn(bidaiari);
	    Mockito.when(erreklamazioa.getErrekJarri()).thenReturn(driver);
	    Mockito.when(erreklamazioa.getLarri()).thenReturn(Erreklamazioa.ErrekLarri.HANDIA);
	    Mockito.when(erreklamazioa.getEskaera()).thenReturn(eskaera);
	    Mockito.when(eskaera.getPrez()).thenReturn(100f);

	    // Mock DB finds
	    Mockito.when(db.find(Erreklamazioa.class, 3)).thenReturn(erreklamazioa);
	    Mockito.when(db.find(Driver.class, driver.getEmail())).thenReturn(driver);
	    Mockito.when(db.find(Bidaiari.class, bidaiari.getEmail())).thenReturn(bidaiari);

	    // Act
	    sut.acceptErrek(erreklamazioa);

	    // Assert
	    // Bidaiari loses 150
	    Mockito.verify(bidaiari).diruSartuBid(-150f);
	    // Driver gains 150
	    Mockito.verify(driver).diruSartuDri(150f);
	    // Status set to ACCEPTED
	    Mockito.verify(erreklamazioa).setMota(Erreklamazioa.ErrekMota.ACCEPTED);
	    // Movements and alerts persisted
	    Mockito.verify(db, Mockito.atLeastOnce()).persist(Mockito.any());
	}
	// acceptErrekMockBlackTest.java

	@Test
	public void testAcceptErrek_DriverReceives() {
	    // Arrange
	    Driver driver = Mockito.mock(Driver.class);
	    Bidaiari bidaiari = Mockito.mock(Bidaiari.class);
	    Eskaera eskaera = Mockito.mock(Eskaera.class);
	    Erreklamazioa erreklamazioa = Mockito.mock(Erreklamazioa.class);

	    // Mock IDs and emails
	    Mockito.when(erreklamazioa.getId()).thenReturn(4);
	    Mockito.when(erreklamazioa.getErrekJaso()).thenReturn(driver);
	    Mockito.when(erreklamazioa.getErrekJarri()).thenReturn(bidaiari);
	    Mockito.when(erreklamazioa.getEskaera()).thenReturn(eskaera);
	    Mockito.when(eskaera.getPrez()).thenReturn(100f);

	    // Mock DB finds
	    Mockito.when(db.find(Erreklamazioa.class, 4)).thenReturn(erreklamazioa);
	    Mockito.when(db.find(Driver.class, driver.getEmail())).thenReturn(driver);
	    Mockito.when(db.find(Bidaiari.class, bidaiari.getEmail())).thenReturn(bidaiari);

	    // Act
	    sut.acceptErrek(erreklamazioa);

	    // Assert
	    // Bidaiari gains 100
	    Mockito.verify(bidaiari).diruSartuBid(100f);
	    // Driver loses 100
	    Mockito.verify(driver).diruSartuDri(-100f);
	    // Status set to ACCEPTED
	    Mockito.verify(erreklamazioa).setMota(Erreklamazioa.ErrekMota.ACCEPTED);
	    // Movements and alerts persisted
	    Mockito.verify(db, Mockito.atLeastOnce()).persist(Mockito.any());
	}

	// acceptErrekMockBlackTest.java

	@Test(expected = NullPointerException.class)
	public void test5() {
	    sut.acceptErrek(null);
	}



   
}