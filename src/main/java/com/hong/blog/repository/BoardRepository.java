package com.hong.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hong.blog.model.Board;

public interface BoardRepository extends JpaRepository<Board, Integer>{
	
}
