package main;
import java.util.ArrayList;
import java.util.ListIterator;

public class ElevatorController {
	
	private static int num_floors; //number floors
	private static int num_elevators; //number elevators
	private static ArrayList<Elevator> elevators;
	private static ArrayList<Thread> motors = new ArrayList<Thread>();
	
    public static void main(String[] args) { 
    	//initialize with command line arguments
    	try {
    		int f = Integer.parseInt(args[0]);
    		int e = Integer.parseInt(args[1]);
    		
    		if(f<1 || e<1) {
    			System.out.println("Intializing with default values..");
    			initialize(10 , 5 );
    		}
    		else {
    			initialize(f , e );
    		}
    	}
    	catch(Exception e) {
    		System.out.println(e.getMessage() + "/nInitializing with deafult values...");
        	initialize(10 , 5 );
    	}
    	
    	addMotors();
        startMotors();
        DrawGUI.initGraphics(elevators,num_floors); 
    }
    
    public static void initialize(int floors, int elevators) {
    	
    	System.out.println("Welcome to Elevator System");
		num_floors = floors;
		num_elevators = elevators;
        
        /*adds motors for all elevators. Consequently, 
         *also adds requestListener for all requests*/
    }
    
    
    public static void addMotors() {
    	/*
    	 * 
    	1. Create object of Elevator class add to its ArrayList and 
    	2. Create NEW object of Motor class which is a thread
    	3. Make each elevator know what motor thread is running it.
        */
    	elevators = new ArrayList<Elevator>();
        for (int elevator_num = 0; elevator_num < num_elevators; elevator_num++) {
        	Elevator elevator_object = new Elevator(elevator_num);
        	/*
        	 * 
        	 * Since this class does not extend the thread class, 
        	 * the object of scheduler class are bounded to the thread by passing it as an argument in the constructor.
        	 * 
        	 * This is equivalent to 
        	 * Motor motor = new Motor(elevator);
        	 * motor.start();
        	 * if this class extended from the Thread class
        	 * 
        	 * */

        	Thread motor = new Thread(new Motor(elevator_object), "MotorThread"+elevator_num);
        	elevator_object.setMotorThread(motor); 
        	elevators.add(elevator_object);
        	motors.add(motor);
        }

    	
    }
    

    
    public static void startMotors() {
        
        /*
         * 
         * Motor Thread for each elevator starts here.
         * Motors help elevator move to requested floor
         * 
         * 
         * */        
    	ListIterator<Thread> litr = motors.listIterator();
        while (litr.hasNext()) {
            Thread motor = litr.next();
            motor.start();
        }
    	
    }

    /*public static void addRequestListener(ArrayList<Elevator> elevators) {
        
         * 
         * Request Listener Thread for the entire elevator system starts here.
         * In essence this means the elevator is ready to listen to the external calls
         * This method for Console mode; is not required for GUI based inputs
         
        Thread requestListenerThread = new Thread(new RequestListener(elevators),"RequestListenerThread");
         requestListenerThread.start();
        
    }*/
    
    public static int getNumberOfFloors()
    {
    	return num_floors;
    }
    
    public static int getNumberOfElevators()
    {
    	return num_elevators;

    }
    
    public static ArrayList<Elevator> getElevators()
    {
    	return elevators;
    }
    
    public static ArrayList<Thread> getMotors()
    {
    	return motors;
    }    
    
}