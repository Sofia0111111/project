package org.example;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class TelegramService {

    private static final String TOKEN = "8789183741:AAFkgX7KkPAUReQOVoh9OpR27XB7xhNZCiI";
    private static final String CHAT_ID = "411654017";

    public static void sendMessage(String text) {
        try {
            String urlString = "https://api.telegram.org/bot" + TOKEN + "/sendMessage";
            URL url = new URL(urlString);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            String data = "chat_id=" + CHAT_ID + "&text=" + text;

            OutputStream os = conn.getOutputStream();
            os.write(data.getBytes());
            os.flush();
            os.close();

            conn.getResponseCode();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}