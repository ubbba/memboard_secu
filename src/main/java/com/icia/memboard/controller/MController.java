package com.icia.memboard.controller;

import com.icia.memboard.dto.MEMBER;
import com.icia.memboard.service.MService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.io.IOException;

@Controller
public class MController {

    @Autowired
    private MService msvc;

    @Autowired
    private HttpSession session;


    private ModelAndView mav = new ModelAndView();

    // joinForm : 회원가입 페이지 이동
    @RequestMapping(value = "joinForm", method = RequestMethod.GET)
    public String joinForm() {

        return "joinForm";
    }

    // mJoin : 회원가입
    @RequestMapping(value = "mJoin", method = RequestMethod.POST)
    public ModelAndView mJoin(@ModelAttribute MEMBER member) throws IOException {
        mav = msvc.mJoin(member);
        return mav;
    }

    // loginForm : 로그인 페이지로 이동
    @RequestMapping(value = "loginForm", method = RequestMethod.GET)
    public String loginForm() {

        return "loginForm";
    }

    // mLogin : 로그인
    @RequestMapping(value = "mLogin", method = RequestMethod.POST)
    public ModelAndView mLogin(@ModelAttribute MEMBER member) {
        mav = msvc.mLogin(member);
        return mav;
    }

    // logout : 로그아웃
    @RequestMapping(value = "logout", method = RequestMethod.GET)
    public String logout() {
        session.invalidate();
        
        return "index";
    }
    
    // mList : 회원목록
    @RequestMapping(value = "mList", method = RequestMethod.GET)
    public ModelAndView mList() {

        mav = msvc.mList();
        return mav;
    }

    // mView : 회원 상세보기
    @RequestMapping(value = "mView", method = RequestMethod.GET)
    public ModelAndView mView(@RequestParam("mId") String mId) {

        mav = msvc.mView(mId);
        return mav;
    }

    // mModiForm : 회원수정 페이지로 이동
    @RequestMapping(value = "mModiForm", method = RequestMethod.GET)
    public ModelAndView mModiForm(@RequestParam("mId") String mId) {

        mav = msvc.mModiForm(mId);

        return mav;
    }

    // mModify : 회원수정
    @RequestMapping(value = "mModify", method = RequestMethod.POST)
    public ModelAndView mModify(@ModelAttribute MEMBER member) {

        mav = msvc.mModify(member);

        return mav;
    }

    // mDelete : 회원삭제
    @RequestMapping(value = "mDelete", method = RequestMethod.GET)
    public ModelAndView mDelete(@RequestParam("mId") String mId) {

        mav = msvc.mDelete(mId);

        return mav;
    }


}
