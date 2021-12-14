package com.icia.memboard.dao;

import com.icia.memboard.dto.BOARD;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BDAO {
    int bWrite(BOARD board);

    List<BOARD> bList();

    BOARD bView(int bNo);

    void bCount(int bNo);

    int bModify(BOARD board);

    int bDelete(int bNo);
}
