package com.hong.blog.controller;

import java.util.UUID;

import org.hibernate.internal.build.AllowSysOut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hong.blog.model.KakaoProfile;
import com.hong.blog.model.OAuthToken;
import com.hong.blog.model.User;
import com.hong.blog.service.UserService;

// 인증이 안된 사용자들이 출입할 수 있는 경로를 /auth/** 허용
// 그냥 주소가 / 이면 index.jsp 허용
// static이하에 있는 /js/**, /css/**, /image/** 허용

@Controller
public class UserController {

	@Value("${cos.key}")
	private String cosKey;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@GetMapping("/auth/joinForm")
	public String joinForm() {
		return "user/joinForm";
	}
	
	@GetMapping("/auth/loginForm")
	public String loginForm() {
		return "user/loginForm";
	}
	
	@GetMapping("/user/updateForm")
	public String updateForm() {
		return "user/updateForm";
	}
	
	@GetMapping("/auth/kakao/callback")
	public String kakaoCallback(String code) { // Data를 리턴해주는 컨트롤러 함수
		
		// POST 방식으로 key=value 데이터를 요청(카카오쪽으로)
		
		RestTemplate rt = new RestTemplate();
		
		// HttpHeader 오브젝트 생성
		org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
		headers.add("Content-type", "application/x-www-form-urlencoded");
		
		// HttpBody 오브젝트 생성
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("grant_type", "authorization_code");
		params.add("client_id", "f2b4bc2077b5fb9efe4298be86888639");
		params.add("redirect_uri", "http://localhost:8000/auth/kakao/callback");
		params.add("code", code);
		
		// HttpHeader와 HttpBodty를 하나의 오브젝트에 담기
		HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = 
				new HttpEntity<>(params, headers);
		
		// Http 요청하기 - POST방식으로 - 그리고 response 변수의 응답 받음.
		ResponseEntity<String> response = rt.exchange(
			"https://kauth.kakao.com/oauth/token",
			HttpMethod.POST,
			kakaoTokenRequest,
			String.class
		);
		
		// Gson, Json Simple, ObjectMapper
		ObjectMapper objectMapper = new ObjectMapper();
		OAuthToken oauthToken = null;
		
		try {
			oauthToken = objectMapper.readValue(response.getBody(), OAuthToken.class);
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		System.out.println("카카오 엑세스 토큰: " + oauthToken.getAccess_token());
		
		RestTemplate rt2 = new RestTemplate();
		
		// HttpHeader 오브젝트 생성
		org.springframework.http.HttpHeaders headers2 = new org.springframework.http.HttpHeaders();
		headers2.add("Authorization", "Bearer " + oauthToken.getAccess_token());
		headers2.add("Content-type", "application/x-www-form-urlencoded");
		
		// HttpHeader와 HttpBodty를 하나의 오브젝트에 담기
		HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest = 
				new HttpEntity<>(headers2);
		
		// Http 요청하기 - POST방식으로 - 그리고 response 변수의 응답 받음.
		ResponseEntity<String> response2 = rt2.exchange(
			"https://kapi.kakao.com/v2/user/me",
			HttpMethod.POST,
			kakaoProfileRequest,
			String.class
		);
		
		ObjectMapper objectMapper2 = new ObjectMapper();
		KakaoProfile kakaoProfile = null;
		
		try {
			kakaoProfile = objectMapper2.readValue(response2.getBody(), KakaoProfile.class);
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		System.out.println("카카오 아이디: " + kakaoProfile.getId());
		System.out.println("카카오 닉네임: " + kakaoProfile.getKakao_account().getProfile().getNickname());
		
		System.out.println("블로그서버 유저네임: " 
					+ kakaoProfile.getKakao_account().getProfile().getNickname()
					+ "_"
					+ kakaoProfile.getId());
		System.out.println("블로그서버 닉네임: " + kakaoProfile.getKakao_account().getProfile().getNickname());
		
		// UUID란 -> 중복되지 않는 어떤 특정 값을 만들어내는 알고리즘
		
		System.out.println("블로그서버 패스워드: " + cosKey);
		
		User kakaoUser = User.builder()
					.username(kakaoProfile.getKakao_account().getProfile().getNickname()
							+ "_"
							+ kakaoProfile.getId())
					.password(cosKey)
					.email(kakaoProfile.getKakao_account().getProfile().getNickname())
					.oauth("kakao")
					.build();
		
		// 가입자 혹은 비가입자 체크 해서 처리
		User originUser = userService.userFind(kakaoUser.getUsername());
		
		if(originUser.getUsername() == null) {
			System.out.println("기존회원이 아닙니다. > 자동 회원가입을 진행합니다.");
			userService.join(kakaoUser);
		}
		
		System.out.println("자동 로그인을 진행합니다.");
		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(kakaoUser.getUsername(), cosKey));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		return "redirect:/";
	}	
}
