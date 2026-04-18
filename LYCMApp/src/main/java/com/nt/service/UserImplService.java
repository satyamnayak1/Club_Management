package com.nt.service;



import java.math.BigDecimal;
import java.util.HashMap;

//import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.nt.dto.AdminCreateDto;
import com.nt.dto.AdminUpdateDto;
import com.nt.dto.FundAddDto;
import com.nt.dto.FundResponseDto;
import com.nt.dto.LoginDto;
import com.nt.dto.PageResponseDto;
import com.nt.dto.TransactionDetailsDto;
import com.nt.dto.TransactionDto;
import com.nt.dto.UserDeleteDto;
import com.nt.dto.UserRegisterDto;
import com.nt.dto.UserResponseDto;
import com.nt.dto.UserUpdateDto;
import com.nt.entity.Fund;
import com.nt.entity.FundTransaction;
import com.nt.entity.RoleEntity;
import com.nt.entity.User;
import com.nt.entity.UserPrinciple;
import com.nt.enums.Role;
import com.nt.exception.FundIsNotAvailableException;
import com.nt.exception.InsufficientFundException;
import com.nt.exception.InvalidAmountException;
import com.nt.exception.UserNameIsAlreadyAvailable;
import com.nt.exception.UserNotFoundException;

import com.nt.repository.IFundRepository;
import com.nt.repository.IRoleRepository;
import com.nt.repository.ITransactionRepository;
import com.nt.repository.IUserRepository;
import com.nt.security.JwtService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class UserImplService implements IUserMgmtService {
	
	private final IUserRepository userRepo;
	

	private final PasswordEncoder encoder;
	
	private final IRoleRepository roleRepository;
	

	@Override
	@Transactional
	public UserResponseDto register(UserRegisterDto registerDto) {
		
		//check user is already exist or not
		   if(userRepo.existsByEmail(registerDto.getEmail())) {
			   throw new IllegalArgumentException("User registration field");
		   }
		   
		   //add default role to user
		  RoleEntity role= roleRepository.findByName(Role.ROLE_USER).orElse(null);
		  
		  //create user entity
		  User user=new User();
		  user.setEmail(registerDto.getEmail());
		  user.setName(registerDto.getName());
		  user.setPassword(encoder.encode(registerDto.getPassword())); 
		  user.setMobileNo(registerDto.getMobileNo());
		  user.getRole().add(role);
		  
		  //save the user in the database
		  User saveUser=userRepo.save(user);
		  
		 //return the register user	
	     return new UserResponseDto(saveUser.getEmail(),saveUser.getName(),saveUser.getMobileNo());
   
		}

//	@Override
//	public UserResponseDto addMember(AdminCreateDto adminDto) {
//		
////		if(userRepo.existsByUserName(adminDto.getUserName())) {
////			throw new UserNameIsAlreadyAvailable("User registration field");
////		}
////		
////       User user =null;// mapper.toEntity(adminDto);
////             
////        user.setPassword(encoder.encode(adminDto.getPassword()));
//////        user.getRoles().add(Role.USER);
////        
//////        if (adminDto.getSecrateId() != null && secretKey.equals(adminDto.getSecrateId())) {
//////            user.getRoles().add(Role.ADMIN);
//////        }
////        
////        User savedUser = userRepo.save(user);
//        return null;//mapper.toDto(savedUser);			
//	}
//
//	@Override
//	public PageResponseDto<UserResponseDto> findAllUser(int no) {
//		//find all user
//		
////		Sort sort=Sort.by(Direction.ASC,"name");
////		
////		Pageable pageable=PageRequest.of(no,8,sort);
////		
////		Page<User> page=userRepo.findAll(pageable);
////		
////		List<UserResponseDto> list=page.getContent().stream().map(mapper::toDto).toList();
////		
////		return new PageResponseDto<UserResponseDto>(
////				list,
////				page.getNumber(),
////		        page.getSize(),
////		        page.getTotalElements(),
////		        page.getTotalPages(),
////		        page.isLast());
//		return null;
//	}
//	
//	@Transactional
//	@Override
//	public UserResponseDto updateMember(AdminUpdateDto updateDto,String userId) {
////		//check user is available or not
////		User existingUser = userRepo.findById(userId)
////		        .orElseThrow(() -> new UserNotFoundException("User not found"));
////		
////		// Update userName if provided
////	    if (updateDto.getUserName() != null && !updateDto.getUserName().trim().isEmpty()) {
////	        existingUser.setUserName(updateDto.getUserName().trim());
////	    }
////	    
////	    if (updateDto.getName() != null && !updateDto.getName().trim().isEmpty()) {
////            existingUser.setName(updateDto.getName().trim());
////        }
////	    
//////        if (updateDto.getFatherName() != null && !updateDto.getFatherName().trim().isEmpty()) {
//////            existingUser.setFatherName(updateDto.getFatherName().trim());
//////        }
////
////        // Handle password update. Passwords should always be encoded before saving.
////        if (updateDto.getPassword() != null && !updateDto.getPassword().isEmpty()) {
////            existingUser.setPassword(encoder.encode(updateDto.getPassword()));
////        }
////
////        if (updateDto.getMobileNo() != null && !updateDto.getMobileNo().trim().isEmpty()) {
////            existingUser.setMobileNo(updateDto.getMobileNo().trim());
////        }
////
//////        if (updateDto.getDob() != null) {
//////            existingUser.setDob(updateDto.getDob());
//////        }
////
////        if (updateDto.getEmail() != null && !updateDto.getEmail().trim().isEmpty()) {
////            existingUser.setEmail(updateDto.getEmail().trim());
////        }
////
//////        if (updateDto.getAddress() != null && !updateDto.getAddress().trim().isEmpty()) {
//////            existingUser.setAddress(updateDto.getAddress().trim());
//////        }
////	    
////	    User savedUser = userRepo.save(existingUser);
//	    
//	    return null; //mapper.toDto(savedUser);
//	    
//	}
//	
//	
//	@Transactional
//	@Override
//	public UserResponseDto updateUser(UserUpdateDto updateDto) {
//		
//		// Get the Authentication object from the security context
//	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//	    // The principal is the UserPrinciple object you created
//	    UserPrinciple userPrinciple = (UserPrinciple) authentication.getPrincipal();
//
//	    // Get the userId directly from the principal
////	    String userId = userPrinciple.getUserId();
////		
////	    //checking user is existed or not
////	    User existingUser = userRepo.findById(userId)
////	            .orElseThrow(() -> new UserNotFoundException("User not found"));
//	    
////        if(updateDto.getUserName()!=null && !existingUser.getUserName().equals(updateDto.getUserName())) {
////	    	existingUser.setUserName(updateDto.getUserName());
////	    }
////        else {
////        	throw new UserNameIsAlreadyAvailable("User updation field") ;
////        }
////        
////        if (updateDto.getName() != null && !updateDto.getName().trim().isEmpty()) {
////            existingUser.setName(updateDto.getName().trim());
////        }
////
////        if (updateDto.getFatherName() != null && !updateDto.getFatherName().trim().isEmpty()) {
////            existingUser.setFatherName(updateDto.getFatherName().trim());
////        }
////
////        if (updateDto.getMobileNo() != null && !updateDto.getMobileNo().trim().isEmpty()) {
////            existingUser.setMobileNo(updateDto.getMobileNo().trim());
////        }
////
////        if (updateDto.getDob() != null) {
////            existingUser.setDob(updateDto.getDob());
////        }
////
////        if (updateDto.getEmail() != null && !updateDto.getEmail().trim().isEmpty()) {
////            existingUser.setEmail(updateDto.getEmail().trim());
////        }
////
////        if (updateDto.getAddress() != null && !updateDto.getAddress().trim().isEmpty()) {
////            existingUser.setAddress(updateDto.getAddress().trim());
////        }
////
////	    User savedUser = userRepo.save(existingUser);
//	    return null;
//	    		//mapper.toDto(savedUser);
//	    
//	}
//		
//	@Override
//	public UserDeleteDto deleteMember(String userId) {
//		
//		//check user is exist or not
////	    User existingUser = userRepo.findById(userId)
////	            .orElseThrow(() -> new UserNotFoundException("User not found"));
//
////	    userRepo.delete(existingUser);
//	    return null;
////	    return new UserDeleteDto("Member deleted successfully",
////	            existingUser.getUserId(),
////	            existingUser.getName());	    
//	}
//	
//	@Override
//	public UserResponseDto getProfile(String name) {
//		
//		Optional<User> opt=userRepo.findByUserName(name);
//		
//		if(opt.isEmpty()) {
//			throw new IllegalArgumentException("Invalid User");
//		}
//		
//		return null;//mapper.toDto(opt.get());
//	}
//
//	@Override
//	public String verifyUser(LoginDto loginDto) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public FundResponseDto performTransaction(FundAddDto dto) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public Map<String, Object> loginAsGuest(HttpServletRequest request) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public FundResponseDto getTheFundDetail() {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public PageResponseDto<TransactionDetailsDto> getAllTransactions(int page, int size) {
//		// TODO Auto-generated method stub
//		return null;
//	}

}
		
