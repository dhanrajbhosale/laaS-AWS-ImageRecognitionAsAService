```
Group Member Names:
Pratyush Pandey, ASU ID: 1225718442, Email: ppande28@asu.edu
Dhanraj Bhosale, ASU ID: 1225506620, Email: dbhosal1@asu.edu
Glen Dsouza, ASU ID: 1222318617, Email: gsdsouza@asu.edu
```

```
Member Tasks:

Pratyush
1. Creating the AMI image id and template with all dependencies. Took up the responsibility of understanding how AMI image id can be templatized. Figured out how all the dependencies(downloading java, bringing up jar on bootup) is done. 
2. Creating App Tier and debugging/testing using Spring Boot. Figured out technical issues with the response queue deletion. Figured out how to process images in app-tier, there were issues with consuming the message and sending it as an input to invoke the python process.
3. Adding template to autoscaling group, figuring out the autoscaling process. Testing end to end. Testing the scaling process. 
4. Configure the script to auto run the jar to start the Apptier application

Dhanraj
1. Configured, deployed, and tested web-tier and app-tier infrastructure on AWS. 
2. Conducted a series of end-to-end tests to identify and fix issues.
3. Handled CloudWatch alarms and policies for autoscaling group to ensure smooth scaling.
4. Conducted a POC to fine-tune visibility timeout, wait-time, and maximum number of messages of SQS system for improved speed and to prevent HTTP timeouts.
5. Identified and fixed a critical issue with the deletion of the SQS response queue.
6. Created an AWS Dashboard to provide one centralized location for evaluating project metrics.

Glen
1. Created web-tier using Spring Boot and debugging/testing using Spring Boot. Entire web-tier application, AWS java sdk, initialization of utility classes required to connect to AWS. 
2. Wrote the Logic flow within web tier and SQS and S3 services. Debugged issues with messages successfully getting sent to the queue and buckets.
3. Created App Tier and debugging/testing using Spring Boot. AWS java sdk, initialization of utility classes, java code in all the packages. 
4. Wrote the the logic flow within and between the app-tier application, s3 and sqs services.
5. Project report, architecture diagram. 


```
```

PEM key for web-tier SSH access: Can be sent on email seperately due to security concerns
Web tier’s URL: http://ec2-44-205-23-195.compute-1.amazonaws.com:8080/uploadImagesAndGetResults
EIP: Not Required
SQS queue names: cc-input-sqs, cc-output-sqs
S3 bucket names: cc-project-image-bucket, cc-project-result-bucket
```
