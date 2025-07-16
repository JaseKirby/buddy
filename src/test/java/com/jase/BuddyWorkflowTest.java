package com.jase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

/**
 * Test class for Buddy AI Agent components
 */
public class BuddyWorkflowTest {
    
    @Test
    public void testBuddyActivitiesDirectly() {
        BuddyActivities activities = new BuddyActivities();
        
        // Test preprocessing
        String preprocessed = activities.preprocessInput("  Hello World  ");
        assertEquals("Hello World", preprocessed);
        
        // Test response generation
        String response = activities.generateResponse("Hello");
        assertNotNull(response);
        assertFalse(response.trim().isEmpty());
        
        // Test post-processing
        String postprocessed = activities.postprocessResponse("  Test Response  ");
        assertEquals("Test Response", postprocessed);
    }
    
    @Test
    public void testBuddyActivitiesWithEmptyInput() {
        BuddyActivities activities = new BuddyActivities();
        
        // Test with empty input
        String response = activities.preprocessInput("");
        assertNotNull(response);
        assertFalse(response.trim().isEmpty());
        
        // Test with null input
        String response2 = activities.preprocessInput(null);
        assertNotNull(response2);
        assertFalse(response2.trim().isEmpty());
    }
    
    @Test
    public void testBuddyActivitiesResponseGeneration() {
        BuddyActivities activities = new BuddyActivities();
        
        // Test various inputs to ensure mock responses work
        String helloResponse = activities.generateResponse("Hello");
        assertNotNull(helloResponse);
        assertTrue(helloResponse.toLowerCase().contains("hello") || helloResponse.toLowerCase().contains("buddy"));
        
        String howAreYouResponse = activities.generateResponse("How are you?");
        assertNotNull(howAreYouResponse);
        assertFalse(howAreYouResponse.trim().isEmpty());
        
        String weatherResponse = activities.generateResponse("What's the weather?");
        assertNotNull(weatherResponse);
        assertFalse(weatherResponse.trim().isEmpty());
    }
    
    @Test
    public void testConfigConstants() {
        // Test that configuration constants are accessible
        assertNotNull(BuddyConfig.TASK_QUEUE);
        assertNotNull(BuddyConfig.DEFAULT_MODEL);
        assertNotNull(BuddyConfig.SYSTEM_PROMPT);
        assertEquals("buddy-task-queue", BuddyConfig.TASK_QUEUE);
        assertEquals("gpt-4o-mini", BuddyConfig.DEFAULT_MODEL);
    }
}
