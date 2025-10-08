package acceptErrekTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import dataAccess.DataAccess;
import domain.Bidaiari;
import domain.Driver;
import domain.Erreklamazioa;
import domain.Erreklamazioa.ErrekLarri;
import domain.Erreklamazioa.ErrekMota;
import testOperations.TestDataAccess;
import domain.Eskaera;

public class acceptErrekBDBlackTest {
        static DataAccess sut=new DataAccess();
              
               
               static TestDataAccess testDA=new TestDataAccess();
              

@Test
public void test1() {
              try {
        sut.open();                
        sut.acceptErrek(null);     
        assertTrue(true);          
    } catch (Exception e) {
        fail("rollback egin");
    } finally {
        sut.close();
    }
}

                      
                      
               
    
    
}
