package demoProject;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;

import java.util.stream.Collectors;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage stage) {
    	// greeting message provided by the DLL
		String greeting = new Greeting().GetGreeting();
        Label label = new Label(greeting);
        label.setFont(Font.font(48));
        
        // customer-specific message color 
        String color = CustomerConfig.get("color", "red");
        label.setStyle("-fx-text-fill: " + color);

        BorderPane pane = new BorderPane(label);

        // customer-specific logo
        pane.setLeft(new ImageView(Logo.IMAGE));
        
        TextArea textArea = new TextArea(getAppInfo());
        pane.setBottom(textArea);
        
        Scene scene = new Scene(pane, 640, 480);
        stage.setScene(scene);
        stage.show();
    }

    private String getAppInfo() {
    	String info = "Arguments:\n" + getAppArgs();
    	info += "\n\nJVM arguments:\n" + getAppJvmArgs();
    	info += "\n\nSystem properties:\n" + getSystemProperties();
    	return info;
    }
    
    private String getAppArgs() {
    	return getParameters().getRaw().stream()
    			.map(s -> "\t" + s)
    			.collect(Collectors.joining("\n"));
    }
    
    private String getAppJvmArgs() {
    	try {
			RuntimeMXBean runtimeMxBean = ManagementFactory.getRuntimeMXBean();
			var arguments = runtimeMxBean.getInputArguments();
			return arguments.stream()
					.map(s -> "\t" + s)
					.collect(Collectors.joining("\n"));
		} catch (Throwable e) {
			return e.toString();
		}
    }
    
    private String getSystemProperties() {
    	return System.getProperties().entrySet().stream()
    			.map(e -> "\t" + e.getKey() + ": " + e.getValue())
    			.sorted()
    			.collect(Collectors.joining("\n"));
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
