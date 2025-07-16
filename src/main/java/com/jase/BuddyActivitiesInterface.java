package com.jase;

import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

/**
 * Temporal activities interface for Buddy AI Agent
 * These activities handle the actual AI processing work
 */
@ActivityInterface
public interface BuddyActivitiesInterface {
    
    /**
     * Preprocess user input (cleaning, normalization, etc.)
     * @param input Raw user input
     * @return Preprocessed input
     */
    @ActivityMethod
    String preprocessInput(String input);
    
    /**
     * Generate AI response using LangChain4j
     * @param input Preprocessed user input
     * @return AI generated response
     */
    @ActivityMethod
    String generateResponse(String input);
    
    /**
     * Post-process the AI response (formatting, filtering, etc.)
     * @param response Raw AI response
     * @return Final processed response
     */
    @ActivityMethod
    String postprocessResponse(String response);
}
