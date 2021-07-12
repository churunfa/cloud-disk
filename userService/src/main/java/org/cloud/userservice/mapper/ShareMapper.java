package org.cloud.userservice.mapper;

import com.cloud.common.pojo.file.Share;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ShareMapper {

    @Insert({
            "<script>",
            "insert into share",
            "<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">",
            "<if test=\"id != null\">id,</if>",
            "<if test=\"user.id != null\">uid,</if>",
            "<if test=\"userFile.id != null\">fid,</if>",
            "<if test=\"status != null\">status,</if>",
            "<if test=\"token != null\">token,</if>",
            "<if test=\"gmt_create != null\">gmt_create,</if>",
            "<if test=\"gmt_modified != null\">gmt_modified,</if>",
            "<if test=\"invalid_time != null\">invalid_time,</if>",
            "</trim>",
            "<trim prefix=\"values (\" suffix=\")\" suffixOverrides=\",\">",
            "<if test=\"id != null\">#{id},</if>",
            "<if test=\"user.id != null\">#{user.id},</if>",
            "<if test=\"userFile.id != null\">#{userFile.id},</if>",
            "<if test=\"status != null\">#{status},</if>",
            "<if test=\"token != null\">#{token},</if>",
            "<if test=\"gmt_create != null\">#{gmt_create},</if>",
            "<if test=\"gmt_modified != null\">#{gmt_modified},</if>",
            "<if test=\"invalid_time != null\">#{invalid_time},</if>",
            "</trim>",
            "</script>"
    })
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int insert(Share share);

    @Select({
            "<script>",
            "select * from share",
            "<where>",
            "<if test=\"id != null\">id = #{id} and</if>",
            "<if test=\"user.id != null\">uid = #{user.id} and</if>",
            "<if test=\"userFile.id != null\">fid = #{userFile.id} and</if>",
            "<if test=\"token != null\">token = #{token} and</if>",
            "<if test=\"gmt_create != null\">gmt_create = #{gmt_create} and</if>",
            "<if test=\"gmt_modified != null\">gmt_modified = #{gmt_modified} and</if>",
            "<if test=\"invalid_time != null\">invalid_time = #{invalid_time} and</if>",
            "</where>",
            "status != 'INVALID'",
            "</script>"
    })
    @Results({
            @Result(column = "uid", property = "user", one = @One(select = "org.cloud.userservice.mapper.FileMapper.queryUserById")),
            @Result(column = "fid", property = "userFile", one = @One(select = "org.cloud.userservice.mapper.FileMapper.queryUserFileById"))
    })
    List<Share> queryAllShare(Share share);

    @Select({
            "<script>",
            "select * from share",
            "<where>",
            "<if test=\"id != null\">id = #{id} and</if>",
            "<if test=\"user.id != null\">uid = #{user.id} and</if>",
            "<if test=\"userFile.id != null\">fid = #{userFile.id} and</if>",
            "<if test=\"token != null\">token = #{token} and</if>",
            "<if test=\"gmt_create != null\">gmt_create = #{gmt_create} and</if>",
            "<if test=\"gmt_modified != null\">gmt_modified = #{gmt_modified} and</if>",
            "<if test=\"invalid_time != null\">invalid_time = #{invalid_time} and</if>",
            "</where>",
            "status != 'INVALID'",
            "limit #{st}, #{len}",
            "</script>"
    })
    @Results({
            @Result(column = "uid", property = "user", one = @One(select = "org.cloud.userservice.mapper.FileMapper.queryUserById")),
            @Result(column = "fid", property = "userFile", one = @One(select = "org.cloud.userservice.mapper.FileMapper.queryUserFileById"))
    })
    List<Share> queryShare(Share share, int st, int lim);


    @Update({
            "<script>",
            "update share",
            "<set>",
            "<if test=\"user.id != null\">uid = #{user.id},</if>",
            "<if test=\"userFile.id != null\">fid = #{userFile.id},</if>",
            "<if test=\"token != null\">token = #{token},</if>",
            "<if test=\"gmt_create != null\">gmt_create = #{gmt_create},</if>",
            "<if test=\"status != null\">status = #{status},</if>",
            "<if test=\"gmt_modified != null\">gmt_modified = #{gmt_modified},</if>",
            "<if test=\"invalid_time != null\">invalid_time = #{invalid_time},</if>",
            "</set>",
            "where id = #{id}",
            "</script>"
    })
    int updateShare(Share share);

}
