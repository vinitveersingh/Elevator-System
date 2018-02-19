package unit;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Random;

import org.junit.Test;

import main.Elevator;
import main.ElevatorController;

public class ElevatorControllerTest {

	@Test(timeout = 100000)
	public void initializationTest()
	{
		Random r = new Random();
		
		int n_floor = r.nextInt(1000);
		int n_elevator = r.nextInt(1000);
		
		ElevatorController t = new ElevatorController();
		t.initialize(n_floor,n_elevator);
		t.addMotors();
		
		assertEquals(t.getNumberOfFloors(),n_floor);
		assertEquals(t.getNumberOfElevators(),n_elevator);
		
		ArrayList<Elevator> te = t.getElevators();
		ArrayList<Thread> tm = t.getMotors();
		
		for (int i = 0; i < te.size(); i++) 
		{
			assertEquals(te.get(i).getMotorThread(),tm.get(i));
			assertEquals(tm.get(i).getName(),"MotorThread"+i);
			assertTrue(!tm.get(i).isAlive());
		}
		
		t.startMotors();
		
		for (int i = 0; i < te.size(); i++) 
		{
			assertTrue(t.getMotors().get(i).isAlive());
		}
	}
}
