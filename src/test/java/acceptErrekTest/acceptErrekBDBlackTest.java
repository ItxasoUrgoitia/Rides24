package acceptErrekTest;

import static org.junit.Assert.assertEquals;

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
	 
	 //additional operations needed to execute the test 
	 static TestDataAccess testDA=new TestDataAccess();
	 
	 @Test(expected = NullPointerException.class)
	    // sut.acceptErrek: parameter is null → should throw NullPointerException
	    public void test1() {
	        sut.open();
	        sut.acceptErrek(null);
	        sut.close();
	    }
    
    
}