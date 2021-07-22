package org.cloud.userservice.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserMapper {

    @Select("select sum(size) from user_file where uid = 1 and `delete` = 0 or (`delete` = #{uid} and file_id is null)")
    Long getUserSize(int id);

    @Update("update user set count_size = #{size} where id = #{uid}")
    int updateUserSize(Long size, int uid);
}
