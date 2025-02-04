package com.d111.PrePay.config;


import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;

@Configuration
public class FirebaseConfig {
    @Bean
    public Storage firebaseStorage() throws IOException {
        FileInputStream serviceAccount = new FileInputStream("src/main/resources/firebase-service-key.json"); // 파이어베이스 프로젝트 변경 시 수정

        return StorageOptions.newBuilder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build()
                .getService();
    }

    @Bean
    public Bucket fireBaseBucket(Storage storage){
        return storage.get("kyung0216-10d14.appspot.com"); // 파이어베이스 프로젝트 변경 시 수정
    }



}
