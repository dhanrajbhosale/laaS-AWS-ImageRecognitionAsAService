package org.cse546.service;

import com.amazonaws.services.sqs.model.*;
import org.cse546.utility.AWSUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SQSService {

    private static final Logger logger = LoggerFactory.getLogger(SQSService.class);
    @Autowired
    private AWSUtility awsUtility;

    public List<Message> receiveSqsRequestMessage(){
        ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(awsUtility.getSqsRequestUrl());
        receiveMessageRequest.setWaitTimeSeconds(0);
        receiveMessageRequest.setVisibilityTimeout(30);
        receiveMessageRequest.setMaxNumberOfMessages(1);
        ReceiveMessageResult receiveMessageResult  = awsUtility.getSQSQueue().receiveMessage(receiveMessageRequest);
        List<Message> messageList = receiveMessageResult.getMessages();
        if(messageList.isEmpty()) return null;
        return messageList;
    }

    public void sendMessage(String output, String queueName){
        SendMessageRequest sendMessageRequest = new SendMessageRequest()
                .withQueueUrl(awsUtility.getSqsResponseUrl())
                .withMessageBody(output);
        awsUtility.getSQSQueue().sendMessage(sendMessageRequest);
    }

    public void deleteQueueMessages(List<Message> messages, String queueUrl) {
        logger.info("Delete messages in the queue {}", queueUrl);
        List<DeleteMessageBatchRequestEntry> batchEntries = new ArrayList<>();

        for(Message message : messages) {
            DeleteMessageBatchRequestEntry entry = new DeleteMessageBatchRequestEntry(message.getMessageId(), message.getReceiptHandle());
            batchEntries.add(entry);
        }
        DeleteMessageBatchRequest batch = new DeleteMessageBatchRequest(queueUrl, batchEntries);
        awsUtility.getSQSQueue().deleteMessageBatch(batch);
    }
}
