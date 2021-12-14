package com.icia.memboard.service;

import com.icia.memboard.dao.MDAO;
import com.icia.memboard.dto.MEMBER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
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

    @Autowired
    private PasswordEncoder pwEnc;

    @Autowired
    private JavaMailSender mailSender;

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

        member.setMPw(pwEnc.encode(member.getMPw()));

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

        if(pwEnc.matches(member.getMPw(), member1.getMPw())){
            System.out.println("비밀번호 일치");
            mav.setViewName("index");

            // 이메일 발송

            // 임의의 문자 6자리 생성(보안코드)
            String uuid = UUID.randomUUID().toString().substring(1,7);

            // 보낼 메시지 생성(HTML형식)
            String str = "<h2>안녕하세요. 인천 일보 아카데미입니다.</h2>"+ "<p>로그인에 성공하셨습니다. 인증번호는" +uuid+"입니다.</p>";

            MimeMessage mail = mailSender.createMimeMessage();

            try {
                mail.setSubject("스프링부트 이메일 인증테스트"); // 메일 제목
                mail.setText(str, "UTF-8", "html"); // 내용 ,인코딩 방식, 형식
                mail.addRecipient(Message.RecipientType.TO, new InternetAddress(member1.getMEmail())); // 받는 사람

                // 메일 전송
                mailSender.send(mail);

            } catch (MessagingException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("비밀번호 불일치");
            mav.setViewName("index");
        }
/*        if(member1.getMId()!=null) {
            // 성공
            session.setAttribute("loginId",member1.getMId());
            session.setAttribute("loginProfile",member1.getMProfileName());
            mav.setViewName("index");
        } else {
            // 실패
            mav.setViewName("loginForm");
        }*/

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
