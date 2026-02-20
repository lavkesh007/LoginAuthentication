package com.Spring.Student.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Spring.Student.Model.EmailOTP;

public interface OtpRepo extends JpaRepository<EmailOTP,Integer>{
	EmailOTP findByUserIdAndOtp(int userId, int otp);
	EmailOTP findByEmailAndOtp(String email, int otp);

	EmailOTP findByOtp(int otp);


}
