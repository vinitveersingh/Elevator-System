package integration;

import static org.junit.Assert.*;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import javax.swing.JButton;
import org.junit.Test;
import main.ElevatorController;

public class SystemTest {

	Random r = new Random();

	int floors = r.nextInt(20);
	int elevators = r.nextInt(15);
	
//	@org.junit.Test
//	public void test() 
//	{
//		String[] args = {floors+"",elevators+""};
//		ElevatorController sys = new ElevatorController();
//		
////		new Thread(new Runnable() {
////		    public void run() {
////		        sys.main(args);
////		    }
////		}).start();
//		
//		new Thread(new Runnable() {
//		    public void run() {
//		        generateCalls(sys);
//		    }
//		}).start();
//		
//		sys.main(args);
//	}

	public void generateCalls(ElevatorController e) 
	{
		try {
			TimeUnit.SECONDS.sleep(5);
		} catch (InterruptedException f) {
			// TODO Auto-generated catch block
			f.printStackTrace();
		}
		
		int calls = 10 + r.nextInt(20);
		
		for (int i = 0; i < calls; i++) 
		{
			try {
				TimeUnit.MILLISECONDS.sleep(500);
			} catch (InterruptedException g) {
				// TODO Auto-generated catch block
				g.printStackTrace();
			}
			
			JButton button = new JButton();
			int elevator = r.nextInt(elevators);
			int floor = r.nextInt(floors);
			char dir = ' ';			
			char b;
			
			if (r.nextBoolean()) { b = 'e';} else { b = 'i';}
			
			if (r.nextBoolean()) {dir = 'd';} else { dir = 'u';}
			
			if (b == 'e') 
			{
				button.setName(floor+""+dir+""+b);
			}
			else 
			{
				button.setName(elevator+""+floor+""+b);
			}
			//button.addActionListener(e.listener);
			button.setEnabled(true);
			//ActionEvent call = new ActionEvent(button, r.nextInt(), null);
			button.doClick();
			//System.out.println(call.toString());
		}
	}


}
