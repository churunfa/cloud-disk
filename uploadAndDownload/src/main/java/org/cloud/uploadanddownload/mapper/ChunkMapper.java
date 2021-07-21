package org.cloud.uploadanddownload.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ChunkMapper {
    @Select("select sum(size) from chunk where user_file_id = #{user_file_id}")
    Long getChunkSize(int user_file_id);
}
