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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
import com.nt.entity.User;
import com.nt.entity.UserPrinciple;
import com.nt.enums.Role;
import com.nt.exception.FundIsNotAvailableException;
import com.nt.exception.InsufficientFundException;
import com.nt.exception.InvalidAmountException;
import com.nt.exception.UserNameIsAlreadyAvailable;
import com.nt.exception.UserNotFoundException;
import com.nt.mapper.IUserMapper;
import com.nt.repository.IFundRepository;
import com.nt.repository.ITransactionRepository;
import com.nt.repository.IUserRepository;
import com.nt.security.JwtService;

import jakarta.servlet.http.HttpServletRequest;


@Service
public class UserImplService implements IUserMgmtService {
	@Autowired
	private IUserRepository userRepo;
	
	@Autowired
	private IFundRepository fundRepo;
	
	@Autowired
	private ITransactionRepository transactionRepo;
	
	@Autowired
	private IUserMapper mapper;
	
	@Autowired
	private AuthenticationManager authManager;
	
	@Autowired
	private JwtService jwtService;
	
	@Autowired
	private PasswordEncoder encoder;
	
//	@Autowired
//	private JavaMailSender sender;
	

	@Override
	public UserResponseDto registerUser(UserRegisterDto registerDto) {
		
		//check user is already exist or not
		   if(userRepo.existsByUserName(registerDto.getUserName())) {
			   throw new UserNameIsAlreadyAvailable("User registration field");
		   }
		    	//converting to user
		    	 User user = mapper.toEntity(registerDto);
		    	 
		    	    System.out.println(mapper.toEntity(registerDto));
		    	 
		            user.setPassword(encoder.encode(registerDto.getPassword()));

		            // Always give USER role
		            user.getRoles().add(Role.USER);
		            
		            // If secrate_id matches, also give ADMIN role
		            if ("Secrate123".equals(registerDto.getSecretId())) {
		                user.getRoles().add(Role.ADMIN);
		            }
		            
		            //System.out.println(opt.get());
				    User savedUser = userRepo.save(user);
				    System.out.println(savedUser);
				    
				    System.out.println(savedUser.getUserId());
				    
				    System.out.println(mapper.toDto(savedUser));
				    
				    return mapper.toDto(savedUser);
   
		}

	@Override
	public UserResponseDto addMember(AdminCreateDto adminDto) {
		
		if(userRepo.existsByUserName(adminDto.getUserName())) {
			throw new UserNameIsAlreadyAvailable("User registration field");
		}
		
       User user = mapper.toEntity(adminDto);
             
        user.setPassword(encoder.encode(adminDto.getPassword()));
        user.getRoles().add(Role.USER);
        
//        if (adminDto.getSecrateId() != null && secretKey.equals(adminDto.getSecrateId())) {
//            user.getRoles().add(Role.ADMIN);
//        }
        
        User savedUser = userRepo.save(user);
        return mapper.toDto(savedUser);			
	}

	@Override
	public PageResponseDto<UserResponseDto> findAllUser(int no) {
		//find all user
		
		Sort sort=Sort.by(Direction.ASC,"name");
		
		Pageable pageable=PageRequest.of(no,8,sort);
		
		Page<User> page=userRepo.findAll(pageable);
		
		List<UserResponseDto> list=page.getContent().stream().map(mapper::toDto).toList();
		
		return new PageResponseDto<UserResponseDto>(
				list,
				page.getNumber(),
		        page.getSize(),
		        page.getTotalElements(),
		        page.getTotalPages(),
		        page.isLast());
	}
	
	@Transactional
	@Override
	public UserResponseDto updateMember(AdminUpdateDto updateDto,String userId) {
		//check user is available or not
		User existingUser = userRepo.findById(userId)
		        .orElseThrow(() -> new UserNotFoundException("User not found"));
		
		// Update userName if provided
	    if (updateDto.getUserName() != null && !updateDto.getUserName().trim().isEmpty()) {
	        existingUser.setUserName(updateDto.getUserName().trim());
	    }
	    
	    if (updateDto.getName() != null && !updateDto.getName().trim().isEmpty()) {
            existingUser.setName(updateDto.getName().trim());
        }
	    
//        if (updateDto.getFatherName() != null && !updateDto.getFatherName().trim().isEmpty()) {
//            existingUser.setFatherName(updateDto.getFatherName().trim());
//        }

        // Handle password update. Passwords should always be encoded before saving.
        if (updateDto.getPassword() != null && !updateDto.getPassword().isEmpty()) {
            existingUser.setPassword(encoder.encode(updateDto.getPassword()));
        }

        if (updateDto.getMobileNo() != null && !updateDto.getMobileNo().trim().isEmpty()) {
            existingUser.setMobileNo(updateDto.getMobileNo().trim());
        }

//        if (updateDto.getDob() != null) {
//            existingUser.setDob(updateDto.getDob());
//        }

        if (updateDto.getEmail() != null && !updateDto.getEmail().trim().isEmpty()) {
            existingUser.setEmail(updateDto.getEmail().trim());
        }

//        if (updateDto.getAddress() != null && !updateDto.getAddress().trim().isEmpty()) {
//            existingUser.setAddress(updateDto.getAddress().trim());
//        }
	    
	    User savedUser = userRepo.save(existingUser);
	    
	    return mapper.toDto(savedUser);
	    
	}
	
