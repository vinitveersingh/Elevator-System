chan synch = [0] of {int};
int num_elevator = 2;
int num_floors = 10;
int selected_elevator;
int floor;
int floor1;
int empty2 = 1;
int empty1 = 1;
int d1 = 0; //close
int d2 =0 ; //close
typedef TreeSet
{
	int requestSet[10] = -1; //-1 = no request
}

TreeSet TreeSetArray[num_elevator];

typedef DirectionFloor 
{
	int current = 0;
	int direction = 0; //0 is up //1 is down  
}

inline seeRequestSet()
{
	int a = 0;
	for( a: 0 .. (num_floors -1) ){
	 if
	 :: (TreeSetArray[0].requestSet[a] != -1) --> {
		 empty2 = 0;
	     break;
		}
	 :: else -->
	    empty2 = 1;
	 fi
	 //printf("\nTreeSet position value: %d %d" , a, TreeSetArray[0].requestSet[a] );
	}

}

inline seeRequestSet1()
{
	int a = 0;
	for( a: 0 .. (num_floors -1) ){
	 if
	 :: (TreeSetArray[1].requestSet[a] != -1) --> {
		 empty1 = 0;
	     break;
		}
	 :: else -->
	    empty1 = 1;
	 fi
	 //printf("\nTreeSet position value: %d %d" , a, TreeSetArray[1].requestSet[a] );
	}

}

DirectionFloor DirectionFloorArray[num_elevator];

typedef Request
{
	int requestedFloor;
	int requestTypeExtenal[10] = -1; //-1 off //1 up //0 down //10 on
}

Request request;

inline Scheduler()
{	
	select(selected_elevator : 0 .. (num_elevator - 1));	
}

inline serveFloor()
{
	if
	:: selected_elevator == 0 --> 
	   TreeSetArray[0].requestSet[request.requestedFloor] = request.requestedFloor;
	   synch!0;
	:: selected_elevator == 1 --> 
	   TreeSetArray[1].requestSet[request.requestedFloor] = request.requestedFloor;
	   synch!1;
	:: else
	fi
}	

inline ceiling0()
{

    int k = DirectionFloorArray[0].current;
	for( k : DirectionFloorArray[0].current .. (num_floors - 1)){	
     if
     :: (TreeSetArray[0].requestSet[k] != -1 ) -->{
		 floor = TreeSetArray[0].requestSet[k];
	     break;
	    } 
     :: else		
	 fi
	}  
    //printf("\nDone Ceiling");	

}

inline ceiling1()
{

    int k = DirectionFloorArray[1].current;
	for( k : DirectionFloorArray[1].current .. (num_floors - 1)){	
     if
     :: (TreeSetArray[1].requestSet[k] != -1 ) -->{
		 floor1 = TreeSetArray[1].requestSet[k];
	     break;
	    } 
     :: else		
	 fi
	}  
    //printf("\nDone Ceiling");	

}

inline floorFun1()
{
    int l = DirectionFloorArray[1].current;
	do
	:: l >= 0 -->
		if
		:: (TreeSetArray[1].requestSet[l] != -1) --> 
		    floor1 = TreeSetArray[1].requestSet[l];
            break;
        :: else
        fi		
	    l = l - 1;
	:: else --> break;
	od
}

inline floorFun0()
{
    int l = DirectionFloorArray[0].current;
	do
	:: l >= 0 -->
		if
		:: (TreeSetArray[0].requestSet[l] != -1) --> 
		    floor = TreeSetArray[0].requestSet[l];
            break;
        :: else
        fi		
	    l = l - 1;
	:: else --> break;
	od
}

inline nextFloor()
{   atomic{
    floor = -1;
	   if
	   :: DirectionFloorArray[0].direction == 0 -->
	        printf("\nUP");
       	    ceiling0();
			printf("Floor%d" , floor);
			if 
			:: floor == -1 -->
			   floorFun0();
			:: else
			fi
	   :: DirectionFloorArray[0].direction == 1 --> 
      	    floorFun0();
			if 
			:: floor == -1 -->
			   ceiling0();
			:: else
			fi
	   :: else
	   fi	 
	   if
       :: floor == - 1 -->
	   	  synch?0;	      
	   :: else -->
	      TreeSetArray[0].requestSet[DirectionFloorArray[0].current] = -1;
       fi

	}
}

