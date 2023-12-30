package com.hong.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hong.blog.model.User;

public interface UserRepository extends JpaRepository<User, Integer>{

}


//JPA Naming 전략
//SELECT * FROM user WHERE username=?1 AND password=?2
//User findByUsernameAndPassword(String username, String password);
