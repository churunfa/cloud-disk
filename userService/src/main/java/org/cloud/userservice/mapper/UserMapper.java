package org.cloud.userservice.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserMapper {

    @Select("SELECT sum(size) FROM user_file where uid = #{id} and `delete` = 0")
    Long getUserSize(int id);

    @Update("update `user` set count_size = #{size} where id = #{uid}")
    int updateUserSize(Long size, int uid);

}