inline nextFloor1()
{   atomic{
    floor1 = -1;
	   if
	   :: DirectionFloorArray[1].direction == 0 -->
	        printf("\nUP");
       	    ceiling1();
			printf("Floor%d" , floor1);
			if 
			:: floor1 == -1 -->
			   floorFun1();
			:: else
			fi
	   :: DirectionFloorArray[1].direction == 1 --> 
      	    floorFun1();
			if 
			:: floor1 == -1 -->
			   ceiling1();
			:: else
			fi
	   :: else
	   fi	 
	   if
       :: floor1 == - 1 -->
	   	  synch?1;	      
	   :: else -->
	      TreeSetArray[1].requestSet[DirectionFloorArray[0].current] = -1;
       fi
	}
}
proctype Motor0() 
{ 
    do
    :: printf("\nElevator 0 waiting. \n");
       seeRequestSet();	
       nextFloor();	   
	   int currentFloor = DirectionFloorArray[0].current;
	   if
	   :: floor != -1 -->
	   if
	   ::  currentFloor < floor - 1 -->
	       for(currentFloor : DirectionFloorArray[0].current .. floor - 1  ){
		     printf("Elevator 0 Current Floor %d\n" , DirectionFloorArray[0].current);
		     DirectionFloorArray[0].current = DirectionFloorArray[0].current + 1;				 
	         printf("Up");
             DirectionFloorArray[0].direction = 0;
			}			
		   d1 = 1;	//Door Open
		   printf("Elevator 0 Current Floor %d\n" , DirectionFloorArray[0].current);
	       TreeSetArray[0].requestSet[DirectionFloorArray[0].current] = -1;
		   d1 = 0; // Door Close
	   :: (currentFloor > floor) -->
	       do 
		   ::  floor + 1 <= currentFloor --> {
		        if 
				:: DirectionFloorArray[0].current == 0 -->
				   break;
				:: else -->
		           printf("Elevator 0 Current Floor %d\n" , currentFloor);
                   DirectionFloorArray[0].current = DirectionFloorArray[0].current - 1;
	               printf("Down");				
                   DirectionFloorArray[0].direction = 1;
				   currentFloor = currentFloor - 1;
				fi
               }
		   ::  else --> break; 
		   od
		       d1 = 1; //Door Open
			   printf("Elevator 0 Current Floor : %d\n" , DirectionFloorArray[0].current);		   
	           TreeSetArray[0].requestSet[DirectionFloorArray[0].current] = -1;
			   d1 = 0; //Door Close
	   :: else 
	   fi
	   :: else
	   fi
	   if
	   :: empty2 == 0 -->
	      printf("Not Empty");
	   :: empty2 == 1 -->
	   	  emp:
		  lightsoff:
	      printf("Empty")
	   :: else
	   fi	  
       if
       :: d1 == 0 -->
	      printf("Door close 0");
		  doorclose:		  
       :: d1 == 1 -->
          printf("Door Open 0");
       :: else
	   fi
    od
}

