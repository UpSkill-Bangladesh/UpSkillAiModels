package IntegratedChatBot;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.http.entity.StringEntity;
import org.json.JSONObject;

public class ChatbotHuggingFace {

    // Define Hugging Face API URL for the conversational model (DialoGPT)
    private static final String API_URL = "https://api-inference.huggingface.co/models/microsoft/DialoGPT-medium";
    
    // Define the API key for Hugging Face (replace with your API key)
    private static final String API_KEY = "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"; // Add your API key here

    public static void main(String[] args) {
        // Start the chatbot
        System.out.println("Chatbot: Hello! How can I assist you today?");
        
        java.util.Scanner scanner = new java.util.Scanner(System.in);
        
        while (true) {
            System.out.print("You: ");
            String userInput = scanner.nextLine();
            
            if (userInput.equalsIgnoreCase("exit")) {
                System.out.println("Chatbot: Goodbye!");
                break;
            }
            
            // Get response from Hugging Face model
            String response = getChatbotResponse(userInput);
            System.out.println("Chatbot: " + response);
        }
        
        scanner.close();
    }

    // Function to get chatbot response from Hugging Face API
    public static String getChatbotResponse(String userInput) {
        try {
            // Create HTTP client
            CloseableHttpClient client = HttpClients.createDefault();
            HttpPost post = new HttpPost(API_URL);

            // Set the API key in the headers
            post.setHeader("Authorization", "Bearer " + API_KEY);
            
            // Prepare the request body with user input
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("inputs", userInput);
            
            // Set the JSON payload as the entity
            StringEntity entity = new StringEntity(jsonBody.toString());
            post.setEntity(entity);

            // Execute the request
            org.apache.http.HttpResponse response = client.execute(post);
            HttpEntity responseEntity = response.getEntity();
            String responseString = EntityUtils.toString(responseEntity);
            
            // Parse the response (which is JSON)
            JSONObject jsonResponse = new JSONObject(responseString);
            String botOutput = jsonResponse.getJSONArray("generated_texts").getString(0);
            
            return botOutput;

        } catch (Exception e) {
            e.printStackTrace();
            return "Sorry, I couldn't process your request.";
        }
    }
}