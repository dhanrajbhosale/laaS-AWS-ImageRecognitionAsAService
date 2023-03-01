package org.cse546.service;

import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.cse546.utility.AWSUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class S3Service {

    @Autowired
    private AWSUtility awsUtility;

    private static final Logger logger = LoggerFactory.getLogger(S3Service.class);

    public void insertResultData(String key, String val){
        logger.info("Inserting result data {} : {} to S3 result bucket", key, val);
        Map<String, String> result = new HashMap<String, String>();
        String keyMap = "(" + key.replace(".JPEG", ".txt") + ")";
        result.put(key.replace(".JPEG", ".txt"), val);
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        ObjectOutputStream out = null;
       // String mapAsString = result.toString ();
//                result.keySet().stream()
//                .map(k -> k + "=" + result.get(k))
//                .collect(Collectors.joining(", ", "{", "}"));
//        try {
//            out = new ObjectOutputStream(byteOut);
//            out.writeObject(result);
//            ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
//            ObjectInputStream in = new ObjectInputStream(byteIn);
//            ObjectMetadata meta = new ObjectMetadata();
//            meta.setContentLength(byteIn.available());
//            awsUtility.getAmazonS3().putObject(awsUtility.getResultBucketName(), mapAsString, in, meta);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }

        try {
            String prediction = new ObjectMapper ().writeValueAsString(result);
            logger.info("Saving output prediction for image: " + key);
            byte[] bytes = val.getBytes();
            ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(bytes.length);
            PutObjectRequest request = new PutObjectRequest(awsUtility.getResultBucketName(), keyMap, inputStream, metadata);

            awsUtility.getAmazonS3().putObject(request);

        } catch (Exception e) {
            logger.error(e.getMessage());
        }


    }

}
