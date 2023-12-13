package com.hong.blog.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.hong.blog.dto.ResponseDto;
import com.hong.blog.model.RoleType;
import com.hong.blog.model.User;
import com.hong.blog.service.UserService;

@RestController
public class UserApiController {

	@Autowired
	private UserService userService;
	
	@PostMapping("/api/user")
	public ResponseDto<Integer> save(@RequestBody User user) {
		System.out.println("UserApiController : save() 호출됨.");
		// 실제 DB에 insert
		user.setRole(RoleType.USER);
		userService.join(user);
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
	}
	
}