	@Transactional
	@Override
	public UserResponseDto updateUser(UserUpdateDto updateDto) {
		
		// Get the Authentication object from the security context
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

	    // The principal is the UserPrinciple object you created
	    UserPrinciple userPrinciple = (UserPrinciple) authentication.getPrincipal();

	    // Get the userId directly from the principal
	    String userId = userPrinciple.getUserId();
		
	    //checking user is existed or not
	    User existingUser = userRepo.findById(userId)
	            .orElseThrow(() -> new UserNotFoundException("User not found"));
	    
        if(updateDto.getUserName()!=null && !existingUser.getUserName().equals(updateDto.getUserName())) {
	    	existingUser.setUserName(updateDto.getUserName());
	    }
        else {
        	throw new UserNameIsAlreadyAvailable("User updation field") ;
        }
        if (updateDto.getName() != null && !updateDto.getName().trim().isEmpty()) {
            existingUser.setName(updateDto.getName().trim());
        }

        if (updateDto.getFatherName() != null && !updateDto.getFatherName().trim().isEmpty()) {
            existingUser.setFatherName(updateDto.getFatherName().trim());
        }

        // Handle password update. Passwords should always be encoded before saving.
        if (updateDto.getPassword() != null && !updateDto.getPassword().isEmpty()) {
            existingUser.setPassword(encoder.encode(updateDto.getPassword()));
        }

        if (updateDto.getMobileNo() != null && !updateDto.getMobileNo().trim().isEmpty()) {
            existingUser.setMobileNo(updateDto.getMobileNo().trim());
        }

        if (updateDto.getDob() != null) {
            existingUser.setDob(updateDto.getDob());
        }

        if (updateDto.getEmail() != null && !updateDto.getEmail().trim().isEmpty()) {
            existingUser.setEmail(updateDto.getEmail().trim());
        }

        if (updateDto.getAddress() != null && !updateDto.getAddress().trim().isEmpty()) {
            existingUser.setAddress(updateDto.getAddress().trim());
        }

	    User savedUser = userRepo.save(existingUser);
	    return mapper.toDto(savedUser);
	    
	}
	
	
	@Override
	public UserDeleteDto deleteMember(String userId) {
		
		//check user is exist or not
	    User existingUser = userRepo.findById(userId)
	            .orElseThrow(() -> new UserNotFoundException("User not found"));

	    userRepo.delete(existingUser);
	    return new UserDeleteDto("Member deleted successfully",
	            existingUser.getUserId(),
	            existingUser.getName());	    
	}
	
