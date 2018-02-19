package main;
import java.util.ListIterator;

public class Motor implements Runnable { 
	
	private Elevator elevator;
	
	public Motor(Elevator elevator) {
		this.elevator = elevator;
	}

    @Override
    public void run() { 
        while (true) {
            Motor.move(this.elevator);
        }
    }
    
    public static void move(Elevator elevator) {
        /*
         *  nextFloor method:
         * 1. Initially puts all the Motor threads in the waiting state. 
         * 2. Once the RequestListner thread takes in a request to serve a floor the addFloor method gets called and 
         *    puts the selected motor of the chosen elevator out of the waiting state.it returns -1.
         * 3. Now the motor thread for the selected elevator runs until it servers all the request from the treeSet.
         * 4. Once all the requests are server it will RETURN TO WAITING STATE
         */
        double floor = elevator.nextFloor(); //get floor from request set 
        double currentFloor = elevator.getCurrentFloor();
        /* 
         * The motor is making the selected elevator  to the desired floor.
         */
        try{
            if (floor >= 0) {
                if (currentFloor > floor) { 
                    System.out.println("Floor : " + currentFloor); 
                    while (currentFloor > floor) { 
                    	currentFloor= Util.round((currentFloor-0.1),1);
                        elevator.setCurrentFloor(currentFloor); 
                    }
                    requestCompleted(elevator);
                    
                	/*
                	 * Turn OFF floor Lights on here. No logic required for this since  external lights are bounded to the floors 
                	 * and not the elevator, so it is definitely not an elevator property.
                	 * But it gets deactivated during a when the door closes and a particular request gets served.
 					 * So it must get turned on here directly in the GUI.
                	 * 
                	 */
                } else {
                    System.out.println("Floor : " + currentFloor); 
                    while (currentFloor < floor) {
                    	currentFloor= Util.round((currentFloor+0.1),1);
                        elevator.setCurrentFloor(currentFloor);
                    }
                    requestCompleted(elevator);
                    
                	/*
                	 * Turn OFF floor Lights on here. No logic required for this since  external lights are bounded to the floors 
                	 * and not the elevator, so it is definitely not an elevator property.
                	 * But it gets deactivated during a when the door closes and a particular request gets served.
 					 * So it must get turned on here directly in the GUI.
                	 * 
                	 */
                }
                
                System.out.println("\n---------------------------------Current Status------------------------------------------");
            	ListIterator<Elevator> litr = ElevatorController.getElevators().listIterator();
                while (litr.hasNext()) {
                    Elevator all_elevator = litr.next();
                    System.out.println("Elevator : " + all_elevator.getName() + " is on floor : " + all_elevator.getCurrentFloor());
                }
                System.out.println("-----------------------------End of Status------------------------------------------");

            }
             
        }catch(InterruptedException e){ 
            // If a new request has interrupted a current request processing then check - 
            // -whether the current request has already been processed 
            // -if not add it back to the request Set 
            if(elevator.getCurrentFloor() != floor){ 
                elevator.getRequestSet().add(floor);
            } 
        } 
    }

	private static void requestCompleted(Elevator elevator) {
		RequestListener.setExternalLight((int) elevator.getCurrentFloor(), 0, false);
		RequestListener.setExternalLight((int) elevator.getCurrentFloor(), 1, false);                 
		RequestListener.setInternalLight(elevator.getName(), (int) elevator.getCurrentFloor(), false);
		
		System.out.print("For elevator: " + elevator.getName()+ " door opened by ");
		elevator.openDoors();
		elevator.setInternalEnabled(true);
		
		System.out.print("\nFor elevator: " + elevator.getName()+ " door will close in ");
		elevator.closeDoors();
	}
} 