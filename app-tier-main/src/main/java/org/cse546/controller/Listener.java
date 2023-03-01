package org.cse546.controller;


import org.cse546.service.ImageRecognitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;


@Component
public class Listener {

    @Autowired
    private ImageRecognitionService imageRecognitionService;

    public void processQueue(){
        imageRecognitionService.processImageAndGetResults();
    }
}
