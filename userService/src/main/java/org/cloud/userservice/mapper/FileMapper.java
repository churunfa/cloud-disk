package org.cloud.userservice.mapper;

import com.cloud.common.pojo.User;
import com.cloud.common.pojo.file.FileDB;
import com.cloud.common.pojo.file.UserFile;
import org.apache.ibatis.annotations.*;

import java.util.Date;
import java.util.List;

@Mapper
public interface FileMapper {
    @Select("select * from file where id = #{id}")
    FileDB queryFileById(int id);

    @Select("select * from user where id = #{id} and deleted = 0;")
    User queryUserById(int id);

    @Select({
            "<script>",
            "select * from user_file",
            "<where>",
            "<if test=\"userFile.id != null\">id = #{userFile.id} and</if>",
            "<if test=\"userFile.user.id != null\">uid = #{userFile.user.id} and</if>",
            "<if test=\"userFile.file.id != null\">file_id = #{userFile.file.id} and</if>",
            "<if test=\"userFile.file_name != null\">file_name = #{userFile.file_name} and</if>",
            "<if test=\"userFile.dir != null\">dir = #{userFile.dir} and</if>",
            "<if test=\"userFile.fileType != null\">fileType = #{userFile.fileType} and</if>",
            "<if test=\"userFile.delete_time != null\">delete_time = #{userFile.delete_time} and</if>",
            "<if test=\"userFile.gmt_create != null\">gmt_create = #{userFile.gmt_create} and</if>",
            "<if test=\"userFile.gmt_modified != null\">gmt_modified = #{userFile.gmt_modified} and</if>",
            "<if test=\"userFile.size != null\">size = #{userFile.size} and</if>",
            "`delete` = 0",
            "</where>",
            "order by fileType desc",
            "limit #{st}, #{len}",
            "</script>"
    })
    List<UserFile> queryByUserFile(UserFile userFile, int st, int len);

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
            "order by fileType desc",
            "</script>"
    })
    List<UserFile> queryAllUserFile(UserFile userFile);

    @Select({
            "<script>",
            "select count(*) from user_file",
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
            "<if test=\"size != null\">size = #{userFile.size} and</if>",
            "`delete` = 0",
            "</where>",
            "</script>"
    })
    int queryCountByUserFile(UserFile userFile);

    @Update({
            "<script>",
            "update user_file",
            "<set>",
            "<if test=\"user.id != null\">uid = #{user.id},</if>",
            "<if test=\"file.id != null\">file_id = #{file.id},</if>",
            "<if test=\"file_name != null\">file_name = #{file_name},</if>",
            "<if test=\"dir != null\">dir = #{dir},</if>",
            "<if test=\"fileType != null\">fileType = #{fileType},</if>",
            "<if test=\"`delete` != null\">`delete` = #{delete},</if>",
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


    @Update("update user_file set `delete` = 1, delete_time = #{date} where uid = #{uid} and dir like  concat(#{dir}, '%') and `delete` = 0")
    int updateDeleteByDir(@Param("uid") int uid, @Param("dir") String dir, @Param("date") Date date);

}
