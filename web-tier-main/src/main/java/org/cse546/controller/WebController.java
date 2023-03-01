package org.cse546.controller;

import org.cse546.services.S3Service;
import org.cse546.services.SQSService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping
public class WebController {

    private static final Logger logger = LoggerFactory.getLogger(WebController.class);

    @Autowired
    private S3Service s3Service;

    @Autowired
    private SQSService sqsService;

    @GetMapping("/test")
    public String defaultStatus() {
        return "OK";
    }

    @PostMapping("/uploadImagesAndGetResults")
    public String uploadImagesAndGetResults(@RequestParam("myfile") MultipartFile files) throws InterruptedException {
        logger.info("in upload image and get results");

        MultipartFile[] files_arr = new MultipartFile[1];
        files_arr[0] = files;
        String image_name = files.getOriginalFilename ();
        //processes the input and waits and fetches all the responses
        List<String> fileNameList = s3Service.saveImagesToS3(files_arr);
        //saved images are sent to SQS request queue
        sqsService.sendSavedImagesToRequestQueue(fileNameList);
        Map<String,String> result = sqsService.receiveSQSResponse(fileNameList, image_name);
        String finalResult = new String();


        for (Map.Entry< String, String > m: result.entrySet ()){
            String res = m.getValue();
            String[] parts = res.split(",");
            String k = parts[0];
            String v = parts[1];

            // Format the value string using placeholders
            String formattedValue = String.format("{%s}: {%s}", k, v);

            finalResult = formattedValue;
        }
        return finalResult ;
    }


}
