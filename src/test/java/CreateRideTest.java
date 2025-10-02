import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;

import dataAccess.DataAccess;
import domain.Ride;
import exceptions.RideAlreadyExistException;
import exceptions.RideMustBeLaterThanTodayException;
import testOperations.TestDataAccess;
import domain.Driver;

public class CreateRideTest {

	 //sut:system under test
	 static DataAccess sut=new DataAccess();
	 
	 //additional operations needed to execute the test 
	 static TestDataAccess testDA=new TestDataAccess();

	private Driver driver; 

	@Test
	//sut.createRide:  The Driver("Aitor Fernandez", "driver11@gmail.com") HAS one ride "from" "to" in that "date". 
	//Driver batek badauka ride bat eta berdiña sortzen ari naiz. 
	public void test1() {
		String driverEmail="driver14@gmail.com";
		String driverName="Aitor Fernandez";

		String rideFrom="Donostia";
		String rideTo="Zarautz";
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date rideDate=null;
		try {
			rideDate = sdf.parse("05/10/2025");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		boolean existDriver=false;
		try {
			
			//define parameters
			
			 
			//configure the state of the system (create object in the database)
			testDA.open();
			 existDriver=testDA.existDriver(driverEmail);//Driver existitzen den egiaztatu
			testDA.addDriverWithRide(driverEmail, driverName, rideFrom, rideTo, rideDate, 0, 0);//Metodo bat sortzen duena db-an driver bat bere bidaiarekin
			testDA.close();			
			
			
			//invoke System Under Test (sut)  
			sut.open();
		    sut.createRide(rideFrom, rideTo, rideDate, 0, 0, driverEmail); 
			sut.close();
			
			fail();
			
		   } catch (RideAlreadyExistException e) {
			 //verify the results
				sut.close();
				assertTrue(true);
			} catch (RideMustBeLaterThanTodayException e) {
			// TODO Auto-generated catch block
			fail();
		} finally {//Bukaera, berdin da nondik doan, hau beti exekutatuko da. 
				  //Remove the created objects in the database (cascade removing)   
				testDA.open();
				  if (existDriver) //Existitzen bada driver hori, bakarrik ezabatu bere bidaia
					  testDA.removeRide(driverEmail, rideFrom, rideTo, rideDate);
				  else //Ez bada existitzen, ezabatu dena, driverra eta bidaia
					  testDA.removeDriver(driverEmail);
		          testDA.close();
		        }//DB-a aurretik zegoen egoeran gelditu behar da
		   } 
	
	
	@Test
	//sut.createRide:  The Driver("Aitor Fernandez", "driver11@gmail.com") HAS NOT one ride "from" "to" in that "date". 
	public void test2() {
		//define paramaters
		String driverName="Aitor Fernandez2";
		String driverEmail="driver11@gmail.com";

		String rideFrom="Donostia";
		String rideTo="Zarautz";
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date rideDate=null;;
		try {
			rideDate = sdf.parse("05/10/2025");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
		try {
			//Check if exist this ride for this driver, and if exist, remove it.
			
			testDA.open();
			testDA.createDriver(driverEmail,driverName);
			testDA.close();
			
			
			//invoke System Under Test (sut)  
			sut.open();
			Ride ride=sut.createRide(rideFrom, rideTo, rideDate, 0, 0, driverEmail);
			sut.close();
			//verify the results
			assertNotNull(ride);
			assertEquals(ride.getFrom(),rideFrom);
			assertEquals(ride.getTo(),rideTo);
			assertEquals(ride.getDate(),rideDate);
			
			//ride datubasean dago
			testDA.open();
			boolean existRide=testDA.existRide(driverEmail,ride.getFrom(), ride.getTo(), ride.getDate());
				
			assertTrue(existRide);
			testDA.close();
			
		   } catch (RideAlreadyExistException e) {
			// if the program goes to this point fail  
			fail();
			
			} catch (RideMustBeLaterThanTodayException e) {
				// if the program goes to this point fail  

			fail();
			
		} finally {
			
			testDA.open();
			testDA.removeDriver(driverEmail);//Ezabatu sortutako driverra
			testDA.close();     
		        }
		   } 
	@Test
	//sut.createRide:  The Driver is null. The test must return null. If  an Exception is returned the createRide method is not well implemented.
		public void test3() {
			try {
				
				//define parameters
				driver=null;

				String rideFrom="Donostia";
				String rideTo="Zarautz";
				
				String driverEmail=null;

				
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				Date rideDate=null;;
				try {
					rideDate = sdf.parse("05/10/2025");
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
				
				
				
				//invoke System Under Test (sut)  
				sut.open();
				Ride ride=sut.createRide(rideFrom, rideTo, rideDate, 0, 0, driverEmail);
				System.out.println("ride "+ride);

				//verify the results
				assertNull(ride);
				
				
			   } catch (RideAlreadyExistException e) {
				// TODO Auto-generated catch block
				// if the program goes to this point fail  
				fail();
				} catch (RideMustBeLaterThanTodayException e) {
				// TODO Auto-generated catch block
					fail();
				} catch (Exception e) {
				// TODO Auto-generated catch block
					fail();
					
				} finally {
					sut.close();
				}
			
			   } 
	@Test
	//sut.createRide:  The ride "from" is null. The test must return null. If  an Exception is returned the createRide method is not well implemented.

	//This method detects a fail in createRide method because the method does not check if the parameters are null, and the ride is created.
	
	public void test4() {
		String driverEmail="driver11@gmail.com";
		String driverName="Aitor Fernandez2";

		String rideFrom=null;
		String rideTo="Zarautz";
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date rideDate=null;;
		try {
			rideDate = sdf.parse("05/10/2025");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		Ride ride=null;
		try {
			
			testDA.open();
			testDA.createDriver(driverEmail,driverName);
			testDA.close();
			
			//invoke System Under Test (sut)  
			sut.open();
			 ride=sut.createRide(rideFrom, rideTo, rideDate, 0, 0, driverEmail);
			sut.close();			
			
			//verify the results
			System.out.println("Ride: "+ride);

			assertNull(ride);
			
			//q datubasean dago
			testDA.open();
			boolean exist=testDA.existRide(driverEmail,rideFrom, rideTo, rideDate);
				
			assertTrue(!exist);
			testDA.close();
			
		   } catch (RideAlreadyExistException e) {
			// TODO Auto-generated catch block
			// if the program goes to this point fail  
			fail();
			} catch (RideMustBeLaterThanTodayException e) {

			// TODO Auto-generated catch block
			fail();
			}  catch (Exception e) {
			// TODO Auto-generated catch block
			fail();
			}
		
		
		finally {   

			testDA.open();
			testDA.removeDriver(driverEmail);
			testDA.close();
			
		        }
		   }
}

