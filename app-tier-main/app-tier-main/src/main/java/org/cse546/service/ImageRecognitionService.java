package org.cse546.service;

import java.awt.*;
import java.io.*;
import java.util.List;

import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.sqs.model.Message;
import org.cse546.utility.AWSUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.commons.io.IOUtils;

@Service
public class ImageRecognitionService {

    private static final Logger logger = LoggerFactory.getLogger(ImageRecognitionService.class);

    private static final String UBUNTU_PATH = "/home/ubuntu/";
    private static final String UBUNTU_COMMAND = "cd  /home/ubuntu && " + "python3 image_classification.py ";

    @Autowired
    private SQSService sqsService;

    @Autowired
    private S3Service s3Service;

    @Autowired
    private AWSUtility awsUtility;

    public void processImageAndGetResults(){

        while (true){
            List<Message> messageList = sqsService.receiveSqsRequestMessage();
            if(messageList == null){
                logger.info("Finished processing all images. Request Queue is empty");
            } else {
                processImages (messageList);
            }
        }
    }

    private void processImages(List<Message> imageList){
        logger.info("Processing images");
        for (Message message : imageList){
            String fileName = message.getBody();
            String predictedValue = getPrediction(fileName);
            if(predictedValue == null){
                predictedValue = new String("No image predicted");
            }
            logger.info("Predicted image name for file: {} is, {}", fileName, predictedValue);
            s3Service.insertResultData(fileName, predictedValue);
            String result = fileName + ":" + predictedValue;
            sqsService.sendMessage(result, awsUtility.getSqsResponseUrl());

        }
        sqsService.deleteQueueMessages(imageList, awsUtility.getSqsRequestUrl());
    }

    private String getPrediction(String messageName){
        GetObjectRequest request = new GetObjectRequest(awsUtility.getImageBucketName(), messageName);
        S3Object object = awsUtility.getAmazonS3().getObject(request);
        S3ObjectInputStream objectContent = object.getObjectContent();
        try {
            IOUtils.copy(objectContent, new FileOutputStream(UBUNTU_PATH + messageName));
        } catch (Exception e) {
            e.printStackTrace();
        }
        String output = null;
        Process p;
        try {
            p = new ProcessBuilder("/bin/bash", "-c", UBUNTU_COMMAND + messageName).start();
            p.waitFor();
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            output = br.readLine();
            p.destroy();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return output.trim();

    }
}
