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
              


              // parametroa null denean ziurtatu rollback egiten duela

              @Test
              public void test1() {
                  sut.acceptErrek(null);
                  // rollback itxen dala ziurtatu
                  Mockito.verify(et).rollback();
              }



   
}
