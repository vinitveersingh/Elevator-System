The system can be run using the code staright from IDE or from command-line using the provided JAR file.

1. For running using IDE: Use "Run Configurations" to specify command-line arguments. 
2. For running using JAr: simply pass command line arguments as positive integers. (first number of floors, seccond number of elevators)
Note**The software is fixed width, so for the best visual simiulation it is recommended to use 10 floors and 5 elevators.

Once the system is initialized, you can click any buttons labeled as "^" and "v" to request elevators externally.
Internal buttons are implemented on a separete panel (on the right of main panel and label with floor numbers) that
takes clicks only after an elevator is activated for internal requests, which it after it serves it's very first request.

You can see the simulation in the GUI, as well as console outputs.

You can swith between algorithms by uncommenting/commenting any line in RequestListener class (staring line 112).
//int chosen = Scheduling.randomCar(elevators.size());
//int chosen = Scheduling.sectorAlgo(num_floors,elevators.size(), floorNumber);
int chosen = Scheduling.nearestCar(elevators, num_floors, floorDirection, floorNumber);