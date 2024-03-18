package com.airlinereservation.service;

import java.util.List;

import com.airlinereservation.entity.User;

public interface UserService {
	
	User registerUser(User user);
	User updateUser(User user);
	User getUserById(int userId);
	User getUserByEmailAndPassword(String email, String password);
	User getUserByEmailAndPasswordAndRoles(String email, String password, String role);
	User getUserByEmail(String email);
	User getUserByEmailAndRoles(String email, String roles);
	List<User> getUsersByRolesAndStatus(String role, String status);
	List<User> getUserByRoles(String role);

}
