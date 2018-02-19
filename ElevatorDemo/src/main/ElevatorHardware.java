package main;

public interface ElevatorHardware 
{
	/*
	 // start the elevator moving up
	  void startUp(int elId);

	  void startDown(int elId);
	 */
	
	  void setCurrentFloor(double floor) throws InterruptedException;
	  /*
	  // place a stop at a specified floor.  The elevator will stop
	  // when it reaches that floor
	  void placeStop(int elId, int floor);
	  */
	  
	  void serveFloor(double floor);
	  // start opening the doors
	  void openDoors();

	  void closeDoors();

	  /*
	 // turn on the light on that button
	  void turnOnInternalLight(int elId, int floor);

	  void turnOffInternalLight(int elId, int fllor);

	  void turnOnExternalLight(int floor, boolean isUp);

	  void tunOffExternalLight(int floor, boolean isUp);
	  */
	
}
