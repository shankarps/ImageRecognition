package com.shankar.image.service.aws;

import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.stereotype.Component;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.rekognition.model.AmazonRekognitionException;
import com.amazonaws.services.rekognition.model.DetectLabelsRequest;
import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.AmazonRekognitionClientBuilder;
import com.amazonaws.services.rekognition.model.DetectTextRequest;
import com.amazonaws.services.rekognition.model.DetectTextResult;
import com.amazonaws.services.rekognition.model.Image;
import com.amazonaws.services.rekognition.model.S3Object;
import com.amazonaws.services.rekognition.model.TextDetection;


@Component
public class AWSImageRekogitionFindText {

   public void process() throws Exception {
      
  
      String photo = "sandwich-board.jpg";

      AWSCredentials credentials;
      try {
          credentials = new ProfileCredentialsProvider("default").getCredentials();
      } catch(Exception e) {
         throw new AmazonClientException("Cannot load the credentials from the credential profiles file. "
          + "Please make sure that your credentials file is at the correct "
          + "location (/Users/userid/.aws/credentials), and is in a valid format.", e);
      }


      AmazonRekognition rekognitionClient = AmazonRekognitionClientBuilder
              .standard()
              .withRegion(Regions.US_EAST_1)
              .withCredentials(new AWSStaticCredentialsProvider(credentials))
              .build();
      
      Path path = Paths.get("/home/shankar/Downloads/" + photo);
      ByteBuffer bytes = ByteBuffer.wrap(Files.readAllBytes(path));

      
      DetectTextRequest request = new DetectTextRequest()
              .withImage(new Image().withBytes(bytes));
    

      try {
         DetectTextResult result = rekognitionClient.detectText(request);
         List<TextDetection> textDetections = result.getTextDetections();

         System.out.println("Detected lines and words for " + photo);
         for (TextDetection text: textDetections) {
      
                 System.out.println("Detected: " + text.getDetectedText());
                 System.out.println("Confidence: " + text.getConfidence().toString());
                 System.out.println("Id : " + text.getId());
                 System.out.println("Parent Id: " + text.getParentId());
                 System.out.println("Type: " + text.getType());
                 System.out.println();
         }
      } catch(AmazonRekognitionException e) {
         e.printStackTrace();
      }
   }
}
