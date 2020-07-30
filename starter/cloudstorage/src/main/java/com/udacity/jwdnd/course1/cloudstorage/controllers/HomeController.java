package com.udacity.jwdnd.course1.cloudstorage.controllers;

import com.udacity.jwdnd.course1.cloudstorage.models.Credential;
import com.udacity.jwdnd.course1.cloudstorage.models.File;
import com.udacity.jwdnd.course1.cloudstorage.models.Note;
import com.udacity.jwdnd.course1.cloudstorage.models.User;
import com.udacity.jwdnd.course1.cloudstorage.services.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpSession;
import java.io.IOException;

@Controller
@RequestMapping("/home")
public class HomeController {

    private final FileService fileService;
    private final NoteService noteService;
    private final CredentialService credentialService;
    private final EncryptionService encryptionService;
    private Integer USERID;

    public HomeController(FileService fileService, NoteService noteService,
                          CredentialService credentialService, EncryptionService encryptionService) {
        this.fileService = fileService;
        this.noteService = noteService;
        this.credentialService = credentialService;
        this.encryptionService = encryptionService;
    }

    @GetMapping()
    public String homeView(HttpSession httpSession, Model model) {
        USERID = (Integer) httpSession.getAttribute("userid");
        model.addAttribute("files", fileService.getFilesByUserid(USERID));
        model.addAttribute("notes", noteService.getNotesByUserId(USERID));
        model.addAttribute("credentials", credentialService.getCredentialsByUserId(USERID));
        model.addAttribute("encryptionService", encryptionService);
        return "home";
    }

    @PostMapping("/file")
    public RedirectView uploadFile(@RequestParam("fileUpload") MultipartFile file, HttpSession httpSession, RedirectAttributes redirectAttributes) {
        String crudFileError = null;
        USERID = (Integer) httpSession.getAttribute("userid");

        if (!fileService.isFileNameTaken(file.getOriginalFilename(), USERID)) {
            crudFileError = "You have already used this filename. Please select another file";
        }

        if (crudFileError == null) {
            try {
                fileService.uploadFile(new File(null, file.getOriginalFilename(), file.getContentType(),
                        Long.toString(file.getSize()), USERID, file.getBytes()));
            } catch (IOException e) {
                crudFileError = e.getMessage();
            }
        }

        if (crudFileError == null) {
            redirectAttributes.addFlashAttribute("crudFileSuccess", "File upload was a success!");
        } else {
            redirectAttributes.addFlashAttribute("crudFileError", crudFileError);
        }

        RedirectView redirectView = new RedirectView("/home", true);
        return redirectView;
    }

    @GetMapping("/file/download")
    public ResponseEntity downloadFile(@RequestParam("fileid") Integer fileId) {
        File file = fileService.getFileById(fileId);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(file);
    }

    @GetMapping("/file/delete")
    public RedirectView deleteFile(@RequestParam("fileid") Integer fileId, RedirectAttributes redirectAttributes) {
        String crudFileError = null;

        int rowsDeleted = fileService.deleteFileById(fileId);
        if (rowsDeleted <= 0 ) {
            crudFileError = "There was an error deleting the file you selected. Please try again";
        }

        if (crudFileError == null) {
            redirectAttributes.addFlashAttribute("crudFileSuccess", "File has been successfully deleted");
        } else {
            redirectAttributes.addFlashAttribute("crudFileError", crudFileError);
        }

        RedirectView redirectView = new RedirectView("/home", true);
        return redirectView;
    }

