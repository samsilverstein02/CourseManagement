package authenticatedUsers;

import SystemState.System_State;
import authenticationServer.AuthenticationToken;
import offerings.CourseOffering;
import offerings.OfferingFactory;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;


public class LoggedInAdmin implements LoggedInAuthenticatedUser {

	private String name;
	private String surname;
	private String ID;
	private AuthenticationToken authenticationToken;
	
	//a constructor for a logged in admin object 
	//attributes will be made with setter functions, after the user is created 
	public LoggedInAdmin(){
		this.name = "";
		this.surname = "";
		this.ID = "";
		this.authenticationToken = null;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getSurname() {
		return surname;
	}
	
	public void setSurname(String surname) {
		this.surname = surname;
	}
	
	public String getID() {
		return ID;
	}
	
	public void setID(String iD) {
		ID = iD;
	}
	
	public AuthenticationToken getAuthenticationToken() {
		return authenticationToken;
	}
	
	public void setAuthenticationToken(AuthenticationToken authenticationToken) {
		this.authenticationToken = authenticationToken;
	}
	
	//a function which starts the system
	public void StartSystem() {
		if(System_State.state == 1) { //if already in started state, just inform the console, nothing needs to be done after
			System.out.println("System is already in a started state");
			return;
		}
		System_State.state = 1; //if not in started state, then it puts the system in a started state
		System.out.println("System has been started");
	}
	
	//a function which puts the system in a stopped state
	public void StopSystem() { 
		System_State.state = 0;
		System.out.println("System stoppage, you have been logged out and no operations can be performed until started again");
	}
	
	//a function which reads course files, given the input by the user, which is the text file 
	public void readCourseFiles() {
		BufferedReader br = null;
		BufferedReader reader = null;
		try {
			br =  new BufferedReader(new InputStreamReader(System.in));
			System.out.println("Enter course file here: ");
			String fileName = br.readLine();
			reader = new BufferedReader(new FileReader(fileName));
			OfferingFactory factory = new OfferingFactory();
			CourseOffering course = factory.createCourseOffering(reader);
			System.out.println("Course file has been read succesfully");
			
		} catch(IOException e) {
			System.out.println("File does not exist");
		}
		
	}
	
	
	 
}
