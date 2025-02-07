package com.d111.PrePay.service;

import com.d111.PrePay.dto.respond.FcmMessageWithData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.common.net.HttpHeaders;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class FCMService {

    public final ObjectMapper objectMapper;
    private String API_URL;
    /**
     * FCM에 push 요청을 보낼 때 인증을 위해 Header에 포함시킬 AccessToken 생성
     *
     * @return
     * @throws IOException
     */
    private String getAccessToken() throws IOException {

        // GoogleApi를 사용하기 위해 oAuth2를 이용해 인증한 대상을 나타내는객체
        GoogleCredentials googleCredentials = GoogleCredentials
                // 서버로부터 받은 service key 파일 활용
                .fromStream(new ClassPathResource("src/main/resources/fcm-google-services.json").getInputStream())
                // 인증하는 서버에서 필요로 하는 권한 지정
                .createScoped(Arrays.asList("https://www.googleapis.com/auth/cloud-platform"));

        googleCredentials.refreshIfExpired();

        return googleCredentials.getAccessToken().getTokenValue();
    }

    /**
     * FCM 알림 메시지 생성
     * background 대응을 위해서 data로 전송한다.
     *
     * @param targetToken
     * @param title
     * @param body
     * @return
     * @throws JsonProcessingException
     */
    private String makeDataMessage(String targetToken, String title, String body) throws JsonProcessingException {
//        Notification noti = new FcmMessage.Notification(title, body, null);
        Map<String, String> map = new HashMap<>();
        map.put("myTitle", title);
        map.put("myBody", body);

        FcmMessageWithData.Message message = new FcmMessageWithData.Message();
        message.setToken(targetToken);
        message.setData(map);

        FcmMessageWithData fcmMessage = new FcmMessageWithData(false, message);

        return objectMapper.writeValueAsString(fcmMessage);
    }


    /**
     * targetToken에 해당하는 device로 FCM 푸시 알림 전송
     * background 대응을 위해서 data로 전송한다.
     *
     * @param targetToken
     * @param title
     * @param body
     * @throws IOException
     */
    public void sendDataMessageTo(String targetToken, String title, String body) throws IOException {
        String message = makeDataMessage(targetToken, title, body);
        log.info("message : {}", message);


        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(message, MediaType.get("application/json; charset=utf-8"));
        log.error("url : {}",API_URL);
        Request request = new Request.Builder()
                .url(API_URL)
                .post(requestBody)
                // 전송 토큰 추가
                .addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
                .addHeader(HttpHeaders.CONTENT_TYPE, "application/json; UTF-8")
                .build();

        Response response = client.newCall(request).execute();

        System.out.println(response.body().string());
//        logger.info("message : {}", message);
    }




    // 등록된 모든 토큰을 이용해서 broadcasting 나중에 할지? token이 null이 아니면 가져와서 발송
   /*public int broadCastDataMessage(String title, String body) throws IOException {

        for (String token : Constants.clientTokens) {
            log.debug("broadcastmessage : {},{},{}", token, title, body);
            sendDataMessageTo(token, title, body);
        }
        return Constants.clientTokens.size();
    }*/














}
