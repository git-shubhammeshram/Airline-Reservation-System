package com.airlinereservation.utility;

public class Constants {
	
	public enum UserRole {
		ROLE_PASSENGER("PASSENGER"),
		ROLE_ADMIN("ADMIN");
		
		private String role;

	    private UserRole(String role) {
	      this.role = role;
	    }

	    public String value() {
	      return this.role;
	    }    
	}
	
	public enum UserStatus {
		ACTIVE("Active"),
		DEACTIVATED("Deactivated");
		
		
		private String status;

	    private UserStatus(String status) {
	      this.status = status;
	    }

	    public String value() {
	      return this.status;
	    }    
	}
	
	public enum FlightStatus {
		SCHEDULED("Scheduled"),
		ON_TIME("On Time"),
		DELAYED("Delayed"),
		CANCELED("Cancelled"),
		COMPLETED("Completed");
		
		
		private String status;

	    private FlightStatus(String status) {
	      this.status = status;
	    }

	    public String value() {
	      return this.status;
	    }    
	}
	
	public enum FlightBookingStatus {
		CONFIRMED("Confirmed"),
		PENDING("Pending"),
		CANCELLED("Cancelled"),
		WAITING("Waiting"),
		AVAILABLE("Available");
		
		private String status;

	    private FlightBookingStatus(String status) {
	      this.status = status;
	    }

	    public String value() {
	      return this.status;
	    }    
	}
	
	public enum FlightClassType {
		ECONOMY("Economy"),
		BUSINESS("Business"),
		FIRST_CLASS("First Class");
		
		private String type;

	    private FlightClassType(String type) {
	      this.type = type;
	    }

	    public String value() {
	      return this.type;
	    }    
	}
	
	public enum AirplaneStatus {
		ACTIVE("Active"),
		DEACTIVATED("Deactivated");
		
		
		private String status;

	    private AirplaneStatus(String status) {
	      this.status = status;
	    }

	    public String value() {
	      return this.status;
	    }    
	}

}
