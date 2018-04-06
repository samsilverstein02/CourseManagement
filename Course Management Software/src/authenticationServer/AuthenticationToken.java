package authenticationServer;

import java.io.IOException;
import java.util.Date;

public class AuthenticationToken {

	private String userType;
	private Integer tokenID;
	private Integer SessionID;
	private static Integer count = 0; //a static variable, for each authentication token made 
	
	//a constructor for the authentication token 
	//takes in parameter of the user type, so it is not set later 
	public AuthenticationToken(String userType) {
		this.userType = userType;
		this.tokenID = getNextTokenID(); //sets it, then it will increment it by 1 for the next unique token ID 
		this.SessionID = (int) new Date().getTime(); //session ID is the time in which the login occures 
	}

	public String getUserType() {
		return this.userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}
	public Integer getTokenID() {
		return tokenID;
	}
	public void setTokenID(Integer tokenID) {
		this.tokenID = tokenID;
	}
	public Integer getSessionID() {
		return SessionID;
	}
	public void setSessionID(Integer sessionID) {
		SessionID = sessionID;
	}
	
	//a static variable, which increments, so each time a new authentication token is made, it creates a uniue token ID which increments by 1 each time 
	private static Integer getNextTokenID(){
		count++;
		return count;
	}
	
}
