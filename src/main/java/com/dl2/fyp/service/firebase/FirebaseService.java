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

    @Autowired
    private FirebaseService userInfoService;

    private static FirebaseApp firebaseApp;

    @Value("${firebase.key-url}")
    private String keyUrl;

    @Value("${firebase.database-url}")
    private String dataBaseUrl;

    private void firebaseInitialization() throws IOException {
        InputStream serviceAccount =
                 new URL("https://s3.ap-east-1.amazonaws.com/test.howard.gnil/fyp/fyp2020-80b5e-firebase-adminsdk-lppqp-c51e0a22b2.json").openStream();
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl("https://fyp2020-80b5e.firebaseio.com")
                .build();
        firebaseApp = FirebaseApp.initializeApp(options);
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
