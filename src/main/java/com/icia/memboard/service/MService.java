package com.icia.memboard.service;

import com.icia.memboard.dao.MDAO;
import com.icia.memboard.dto.MEMBER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
public class MService {

    @Autowired
    private MDAO dao;

    @Autowired
    private HttpSession session;

    private ModelAndView mav = new ModelAndView();

    // 회원가입
    public ModelAndView mJoin(MEMBER member) throws IOException {
        // (1) 파일 불러오기
        MultipartFile mProfile = member.getMProfile();

        // (2) 파일이름 설정하기
        String originalFileName = mProfile.getOriginalFilename();

        // (3) 난수 생성하기
        String uuid = UUID.randomUUID().toString().substring(1,7);


        // (4) 난수와 파일 이름 합치기
        String mProfileName = uuid + "_" + originalFileName;

        // (5) 파일 저장위치
        String savePath = "D:/Spring/memboard/src/main/resources/static/profile/"+mProfileName;

        // (6) 파일 선택여부
        if(!mProfile.isEmpty()) {
            member.setMProfileName(mProfileName);
            mProfile.transferTo(new File(savePath));
        } else {
            member.setMProfileName("default.png");
        }
        
        int result = dao.mJoin(member);
        
        if(result>0) {
            // 성공
            mav.setViewName("index");
        } else {
            // 실패
            mav.setViewName("joinForm");
        }


        return mav;
    }

    // 로그인
    public ModelAndView mLogin(MEMBER member) {


        MEMBER member1 = dao.mLogin(member);

        if(member1.getMId()!=null) {
            // 성공
            session.setAttribute("loginId",member1.getMId());
            session.setAttribute("loginProfile",member1.getMProfileName());
            mav.setViewName("index");
        } else {
            // 실패
            mav.setViewName("loginForm");
        }

        return mav;
    }
    
    // 회원목록
    public ModelAndView mList() {
        List<MEMBER> memberList =dao.mList();

        mav.setViewName("list");
        mav.addObject("memberList",memberList);

        return mav;
    }

    // 회원 상세보기
    public ModelAndView mView(String mId) {
        MEMBER member= dao.mView(mId);

        if(member!=null) {
            // 검색한 회원의 정보가 존재할 때
            mav.addObject("member",member);
            mav.setViewName("view");
        } else {
            // 검색한 회원의 정보가 존재하지 않을 때
            // html파일이 아닌 controller의 주소로 값을 보낼 때 redirect:/주소
            mav.setViewName("redirect:/mList");
        }
        return mav;
    }

    // 회원수정 페이지
    public ModelAndView mModiForm(String mId) {
        MEMBER member= dao.mView(mId);

        if(member!=null) {
            mav.addObject("member",member);
            mav.setViewName("modify");
        } else {
            mav.setViewName("redirect:/mList");
        }
        return mav;
    }

    // 회원 수정
    public ModelAndView mModify(MEMBER member) {

        int result = dao.mModify(member);

        if(result>0) {
            // 성공
            mav.setViewName("redirect:/mView?"+member.getMId());
        } else {
            // 실패
            mav.setViewName("redirect:/mModiForm?"+member.getMId());
        }
        return mav;
    }


    public ModelAndView mDelete(String mId) {

        int result = dao.mDelete(mId);

        if(result>0) {
            // 성공
            mav.setViewName("redirect:/mList");
        } else {
            // 실패
            mav.setViewName("index");
        }

        return mav;
    }
}
