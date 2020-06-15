package demoProject;

public class Greeting {
	static {
		System.loadLibrary("Greeting");
	}
	
	public native String GetGreeting();
}