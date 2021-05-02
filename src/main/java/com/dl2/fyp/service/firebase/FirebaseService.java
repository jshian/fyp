package com.dl2.fyp.service.firebase;

import com.dl2.fyp.domain.Result;
import com.dl2.fyp.entity.Account;
import com.dl2.fyp.entity.User;
import com.dl2.fyp.entity.UserDevice;
import com.dl2.fyp.entity.UserInfo;
import com.dl2.fyp.repository.user.UserDeviceRepository;
import com.dl2.fyp.repository.user.UserRepository;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.*;
import com.google.firebase.auth.*;
import com.google.firebase.messaging.BatchResponse;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.MulticastMessage;
import com.google.firebase.messaging.Notification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;


@Service
public class FirebaseService{
    private static Logger LOG = LoggerFactory.getLogger(com.dl2.fyp.service.firebase.FirebaseService.class);

    private static FirebaseApp firebaseApp;

    @Value("${firebase.key-url}")
    private String keyUrl;

    @Value("${firebase.database-url}")
    private String dataBaseUrl;

    private void firebaseInitialization() throws IOException {
        InputStream serviceAccount =
                 new URL(keyUrl).openStream();
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl(dataBaseUrl)
                .build();
        firebaseApp = FirebaseApp.initializeApp(options);
    }

    public void sendPushNotification(List<String> tokens, String title, String content){
        try
        {
            var messageBuilder = MulticastMessage.builder();
            messageBuilder.addAllTokens(tokens);

            var notificationBuilder = Notification.builder();
            notificationBuilder.setTitle(title);
            notificationBuilder.setBody(content);

            messageBuilder.setNotification(notificationBuilder.build());

            var message = messageBuilder.build();
            System.out.println(message.toString());
            BatchResponse response = FirebaseMessaging.getInstance(getFirebaseApp()).sendMulticast(message);
            // See the BatchResponse reference documentation
            // for the contents of response.
            System.out.println(response.getSuccessCount() + " messages were sent successfully");
        }
        catch (Exception ex)
        {
            System.out.println("Bug");
            ex.printStackTrace();
        }
    }

    public FirebaseApp getFirebaseApp() {
        if (firebaseApp == null){
            try{
                firebaseInitialization();
            }
            catch (IOException ex){
                System.out.println(ex.toString());
            }
        }
        return firebaseApp;
    }
}
