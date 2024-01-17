package com.hong.blog.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hong.blog.model.RoleType;
import com.hong.blog.model.User;
import com.hong.blog.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder encoder;
	
	@Transactional
	public void join(User user) {
		String rawPassword = user.getPassword();
		String encPassword = encoder.encode(rawPassword);
		user.setPassword(encPassword);
		user.setRole(RoleType.USER);
		userRepository.save(user);
	}
	
	@Transactional
	public void userUpdate(User user) {
		// 수정시에는 영속성 컨텍스트 User 오브젝트를 영속화시키고, 영속화된 User 오브젝트를 수정
		// SELECT를 해서 User오브젝트를 DB로 부터 가져오는 이유는 영속화하기 위해서!
		// 영속화된 오브젝트를 변경하면 자동으로 DB에 update문을 날려준다.
		User persistence = userRepository.findById(user.getId())
						  .orElseThrow(()-> {
							  return new IllegalArgumentException("회원 찾기 실패");
						  });
		String rawPassword = user.getPassword();
		String encPassword = encoder.encode(rawPassword);
		persistence.setPassword(encPassword);
		persistence.setEmail(user.getEmail());
		
		// 회원수정 함수 종료시 = 서비스 종료 = 트랜잭션 종료 = commit 자동
		// 영속화된 persistence 객체의 변화가 감지되면 더티체킹이 되어 update문을 날려준다.
	}
	
}
