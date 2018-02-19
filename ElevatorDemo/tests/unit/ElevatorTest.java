package unit;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Random;
import java.util.TreeSet;

import org.junit.Test;

import main.Elevator;
import main.Motor;
import main.Util;

public class ElevatorTest {

	@Test
	public void currentFloorUp() throws InterruptedException {

		Elevator elevator = new Elevator(0);
		double currentFloor = 3;	   
		int floorFromRequestSet = 5;
		elevator.setCurrentFloor(currentFloor);

		if (floorFromRequestSet >= 0) {
			if (currentFloor > floorFromRequestSet) 
			{ 
				while (currentFloor > floorFromRequestSet) 
				{ 
					currentFloor= Util.round((currentFloor-0.1),1);
					elevator.setCurrentFloor(currentFloor); 
				}
			} 
			else 
			{
				while (currentFloor < floorFromRequestSet) 
				{
					currentFloor= Util.round((currentFloor+0.1),1);
					elevator.setCurrentFloor(currentFloor);
				}
			}
		} 

		assertEquals(elevator.getCurrentFloor() , floorFromRequestSet , 0.0);
	}
	
	@Test
	public void currentFloorDown() throws InterruptedException {
		Elevator elevator = new Elevator(0);
		double currentFloor = 13;	   
		int floorFromRequestSet = 11;
		elevator.setCurrentFloor(currentFloor);

		if (floorFromRequestSet >= 0) {
			if (currentFloor > floorFromRequestSet) 
			{ 
				while (currentFloor > floorFromRequestSet) 
				{ 
					currentFloor= Util.round((currentFloor-0.1),1);
					elevator.setCurrentFloor(currentFloor); 
				}
			} 
			else 
			{
				while (currentFloor < floorFromRequestSet) 
				{
					currentFloor= Util.round((currentFloor+0.1),1);
					elevator.setCurrentFloor(currentFloor);
				}
			}
		} 

		assertEquals(elevator.getCurrentFloor() , floorFromRequestSet , 0.0);
	}
	
	@Test(timeout = 500000)
	public void elevatorMovement() 
	{
		Random r = new Random();
		ArrayList<Elevator> a = new ArrayList<Elevator>();
		int floors = r.nextInt(20);
		
		for (int i = 0; i < 3+r.nextInt(10); i++) 
		{
			int j = r.nextInt(30);
			
			a.add(new Elevator(i));
			a.get(i).setMotorThread(new Thread(new Motor(a.get(i)), "MotorThread"+i));
			a.get(i).getMotorThread().start();
			
			TreeSet<Double> t = new TreeSet<Double>();
			
//			if (r.nextBoolean()) {try {
//				a.get(i).setCurrentFloor(new Double(null));
//			} catch (NumberFormatException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}}
			
			while (j<20)
			{
				double d = (double) r.nextInt(floors);
				t.add(d);
				if (r.nextBoolean()) {a.get(i).serveFloor(d); } else { a.get(i).serveFloorInt(d); }
				j++;
			}

		}
	}

}
