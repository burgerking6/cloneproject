package com.example.cloneburgerking.service;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import com.example.cloneburgerking.entity.Menu;
import com.example.cloneburgerking.repository.MenuRepository;
import com.example.cloneburgerking.status.CustomException;
import com.example.cloneburgerking.status.ErrorCode;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3Service {
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    private final AmazonS3 amazonS3;



    public String uploadFile(MultipartFile File) throws IOException {
        System.out.println("---------S3 Service-----------");

        String fileName = UUID.randomUUID().toString() + "_" + File.getOriginalFilename();

        //파일 형식 구하기
        String ext = fileName.split("\\.")[1];
        String contentType = "";

        //content type을 지정해서 올려주지 않으면 자동으로 "application/octet-stream"으로 고정이 되서 링크 클릭시 웹에서 열리는게 아니라 자동 다운이 시작됨.
        switch (ext) {
            case "jpeg" -> {
                contentType = "image/jpeg";
            }
            case "png" -> {
                contentType = "image/png";
            }
            case "txt" -> {
                contentType = "text/plain";
            }
            case "csv" -> {
                contentType = "text/csv";
            }
        }

        try {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(contentType);

            amazonS3.putObject(new PutObjectRequest(bucket, fileName, File.getInputStream(), metadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (SdkClientException e) {
            e.printStackTrace();
        }


        //object 정보 가져오기
        ListObjectsV2Result listObjectsV2Result = amazonS3.listObjectsV2(bucket);
        List<S3ObjectSummary> objectSummaries = listObjectsV2Result.getObjectSummaries();

        for (S3ObjectSummary object : objectSummaries) {
            System.out.println("object = " + object.toString());
        }
        return amazonS3.getUrl(bucket, fileName).toString();
    }



    public String uploadFile(String fileName, byte[] content, String contentType) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(contentType);

        amazonS3.putObject(new PutObjectRequest(bucket, fileName, new ByteArrayInputStream(content), metadata)
                .withCannedAcl(CannedAccessControlList.PublicRead));

        return amazonS3.getUrl(bucket, fileName).toString();
    }

    public void deleteFile(String key) {
        amazonS3.deleteObject(bucket, key);
    }

}


