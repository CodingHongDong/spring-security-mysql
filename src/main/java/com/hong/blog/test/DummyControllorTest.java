package com.hong.blog.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hong.blog.model.RoleType;
import com.hong.blog.model.User;
import com.hong.blog.repository.UserRepository;

@RestController
public class DummyControllorTest {

	@Autowired
	private UserRepository userRepository;
	
	@PostMapping("/dummy/insert")
	public String join(User user) {
		user.setRole(RoleType.USER);
		userRepository.save(user);
		return "회원가입이 완료되었습니다.";
	}
	
}
