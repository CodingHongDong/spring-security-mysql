package com.hong.blog.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hong.blog.model.User;

public interface UserRepository extends JpaRepository<User, Integer>{
	// SELECT * FROM user WHERE username = 1?;
	Optional<User> findByUsername(String username);
}


//JPA Naming 전략
//SELECT * FROM user WHERE username=?1 AND password=?2
//User findByUsernameAndPassword(String username, String password);
