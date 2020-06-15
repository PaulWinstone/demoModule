package demoProject;

import org.junit.Test;
import static org.junit.Assert.*;

public class GreetingTest {
    @Test public void testGreeting() {
        assertEquals("Hello, world!", new Greeting().GetGreeting());
    }
}
