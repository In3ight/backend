package kr.co.suitcarrier.web.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;

@Configuration
public class SmsConfig {

    @Value("${sms.api.key}")
    private String apiKey;

    @Value("${sms.api.secret}")
    private String apiSecret;

    private DefaultMessageService messageService = null;
    
    @PostConstruct
    public void init() {
        messageService = NurigoApp.INSTANCE.initialize(apiKey, apiSecret, "https://api.coolsms.co.kr");
    }

    // 예제: https://github.com/coolsms/coolsms-java-examples/blob/main/gradle-spring-demo/src/main/java/net/nurigo/gradlespringdemo/ExampleController.java

    // 단일 메시지 발송 예제
    public SingleMessageSentResponse sendOne(String to, String text) {
        
        System.out.println("to: " + to);
        System.out.println("text: " + text);

        Message message = new Message();
        // 발신번호 및 수신번호는 반드시 01012345678 형태로 입력되어야 합니다.
        message.setFrom("01064602935");
        message.setTo(to);
        // 한글 45자, 영자 90자 이하 입력되면 자동으로 SMS타입의 메시지가 추가됩니다.
        message.setText(text);

        SingleMessageSentResponse response = this.messageService.sendOne(new SingleMessageSendingRequest(message));
        System.out.println(response);

        return response;
    }

}
