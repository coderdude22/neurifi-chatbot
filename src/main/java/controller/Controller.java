package controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import okhttp3.*;

public class Controller {

    @FXML
    private TextField messageInput;

    @FXML
    private Button sendButton;

    @FXML
    private TextArea chatDisplay;

    private WebSocket webSocket;
    private boolean isConnected = false; // Flag to track connection status

    @FXML
    public void initialize() {
        System.out.println("Controller initialized");

        if (chatDisplay == null) {
            System.out.println("chatDisplay is NULL!");
        } else {
            System.out.println("chatDisplay is initialized");
        }

        connectWebSocket();
        sendButton.setOnAction(event -> sendMessage());
    }

    private void connectWebSocket() {
        OkHttpClient client = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .build();

        Request request = new Request.Builder()
                .url("ws://127.0.0.1:5000/ws") // Ensure server is running
                .build();

        webSocket = client.newWebSocket(request, new WebSocketListener() {
            @Override
            public void onOpen(WebSocket webSocket, Response response) {
                isConnected = true; // Mark WebSocket as connected
                Platform.runLater(() -> chatDisplay.appendText("Connected to chatbot.\n"));
            }

            @Override
            public void onMessage(WebSocket webSocket, String text) {
                System.out.println("WebSocket received: " + text);

                // Ensure chatbot response appears on UI
                Platform.runLater(() -> {
                    chatDisplay.appendText("Bot: " + text + "\n");
                    chatDisplay.setScrollTop(Double.MAX_VALUE);
                    chatDisplay.requestLayout();
                });
            }

            @Override
            public void onFailure(WebSocket webSocket, Throwable t, Response response) {
                isConnected = false; // Mark WebSocket as disconnected
                Platform.runLater(() -> chatDisplay.appendText("Connection error: " + t.getMessage() + "\n"));
            }
        });
    }

    private void sendMessage() {
        String message = messageInput.getText().trim();

        if (message.isEmpty()) {
            return; // Don't send empty messages
        }

        if (webSocket == null || !isConnected) {
            chatDisplay.appendText("Error: WebSocket not connected.\n");
            return;
        }

        try {
            chatDisplay.appendText("You: " + message + "\n");
            webSocket.send(message);
            messageInput.clear();
        } catch (Exception e) {
            System.err.println("Error sending message: " + e.getMessage());
        }
    }
}
