package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mappers.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.models.Credential;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.List;

@Service
public class CredentialService {
    private CredentialMapper credentialMapper;
    private EncryptionService encryptionService;

    public CredentialService(CredentialMapper credentialMapper, EncryptionService encryptionService) {
        this.credentialMapper = credentialMapper;
        this.encryptionService = encryptionService;
    }

    public int insertCredential(Credential credential) {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        String encodedSalt = Base64.encodeBase64String(salt);
        String encryptedPassword = encryptionService.encryptValue(credential.getPassword(), encodedSalt);
        return credentialMapper.insertCredential(new Credential(credential.getCredentialid(), credential.getUrl(),
                credential.getUsername(), encodedSalt, encryptedPassword, credential.getUserid()));
    }

    public List<Credential> getCredentialsByUserId(Integer userid) {
        return credentialMapper.getCredentialsByUserid(userid);
    }

    public Credential getCredentialById(Integer credentialid) {
        return credentialMapper.getCredentialsById(credentialid);
    }

    public int updateCredentialById(Integer credentialid, String url, String username, String password) {
        return credentialMapper.updateCredentialById(credentialid, url, username, password);
    }

    public int deleteCredentialById(Integer credentialid) {
        return credentialMapper.deleteCredentialById(credentialid);
    }
}
