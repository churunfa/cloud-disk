package org.cloud.uploadanddownload.mapper;

import com.cloud.common.pojo.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {
    @Select("select * from user where username = #{username} and deleted = 0")
    User queryUserByUsername(String username);

    @Select("select * from user where id = #{id} and deleted = 0;")
    User queryUserById(int id);

    @Insert({
            "<script>",
            "insert into user",
            "<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">",
            "<if test=\"username != null\">username,</if>",
            "<if test=\"password != null\">password,</if>",
            "<if test=\"deleted != null\">delete,</if>",
            "<if test=\"locked != null\">locked,</if>",
            "<if test=\"gmt_create != null\">gmt_create,</if>",
            "<if test=\"gmt_modified != null\">gmt_modified,</if>",
            "<if test=\"last_login != null\">last_login,</if>",
            "<if test=\"count_size != null\">count_size,</if>",
            "<if test=\"total_size != null\">total_size,</if>",
            "</trim>",
            "<trim prefix=\"values (\" suffix=\")\" suffixOverrides=\",\">",
            "<if test=\"username != null\">#{username},</if>",
            "<if test=\"password != null\">#{password},</if>",
            "<if test=\"deleted != null\">#{delete},</if>",
            "<if test=\"locked != null\">#{locked},</if>",
            "<if test=\"gmt_create != null\">#{gmt_create},</if>",
            "<if test=\"gmt_modified != null\">#{gmt_modified},</if>",
            "<if test=\"last_login != null\">#{last_login},</if>",
            "<if test=\"count_size != null\">#{count_size},</if>",
            "<if test=\"total_size != null\">#{total_size},</if>",
            "</trim>",
            "</script>"
    })
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int insertUser(User user);

    @Update({
            "<script>",
            "update user",
            "<set>",
            "<if test=\"username != null\">username = #{username},</if>",
            "<if test=\"password != null\">password = #{password},</if>",
            "<if test=\"deleted != null\">delete = #{delete},</if>",
            "<if test=\"locked != null\">locked = #{locked},</if>",
            "<if test=\"gmt_create != null\">gmt_create = #{gmt_create},</if>",
            "<if test=\"gmt_modified != null\">gmt_modified = #{gmt_modified},</if>",
            "<if test=\"last_login != null\">last_login = #{last_login},</if>",
            "<if test=\"count_size != null\">count_size = #{count_size},</if>",
            "<if test=\"total_size != null\">total_size = #{total_size},</if>",
            "</set>",
            "where id = #{id}",
            "</script>"
    })
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int updateUser(User user);

}
