package com.hong.blog;

import org.junit.Test;

import com.hong.blog.model.Reply;

public class ReplyObjectTest {
	
	@Test
	public void toStringTest() {
		Reply reply = Reply.builder()
					 .id(1)
					 .user(null)
					 .board(null)
					 .content("GDSGSD")
					 .build();
		
		System.out.println(reply);
	}
	
}
