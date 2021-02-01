package com.dl2.fyp.service.firebase;


import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;


@Service
public class FirebaseService {

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

    public FirebaseApp getFirebaseApp() throws IOException {
        if (firebaseApp == null) {
            firebaseInitialization();
        }
        return firebaseApp;
    }
}
