package com.cloud.resources.mapper;

import com.cloud.common.pojo.file.FileDB;
import com.cloud.common.pojo.file.UserFile;
import org.apache.ibatis.annotations.*;

public interface UserFileMapper {

    @Select("select * from user_file where id = #{id}")
    @Results(
            @Result(column = "file_id", property = "file", one = @One(select = "com.cloud.resources.mapper.UserFileMapper.queryFileById"))
    )
    UserFile queryUserFileById(int id);

    @Select("select * from file where id = #{id}")
    FileDB queryFileById(int id);

}
