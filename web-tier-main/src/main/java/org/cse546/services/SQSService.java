package org.cse546.services;

import com.amazonaws.services.sqs.model.*;
import org.cse546.utility.AWSUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

//Service used to store images

@Service
public class SQSService {

    private static final Logger logger = LoggerFactory.getLogger(SQSService.class);

    @Autowired
    private AWSUtility awsUtility;

    Random random = new Random ();

    //Request queue
    public void sendSavedImagesToRequestQueue(List<String> fileNames){
        for(String fileName :  fileNames) {
            logger.info("Sending image {} to SQS Request Queue ",fileName);
            awsUtility.getSQSQueue().sendMessage(awsUtility.getSqsRequestUrl(), fileName);
        }
    }

    public List<Message> readSQSResponseMessages(){
        ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest()
                .withQueueUrl(awsUtility.getSqsResponseUrl())
                .withMaxNumberOfMessages(1)
                .withWaitTimeSeconds(0);	//use long polling


        ReceiveMessageResult result = awsUtility.getSQSQueue().receiveMessage(receiveMessageRequest);
        List<Message> msgList=result.getMessages();

        logger.info("Messages Read from queue {} ",msgList.size());
        return msgList;
    }

    public Map<String,String> receiveSQSResponse(List<String> fileNameList, String image_name) throws InterruptedException {
        int originalFileSize = fileNameList.size();
        int filesRead = 0;
        Map<String,String> results = new HashMap<>();
        while(filesRead<originalFileSize){
            List<Message> messages = readSQSResponseMessages();
            filesRead+=messages.size();
            for(Message m : messages){
                String body = m.getBody();
                //responses are of the form "image name,result"
                String[] values = body.split(":");

                //first part is the name of image, second part is the result from the
                //Neural network
//                if (values[0].equals (image_name)) {
                    results.put (values[0], values[1]);

                    logger.info ("Received " + values[0]);
                    try {
                        //delete the message
                        awsUtility.getSQSQueue ().deleteMessage (awsUtility.getSqsResponseUrl (), m.getReceiptHandle ());
                        logger.info ("Message Deleted");
                    } catch (Exception e) {
                        logger.info ("exception while deleting message, maybe deleted already");
                    }
//                } else
//                {
//                    Thread.sleep((long)((random.nextDouble()*3+1)*1000));
//                }
            }
        }
        return results;
    }

}