proctype Motor1() 
{ 

    do
    :: printf("\nElevator 1 waiting. \n");
       nextFloor1();
	   int currentFloor = DirectionFloorArray[1].current;
	   if
	   :: floor1 != -1 -->
	   if
	   ::  currentFloor < floor1 - 1 -->
	       for(currentFloor : DirectionFloorArray[1].current .. floor1 - 1  ){
		     printf("Elevator 1 Current Floor %d\n" , DirectionFloorArray[1].current);
		     DirectionFloorArray[1].current = DirectionFloorArray[1].current + 1;			 
	         printf("Up");
             DirectionFloorArray[1].direction = 0;
			}			
		   printf("Elevator 1 Current Floor %d\n" , DirectionFloorArray[1].current);
		   d2 = 1; //door open 
	       TreeSetArray[1].requestSet[DirectionFloorArray[1].current] = -1;
		   d2 = 0; //door close
	   :: (currentFloor > floor1) -->
	       do 
		   ::  floor1 + 1 <= currentFloor --> {
		        if 
				:: DirectionFloorArray[1].current == 0 -->
				   break;
				:: else -->
		           printf("Elevator 1 Current Floor %d\n" , currentFloor);
                   DirectionFloorArray[1].current = DirectionFloorArray[1].current - 1;
	               printf("Down");				
                   DirectionFloorArray[1].direction = 1;
				   currentFloor = currentFloor - 1;
				fi
               }
		   ::  else --> break; 
		   od
		   d2 = 1; //door open 
		   printf("Elevator 1 Current Floor : %d\n" , DirectionFloorArray[1].current);		   
	       TreeSetArray[1].requestSet[DirectionFloorArray[1].current] = -1;
		   d2 = 0; //door close
	   :: else 
	   fi
	   :: else
	   fi
	   if
	   :: empty1 == 0 -->
	      printf("Not Empty");
	   :: empty1 == 1 -->
	   	  emp1:
		  lightsoff1:
	      printf("Empty")
	   :: else
	   fi	
       if
       :: d2 == 0 -->
          printf("Door close.");
		  doorclose1:		  
	   :: d2 == 1 -->
	      printf("Door Open.");
	   :: else
	   fi
    od

}

proctype RequestListener()
{

	   atomic{
		     request.requestedFloor = 3;
			 printf("Request for floor : %d" , 3);
			 request.requestTypeExtenal[request.requestedFloor]  = 0;	    
			 selected_elevator = 0;
		     serveFloor(); 		   
	   }
	   atomic{
		     request.requestedFloor = 8;
			 printf("\nRequest for floor : %d" , request.requestedFloor);
			 request.requestTypeExtenal[request.requestedFloor]  = 0;	    
			 selected_elevator = 0;
		     serveFloor(); 		   
	   }
	   atomic{
		     request.requestedFloor = 5;
			 printf("\nRequest for floor : %d" , request.requestedFloor);
			 request.requestTypeExtenal[request.requestedFloor]  = 1;	    
			 selected_elevator = 1;
		     serveFloor(); 		   
	   }		
	   atomic{
		     request.requestedFloor = 3;
			 printf("\nRequest for floor : %d" , request.requestedFloor);
			 request.requestTypeExtenal[request.requestedFloor]  = 1;	    
			 selected_elevator = 0;
		     serveFloor(); 		   
	   }
	   atomic{
		     request.requestedFloor = 3;
			 printf("\nRequest for floor : %d" , request.requestedFloor);
			 request.requestTypeExtenal[request.requestedFloor]  = 1;	    
			 selected_elevator = 1;
		     serveFloor(); 		   
	   }
	   atomic{
		     request.requestedFloor = 8;
			 printf("\nRequest for floor : %d" , request.requestedFloor);
			 request.requestTypeExtenal[request.requestedFloor]  = 1;	    
		     Scheduler();
		     serveFloor(); 		   
	   }	   
	   atomic{
		     request.requestedFloor = 0;
			 printf("\nRequest for floor : %d" , request.requestedFloor);
			 request.requestTypeExtenal[request.requestedFloor]  = 1;	    
			 selected_elevator = 1;
		     serveFloor(); 		   
	   }
	   
}

active proctype Main() 
{
	run Motor0();
	run Motor1();
	run RequestListener();
}

#define w0 Motor0@emp
#define w1 Motor0@lightsoff
#define w2 Motor0@doorclose
#define w3 Motor1@emp1
#define w4 Motor1@doorclose1
#define w5 Motor1@lightsoff1

ltl isEmpty1 {(<>w0) && (<>w1) && (<>w2)};
ltl isEmpty2 {(<>w3) && (<>w4) && (<>w5)};