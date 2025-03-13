package mainchat;


import java.io.IOException;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

public class MainChat extends Application{
	
	@Override
    public void start(Stage primaryStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/chat.fxml"));
			Parent root = loader.load();
			
			Scene scene = new Scene(root, 600, 400);
			
			primaryStage.setTitle("NeurifiAI");
			primaryStage.setScene(scene);
			primaryStage.show();		
			
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Error loading FXML file.");
		}
	}

    public static void main(String[] args) {
        launch(args);
    }
}
