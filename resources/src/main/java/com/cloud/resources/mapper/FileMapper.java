package com.cloud.resources.mapper;

import com.cloud.common.pojo.file.FileDB;
import com.cloud.common.pojo.file.UserFile;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FileMapper {
    @Select("select * from file where id = #{id}")
    FileDB queryFileById(int id);

    @Select("select * from file where md5 = #{md5}")
    List<FileDB> queryByMd5(String md5);

    @Insert(value = {
            "<script>",
            "insert into file",
            "<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">",
            "<if test=\"filename != null\">filename,</if>",
            "<if test=\"path != null\">path,</if>",
            "<if test=\"md5 != null\">md5,</if>",
            "<if test=\"gmt_create != null\">gmt_create,</if>",
            "<if test=\"gmt_modified != null\">gmt_modified,</if>",
            "<if test=\"size != null\">size,</if>",
            "</trim>",
            "<trim prefix=\"values (\" suffix=\")\" suffixOverrides=\",\">",
            "<if test=\"filename != null\">#{filename},</if>",
            "<if test=\"path != null\">#{path},</if>",
            "<if test=\"md5 != null\">#{md5},</if>",
            "<if test=\"gmt_create != null\">#{gmt_create},</if>",
            "<if test=\"gmt_modified != null\">#{gmt_modified},</if>",
            "<if test=\"size != null\">#{size},</if>",
            "</trim>",
            "</script>"
    })
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int insert(FileDB fileDB);

    @Update({
            "<script>",
            "update file",
            "<set>",
            "<if test=\"filename != null\">filename = #{filename},</if>",
            "<if test=\"path != null\">path = #{path},</if>",
            "<if test=\"md5 != null\">md5 = #{md5},</if>",
            "<if test=\"gmt_create != null\">gmt_create = #{gmt_create},</if>",
            "<if test=\"gmt_modified != null\">gmt_modified = #{gmt_modified},</if>",
            "<if test=\"size != null\">size = #{size},</if>",
            "</set>",
            "where id = #{id}",
            "</script>"
    })
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int update(FileDB fileDB);

    @Update({
            "<script>",
            "update user_file",
            "<set>",
            "<if test=\"user.id != null\">uid = #{user.id},</if>",
            "<if test=\"file.id != null\">file_id = #{file.id},</if>",
            "<if test=\"file_name != null\">file_name = #{file_name},</if>",
            "<if test=\"dir != null\">dir = #{dir},</if>",
            "<if test=\"fileType != null\">fileType = #{fileType},</if>",
            "<if test=\"delete != null\">`delete` = #{delete},</if>",
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
