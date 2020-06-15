package demoProject;

import java.io.IOException;
import java.io.InputStream;

import javafx.scene.image.Image;

public class Logo {
    public static final Image IMAGE;
	static {
        try(InputStream logoStream = Logo.class.getResourceAsStream("/logo.png")) {
            if(logoStream == null) {
            	throw new RuntimeException("Resource logo.png not found.");
            }
            IMAGE = new Image(logoStream);
        } catch(IOException e) {
        	System.err.println("Cannot read logo.png");
        	throw new ExceptionInInitializerError(e);
        }
	}
}
