package com.cloud.resources.mapper;

import com.cloud.common.pojo.file.Chunk;
import com.cloud.common.pojo.file.UserFile;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ChunkMapper {

    @Select("select * from user_file where id = #{id}")
    UserFile queryUserFile(int id);

    @Insert(value = {
            "<script>",
            "insert into chunk",
            "<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">",
            "<if test=\"fileDB.id != null\">file_id,</if>",
            "<if test=\"user_file_id != null\">user_file_id,</if>",
            "<if test=\"name != null\">name,</if>",
            "<if test=\"path != null\">path,</if>",
            "<if test=\"md5 != null\">md5,</if>",
            "<if test=\"gmt_create != null\">gmt_create,</if>",
            "<if test=\"gmt_modified != null\">gmt_modified,</if>",
            "<if test=\"size != null\">size,</if>",
            "<if test=\"status != null\">status,</if>",
            "<if test=\"chunk_number != null\">chunk_number</if>",
            "</trim>",
            "<trim prefix=\"values (\" suffix=\")\" suffixOverrides=\",\">",
            "<if test=\"fileDB.id != null\">#{fileDB.id},</if>",
            "<if test=\"user_file_id != null\">#{user_file_id},</if>",
            "<if test=\"name != null\">#{name},</if>",
            "<if test=\"path != null\">#{path},</if>",
            "<if test=\"md5 != null\">#{md5},</if>",
            "<if test=\"gmt_create != null\">#{gmt_create},</if>",
            "<if test=\"gmt_modified != null\">#{gmt_modified},</if>",
            "<if test=\"size != null\">#{size},</if>",
            "<if test=\"status != null\">#{status},</if>",
            "<if test=\"chunk_number != null\">#{chunk_number}</if>",
            "</trim>",
            "</script>"
    })
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int insert(Chunk chunk);

    @Update({
            "<script>",
            "update chunk",
            "<set>",
            "<if test=\"fileDB.id != null\">file_id = #{fileDB.id},</if>",
            "<if test=\"user_file_id != null\">user_file_id = #{user_file_id},</if>",
            "<if test=\"name != null\">name = #{name},</if>",
            "<if test=\"path != null\">path = #{path},</if>",
            "<if test=\"md5 != null\">md5 = #{md5},</if>",
            "<if test=\"gmt_create != null\">gmt_create = #{gmt_create},</if>",
            "<if test=\"gmt_modified != null\">gmt_modified = #{gmt_modified},</if>",
            "<if test=\"size != null\">size = #{size},</if>",
            "<if test=\"status != null\">status = #{status},</if>",
            "<if test=\"chunk_number != null\">chunk_number = #{chunk_number},</if>",
            "</set>",
            "where id = #{id}",
            "</script>"
    })
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int update(Chunk chunk);

    @Select("select * from chunk where user_file_id = #{user_file_id} and chunk_number = #{chunk_number}")
    Chunk queryChunk(Integer user_file_id, Integer chunk_number);

    @Select("select * from chunk where user_file_id = #{user_file_id}")
    List<Chunk> queryListChunk(Integer user_file_id);

    @Select("select count(*) from chunk where user_file_id = #{user_file_id}")
    int queryCountByUserFileId(Integer user_file_id);

    @Update("delete from chunk where id = #{id}")
    int delete(int id);

    @Update("delete from chunk where user_file_id = #{uid} and chunk_number = #{chunk_number}")
    int deleteByUserFileIdAndBlock(int uid, int chunk_number);
}
