package org.cloud.uploadanddownload.mapper;

import com.cloud.common.pojo.file.Chunk;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface ChunkMapper {
    @Select("select sum(size) from chunk where user_file_id = #{user_file_id}")
    Long getChunkSize(int user_file_id);

    @Select("select * from chunk where user_file_id = #{user_file_id} and chunk_number = #{chunk_number}")
    Chunk getChunk(Integer user_file_id, Integer chunk_number);

    @Update("delete from chunk where id = #{id}")
    int deleteChunk(int id);

    @Select("select chunk_number + 1 from chunk where user_file_id = #{user_file_id} and status != 'UPLOADING' order by chunk_number desc limit 0, 1")
    Integer getNext(int user_file_id);

}
