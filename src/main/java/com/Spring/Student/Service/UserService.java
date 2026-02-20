package com.Spring.Student.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.Spring.Student.Model.EmailOTP;
import com.Spring.Student.Model.User;
import com.Spring.Student.Repository.OtpRepo;
import com.Spring.Student.Repository.UserRepo;

@Service
public class UserService {
	
	private UserRepo repo;
	@Autowired
	private OtpRepo otpRepo;
	@Autowired
	JavaMailSender mailSender;
	public UserService(UserRepo repo) {
		this.repo = repo;
	}
	
	public String checkCredential(String email,String Password) {
		User user = repo.findByEmail(email);
		if(user==null) {
	        return "UserNotFound";   // user not found
	    }
		if(!user.getPassword().equals(Password)) return "Invalid";
		return"home";
	}
	
	
	
	public String GenrateOtp(String email) {
		User user = repo.findByEmail(email);
		
		if(user==null) {
			return "UserNotFound";
		}
		
		int otp = GenerateOTP();
		EmailOTP eOTP = new EmailOTP();
		eOTP.setOtp(otp);
		eOTP.setUserId(user.getid());
		eOTP.setCurrentTime(LocalDateTime.now());
	
		otpRepo.save(eOTP);
		
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(user.getEmail());
		message.setSubject("OTP Verification");
		message.setText("Hello " + user.getName()+" this is your otp: " + otp);
		mailSender.send(message);
		
		return "OTP";
		
	}
	
	public String VerifyOtp(int otp,String email) {
		User user = repo.findByEmail(email);
		if(user==null)return "Login";
		EmailOTP otpverify = otpRepo.findByUserIdAndOtp(user.getid(),otp);
		if(otpverify==null)return "Invalid OTP";
		LocalDateTime expriedTime = otpverify.getCurrentTime().plusMinutes(5);
	
		if(LocalDateTime.now() .isAfter(expriedTime)){
			return "Expired";
		}
		
		
		
		return "resetPassword";
	}
	public String UpdatePassword(String email,String Password,String confirmPassword) {
		if(!Password.equals(confirmPassword)) {
			return "Password Not Match";
		}
		User user = repo.findByEmail(email);
		if(user==null) {
			return "User Not Found";
		}
		user.setPassword(Password);
		repo.save(user);
		return "Login";
	}

	public String GenerateMailOTP(String email) {
		User user = repo.findByEmail(email);
		if(user!=null) {
			return "Email Already Exist";
		}
		
		int OTP = GenerateOTP();
		EmailOTP eOTP = new EmailOTP();
		eOTP.setOtp(OTP);
		eOTP.setEmail(email);
		eOTP.setCurrentTime(LocalDateTime.now());
		otpRepo.save(eOTP);
		
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(email);
		message.setSubject("OTP Verification");
		message.setText("This Registration  otp: " + OTP);
		mailSender.send(message);
		
		return "VerifyRegisterMail";
	}
	
	public int GenerateOTP() {
		return new Random().nextInt(100000,1000000);
	}

	public String VerifyMail(User user, int otp) {
		
		EmailOTP otpverify = otpRepo.findByEmailAndOtp(user.getEmail(),otp);
		if(otpverify==null) {
			return"Invalid";
		}
		LocalDateTime expireTime = otpverify.getCurrentTime().plusMinutes(5);
		if(LocalDateTime.now().isAfter(expireTime)) {
			return "Expired";
		}
		repo.save(user);
		otpRepo.delete(otpverify);
		return "Login";
				
	}
}
