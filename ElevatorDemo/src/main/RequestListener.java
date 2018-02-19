package main;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import java.util.ArrayList;
import java.util.ListIterator;

public class RequestListener implements Runnable, ElevatorListener, ActionListener{ 
	
	private ArrayList<Elevator> elevators = new ArrayList<Elevator>();
	private static int num_floors = 0;
	private static int num_elevators = 0;
	private static boolean[][] external_light;
	private static boolean[][] internal_light;
	public static String request = "";
	private static ArrayList<JButton> active_buttons = new ArrayList<JButton>();
	
	public RequestListener(ArrayList<Elevator> elevators, int floors) {
		this.elevators = elevators;
		num_floors = floors;
		num_elevators = elevators.size();
		external_light = new boolean [num_floors][2];
		internal_light = new boolean [num_elevators][num_floors];
	}


    @Override 
    public void run() { 

        /*while (true) {*/ 
            String floorNumberString = null;
            Buttons buttonType = null;
            int floorNumber = 0;
            Direction floorDirection = null;
            int elevator_name = 0;
            /*try {*/
            	if(request!="") {
	            	String[] request_items = request.split("");
	            	System.out.println(request_items[0]);
	            	System.out.println(request_items[1]);
	            	System.out.println(request_items[2]);
            	
	            	//thread wait until it gets the input of floor number and direction it has to go in
	                //BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in)); 
	            	//System.out.println("Is it an internal call or an external call? ");
	                buttonType =  getButton(request_items[2]);
	                
	                if(buttonType == Buttons.EXTERNAL)
	                {
	                	//System.out.println("Floor Number? ");
	                    floorNumberString = request_items[0];                 	
	                    floorNumber = Integer.parseInt(floorNumberString);
	                	//System.out.println("Direction");
	                    floorDirection = getFloorDir(request_items[1]); 
	                    /*
	                     * 
	                     *  isValidFloorNumber takes three parameters because it has to check:
	                     *  1. If the user is on bottom floor then the user can't go further down 
	                     *  2. If the user is on top floor then the user can't go further up
	                     *  3. If the door is open then if it pressed again the request for that floor isn't taken again
	                     */
	                    
	                    if(isValidExternalRequest(floorNumberString , floorDirection));
						{
							externalCall(floorNumber , floorDirection);
						}
	                }
	                
	                if(buttonType == Buttons.INTERNAL)
	                {
	                    floorNumberString = request_items[1];               	
	                    floorNumber = Integer.parseInt(floorNumberString);
	                	elevator_name = Integer.parseInt(request_items[0]);
	                    if(isValidInternalRequest(floorNumberString , elevator_name))
						{
							internalCall(floorNumber , elevators.get(elevator_name) );
						}               	
	                }
	                
	                /*Note: minus value will give error. Fix this later*/
                
            	}

                
            /*} catch (IOException e) { 
                //e.printStackTrace();
                System.out.println("Enter valid input!");
            }*/
            

            
        //} 
    }

    public void externalCall(int floorNumber , Direction floorDirection)
    {
       	/*
    	 * Turn ON floor Lights on here. No logic required for this since  external lights are bounded to the floors 
    	 * and not the elevator so it is definitely not an elevator property. But it get activated during a request
    	 * so it must get turned on here directly in the GUI.
    	 * 
    	 */
		 
		int dir = floorDirection==Direction.UP?0:1;
        setExternalLight(floorNumber, dir, true);
    	
    		
        System.out.println("You Pressed : " + floorNumber + " Direction: " + floorDirection.toString());
    	
        /*call the desired algorithm below*/
        //int chosen = Scheduling.randomCar(elevators.size());
        //int chosen = Scheduling.sectorAlgo(num_floors,elevators.size(), floorNumber);
        int chosen = Scheduling.nearestCar(elevators, num_floors, floorDirection, floorNumber);
        
        System.out.println("Elevator " + chosen + " was chosen");
        /* 
         * The addFloor methods:
         * Puts the requested floor in the RequestSet for THE CHOOSEN ELEVATOR
         * 
         */
        Elevator chosen_elevator = elevators.get(chosen);
        System.out.print("Elevator " + chosen_elevator.getName() + " ");
        chosen_elevator.serveFloor(floorNumber);
    }
    
    
    public void internalCall(int floorNumber , Elevator  chosen_elevator)
    {
       	/*
    	 * Turn ON floor Lights on here. No logic required for this since  external lights are bounded to the floors 
    	 * and not the elevator so it is definitely not an elevator property. But it get activated during a request
    	 * so it must get turned on here directly in the GUI.
    	 * 
    	 */
    	setInternalLight(chosen_elevator.getName(), floorNumber, true);
    	
    		
        System.out.println("You Pressed : " + floorNumber + " for elevator: " + chosen_elevator.getName());
        chosen_elevator.serveFloorInt(floorNumber);
        
    }
    
    private Direction getFloorDir(String request) {
    	
    	if(request.toLowerCase().equals("u")) {
    		return Direction.UP;
    	}
    	else if(request.equals(""))
    	{
    		return null;
    	}
		return Direction.DOWN;
	}


    private Buttons getButton(String button)
    {
    	if(button.toLowerCase().equals("i")){
    		return Buttons.INTERNAL;
    	}
    	
    	return Buttons.EXTERNAL;
    	
    }
  

