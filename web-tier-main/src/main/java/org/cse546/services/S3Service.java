package org.cse546.services;

import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.util.IOUtils;
import org.cse546.utility.AWSUtility;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.*;

@Service
public class S3Service {

    private static final Logger logger = LoggerFactory.getLogger(S3Service.class);

    @Autowired
    private AWSUtility awsUtility;

    public List<String> saveImagesToS3(MultipartFile[] files) {
        List<String> fileNames = storeImagesToS3StorageBucket(files);
        return fileNames;
    }

    private List<String> storeImagesToS3StorageBucket(MultipartFile[] files){
        List<String> fileNameList = new ArrayList<String>();
        for (MultipartFile multipartFile : files) {
            String fileName = multipartFile.getOriginalFilename();
            String keyName = fileName;
            File file = null;
            try {
                byte[] contents = IOUtils.toByteArray(multipartFile.getInputStream());
                InputStream stream = new ByteArrayInputStream(contents);
                ObjectMetadata meta = new ObjectMetadata();
                meta.setContentLength(contents.length);
                meta.setContentType("jpeg");

                logger.info("Saving Image to s3 bucket: {} keyname: {} metadata: {}",keyName,meta);
                awsUtility.getAmazonS3StorageBucket().putObject(awsUtility.getImageBucketName(), keyName, stream,  meta);
                fileNameList.add(fileName);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }

        return fileNameList;
    }

}
