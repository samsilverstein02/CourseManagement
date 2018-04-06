package SystemRun;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;

import SystemState.System_State;
import authenticationServer.AuthenticationToken;
import loggedInUserFactory.LoggedInUserFactory;
import authenticatedUsers.LoggedInAuthenticatedUser;
import authenticatedUsers.LoggedInInstructor;
import authenticatedUsers.LoggedInStudent;
import authenticatedUsers.LoggedInAdmin;

//This class represents the main function which initiates the login, and the entire session for the user, while in a started state
public class SystemRun {
	static boolean login = true;
	public static void main(String args[]) {
		while(login) {
			checkLogin();	//Make sure that the user presses L to log in
			String [] userInfo = logInUser(); //Get all necessary information in array userInfo
			AuthenticationToken token = new AuthenticationToken(userInfo[3]); //create new authenticationToken, based on the user type, given by the array
			//create a factory, and build the logged in user, based on the authentication token made 
			LoggedInUserFactory factory = new LoggedInUserFactory();
			LoggedInAuthenticatedUser user = factory.createAuthenticatedUser(token);
			user.setName(userInfo[0]);
			user.setSurname(userInfo[1]);
			user.setID(userInfo[2]);
			//Depending on what kind of user they are, it will print to the console operations they can perform, and the corresponding keys to invoke them
			//it then casts the user to the object of that loggedIn type,
			//and then runs a function which invokes the procedure of performing the various operation, that user is able to perform
			if(user.getID().charAt(0) == '0') {
				if(System_State.state == 1)
					printOp("AuthAdminOperations.txt"); 
				else
					printOp("StoppedOperation.txt");
				
				LoggedInAdmin authenticatedAdmin = (LoggedInAdmin) user; 
				adminOperations(authenticatedAdmin); //function which perform the admin operations 
			}
			else if(user.getID().charAt(0) == '1' && System_State.state == 1) {
				printOp("AuthInsturctorOperations.txt");
				LoggedInInstructor authenticatedInstructor = (LoggedInInstructor) user;
				//use instructor operations
			}
			else if(user.getID().charAt(0) == ('2') && System_State.state == 1) {
				printOp("AuthStudentOperations.txt");
				LoggedInStudent authenticatedStudent = (LoggedInStudent) user;
				//use student operations 
			}
			else {
				System.out.println("Error: System is in stopped state, you cannot login at this time"); 
			}
				
		}
			exitMessage(); //inform user they have been logged out 
	}
	
	
//A function which asks the user for their login information, and creates an array which contains their four pieces of identification	
	private static String[] logInUser() {
		String [] info = new String[4];
		 BufferedReader br = null;
	
	       try {
	    	   	   boolean ID = false;
	           br = new BufferedReader(new InputStreamReader(System.in));
	           	System.out.println("Enter first name: ");
	               String input = br.readLine();
	               info[0] = input;
	               System.out.println("Enter last name: ");
	               input = br.readLine();
	               info[1] = input;
	              
	               //depending on what the user inputs as the first digit of their ID, it will store the type of user in the last index of the array 
	               //if they do not enter 0,1,2, then it will continue to prompt them until an appropriate ID is entered
	               while(!ID) {
		               System.out.println("ID's beggining with 0: Admin, 1:Instructor, 2:Student \nEnter ID: ");
		               input = br.readLine();
		               if(input.charAt(0) ==  '0' || input.charAt(0) == '1' || input.charAt(0) == '2') {
		            	   		info[2] = input;
		            	   		ID = true;
		               }
		               else
		            	   		System.out.println("Wrong ID, please try again");
	               }   
	           //switch statement which creates and adds the type of user 
	            switch(info[2].charAt(0)) {
		       		case '0':
		       			info[3] = "Admin";
		       			break;
		       		case '1':
		       			info[3] = "Instructor";
		       			break;
		       		case '2': 
		       			info[3] = "Student";
		       			break;
	       		}
	              
	       } catch (IOException e) {
	           e.printStackTrace();
	       }
	       return info; 
	}
	
//a function which prompts the user to initiate the login
//they will enter L to initiate the login procedure, or will keep prompting them until they enter the right key 
	private static void checkLogin() {
		BufferedReader br = null;
		boolean log = false;
		 try {
			 br = new BufferedReader(new InputStreamReader(System.in));
			 	while(!log){
			 		System.out.println("To begin login, press 'L'");
			 		String input = br.readLine();
			 		if(input.equals("L"))
			 			log = true;
			 		else
			 			System.out.println("Invalid key, please try again.");
			 	}
		                
	     } catch (IOException e) {
	           e.printStackTrace();
	       }
	}
	
	private static void printOp(String file) {
		try {
			System.out.println(new String(Files.readAllBytes(Paths.get(file))));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//this is a function which allows the logged in user to perform their appropriate operation
	//it invokes methods, in the loggedInAdmin class, depending on what is inputted in the console 
	private static void adminOperations(LoggedInAdmin user) {
		boolean login = true;
		while(login && System_State.state == 0) {
			BufferedReader br = null; 	
			try {
				 br = new BufferedReader(new InputStreamReader(System.in));
				 String input = br.readLine();
				 switch(input) {
				 case "1":
					 user.StartSystem();
					 break;
				 
				 default:
					 System.out.println("Cannot perform any operation until system is started");
			
				 }
			}
				 catch (IOException e) {
			           e.printStackTrace();
			       }
			
		}
		while(login && System_State.state == 1) { //while the user is logged in, and the system is in a running state, they are able to pe
			BufferedReader br = null; 	
			try {
				 br = new BufferedReader(new InputStreamReader(System.in));
				 String input = br.readLine();
				 switch(input) {
				 case "1":
					 user.StartSystem();
					 break;
				 
				 case "2": //put a print statement at the end of the main function, once 
					 user.StopSystem();
					 break;
					 
				 case "3": 
					 user.readCourseFiles();
					 System.out.println("Course file updated");
					 break;
				 case "Logout":
					 login = false;
					 System.out.println("You have been logged out");
					 break;
				 default: 
					 System.out.println("Invalid key entry, please try again");
					 break;
				 }	
			}
			catch (IOException e) {
		           e.printStackTrace();
		       }
			
		}
	}
	
	//once the user logs out, this is printed right before it exits the main function
	//it is an exit method which informs the user they have been logged out
	private static void exitMessage() { //writes an exit method, given how it reaches the end of the 
		if(System_State.state == 0)
			System.out.println("Session terminated");
		else
			System.out.println("Logged out");
	}
	
	
}
