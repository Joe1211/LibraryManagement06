package com.wonders.shixi.mapper;

import java.util.List;

import com.wonders.shixi.pojo.Reader;
import com.wonders.shixi.pojo.ReaderExample;
import org.apache.ibatis.annotations.Param;

public interface ReaderMapper {
    long countByExample(ReaderExample example);

    int deleteByExample(ReaderExample example);

    int deleteByPrimaryKey(Integer readerId);

    int insert(Reader record);

    int insertSelective(Reader record);

    List<Reader> selectByExample(ReaderExample example);

    List<Reader> selectByPrimaryKey(Integer readerId);

    Reader selectById(Integer readerId);

    int updateByExampleSelective(@Param("record") Reader record, @Param("example") ReaderExample example);

    int updateByExample(@Param("record") Reader record, @Param("example") ReaderExample example);

    int updateByPrimaryKeySelective(Reader record);

    int updateByPrimaryKey(Reader record);

    Reader readerLogin(String readerPhone);

    Reader adminLogin(String adminPhone);

    int updateBypassword(@Param("readerPhone")String readerPhone,@Param("readerPassword")String readerPassword);

    int getReaderCountWithPhone(String phone);

    int getReaderCountWithEmail(String email);
}