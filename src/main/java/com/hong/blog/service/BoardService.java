package com.hong.blog.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hong.blog.dto.ReplySaveRequestDto;
import com.hong.blog.model.Board;
import com.hong.blog.model.Reply;
import com.hong.blog.model.User;
import com.hong.blog.repository.BoardRepository;
import com.hong.blog.repository.ReplyRepository;
import com.hong.blog.repository.UserRepository;

@Service
public class BoardService {

	@Autowired
	private BoardRepository boardRepository;
	
	@Autowired
	private ReplyRepository replyRepository;
	
	@Transactional(readOnly = true)
	public Page<Board> list(Pageable pageable) {
		return boardRepository.findAll(pageable);
	}
	
	@Transactional(readOnly = true)
	public Board detail(int id) {
		return boardRepository.findById(id)
				.orElseThrow(()-> {
					return new IllegalArgumentException("글 상세보기 실패: 아이디를 찾을 수 없습니다.");
				});
	}
	
	@Transactional
	public void write(Board board, User user) {
		board.setCount(0);
		board.setUser(user);
		boardRepository.save(board);
	}
	
	@Transactional
	public void delete(int id) {
		boardRepository.deleteById(id);
	}
	
	@Transactional
	public void update(int id, Board requestBoard) {
		Board board = boardRepository.findById(id)
						.orElseThrow(()-> {
							return new IllegalArgumentException("글 찾기 실패: 아이디를 찾을 수 없습니다.");
						});
		board.setTitle(requestBoard.getTitle());
		board.setTitle(requestBoard.getContent());
		// 해당 함수로 종료시(Service가 종료될 때) 트랜잭션이 종료됩니다. 이때 더티체킹 - 자동업데이트 됨. db flush
	}
	
	@Transactional
	public void replyWrite(ReplySaveRequestDto replySaveRequestDto) {
		
//		User user = userRepository.findById(replySaveRequestDto.getUserId())
//				.orElseThrow(()-> {
//					return new IllegalArgumentException("댓글 쓰기 실패: 유저 아이디 찾기 실패!");
//				});
//		
//		Board board = boardRepository.findById(replySaveRequestDto.getBoardId())
//						.orElseThrow(()-> {
//							return new IllegalArgumentException("댓글 쓰기 실패: 게시글 아이디 찾기 실패!");
//						});
//		
//		Reply reply = Reply.builder()
//					 .user(user)
//					 .board(board)
//					 .content(replySaveRequestDto.getContent())
//					 .build();
		
		replyRepository.mSave(replySaveRequestDto.getUserId(), replySaveRequestDto.getBoardId(), replySaveRequestDto.getContent());
	}
	
	@Transactional
	public void replyDelete(int replyId) {
		replyRepository.deleteById(replyId);;
	}
	
}
