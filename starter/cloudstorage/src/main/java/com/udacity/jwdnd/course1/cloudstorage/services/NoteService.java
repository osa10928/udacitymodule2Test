package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mappers.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.models.Note;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {

    private NoteMapper noteMapper;

    public NoteService(NoteMapper noteMapper) {
        this.noteMapper = noteMapper;
    }

    public int insertNote(Note note) {
        return noteMapper.insertNote(new Note(note.getNoteid(), note.getNotetitle(),
                note.getNotedescription(), note.getUserid()));
    }

    public List<Note> getNotesByUserId(Integer userid) {
        return noteMapper.getNotesByUserid(userid);
    }

    public int updateNoteById(Integer noteid, String notetitle, String notedescription) {
        return noteMapper.updateNoteById(noteid, notetitle, notedescription);
    }

    public int deleteNoteById(Integer noteid) {
        return noteMapper.deleteNoteById(noteid);
    }
}
