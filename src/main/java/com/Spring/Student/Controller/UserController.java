package com.Spring.Student.Controller;

import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.Spring.Student.Model.User;
import com.Spring.Student.Service.UserService;

@Controller
//@RequestMapping("/")
public class UserController {
	
	private UserService service;
	public UserController(UserService service) {
		this.service = service;
	}
	
	//For Login Page
	@GetMapping("/")
	public String Login() {
		
		return "Login";
	}
	
	//For Registration
	@GetMapping("/userRegister")
	public String RegisterPage() {
		
		return "Register";
	}
	
	//For checking Credentials
	@PostMapping("/check")
	public String CheckCredentials(@RequestParam String email, @RequestParam String password,Model model) {
		String result = service.checkCredential(email, password);
		if(result.equalsIgnoreCase("UserNotFound")) {
			model.addAttribute("Invalid", "User Not Found!!!");
			return "Register";
		}
		if(result.equalsIgnoreCase("Invalid")) {
			model.addAttribute("Invalid", "Invalid Password!!!");
			return "Login";
		}
		return result;
	}
	
	//For Registration SEnding OTP
	@PostMapping("/register")
	public String Register(@ModelAttribute User user,Model model,@RequestParam String confirmPassword) {
		
		String result = service.GenerateMailOTP(user.getEmail());
		if(result.equals("Email Already Exist")) {
			model.addAttribute("Exist", "Email Already Exist!!!");
			return "Register";
		}
		if(user.getName()==null) {
			model.addAttribute("Name", "Enter Name!!!");
			return"Register";
		}
		if(!user.getPassword().equals(confirmPassword)) {
			model.addAttribute("Invalid", "Password Not Match!!!");
			return "Register";
		}
		if(String.valueOf(user.getPhone())==null) {
			model.addAttribute("Phone", "Enter Phone Number!!!");
			return"Register";
		}
		model.addAttribute("email", user.getEmail());
	    model.addAttribute("name", user.getName());
	    model.addAttribute("password", user.getPassword());
	    model.addAttribute("phone", user.getPhone());
		return result;
	}
	
	//For Registration Verification
	@PostMapping("/verifyRegisterMail")
	public String VerifyMail(@RequestParam String name,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam Long phone,@RequestParam int otp ,Model model) {
		User user = new User(name,email,phone,password);
		String result = service.VerifyMail(user,otp);
		if(result.equals("Invalid")) {
			model.addAttribute("Invalid", "Invalid OTP!!!");
		    model.addAttribute("email", email);
		    model.addAttribute("name", name);
		    model.addAttribute("password", password);
		    model.addAttribute("phone", phone);
			return "verifyRegisterMail";
		}
		if(result.equals("Expired")) {
			model.addAttribute("Invalid", "Expired OTP");
			return "Register";
		}
		return result;
		
	}
	
	//Forgot Password
	@GetMapping("/forgot")
	public String Forgot() {
		return "gmailOTP";
	}
	
	//Genreting OTP for Forgot otp
	@PostMapping("/genrateOTP")
	public String Genrate(@RequestParam String email,Model model) {
	    String result = service.GenrateOtp(email);
	    if(result.equalsIgnoreCase("UserNotFound")) {
	    	model.addAttribute("Invalid", "User Not Found!!!");
	    	return"Login";
	    }
	    model.addAttribute("email",email);
	    return result;   // same page reload
	}

	//Verify OTP for resetPassword
	@PostMapping("/verifyOTP")
	public String Verify(@RequestParam String email,@RequestParam int otp,Model model) {
		model.addAttribute("email",email);
		String result = service.VerifyOtp(otp,email);
		if(result.equalsIgnoreCase("Invalid OTP")) {
			model.addAttribute("Invalid", "Invalid OTP!!!");
			model.addAttribute("email",email);
			return"OTP";
		}
		if(result.equals("Expired")) {
			model.addAttribute("Invalid", "OTP is Expired!!!");
			return "Login";
		}
		
		return result;
	}
	
	//Reset Password
	@PostMapping("/resetPassword")
	public String UpdatePassword(@RequestParam String email,@RequestParam String Password,@RequestParam String confirmPassword,Model model) {
		String result= service.UpdatePassword(email,Password,confirmPassword);
		if(result.equalsIgnoreCase("Password Not Match")) {
			model.addAttribute("Invalid", "Password not Match!!!");
			model.addAttribute("email",email);
			return "resetPassword";
		}
		if(result.equals("User Not Found")) {
			model.addAttribute("Invalid", "User Not Found!!!");
		}
		return result;
	}

}
