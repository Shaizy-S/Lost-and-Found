package com.example.myapplication;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

import java.util.HashMap;
import java.util.Map;

public class NotificationSender {

    private static final String TAG = "NotificationSender";

    public void sendNotification(String token, String title, String messageBody) {
        // Send notification using Firebase Cloud Messaging
        FirebaseMessaging.getInstance().send(new RemoteMessage.Builder(token)
                .setMessageId(Integer.toString(getRandomId())) // Message ID
                .setData(createNotificationData(title, messageBody)) // Notification data
                .build());
    }

    private int getRandomId() {
        // Generate a random ID for the message
        return (int) (Math.random() * Integer.MAX_VALUE);
    }

    private Map<String, String> createNotificationData(String title, String messageBody) {
        // Create a map with notification data
        Map<String, String> data = new HashMap<>();
        data.put("title", title);
        data.put("body", messageBody);
        return data;
    }
}