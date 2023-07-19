package kr.co.suitcarrier.web.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sesv2.model.Destination;
import software.amazon.awssdk.services.sesv2.model.EmailContent;
import software.amazon.awssdk.services.sesv2.model.SendEmailRequest;
import software.amazon.awssdk.services.sesv2.model.SesV2Exception;
import software.amazon.awssdk.services.sesv2.SesV2Client;
import software.amazon.awssdk.services.sesv2.model.Template;

@Configuration
public class SesConfig {
    
    @Value("${aws.accessKeyId}")
    private String accessKey;

    @Value("${aws.secretAccessKey}")
    private String secretKey;

    private static final Region region = Region.AP_NORTHEAST_2;

    private static final String sender = "no-reply@suitcarrier.co.kr";

    private SesV2Client sesv2Client = null;

    @PostConstruct
    public void init() {
        sesv2Client = (SesV2Client) SesV2Client.builder()
            .region(region)
            .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, secretKey)))
            .build();

    }

    public boolean sendSignUpTemplateEmail(String recipient, String name, String authCode){

        Destination destination = Destination.builder()
            .toAddresses(recipient)
            .build();

        Template myTemplate = Template.builder()
            .templateName("SignUp-Template")
            .templateData("{\n" +
            "  \"name\": \""+name+"\"\n," +
            "  \"authCode\": \""+authCode+"\"\n" +
            "}")
            .build();

        EmailContent emailContent = EmailContent.builder()
            .template(myTemplate)
            .build();

        SendEmailRequest emailRequest = SendEmailRequest.builder()
            .destination(destination)
            .content(emailContent)
            .fromEmailAddress(sender)
            .build();

        try {
            System.out.println("Attempting to send an email based on a template using the AWS SDK for Java (v2)...");
            sesv2Client.sendEmail(emailRequest);
            return true;

        } catch (SesV2Exception e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            // System.exit(1);
            return false;
        }

    }

    public boolean sendSignUpCompleteTemplateEmail(String recipient, String name){

        Destination destination = Destination.builder()
            .toAddresses(recipient)
            .build();

        Template myTemplate = Template.builder()
            .templateName("SignUpComplete-Template")
            .templateData("{\n" +
            "  \"name\": \""+name+"\"\n" +
            "}")
            .build();

        EmailContent emailContent = EmailContent.builder()
            .template(myTemplate)
            .build();

        SendEmailRequest emailRequest = SendEmailRequest.builder()
            .destination(destination)
            .content(emailContent)
            .fromEmailAddress(sender)
            .build();

        try {
            System.out.println("Attempting to send an email based on a template using the AWS SDK for Java (v2)...");
            sesv2Client.sendEmail(emailRequest);
            return true;
        } catch (SesV2Exception e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            // System.exit(1);
            return false;
        }
    }
}
