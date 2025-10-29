package com.nt.rest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.nt.dto.AdminCreateDto;
import com.nt.dto.AdminUpdateDto;
import com.nt.dto.FundAddDto;
import com.nt.dto.FundResponseDto;
import com.nt.dto.LoginDto;
import com.nt.dto.PageResponseDto;
import com.nt.dto.TotalFundResponseDto;
import com.nt.dto.TransactionDetailsDto;
import com.nt.dto.TransactionDto;
import com.nt.dto.UserDeleteDto;
import com.nt.dto.UserRegisterDto;
import com.nt.dto.UserResponseDto;
import com.nt.service.IUserMgmtService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
public class UserController {
	
	@Autowired
	private IUserMgmtService userService;
	
	@PostMapping("/login")
	public ResponseEntity<Map<String, String>> login(@Valid @RequestBody LoginDto loginDto) {
	    // Authenticate and generate JWT
	    String token = userService.verifyUser(loginDto);
	    // Prepare response
	    Map<String, String> response = new HashMap<>();
	    response.put("token", token);
	    response.put("userName", loginDto.getUserName());  // send username
	            

	    return new  ResponseEntity<>(response,HttpStatus.OK);
	}
	
	@GetMapping("/profile")
	public ResponseEntity<UserResponseDto> showProfile(Authentication auth){
		
		
		System.out.println(auth.getName());
		UserResponseDto dto=userService.getProfile(auth.getName());
		return new ResponseEntity<UserResponseDto>(dto,HttpStatus.OK);
	}
	
	@PostMapping("/register")
	public ResponseEntity<UserResponseDto> registerUser(@Valid @RequestBody UserRegisterDto registerDto){
		
		UserResponseDto responseDto=userService.registerUser(registerDto);
		
		return new ResponseEntity<UserResponseDto>(responseDto,HttpStatus.CREATED);
		
	}
	
	@PostMapping("/guest")
    public ResponseEntity<Map<String, Object>> loginGuest(HttpServletRequest request) {
        
		
		Map<String ,Object> response=userService.loginAsGuest( request);
		
        return new ResponseEntity<Map<String,Object>>(response,HttpStatus.CREATED);
    }
	
	@PreAuthorize("hasAnyRole('ADMIN','USER')")
	@PostMapping("/add")
	public ResponseEntity<UserResponseDto> addMember(@Valid @RequestBody AdminCreateDto adminDto){
		
        UserResponseDto responseDto=userService.addMember(adminDto);
		
		return new ResponseEntity<UserResponseDto>(responseDto,HttpStatus.CREATED);
		
		
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/update/{userId}")
	public ResponseEntity<UserResponseDto> updateMember(@Valid @RequestBody AdminUpdateDto adminDto,@PathVariable String userId){
		
        UserResponseDto responseDto=userService.updateMember(adminDto,userId);
		
		return new ResponseEntity<UserResponseDto>(responseDto,HttpStatus.CREATED);

		
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/delete/{userId}")
	public ResponseEntity<UserDeleteDto> deleteMember(@PathVariable String userId) {
		UserDeleteDto responseDto = userService.deleteMember(userId);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}
	
	@PreAuthorize("hasAnyRole('ADMIN','USER','GUEST')")
	@PostMapping("/fund")
	public ResponseEntity<FundResponseDto> addFund(@Valid @RequestBody FundAddDto addDto){
		
		FundResponseDto responseDto=userService.performTransaction(addDto);
		
		return new ResponseEntity<FundResponseDto>(responseDto,HttpStatus.OK);
	}
	
	
	@GetMapping("/show/{no}")
	public ResponseEntity<PageResponseDto<UserResponseDto>> showAllUser(@PathVariable int no){
		PageResponseDto<UserResponseDto> response=userService.findAllUser(no);
		
		return new ResponseEntity<>(response,HttpStatus.OK);
		
	} 
	
	 
    @GetMapping("/transaction/{page}/{size}")
    public ResponseEntity<PageResponseDto<TransactionDetailsDto>> getAllTransactions(
        @PathVariable int page,
        @PathVariable int size) {
        
        // 1. Call the service layer with the received pagination parameters.
        PageResponseDto<TransactionDetailsDto> response = 
            userService.getAllTransactions(page, size);

        // 2. Return the DTO wrapped in a 200 OK status code.
        return new ResponseEntity<PageResponseDto<TransactionDetailsDto>>(response,HttpStatus.OK);
    }
	@GetMapping("/showfund")
	public ResponseEntity<FundResponseDto> showFundDetails(){
		
		FundResponseDto response=userService.getTheFundDetail();
		
		return new ResponseEntity<FundResponseDto>(response,HttpStatus.OK);
		
	}
	
	
	
	
//    @PostMapping("/events")
//    public Event createEvent(@RequestBody EventDto dto) {
//        return clubService.addEvent(dto);
//    }
//
//    
//    @DeleteMapping("/members/{id}")
//    public String removeMember(@PathVariable Long id) {
//        clubService.deleteMember(id);
//        return "Member deleted successfully.";
//    }
//
//    
//    @PutMapping("/members/{id}")
//    public User modifyMember(@PathVariable Long id, @RequestBody MemberUpdateDto dto) {
//        return clubService.updateMember(id, dto);
//    }

}
