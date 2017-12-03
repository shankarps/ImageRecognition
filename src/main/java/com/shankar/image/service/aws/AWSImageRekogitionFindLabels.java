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
import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.AmazonRekognitionClientBuilder;
import com.amazonaws.services.rekognition.model.AmazonRekognitionException;
import com.amazonaws.services.rekognition.model.DetectLabelsRequest;
import com.amazonaws.services.rekognition.model.DetectLabelsResult;
import com.amazonaws.services.rekognition.model.Image;
import com.amazonaws.services.rekognition.model.Label;


@Component
public class AWSImageRekogitionFindLabels {

   public void process() throws Exception {

      String photo = "sandwich-board.jpg";
      String bucket = "S3bucket";

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
    	         .withRegion(Regions.US_WEST_2)
    	         .withCredentials(new AWSStaticCredentialsProvider(credentials))
    	         .build();
      

      Path path = Paths.get("/home/shankar/Downloads/" + photo);
      ByteBuffer bytes = ByteBuffer.wrap(Files.readAllBytes(path));

      
      DetectLabelsRequest request = new DetectLabelsRequest()
    		  .withImage(new Image().withBytes(bytes))
    		  .withMaxLabels(10)
    		  .withMinConfidence(75F);
      
/*      DetectLabelsRequest request = new DetectLabelsRequest()
    		  .withImage(new Image()
    		  .withS3Object(new S3Object()
    		  .withName(photo).withBucket(bucket)))
    		  .withMaxLabels(10)
    		  .withMinConfidence(75F);*/

      try {
         DetectLabelsResult result = rekognitionClient.detectLabels(request);
         List <Label> labels = result.getLabels();

         System.out.println("Detected labels for " + photo);
         for (Label label: labels) {
            System.out.println(label.getName() + ": " + label.getConfidence().toString());
         }
      } catch(AmazonRekognitionException e) {
         e.printStackTrace();
      }
   }
}