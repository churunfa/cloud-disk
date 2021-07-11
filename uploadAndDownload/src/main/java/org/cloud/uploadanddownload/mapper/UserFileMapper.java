package org.cloud.uploadanddownload.mapper;

import com.cloud.common.pojo.file.FileDB;
import com.cloud.common.pojo.file.UserFile;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserFileMapper {


    @Select("select * from file where id = #{id}")
    FileDB queryFileById(int id);

    @Select({
            "<script>",
            "select * from user_file",
            "<where>",
            "<if test=\"id != null\">id = #{id} and</if>",
            "<if test=\"user.id != null\">uid = #{user.id} and</if>",
            "<if test=\"file.id != null\">file_id = #{file.id} and</if>",
            "<if test=\"file_name != null\">file_name = #{file_name} and</if>",
            "<if test=\"dir != null\">dir = #{dir} and</if>",
            "<if test=\"fileType != null\">fileType = #{fileType} and</if>",
            "<if test=\"delete_time != null\">delete_time = #{delete_time} and</if>",
            "<if test=\"gmt_create != null\">gmt_create = #{gmt_create} and</if>",
            "<if test=\"gmt_modified != null\">gmt_modified = #{gmt_modified} and</if>",
            "<if test=\"size != null\">size = #{size} and</if>",
            "`delete` = 0",
            "</where>",
            "</script>"
    })
    @Results({
            @Result(column = "uid", property = "user", one = @One(select = "org.cloud.uploadanddownload.mapper.UserMapper.queryUserById")),
            @Result(column = "file_id", property = "file", one = @One(select = "org.cloud.uploadanddownload.mapper.UserFileMapper.queryFileById"))
    })
    List<UserFile> queryByUserFile(UserFile userFile);

    @Insert({
            "<script>",
            "insert into user_file",
            "<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">",
            "<if test=\"user.id != null\">uid,</if>",
            "<if test=\"file.id != null\">file_id,</if>",
            "<if test=\"file_name != null\">file_name,</if>",
            "<if test=\"dir != null\">dir,</if>",
            "<if test=\"fileType != null\">fileType,</if>",
            "<if test=\"delete != null\">delete,</if>",
            "<if test=\"delete_time != null\">delete_time,</if>",
            "<if test=\"gmt_create != null\">gmt_create,</if>",
            "<if test=\"gmt_modified != null\">gmt_modified,</if>",
            "<if test=\"size != null\">size,</if>",
            "</trim>",
            "<trim prefix=\"values (\" suffix=\")\" suffixOverrides=\",\">",
            "<if test=\"user.id != null\">#{user.id},</if>",
            "<if test=\"file.id != null\">#{file.id},</if>",
            "<if test=\"file_name != null\">#{file_name},</if>",
            "<if test=\"dir != null\">#{dir},</if>",
            "<if test=\"fileType != null\">#{fileType},</if>",
            "<if test=\"delete != null\">#{delete},</if>",
            "<if test=\"delete_time != null\">#{delete_time},</if>",
            "<if test=\"gmt_create != null\">#{gmt_create},</if>",
            "<if test=\"gmt_modified != null\">#{gmt_modified},</if>",
            "<if test=\"size != null\">#{size},</if>",
            "</trim>",
            "</script>"
    })
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int insertUserFile(UserFile userFile);

    @Update({
            "<script>",
            "update user_file",
            "<set>",
            "<if test=\"user.id != null\">uid = #{user.id},</if>",
            "<if test=\"file.id != null\">file_id = #{file.id},</if>",
            "<if test=\"file_name != null\">file_name = #{file_name},</if>",
            "<if test=\"dir != null\">dir = #{dir},</if>",
            "<if test=\"fileType != null\">fileType = #{fileType},</if>",
            "<if test=\"delete != null\">delete = #{delete},</if>",
            "<if test=\"delete_time != null\">delete_time = #{delete_time},</if>",
            "<if test=\"gmt_create != null\">gmt_create = #{gmt_create},</if>",
            "<if test=\"gmt_modified != null\">gmt_modified = #{gmt_modified},</if>",
            "<if test=\"size != null\">size = #{size},</if>",
            "</set>",
            "where id = #{id}",
            "</script>"
    })
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int updateUserFile(UserFile userFile);

}