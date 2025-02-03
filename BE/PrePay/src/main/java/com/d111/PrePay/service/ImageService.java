package com.d111.PrePay.service;

import com.d111.PrePay.model.Team;
import com.d111.PrePay.repository.TeamRepository;
import com.google.cloud.storage.Acl;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final TeamRepository teamRepository;

    private final Bucket bucket;

    public String uploadImage(MultipartFile file, Long teamId) throws IOException{
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Blob blob = bucket.create(fileName, file.getInputStream(),file.getContentType());

        blob.createAcl(Acl.of(Acl.User.ofAllUsers(),Acl.Role.READER));

        String imgUrl = "https://storage.googleapis.com/" + bucket.getName() + "/" + fileName;

        return imgUrl;
    }

}
