package com.udacity.jwdnd.course1.cloudstorage.mappers;

import com.udacity.jwdnd.course1.cloudstorage.models.File;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FileMapper {

    @Insert("INSERT INTO FILES (fileId, filename, contenttype, filesize, userid, filedata) " +
            "VALUES(#{fileId}, #{filename}, #{contenttype}, #{filesize}, #{userid}, #{filedata})")
    @Options(useGeneratedKeys = true, keyProperty = "fileId")
    int insertFile(File file);

    @Select("SELECT * from FILES WHERE userid = #{userid}")
    List<File> getFilesForUser(Integer userid);

    @Select("SELECT * from FILES WHERE userid = #{userid} AND filename = #{filename}")
    File getFileByFilename(Integer userid, String filename);

    @Select("SELECT * from FILES WHERE fileId = #{fileId}")
    File getFileById(Integer fileId);

    @Delete("DELETE from FILES WHERE fileId = #{fileId}")
    int deleteFileById(Integer fileId);

}
