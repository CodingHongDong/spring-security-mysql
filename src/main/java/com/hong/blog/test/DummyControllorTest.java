package com.hong.blog.test;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.hong.blog.model.RoleType;
import com.hong.blog.model.User;
import com.hong.blog.repository.UserRepository;

@RestController
public class DummyControllorTest {

	@Autowired
	private UserRepository userRepository;
	
	@DeleteMapping("/dummy/user/{id}")
	public String deleteUser(@PathVariable int id) {
		try {
			userRepository.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			return "삭제 실패! 해당 id는 DB에 없습니다.";
		}
		return "삭제되었습니다. id : " + id;
	}
	
	@Transactional
	@PutMapping("/dummy/user/{id}")
	public User updateUser(@PathVariable int id, @RequestBody User requestUser) {
		System.out.println("id : " + id);
		System.out.println("password : " + requestUser.getPassword());
		System.out.println("email : " + requestUser.getEmail());
		
		User user = userRepository.findById(id).orElseThrow(() -> {
			return new IllegalArgumentException("수정 실패 !");
		});
		user.setPassword(requestUser.getPassword());
		user.setEmail(requestUser.getEmail());
		
		//userRepository.save(user);
		
		// 더티 체킹
		return user;
	}
	
	@GetMapping("/dummy/users")
	public List<User> list() {
		return userRepository.findAll();
	}
	
	@GetMapping("/dummy/user")
	public List<User> pageList(@PageableDefault(size = 2, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
		Page<User> pagingUser = userRepository.findAll(pageable);
		List<User> user = pagingUser.getContent();
		return user;
	}
	
	@GetMapping("/dummy/user/{id}")
	public User detail(@PathVariable int id) {
		// 1번 방법
//		User user = userRepository.findById(id).orElseThrow(new Supplier<IllegalArgumentException>() {
//			@Override
//			public IllegalArgumentException get() {
//				return new IllegalArgumentException("해당 유저는 없습니다. id : " + id);
//			}
//		});
		// 2번 방법 (람다식)
		User user = userRepository.findById(id).orElseThrow(() -> {
			return new IllegalArgumentException("해당 유저는 없습니다. id : " + id);
		});
		return user; 
	}
	
	@PostMapping("/dummy/insert")
	public String join(User user) {
		user.setRole(RoleType.USER);
		userRepository.save(user);
		return "회원가입이 완료되었습니다.";
	}
	
}