    @PostMapping("/note/insert")
    public RedirectView insertOrUpdateNote(Note note, HttpSession httpSession, RedirectAttributes redirectAttributes) {
        String crudNoteError = null;
        String crudNoteSuccess = null;
        USERID = (Integer) httpSession.getAttribute("userid");
        int rowsAdded = 0;

        if (note.getNoteid() == null) {
            note.setUserid(USERID);
            rowsAdded = noteService.insertNote(note);
            if (rowsAdded < 0) {
                crudNoteError = "There was an error inserting you note. Please try again";
            } else {
                crudNoteSuccess = "Successfully added Note";
            }
        } else {
            rowsAdded = noteService.updateNoteById(note.getNoteid(), note.getNotetitle(), note.getNotedescription());
            if (rowsAdded < 0) {
                crudNoteError = "There was an error updating you note. Please try again";
            } else {
                crudNoteSuccess = "Successfully updated Note";
            }
        }

        System.out.println(crudNoteSuccess);
        if (crudNoteError == null) {
            redirectAttributes.addFlashAttribute("crudNoteSuccess", crudNoteSuccess);
        } else {
            redirectAttributes.addFlashAttribute("crudNoteError", crudNoteError);
        }

        RedirectView redirectView = new RedirectView("/home", true);
        return redirectView;
    }

    @GetMapping("/note/delete")
    public RedirectView deleteNote(@RequestParam("noteid") Integer noteid, RedirectAttributes redirectAttributes) {
        String crudNoteError = null;

        int rowsDeleted = noteService.deleteNoteById(noteid);
        if (rowsDeleted <= 0 ) {
            crudNoteError = "There was an error deleting the note you selected. Please try again";
        }

        if (crudNoteError == null) {
            redirectAttributes.addFlashAttribute("crudNoteSuccess", "Note has been successfully deleted");
        } else {
            redirectAttributes.addFlashAttribute("crudNoteError", crudNoteError);
        }

        RedirectView redirectView = new RedirectView("/home", true);
        return redirectView;
    }

    @PostMapping("/credential/insert")
    public RedirectView insertOrUpdateCredential(Credential credential, HttpSession httpSession, RedirectAttributes redirectAttributes) {
        String crudCredentialError = null;
        String crudCredentialSuccess = null;
        USERID = (Integer) httpSession.getAttribute("userid");
        int rowsAdded = 0;

        if (credential.getCredentialid() == null) {
            credential.setUserid(USERID);
            rowsAdded = credentialService.insertCredential(credential);
            if (rowsAdded < 0) {
                crudCredentialError = "There was an error inserting you credential. Please try again";
            } else {
                crudCredentialSuccess = "Successfully added Credential";
            }
        } else {
            String credentialKey = credentialService.getCredentialById(credential.getCredentialid()).getKey();
            credential.setPassword(encryptionService.encryptValue(credential.getPassword(), credentialKey));
            rowsAdded = credentialService.updateCredentialById(credential.getCredentialid(),
                    credential.getUrl(), credential.getUsername(), credential.getPassword());
            if (rowsAdded < 0) {
                crudCredentialError = "There was an error updating you credential. Please try again";
            } else {
                crudCredentialSuccess = "Successfully updated Credential";
            }
        }

        if (crudCredentialError == null) {
            redirectAttributes.addFlashAttribute("crudCredentialSuccess", crudCredentialSuccess);
        } else {
            redirectAttributes.addFlashAttribute("crudCredentialError", crudCredentialError);
        }

        RedirectView redirectView = new RedirectView("/home", true);
        return redirectView;
    }

    @GetMapping("/credential/delete")
    public RedirectView deleteCredential(@RequestParam("credentialid") Integer credentialid, RedirectAttributes redirectAttributes) {
        String crudCredentialError = null;

        int rowsDeleted = credentialService.deleteCredentialById(credentialid);
        if (rowsDeleted <= 0 ) {
            crudCredentialError = "There was an error deleting the note you selected. Please try again";
        }

        if (crudCredentialError == null) {
            redirectAttributes.addFlashAttribute("crudCredentialSuccess", "Credential has been successfully deleted");
        } else {
            redirectAttributes.addFlashAttribute("crudCredentialError", crudCredentialError);
        }

        RedirectView redirectView = new RedirectView("/home", true);
        return redirectView;
    }
}