    private boolean isValidExternalRequest(String floorNumber , Direction floorDirection) {
    	if(floorNumber == null)
    	{
    		System.out.println("Enter a floor number.");
    		return false;
    	}
    	else if(isIntNumber(floorNumber) != true)
    	{
    		System.out.println("Enter a valid integer number for floor number.");
    		return false;
    	}
    	else if( (Integer.parseInt(floorNumber) < 0) || ( Integer.parseInt(floorNumber) > (num_floors - 1)) )
    	{
    		System.out.println("There are " + num_elevators + " floors.");
    		System.out.println("There are numbered 0 to " + (num_floors - 1 ));
    		System.out.println("Please enter floor number within range.");
    		return false;    		
    	}
    	else if( (Integer.parseInt(floorNumber) == 0) && (floorDirection == Direction.DOWN )  )
    	{
    		System.out.println("Already on bottom floor, can't go further down");
    		return false;
    	}
    	else if( (Integer.parseInt(floorNumber) == (num_floors -1)) && (floorDirection == Direction.UP ))
    	{
    		System.out.println("Already on top floor, can't go further up");
    		return false;
    	}
    	else if((isDoorOnFloorOpenThenClosed(floorNumber)) == true )
    	{
        	
        	/*
        	 * Turn ON floor Lights on here. No logic required for this since  external lights are bounded to the floors 
        	 * and not the elevator so it is definitely not an elevator property. But it get activated during a request
        	 * so it must get turned on here directly in the GUI.
        	 * 
        	 */
    		return false;
    	}
    	else
    	{
            return true;    		
    	}
    }
    
    private boolean isValidInternalRequest(String floorNumber , int elevator_name) {
    	if(floorNumber == null)
    	{
    		System.out.println("Enter a floor number.");
    		return false;
    	}
    	else if(isIntNumber(floorNumber) != true)
    	{
    		System.out.println("Enter a valid integer number for floor number.");
    		return false;    
    	}
    	else if( (Integer.parseInt(floorNumber) < 0) || ( Integer.parseInt(floorNumber) > (num_floors - 1)) )
    	{
    		System.out.println("There are " + num_elevators + " floors.");
    		System.out.println("There are numbered 0 to " + (num_floors - 1 ));
    		System.out.println("Please enter floor number within range.");
    		return false;    		
    	}
    	else if( elevator_name < 0 || elevator_name > (num_elevators - 1))
    	{
    		System.out.println("There are " + num_elevators + " elevators.");
    		System.out.println("There are numbered 0 to " + (num_elevators - 1 ));
    		System.out.println("Please enter elevator number within range.");
    		return false;
    	}
    	else if(elevators.get(elevator_name).getInternalEnabled() ==  false)
    	{
    		System.out.println("Get into elevator first");
    		return false;
    	}
    	else
    	{
            return true;    		
    	}
    }

    
    private boolean isIntNumber(String s)
    {
    	try
    	{
    		Integer.parseInt(s);
    		return true;
    	}
    	catch(Exception e)
    	{
    		return false;
    	}
    }
    
    private boolean isDoorOnFloorOpenThenClosed(String floorNumber)
    {
    	 for (int i = 0 ; i < num_elevators ; i++){
    		 if( elevators.get(i).getCurrentFloor() == Integer.parseInt(floorNumber))
    			{
    			 	Elevator elevator = elevators.get(i);
    			    if(elevator.getDoorStatus() != Doors.CLOSED)
    			    	{
    			    		 return true;
    			    	}
    		 
    			}
    }
    	 return false;

    }
	
	public static void setExternalLight(int floor, int updown, boolean val) {
		//set for whole intermediate floors
		JButton dummy = new JButton();
		
		if(!val){
	    	ListIterator<JButton> litr = active_buttons.listIterator();
	        while (litr.hasNext()) {
	            JButton button = litr.next();
	            String ud = updown == 0? "u":"d";
	            if(button.getName().contains(floor+""+ud)) {
	            	button.setBackground(Color.gray);
	            	dummy = button;
	            }
	        }
		}
		
		external_light[floor][updown] = val;
		active_buttons.remove(dummy);
    	
    }
    
    public static boolean getExternalLight(int floor, int updown) {
    	return external_light[floor][updown];
    }


	@Override
	public void actionPerformed(ActionEvent e) {
		request = ((JButton)e.getSource()).getName();
		run();
		JButton button = ((JButton)e.getSource());
		if(request.contains("e")) {
			active_buttons.add(button);
			button.setBackground(Color.yellow);
		}
		if(request.contains("i")) {
			Elevator elev = elevators.get((int)(request.charAt(0))-48);
			if(elev.getInternalEnabled()) {
				active_buttons.add(button);
				button.setBackground(Color.yellow);
			}
		}
	}


	public static boolean getInternalLight(int i, int j) {
		return internal_light[i][j];
	}


	public static void setInternalLight(int i, int j, boolean val) {
		//if button in arraylist, set background to yellow else, gray
		JButton dummy = new JButton();
		if(!val) {
	    	ListIterator<JButton> litr = active_buttons.listIterator();
	        while (litr.hasNext()) {
	            JButton button = litr.next();
	            if(button.getName().contains(i+""+j)) {
	            	button.setBackground(Color.gray);
	            	dummy = button;
	            }
	        }
		}
		internal_light[i][j] = val;
		active_buttons.remove(dummy);
	}
    
}