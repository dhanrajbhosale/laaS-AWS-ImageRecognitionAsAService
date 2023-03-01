package org.cse546.utility;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import org.springframework.stereotype.Component;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import com.amazonaws.services.s3.model.PutObjectRequest;//.awssdk.services.s3.model.PutObjectRequest;

import javax.annotation.PostConstruct;

//Logging


@Component
public class AWSUtility {

    private static final Logger logger = LoggerFactory.getLogger(AWSUtility.class);

    // name of s3 bucket that stores images
    private String imageBucketName = "";

    //name of s3 bucket that stores result
    private String resultBucketName;

    private AmazonSQS sqsQueue;

    private AmazonSQS sqsResponseQueue;

    private AmazonS3 s3Storage;

    public static final String SQS_REQUEST_URL = "";
    public static final String SQS_RESPONSE_URL = "";


    private static final String ACCESS_KEY = "";
    private static final String SECRET_KEY = "";

    @PostConstruct
    private void initializeAmazonSQSQueue() {
        BasicAWSCredentials AWS_CREDENTIALS = getAWSCREDENTIALS();
        this.sqsQueue =
                AmazonSQSClientBuilder.standard()
                        .withCredentials(new AWSStaticCredentialsProvider(AWS_CREDENTIALS))
                        .withRegion(Regions.US_EAST_1).
                        build();
    }

    @PostConstruct
    private void initializeS3Storage() {
        logger.info("Initializing s3 storage");
        BasicAWSCredentials AWS_CREDENTIALS = getAWSCREDENTIALS();
        this.s3Storage =
                AmazonS3ClientBuilder.standard()
                        .withCredentials(new AWSStaticCredentialsProvider(AWS_CREDENTIALS))
                        .withRegion(Regions.US_EAST_1).
                        build();
    }

    public static BasicAWSCredentials getAWSCREDENTIALS() {
        logger.info("Fetching AWS credentials");
        BasicAWSCredentials AWS_CREDENTIALS = new BasicAWSCredentials(
                ACCESS_KEY,
                SECRET_KEY
        );
        return AWS_CREDENTIALS;
    }

    public String getImageBucketName(){
        logger.info("fetching image bucket name");
        return this.imageBucketName;
    }

    public String getResultBucketName(){
        logger.info("fetching result bucket name");
        return this.getResultBucketName();
    }

    public AmazonS3 getAmazonS3StorageBucket(){
        return this.s3Storage;
    }

    public AmazonSQS getSQSQueue(){
        return this.sqsQueue;
    }

    public String getSqsRequestUrl(){
        return this.SQS_REQUEST_URL;
    }

    public String getSqsResponseUrl(){
        return this.SQS_RESPONSE_URL;
    }

}
