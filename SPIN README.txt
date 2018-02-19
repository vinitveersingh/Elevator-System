1. Main method --> active proctype Main() 
2. Contrainted to 2 elevators and 10 floors
3. Flow of methods
   Main is called.
   Main calls
   	run Motor0(); --> calls nextFloor method --> puts elevator in wait state initally 
	                  since requestSet for elevator 0 is empty
	run Motor1(); --> does same as Motor0 for elevator 1
	run RequestListener(); 
	--> Takes input for both internal and external requestSet 
	--> Our system made in Java restricts internal calls until very first external request is completed for
      	an elevator. This behaviour has not been checked by spin to show interleaving of internal and external
		request.
4. How requests are to be made:
   Eg:
	   atomic{
		     request.requestedFloor = 0;
			 printf("\nRequest for floor : %d" , request.requestedFloor);
			 request.requestTypeExtenal[request.requestedFloor]  = 1;	    
		     Scheduler();
		     serveFloor(); 		   
	   }   
	This shows external request.
	
	Eg:
		atomic{
		     request.requestedFloor = 3;
			 printf("Request for floor : %d" , 3);
			 request.requestTypeExtenal[request.requestedFloor]  = 0;	    
			 selected_elevator = 0;
		     serveFloor(); 		   
	   }
	This shows internal request for elevator 0.

5. Please run code multiple times to see interleaving behaviour.
	