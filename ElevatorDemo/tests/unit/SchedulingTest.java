package unit;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Random;
import java.util.TreeSet;

import org.junit.Test;

import main.Direction;
import main.Elevator;
import main.Scheduling;

public class SchedulingTest {

	@Test(timeout = 100000)
	public void test() {
		
		Random r = new Random();
		ArrayList<Elevator> a = new ArrayList<Elevator>();
		int floors = r.nextInt(20);
		
		for (int i = 0; i < 3+r.nextInt(10); i++) 
		{
			int j = r.nextInt(11);
			a.add(new Elevator(i));
			TreeSet<Double> t = new TreeSet<Double>();
			while (j<10)
			{
				t.add((double)Math.abs(r.nextInt(floors)));
				j++;
			}
			a.get(i).setRequestSet(t);
			try {
				a.get(i).setCurrentFloor((double)r.nextInt(floors));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			a.get(i).setDirection(randomDirection());
		}
		
		for (int i = 0; i < 50+r.nextInt(100); i++) {
			assertTrue(Scheduling.nearestCar(a, floors, randomDirection(), r.nextInt(floors))<a.size()); 
		}
		assertNotSame(a,new ArrayList<Elevator>());
		
	}
	
	public static Direction randomDirection()
	{
		Random r = new Random();
		if (r.nextBoolean()) 
		{
			return Direction.UP;
		}
		return Direction.DOWN;
	}
}
