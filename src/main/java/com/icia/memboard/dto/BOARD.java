package com.icia.memboard.dto;

import lombok.Data;
import org.apache.ibatis.type.Alias;
import org.springframework.web.multipart.MultipartFile;

@Data
@Alias("board")
public class BOARD {
    private int bNo;
    private String bWriter;
    private String bTitle;
    private String bContent;
    private String bDate;
    private int bHit;
    private MultipartFile bFile;
    private String bFileName;
}
