package com.icia.memboard.dto;

import lombok.Data;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.type.Alias;
import org.springframework.web.multipart.MultipartFile;

@Data
@Alias("member")
public class MEMBER {
    String mId;
    String mPw;
    String mName;
    String mAge;
    String mEmail;
    MultipartFile mProfile;
    String mProfileName;
}
