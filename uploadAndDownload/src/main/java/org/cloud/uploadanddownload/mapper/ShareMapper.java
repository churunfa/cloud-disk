package org.cloud.uploadanddownload.mapper;

import com.cloud.common.pojo.file.FileDB;
import com.cloud.common.pojo.file.Share;
import com.cloud.common.pojo.file.UserFile;
import org.apache.ibatis.annotations.*;

@Mapper
public interface ShareMapper {

    @Select("select * from file where id = #{id}")
    FileDB queryFileById(int id);

    @Select("select * from user_file where id = #{id}")
    @Results({
            @Result(column = "file_id", property = "file", one = @One(select = "org.cloud.uploadanddownload.mapper.ShareMapper.queryFileById"))
    })
    UserFile queryUserFileById(int id);

    @Select("select * from share where id = #{id} and status != 'INVALID'")
    @Results({
            @Result(column = "fid", property = "userFile", one = @One(select = "org.cloud.uploadanddownload.mapper.ShareMapper.queryUserFileById"))
    })
    Share queryShareById(int id);

}
