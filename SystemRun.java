package SystemRun;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;

import SystemState.System_State;
import authenticationServer.AuthenticationToken;
import loggedInUserFactory.LoggedInUserFactory;
import offerings.CourseOffering;
import offerings.ICourseOffering;
import registrar.ModelRegister;
import systemUsers.StudentModel;
import authenticatedUsers.LoggedInAuthenticatedUser;
import authenticatedUsers.LoggedInInstructor;
import authenticatedUsers.LoggedInStudent;
import authenticatedUsers.LoggedInAdmin;

public class SystemRun {
	
	public static void main(String args[]) {
		checkLogin();	//Make sure that the user presses L to log in
		while(System_State.state == 1) {
			String [] userInfo = logInUser(); //Log the user in 
			AuthenticationToken token = new AuthenticationToken(userInfo[3]);
			LoggedInUserFactory factory = new LoggedInUserFactory();
			LoggedInAuthenticatedUser user = factory.createAuthenticatedUser(token);
			user.setName(userInfo[0]);
			user.setSurname(userInfo[1]);
			user.setID(userInfo[2]);
			//Here I want to print a text file to the console, if not then i will make a string notifying what operations they can do as this role
			if(user.getID().charAt(0) == '0') {
				printOp("AuthAdminOperations.txt");
				LoggedInAdmin authenticatedAdmin = (LoggedInAdmin) user;
				adminOperations(authenticatedAdmin); //function which perform the admin operations 
			}
			else if(user.getID().charAt(0) == '1') {
				printOp("AuthInsturctorOperations.txt");
				LoggedInInstructor authenticatedInstructor = (LoggedInInstructor) user;
				break;
				//use instructor operations
			}
			else if(user.getID().charAt(0) == ('2')) {
				printOp("AuthStudentOperations.txt");
				LoggedInStudent authenticatedStudent = (LoggedInStudent) user;
				studentOperations(authenticatedStudent);
				break;
				//use student operations 
			}
			else 
				System.out.println("Error"); //change this to an error text file 
		}
		
		exitMessage();
	}
	
	
	
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
	
	private static void adminOperations(LoggedInAdmin user) {
		boolean login = true;
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
	
	private static void studentOperations(LoggedInStudent user) {
		boolean login = true;
		StudentModel student = (StudentModel) ModelRegister.getInstance().getRegisteredUser(user.getID());
		if (student == null) {
			System.out.println("That ID is not valid");
			return;
		}
		while(login && System_State.state == 1) { //while the user is logged in, and the system is in a running state, they are able to pe
			BufferedReader br = null; 	
			try {
				 br = new BufferedReader(new InputStreamReader(System.in));
				 String input = br.readLine();
				 switch(input) {
				 case "1":
					 System.out.println("Enter the course ID of the course you want to enroll in?");
					 String option = br.readLine();
					 if (!student.getCoursesAllowed().isEmpty()) {
						 for (ICourseOffering course : student.getCoursesAllowed()) {
							 if (course.getCourseID().equals(option)) {
								 if(student.enroll((CourseOffering) course))
									 System.out.print("Enrollment successful!");
								 else
									 System.out.println("Erollment failed. You are not allowed to take that course.");
								 break;
							 }
						 }
					 }
					 System.out.println("You are not allowed to enroll in that course.");
					 break;
				 
				 case "2":
					 System.out.println("1 = Notifications on");
					 System.out.println("0 = Notifications off");
					 int notificationStatus = Integer.parseInt(br.readLine());
					 if (notificationStatus == 0 || notificationStatus == 1) {
						 student.setNotificationStatus(notificationStatus);
						 if (notificationStatus == 0)
							 System.out.println("Notifications turned off");
						 else
							 System.out.println("Notifications turned on");
					 }
					 else
						 System.out.println("That is not a valid option");
					 break;
					 
//				 case "3": 
//					 user.readCourseFiles();
//					 System.out.println("Course file updated");
//					 break;
//				 case "Logout":
//					 login = false;
//					 System.out.println("You have been logged out");
//					 break;
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
	
	private static void exitMessage() { //writes an exit method, given how it reaches the end of the 
		if(System_State.state == 0)
			System.out.println("Session terminated");
		else
			System.out.println("Logged out");
	}
	
	
}
