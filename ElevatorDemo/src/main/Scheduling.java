package main;
import java.util.ArrayList;
import java.util.Random;
import java.util.TreeSet;

public class Scheduling {
	
	public static int nearestCar(ArrayList<Elevator> elevators, int num_floors, Direction dir, int floor) {
        
        int[] figures = new int[elevators.size()]; //array of figures of stability
        int max = 0; //max FS
        int elevatorNumber = -1; //index of elevator chosen to service request in ElevatorController.elevators array
        
        //calculates FS for every elevator
        for (int i = 0; i < elevators.size(); i++) {
            
            TreeSet<Double> requests = elevators.get(i).getRequestSet();
            double currentFloor = elevators.get(i).getCurrentFloor();
            
            if (requests.isEmpty()) {
                figures[i] = num_floors - Math.abs((int)Math.ceil(currentFloor) - floor) + 1;
            }
            else if ((dir == Direction.UP && (currentFloor <= floor)) ||
                    (dir == Direction.DOWN && (currentFloor >= floor))) {
                if (elevators.get(i).getDirection() == dir) {
                    figures[i] = num_floors - Math.abs((int)Math.ceil(currentFloor) - floor) + 2;
                }
                else if (elevators.get(i).getDirection() != dir) {
                    figures[i] = num_floors - Math.abs((int)Math.ceil(currentFloor) - floor) + 1;
                }
            }
            else if ((dir == Direction.UP && (currentFloor >= floor)) ||
                    (dir == Direction.DOWN && (currentFloor <= floor))) {
                figures[i] = 1;
            }
//            else {
//                System.out.println("This was an uncovered case:");
//                System.out.println("Elevator floor, direction - " + 
//                        elevators.get(i).getCurrentFloor() + ", " + 
//                        elevators.get(i).getDirection().toString());
//                System.out.println("Request floor, direction - " +
//                        floor + ", " + dir.toString());
//                figures[i] = 0;
//            }
            //System.out.println("Elevator " + elevators.get(i).getName() + " has set " + requests + " and FS " + figures[i]);
        }
        //chooses max FS
        for (int i = 0; i < elevators.size(); i++) {
            if (figures[i] > max) {
                max = figures[i];
                elevatorNumber = i;
            }
        }        
        return elevatorNumber;
    }
	
	public static int randomCar(int elevators) {
		
        Random rand = new Random();
        int elevatorNumber = rand.nextInt(elevators);
        return elevatorNumber;
        
	}
	
	public static int sectorAlgo(int num_floors, int num_elevators, int floor) {
		
		int floors_sector; //the number of floors each sector has
		int num_sector;  //the number of sectors, it is equal to the number of elevators here.
		int sectors[]; 
		//create the sectors array to store the highest floor the elevator can arrive
		//like sector[0] is equal to num_sector; means elevator0 could arrive from 0 to num_sector
		//sector[1] is equal to num_sector+sector[0]; 
		//means elevator1 could arrive from sector[0] to num_sector+sector[0] 
		int chosen = 0;
		
		//defining sectors
		num_sector= num_elevators;
		sectors = new int[num_sector];
		floors_sector = num_floors/num_elevators;
		sectors[0]=floors_sector;
		for(int i = 1;i<num_elevators-1;i++){
			sectors[i] = floors_sector+sectors[i-1]; 
		}
		sectors[num_elevators-1] = num_floors;
		
		if(floor==0) {
			return randomCar(num_elevators);
		}
	   
		for(int i=0;i<num_sector;i++){
			if(i==0){
				if(floor>0 && floor<=sectors[i]){
					return i;
				}
			}
			else
			{
			    if(floor<=sectors[i] && floor>sectors[i-1]){
				    return i;
			    }
			}
		}
		return chosen;
	   
	}

}
