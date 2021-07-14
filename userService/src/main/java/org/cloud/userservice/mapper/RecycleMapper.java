package org.cloud.userservice.mapper;

import com.cloud.common.pojo.file.Recycle;
import com.cloud.common.pojo.file.UserFile;
import org.apache.ibatis.annotations.*;

import java.util.Date;
import java.util.List;

@Mapper
public interface RecycleMapper {

    @Insert("insert into recycle(user_file_id, recycle_path, recycle_name) values(#{userFile.id}, #{recycle_path}, #{recycle_name})")
    int insertRecycle(Recycle recycle);

    @Update("update recycle set recycle_path = #{recycle_path}, recycle_name = #{recycle_name} where user_file_id = #{userFile.id}")
    int update(Recycle recycle);

    @Update("delete from recycle where user_file_id  = #{id}")
    int removeRecycle(int id);


    @Update("delete from user_file where id = #{id}")
    int removeUserFile(int id);

    @Update("update user_file set `delete` = 1, delete_time = #{date} where id = #{id}")
    int deleteUserFile(int id, Date date);


    @Select({
            "<script>",
            "select * from recycle",
            "<where>",
            "<if test=\"userFile.id != null\">user_file_id = #{userFile.id}</if>",
            "<if test=\"recycle_path != null\">and recycle_path = #{recycle_path}</if>",
            "<if test=\"recycle_name != null\">and recycle_name = #{recycle_name}</if>",
            "</where>",
            "</script>"
    })
    List<Recycle> queryByRecycle(Recycle recycle);

    @Select("select id, recycle_path, recycle_name from user_file, recycle where user_file.id = recycle.user_file_id and user_file.`delete` = '1' and recycle.recycle_path = #{dir} and user_file.uid = #{uid}")
    @Results({
            @Result(column = "id", property = "userFile", one = @One(select = "org.cloud.userservice.mapper.FileMapper.queryUserFileById"))
    })
    List<Recycle> queryAllUserFile(int uid, String dir);

    @Select("select id, recycle_path, recycle_name from user_file, recycle where user_file.id = recycle.user_file_id and user_file.`delete` = '1' and recycle.recycle_path = #{dir} and user_file.uid = #{uid} order by fileType desc limit #{st}, #{len}")
    @Results({
            @Result(column = "id", property = "userFile", one = @One(select = "org.cloud.userservice.mapper.FileMapper.queryUserFileById"))
    })
    List<Recycle> queryUserFile(int uid, String dir, int st, int len);

    @Select("select count(*) from user_file, recycle where user_file.id = recycle.user_file_id and user_file.`delete` = '1' and recycle.recycle_path = #{dir} and user_file.uid = #{uid}")
    int queryDeleteCountUserFile(int uid, String dir);


    @Select("select id from user_file, recycle where uid = #{uid} and id = user_file_id and recycle_path like concat(#{dir}, '%')")
    List<Integer> queryListByDir(Integer uid, String dir);

}
