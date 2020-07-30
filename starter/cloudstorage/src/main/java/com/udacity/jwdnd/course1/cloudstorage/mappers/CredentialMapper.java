package com.udacity.jwdnd.course1.cloudstorage.mappers;

import com.udacity.jwdnd.course1.cloudstorage.models.Credential;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CredentialMapper {

    @Insert("INSERT INTO Credentials (credentialid, url, username, key, password, userid) " +
            "VALUES(#{credentialid}, #{url}, #{username}, #{key}, #{password}, #{userid})")
    @Options(useGeneratedKeys = true, keyProperty = "credentialid")
    int insertCredential(Credential credential);

    @Select("SELECT * FROM Credentials WHERE userId = #{userid}")
    List<Credential> getCredentialsByUserid(Integer userid);

    @Select("SELECT * FROM Credentials WHERE credentialId = #{credentialid}")
    Credential getCredentialsById(Integer credentialid);

    @Update("UPDATE Credentials SET url = #{url}, username = #{username}, password = #{password}" +
            "WHERE credentialid = #{credentialid}")
    int updateCredentialById(Integer credentialid, String url, String username, String password);

    @Delete("DELETE FROM Credentials WHERE credentialid = #{credentialid}")
    int deleteCredentialById(Integer credentialid);

}
