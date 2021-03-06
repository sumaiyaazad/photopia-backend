package com.taaha.photopia.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;


/**
 * Created by Ikhiloya Imokhai on 5/7/20.
 */
public interface StorageService {

    String uploadImage(MultipartFile theImage, String theUsername) throws Exception;

    ArrayList<String> fetchUserImage(String theUsername) throws Exception;

    ResponseEntity<Object> downloadFile(String fileUrl, HttpServletRequest request) throws Exception;

    void deleteImage(String theImage, String theUsername);

    void createFolder(String theUsername) throws Exception;

    void deleteFolder(String theUsername);
}