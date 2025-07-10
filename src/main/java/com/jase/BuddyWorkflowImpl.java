package com.jase;

import io.temporal.activity.ActivityOptions;
import io.temporal.workflow.Workflow;
import org.slf4j.Logger;

import java.time.Duration;

/**
 * Temporal workflow implementation for Buddy AI Agent
 * This orchestrates the AI processing pipeline using activities
 */
public class BuddyWorkflowImpl implements BuddyWorkflow {
    
    private static final Logger logger = Workflow.getLogger(BuddyWorkflowImpl.class);
    
    // Activity options with timeout and retry policy
    private final ActivityOptions activityOptions = ActivityOptions.newBuilder()
            .setStartToCloseTimeout(Duration.ofMinutes(2))
            .setRetryOptions(io.temporal.common.RetryOptions.newBuilder()
                    .setInitialInterval(Duration.ofSeconds(1))
                    .setMaximumInterval(Duration.ofSeconds(10))
                    .setMaximumAttempts(3)
                    .build())
            .build();
    
    // Create activity stub
    private final BuddyActivitiesInterface activities = 
            Workflow.newActivityStub(BuddyActivitiesInterface.class, activityOptions);
    
    @Override
    public String processUserInput(String userInput) {
        logger.info("Processing user input in workflow: {}", userInput);
        
        try {
            // Step 1: Preprocess the input
            String preprocessedInput = activities.preprocessInput(userInput);
            logger.info("Input preprocessed successfully");
            
            // Step 2: Generate response using Semantic Kernel
            String response = activities.generateResponse(preprocessedInput);
            logger.info("Response generated successfully");
            
            // Step 3: Post-process the response
            String finalResponse = activities.postprocessResponse(response);
            logger.info("Response post-processed successfully");
            
            return finalResponse;
            
        } catch (Exception e) {
            logger.error("Error in workflow execution", e);
            // Return a fallback response
            return "I apologize, but I'm having trouble processing your request right now. Please try again.";
        }
    }
}
