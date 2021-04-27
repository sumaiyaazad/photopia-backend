package com.taaha.photopia.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.ReadChannel;
import com.google.cloud.storage.*;
import com.taaha.photopia.model.FirebaseCredential;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.apache.commons.io.IOUtils;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.Channels;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;
import java.util.Objects;

@Service
public class StorageServiceFirebaseImpl implements StorageService {


        private final Environment environment;

        private StorageOptions storageOptions;
        private String bucketName;
        private String projectId;

        public StorageServiceFirebaseImpl(Environment environment) {
            this.environment = environment;
        }

        @PostConstruct
        private void initializeFirebase() throws Exception {
            bucketName = environment.getRequiredProperty("FIREBASE_BUCKET_NAME");
            projectId = environment.getRequiredProperty("FIREBASE_PROJECT_ID");

            InputStream firebaseCredential = createFirebaseCredential();
            this.storageOptions = StorageOptions.newBuilder()
                    .setProjectId(projectId)
                    .setCredentials(GoogleCredentials.fromStream(firebaseCredential)).build();

        }

        @Override
        @Transactional
        public String[] uploadFile(MultipartFile multipartFile) throws IOException {
            System.out.println("bucket name====" + bucketName);
            File file = convertMultiPartToFile(multipartFile);
            Path filePath = file.toPath();
            String objectName = generateFileName(multipartFile);
            System.out.println("bucket name1====" + bucketName);
            Storage storage = storageOptions.getService();
            System.out.println("bucket name2====" + bucketName);
            BlobId blobId = BlobId.of(bucketName, objectName);
            System.out.println("bucket name3====" + bucketName);
            BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
            System.out.println("bucket name4====" + bucketName);
            Blob blob = storage.create(blobInfo, Files.readAllBytes(filePath));
            System.out.println("bucket name5====" + bucketName);
            System.out.println("File " + filePath + " uploaded to bucket " + bucketName + " as " + objectName);
            System.out.println("bucket name6====" + bucketName);
            return new String[]{"fileUrl", objectName};
        }


    @Override
        public ResponseEntity<Object> downloadFile(String fileName, HttpServletRequest request) throws Exception {
            Storage storage = storageOptions.getService();

            Blob blob = storage.get(BlobId.of(bucketName, fileName));
            ReadChannel reader = blob.reader();
            InputStream inputStream = Channels.newInputStream(reader);

            byte[] content = null;
            System.out.println("File downloaded successfully.");

            content = IOUtils.toByteArray(inputStream);

            final ByteArrayResource byteArrayResource = new ByteArrayResource(content);

            return ResponseEntity
                    .ok()
                    .contentLength(content.length)
                    .header("Content-type", "application/octet-stream")
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                    .body(byteArrayResource);

        }


        private File convertMultiPartToFile(MultipartFile file) throws IOException {
            File convertedFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
            FileOutputStream fos = new FileOutputStream(convertedFile);
            fos.write(file.getBytes());
            fos.close();
            return convertedFile;
        }

        private String generateFileName(MultipartFile multiPart) {
            return new Date().getTime() + "-" + Objects.requireNonNull(multiPart.getOriginalFilename()).replace(" ", "_");
        }

        private InputStream createFirebaseCredential() throws Exception {
            FirebaseCredential firebaseCredential = new FirebaseCredential();
            //private key
            String privateKey = environment.getRequiredProperty("FIREBASE_PRIVATE_KEY").replace("\\n", "\n");

            firebaseCredential.setType(environment.getRequiredProperty("FIREBASE_TYPE"));
            System.out.println(environment.getRequiredProperty("FIREBASE_TYPE"));
            firebaseCredential.setProject_id(projectId);
            System.out.println(projectId);
            firebaseCredential.setPrivate_key_id("FIREBASE_PRIVATE_KEY_ID");
            System.out.println(environment.getRequiredProperty("FIREBASE_PRIVATE_KEY_ID"));
            firebaseCredential.setPrivate_key(privateKey);
            System.out.println(privateKey);
            firebaseCredential.setClient_email(environment.getRequiredProperty("FIREBASE_CLIENT_EMAIL"));
            System.out.println(environment.getRequiredProperty("FIREBASE_CLIENT_EMAIL"));
            firebaseCredential.setClient_id(environment.getRequiredProperty("FIREBASE_CLIENT_ID"));
            System.out.println(environment.getRequiredProperty("FIREBASE_CLIENT_ID"));
            firebaseCredential.setAuth_uri(environment.getRequiredProperty("FIREBASE_AUTH_URI"));
            System.out.println(environment.getRequiredProperty("FIREBASE_AUTH_URI"));
            firebaseCredential.setToken_uri(environment.getRequiredProperty("FIREBASE_TOKEN_URI"));
            System.out.println(environment.getRequiredProperty("FIREBASE_TOKEN_URI"));
            firebaseCredential.setAuth_provider_x509_cert_url(environment.getRequiredProperty("FIREBASE_AUTH_PROVIDER_X509_CERT_URL"));
            System.out.println(environment.getRequiredProperty("FIREBASE_AUTH_PROVIDER_X509_CERT_URL"));
            firebaseCredential.setClient_x509_cert_url(environment.getRequiredProperty("FIREBASE_CLIENT_X509_CERT_URL"));
            System.out.println(environment.getRequiredProperty("FIREBASE_CLIENT_X509_CERT_URL"));

            ObjectMapper mapper = new ObjectMapper();
            String jsonString = mapper.writeValueAsString(firebaseCredential);

            //convert jsonString string to InputStream using Apache Commons
            return IOUtils.toInputStream(jsonString);
        }

}