	@Transactional
    @Override
    public FundResponseDto performTransaction(FundAddDto dto) {

        if (dto.getAmount() == null || dto.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException("Invalid amount");
        }

        // Load or create fund
        Fund fund = fundRepo.loadTheFund()
                .orElseGet(() -> {
                    Fund f = new Fund();
                    f.setAmount(BigDecimal.ZERO);
                    return fundRepo.save(f);
                });

        // Update fund
        switch (dto.getType()) {
            case DEPOSITE -> fund.setAmount(fund.getAmount().add(dto.getAmount()));
            case WITHDRAW, REVERSE -> {
                if (fund.getAmount().compareTo(dto.getAmount()) < 0)
                    throw new InsufficientFundException("Insufficient fund");
                fund.setAmount(fund.getAmount().subtract(dto.getAmount()));
            }
            default -> throw new IllegalArgumentException("Invalid transaction type");
        }

        fundRepo.save(fund);

        // Get current admin
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrinciple userPrinciple = (UserPrinciple) authentication.getPrincipal();
        User admin = userRepo.findById(userPrinciple.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        // Create transaction
        FundTransaction txn = new FundTransaction();
        txn.setAmount(dto.getAmount());
        txn.setType(dto.getType());
        txn.setReason(dto.getReason());
        txn.setFund(fund);
        txn.setAdmin(admin);

        transactionRepo.save(txn);

        // Map last 5 transactions to DTOs
        List<TransactionDto> lastTxns = transactionRepo.findTop5ByOrderByCreatedAtDesc()
                .stream()
                .map(mapper::toDto)
                .toList();
                 
        return new FundResponseDto(fund.getAmount(), lastTxns);
    }
	
//	private void sendMail(BigDecimal amount, TransactionType type) throws MessagingException {
//	    List<User> users = userRepo.findAll();
//
//	    for (User user : users) {
//
//	        MimeMessage message = sender.createMimeMessage();
//	        MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");
//
//	        helper.setFrom("satyamnayak0405@gmail.com");
//	        helper.setTo(user.getEmail());
//
//	        helper.setSubject("Fund Update Notification");
//
//	        String mailBody = "Hello " + user.getName() + ",\n\n" +
//	                "A new transaction occurred:\n" +
//	                "Type: " + type + "\n" +
//	                "Amount: ₹" + amount + "\n\n" +
//	                "Thank you!";
//
//	        helper.setText(mailBody);
//
//	        sender.send(message);
//	    }
//	}

    @Transactional(readOnly = true)
    @Override
    public FundResponseDto getTheFundDetail() {

        Fund fund = fundRepo.loadTheFund()
                .orElseThrow(() -> new FundIsNotAvailableException("Fund is not available"));

        List<TransactionDto> lastTxns = transactionRepo.findTop5ByOrderByCreatedAtDesc()
                .stream()
                .map(mapper::toDto)
                .toList();

        return new FundResponseDto(fund.getAmount(), lastTxns);
    }
	
	
//	public String addEvent(Event event) {
//		
//		re
//	}

	@Override
	public String verifyUser(LoginDto loginDto) {
		
		Authentication authentication=authManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUserName(), loginDto.getPassword()));
		
		if(!authentication.isAuthenticated()) {
					
			throw new IllegalArgumentException("Invalid username or password");
			
		}	
		User user = userRepo.findByUserName(loginDto.getUserName())
	            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
		
		return jwtService.generateToken(loginDto.getUserName(),user.getRoles(),user.getUserId());
	}

	@Override
	public Map<String, Object> loginAsGuest(HttpServletRequest request) {
		
		
     // Generate guest ID
        String guestId = "guest_" + UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, 8)
                .toUpperCase();
        
        // Create JWT token for guest with role GUEST
        String token = jwtService.generateToken(
                guestId,                  // username (can be guestId)
                Set.of(Role.GUEST),       // role
                guestId                   // userId claim
        );
		
	        Map<String, Object> response=new HashMap<>();
	        response.put("guestId", guestId);
	        response.put("token", token);
		return  response;
		
	}

	@Override
	public UserResponseDto getProfile(String name) {
		
		Optional<User> opt=userRepo.findByUserName(name);
		
		if(opt.isEmpty()) {
			throw new IllegalArgumentException("Invalid User");
		}
		
		return mapper.toDto(opt.get());
	}

	@Override
	public PageResponseDto<TransactionDetailsDto> getAllTransactions(int page, int size) {
		
		Pageable pageable=PageRequest.of(page, size);
		
		Page<FundTransaction> page1=transactionRepo.findAllByOrderByCreatedAtDesc(pageable);
		
		List<FundTransaction> list=page1.getContent();
		
		List<TransactionDetailsDto> list1=list.stream().map(transaction-> {
			return new TransactionDetailsDto(transaction.getAdmin().getName(),transaction.getAmount(),transaction.getType(),transaction.getReason(),transaction.getCreatedAt());
		}).collect(Collectors.toList());	
		return new PageResponseDto<TransactionDetailsDto>(list1, page1.getNumber(), list1.size(), page1.getTotalElements(), page1.getTotalPages(), page1.isLast());
	}

//	@Override
//    @Transactional(readOnly = true)
//    public Page<TransactionDto> getAllTransactions(int page, int size) {
//		Pageable pageable=PageRequest.of(page,size);
//        Page<FundTransaction> transactions = transactionRepo.findAllByOrderByCreatedAtDesc(pageable);
//
//        // Convert to DTO
//        return transactions.map(mapper::toDto);
//    }


}
		
