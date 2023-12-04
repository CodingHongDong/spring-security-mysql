package com.hong.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hong.blog.model.User;

public interface UserRepository extends JpaRepository<User, Integer>{

}
