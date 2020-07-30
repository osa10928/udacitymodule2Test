package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mappers.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.models.File;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FileService {

    private final FileMapper fileMapper;

    public FileService(FileMapper fileMapper) {
        this.fileMapper = fileMapper;
    }

    public boolean isFileNameTaken(String filename, Integer userid) {
        return this.fileMapper.getFileByFilename(userid, filename) == null;
    }

    public int uploadFile(File file) {
        return fileMapper.insertFile(new File(null, file.getFilename(), file.getContenttype(), file.getFilesize(),
                file.getUserid(), file.getFiledata()));
    }

    public List<File> getFilesByUserid(Integer userid) {
        return fileMapper.getFilesForUser(userid);
    }

    public File getFileById(Integer fileId) {
        return fileMapper.getFileById(fileId);
    }

    public int deleteFileById(Integer fileId) {
        return fileMapper.deleteFileById(fileId);
    }

}
