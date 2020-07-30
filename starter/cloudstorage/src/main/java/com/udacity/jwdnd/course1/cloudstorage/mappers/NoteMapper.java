package com.udacity.jwdnd.course1.cloudstorage.mappers;

import com.udacity.jwdnd.course1.cloudstorage.models.Note;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface NoteMapper {

    @Insert("INSERT INTO Notes (noteid, notetitle, notedescription, userid) " +
            "VALUES(#{noteid}, #{notetitle}, #{notedescription}, #{userid})")
    @Options(useGeneratedKeys = true, keyProperty = "noteid")
    int insertNote(Note note);

    @Select("SELECT * FROM Notes WHERE userId = #{userid}")
    List<Note> getNotesByUserid(Integer userid);

    @Update("UPDATE Notes SET notetitle = #{notetitle}, notedescription = #{notedescription}" +
            "WHERE noteid = #{noteid}")
    int updateNoteById(Integer noteid, String notetitle, String notedescription);

    @Delete("DELETE FROM Notes WHERE noteid = #{noteid}")
    int deleteNoteById(Integer noteid);
}
