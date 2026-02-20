package com.Spring.Student.Model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class EmailOTP {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int otpId;
	@Column
	private int otp;
	@Column
	private int userId;
	@Column
	private String email;
	@Column
	private LocalDateTime createTime;
	public LocalDateTime getCurrentTime() {
		return createTime;
	}
	public void setCurrentTime(LocalDateTime currentTime) {
		this.createTime = currentTime;
	}
	public EmailOTP(int otpId, int otp, int userId, String email, LocalDateTime currentTime) {
		super();
		this.otpId = otpId;
		this.otp = otp;
		this.userId = userId;
		this.email = email;
		this.createTime = currentTime;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public EmailOTP(int otpId, int otp, int userId) {
		super();
		this.otpId = otpId;
		this.otp = otp;
		this.userId = userId;
	}
	public EmailOTP() {
		super();
	}
	public EmailOTP(int otp, int userId) {
		super();
		this.otp = otp;
		this.userId = userId;
	}
	public int getOtpId() {
		return otpId;
	}
	public void setOtpId(int otpId) {
		this.otpId = otpId;
	}
	public int getOtp() {
		return otp;
	}
	public void setOtp(int otp) {
		this.otp = otp;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	
}
