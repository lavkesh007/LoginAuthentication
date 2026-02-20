package com.Spring.Student.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Spring.Student.Model.User;

public interface UserRepo extends JpaRepository<User,Integer>{
	User findByEmail(String email); 
}
