package com.icia.memboard.service;

import com.icia.memboard.dao.BDAO;
import com.icia.memboard.dto.BOARD;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
public class BService {

    @Autowired
    private BDAO dao;

    private ModelAndView mav = new ModelAndView();

    public ModelAndView writeForm() {
        return mav;
    }

    // 글 작성
    public ModelAndView bWrite(BOARD board) throws IOException {
        // 1. 파일 불러오기
        MultipartFile bFile = board.getBFile();

        // 2. 원본 파일 이름 가져오기
        String originalFileName = bFile.getOriginalFilename();

        // 3. 랜덤한 문자열 만들기
        String uuid = UUID.randomUUID().toString().substring(1,7);

        // 4. 2번(원본 이름)과 3번(난수) 합치기
        String bFileName = uuid + "_" + originalFileName;

        // 5. 파일 저장 위치
        String savePath = "D:/Spring/memboard/src/main/resources/static/upload/"+bFileName;

        // 6. 파일 선택 여부
        if(!bFile.isEmpty()) {
            board.setBFileName(bFileName);
            bFile.transferTo(new File(savePath));
        }

        int result = dao.bWrite(board);

        if(result>0) {
            // 성공
            mav.setViewName("index");
        } else {
            // 실패
            mav.setViewName("write");
        }

        return mav;
    }

    // 목록
    public ModelAndView bList() {
        List<BOARD> boardList = dao.bList();

        mav.setViewName("blist");
        mav.addObject("boardList",boardList);

        return mav;
    }

    // 상세보기
    public ModelAndView bView(int bNo) {
    
        // (1) 조회 수 증가
        dao.bCount(bNo);

        // (2) 상세보기
        BOARD board =dao.bView(bNo);

        if(board!=null) {
            mav.addObject("board",board);
            mav.setViewName("bView");
        } else {
            mav.setViewName("redirect:/bList");
        }
        return mav;
    }

    // 수정 페이지 이동
    public ModelAndView bModiForm(int bNo) {

        // 상세보기 때 만들어 놓은 bView(bNo)메소드 사용
        BOARD board = dao.bView(bNo);

        if (board != null) {
            mav.addObject("board", board);
            mav.setViewName("bModify");
        } else {
            mav.setViewName("redirect:/bList");
        }
        return mav;
    }

    // 글 수정
    public ModelAndView bModify(BOARD board) throws IOException {
        // 1. 파일 불러오기
        MultipartFile bFile = board.getBFile();

        // 2. 원본 파일 이름 가져오기
        String originalFileName = bFile.getOriginalFilename();

        // 3. 랜덤한 문자열 만들기
        String uuid = UUID.randomUUID().toString().substring(1,7);

        // 4. 2번(원본 이름)과 3번(난수) 합치기
        String bFileName = uuid + "_" + originalFileName;

        // 5. 파일 저장 위치
        String savePath = "D:/Spring/memboard/src/main/resources/static/upload/"+bFileName;

        // 6. 파일 선택 여부
        if(!bFile.isEmpty()) {
            board.setBFileName(bFileName);
            bFile.transferTo(new File(savePath));
        }

        //  Q. 입력, 수정, 삭제 시 필요한 데이터타입과 변수!
        int result = dao.bModify(board);

        if(result>0) {
            mav.setViewName("redirect:/bView?bNo="+board.getBNo());
        } else {
            mav.setViewName("redirect:/bModiForm?bNo="+board.getBNo());
        }
        return mav;
    }

    public ModelAndView bDelete(int bNo) {
        int result = dao.bDelete(bNo);

        if(result>0) {
            mav.setViewName("redirect:/bList");
        } else {
            mav.setViewName("redirect:/bView?bNo"+bNo);
        }


        return mav;
    }
}
