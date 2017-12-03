package com.shankar.image;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import com.shankar.image.service.aws.AWSImageRekogitionFindFaces;
import com.shankar.image.service.aws.AWSImageRekogitionFindLabels;
import com.shankar.image.service.aws.AWSImageRekogitionFindText;

@SpringBootApplication
public class ImageRecognitionApplication {

	public static void main(String[] args) throws Exception {
		ApplicationContext ctx = SpringApplication.run(ImageRecognitionApplication.class, args);
		AWSImageRekogitionFindLabels imageProcessor = ctx.getBean(AWSImageRekogitionFindLabels.class);
		imageProcessor.process();
		
/*		AWSImageRekogitionFindFaces faceProcessor = ctx.getBean(AWSImageRekogitionFindFaces.class);
		faceProcessor.process();*/
	
		AWSImageRekogitionFindText textProcessor = ctx.getBean(AWSImageRekogitionFindText.class);
		textProcessor.process();
		
	}
	
	
}
