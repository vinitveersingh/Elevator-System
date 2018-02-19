package main;
import java.util.TreeSet; 

   public class Elevator implements ElevatorHardware { 
     
    private TreeSet<Double> requestSet = new TreeSet<Double>(); //to store the requests in sorted, tree structure
     
    private Doors elevators_doors = Doors.CLOSED;
    
    private int door_size = 0;
    
    private double currentFloor = 0;
    
    private int name;
    
    private int door_slide = 0;

    private Direction direction = Direction.UP;
    
    private boolean internalButtons = false;

    public Elevator(int name) {
    	this.name = name;
    };
     
    private Thread motorThread;

    /** 
     * Add request to our TreeSet
     *  
     * @param floor 
     */
    public synchronized void serveFloor(double floor) { 
        requestSet.add(floor); 
         
        /*
         * IMPORTANT: Note that the motor thread is bounded to THE CHOOSEN ELEVATOR to request the external call of the floor
         * So :
         * 1. If the elevator motor is in the wait state then it most be notified to get out of the waiting state
         *    and  it has to move to 
         */
        if(motorThread.getState() == Thread.State.WAITING){ 
            // Notify scheduler thread that a new request has come if it is waiting 
            notify(); 
            System.out.println("moves out of waiting state.");
        }else{ 
            // Interrupt scheduler thread to check if new request should be processed before current request.
        	// should not process new request unless doors are closed
        	try {
				Thread.sleep(250);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
	        	if(elevators_doors == Doors.CLOSED) {
	        		motorThread.interrupt();
	        		System.out.println("is interrupted.");
	        	}
		  	}

        } 
         
    } 

    /** 
     * @return next request to process based on elevator's current floor and direction
     */ 
    public synchronized double nextFloor() {

        Double floor = null;

        if (direction == Direction.UP) {
        	//System.out.println("Direction UP");
            if (requestSet.ceiling(currentFloor) != null) { 	
                floor = requestSet.ceiling(currentFloor); 
            } else { 
                floor = requestSet.floor(currentFloor); 
            } 
        } else { 
        	//System.out.println("Direction DOWN");
            if (requestSet.floor(currentFloor) != null) { 
                floor = requestSet.floor(currentFloor); 
            } else { 
                floor = requestSet.ceiling(currentFloor);                
            } 
        } 
        if (floor == null) {
            try { 
                wait(); 
            } catch (InterruptedException e) { 
                e.printStackTrace(); 
            }
        } else { 
            // Remove the request from TreeSet as it is the request in Progress. 
            requestSet.remove(floor); 
        } 
        return (floor == null) ? -1 : floor; 
    }

    public double getCurrentFloor() { 
        return currentFloor;
    }
     
    /** 
     * Set current floor and direction based on requested floor 
     *  
     * @param currentFloor 
     * @throws InterruptedException  
     */ 
    
    public void setCurrentFloor(double currentFloor) throws InterruptedException { 
    	currentFloor = Util.round(currentFloor,1);
        if (this.currentFloor > currentFloor) { 
            setDirection(Direction.DOWN); 
        } else { 
            setDirection(Direction.UP); 
        } 
      
        System.out.println("Floor : " + currentFloor); 
        this.currentFloor = currentFloor; 
        Thread.sleep(250);
                  
    } 

    
    
    public void openDoors()
    {            
    	setDoorStatus(Doors.OPENING);
    	try { 
        	//Door length is 5 meters
        	for(int door_slide = 0 ; door_slide <= 4; door_slide++)
        	{
        		setDoorSlide(door_slide);
        		System.out.print("  " + (getDoorSlide() + 1) + " meters.");
            	setDoorStatus(Doors.OPENING);
            	Thread.sleep(250);
            	setDoorSize(door_slide);

        	}
        	setDoorStatus(Doors.OPENED);

        	Thread.sleep(1000); // door remains open for 5 seconds for people to come in
        	setDoorStatus(Doors.CLOSING);
    	} catch (InterruptedException e) { 
    		e.printStackTrace(); 
    	}
    	
    }
  
    
    public void closeDoors()
    {
    	
    	//Door length is 5 meters
    	if(getDoorSlide() == 4)
    	{
        	setDoorStatus(Doors.CLOSING);
    		for(int door_slide = getDoorSlide() ; door_slide >= 0; door_slide--)
    		{
    			setDoorSlide(door_slide);
    			System.out.print("  " + (getDoorSlide()+1) + " meters.");
            	setDoorStatus(Doors.CLOSING);
            	setDoorSize(door_slide);
            	try {
					Thread.sleep(250);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    		}
        	setDoorStatus(Doors.CLOSED);
    	}
    }
    
    
    public void setDoorSlide(int door_slide)
    {
    	this.door_slide = door_slide;
    }
    

    public int getDoorSlide()
    {
    	
    	return this.door_slide;
    	
    }
    
    public int getName() { 
        return name; 
    } 
    
    public Direction getDirection() { 
        return direction; 
    } 

    public void setDirection(Direction direction) { 
        this.direction = direction; 
    } 

    public Thread getMotorThread() { 
        return motorThread; 
    }

    public void setMotorThread(Thread motorThread) { 
        this.motorThread = motorThread; 
    } 

    public TreeSet<Double> getRequestSet() { 
        return requestSet; 
    } 

    public void setRequestSet(TreeSet<Double> requestSet) { 
        this.requestSet = requestSet; 
    } 
     
    public void setDoorStatus(Doors elevators_doors)
    {
    	this.elevators_doors = elevators_doors;
    }
    
    public Doors getDoorStatus()
    {
    	return this.elevators_doors;
    }
    
    public boolean getInternalEnabled()
    {
    	return this.internalButtons;
    }
    
    public void setInternalEnabled(boolean value)
    {
    	this.internalButtons = true;
    }

	public int getDoorSize() {
		return door_size;
	}

	public void setDoorSize(int door_size) {
		this.door_size = door_size;
	}
	
    /** 
     * Add internal request to our TreeSet
     *  
     * @param floor 
     */
    public synchronized void serveFloorInt(double floor) { 
        requestSet.add(floor); 
         
        /*
         * IMPORTANT: Note that the motor thread is bounded to THE CHOOSEN ELEVATOR to request the external call of the floor
         * So :
         * 1. If the elevator motor is in the wait state then it most be notified to get out of the waiting state
         *    and  it has to move to 
         */
        if(motorThread.getState() == Thread.State.WAITING){ 
            // Notify scheduler thread that a new request has come if it is waiting 
            notify(); 
            System.out.println("moves out of waiting state.");
        }else{ 
            // Interrupt scheduler thread to check if new request should be processed before current request.
        	// should not process new request unless doors are closed
        	if(elevators_doors == Doors.CLOSED) {
        		motorThread.interrupt();
        		System.out.println("is interrupted.");
        	}
        } 
         
    } 
    
    
}


//public enum Direction { 
//    UP , DOWN
//}

enum Buttons
{
	INTERNAL , EXTERNAL
}

//public enum Doors
//{
//	OPENED , OPENING, CLOSED , CLOSING
//}
