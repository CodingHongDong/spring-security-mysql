let index = {
	init: function() {
		$('#btn-save').on('click', () => {
			this.save();
		});
		$('#btn-login').on('click', () => {
			this.login();
		});
	},
	save: function() {
		//alert("user의 save함수 호출됨.");
		let data = {
			username: $('#username').val(),
			password: $('#password').val(),
			email: $('#email').val()
		};
		
		$.ajax({
			type: "POST",
			url: "/api/user",
			data: JSON.stringify(data), //http body데이터
			contentType: "application/json; charset=utf-8", // body데이터가 어떤 타입인지
		}).done(function(resp) {
			console.log(resp);
			alert("회원가입 완료!");
			location.href="/";
		}).fail(function(error) {
			alert(JSON.stringify(error));
		});
	},
	
	login: function() {
		//alert("user의 save함수 호출됨.");
		let data = {
			username: $('#username').val(),
			password: $('#password').val()
		};
		
		$.ajax({
			type: "POST",
			url: "/api/user/login",
			data: JSON.stringify(data), //http body데이터
			contentType: "application/json; charset=utf-8", // body데이터가 어떤 타입인지
		}).done(function(resp) {
			console.log(resp);
			alert("로그인 완료!");
			location.href="/";
		}).fail(function(error) {
			alert(JSON.stringify(error));
		});
	}

}

index.init();