package IntegratedChatBot;

import okhttp3.*;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

public class ChatbotGPT {
    private static final String API_URL = "https://api.openai.com/v1/chat/completions";
    private static final String API_KEY = "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";

    public static void main(String[] args) throws IOException {
        // Create an HTTP client
        OkHttpClient client = new OkHttpClient();

        // Prompt for the chatbot
        String userMessage = "Tell me a joke";

        // Build the JSON request body
        JSONObject jsonRequest = new JSONObject();
        jsonRequest.put("model", "gpt-3.5-turbo");
        jsonRequest.put("messages", new JSONArray()
            .put(new JSONObject().put("role", "system").put("content", "You are a helpful assistant."))
            .put(new JSONObject().put("role", "user").put("content", userMessage))
        );

        // Create the HTTP request
        Request request = new Request.Builder()
            .url(API_URL)
            .addHeader("Authorization", "Bearer " + API_KEY)
            .addHeader("Content-Type", "application/json")
            .post(RequestBody.create(jsonRequest.toString(), MediaType.get("application/json")))
            .build();

        // Execute the request
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                JSONObject jsonResponse = new JSONObject(response.body().string());
                String botResponse = jsonResponse.getJSONArray("choices").getJSONObject(0).getJSONObject("message").getString("content");
                System.out.println("Chatbot: " + botResponse);
            } else {
                System.err.println("Error: " + response.body().string());
            }
        }
    }
}

