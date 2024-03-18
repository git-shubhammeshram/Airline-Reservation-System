package com.airlinereservation.controller;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.airlinereservation.dto.AddWalletMoneyRequestDto;
import com.airlinereservation.dto.CommonApiResponse;
import com.airlinereservation.dto.RegisterUserRequestDto;
import com.airlinereservation.dto.UserListResponseDto;
import com.airlinereservation.dto.UserLoginRequest;
import com.airlinereservation.dto.UserLoginResponse;
import com.airlinereservation.dto.UserStatusUpdateRequestDto;
import com.airlinereservation.entity.User;
import com.airlinereservation.resource.UserResource;
import com.airlinereservation.service.UserService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("api/user/")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

	@Autowired
	private UserResource userResource;

	@Autowired
	private UserService userService;
	
	// for customer and bank register
	@PostMapping("register")
	@Operation(summary = "Api to register customer or bank user")
	public ResponseEntity<CommonApiResponse> registerUser(@RequestBody RegisterUserRequestDto request) {
		return this.userResource.registerUser(request);
	}

	// RegisterUserRequestDto, we will set only email, password & role from UI
	@PostMapping("/admin/register")
	@Operation(summary = "Api to register admin")
	public ResponseEntity<CommonApiResponse> registerAdmin(@RequestBody RegisterUserRequestDto request) {
		return userResource.registerAdmin(request);
	}

	@PostMapping("login")
	@Operation(summary = "Api to login any User")
	public ResponseEntity<UserLoginResponse> login(@RequestBody UserLoginRequest userLoginRequest) {
		return userResource.login(userLoginRequest);
	}

	@GetMapping("/fetch/role")
	@Operation(summary = "Api to get Users By Role")
	public ResponseEntity<UserListResponseDto> fetchAllUsersByRole(@RequestParam("role") String role) {
		return userResource.getUsersByRole(role);
	}

	@PostMapping("update/status")
	@Operation(summary = "Api to update the user status")
	public ResponseEntity<UserLoginResponse> updateUserStatus(@RequestBody UserStatusUpdateRequestDto request) {
		return userResource.updateUserStatus(request);
	}

	@PostMapping("add/wallet/money")
	@Operation(summary = "Api to add wallet money")
	public ResponseEntity<CommonApiResponse> addMoneyInWallet(@RequestBody AddWalletMoneyRequestDto request) {
		return userResource.addMoneyInWallet(request);
	}

	@GetMapping("passenger/wallet/fetch")
	@Operation(summary = "Api to fetch user wallet money")
	public ResponseEntity<String> getCustomerWallet(@RequestParam("userId") int userId) {
		
		User user = userService.getUserById(userId);
		
		String walletAmount = "0.0";
		
		if(user.getWalletAmount().compareTo(BigDecimal.ZERO) == 0) {
			return new ResponseEntity<>(walletAmount, HttpStatus.OK);
		}
		
		else {
			return new ResponseEntity<>(String.valueOf(user.getWalletAmount()), HttpStatus.OK);
		}
		
	}

}
