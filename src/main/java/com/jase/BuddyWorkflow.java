package com.jase;

import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

/**
 * Temporal workflow interface for Buddy AI Agent
 * This workflow orchestrates the AI agent's processing pipeline
 */
@WorkflowInterface
public interface BuddyWorkflow {
    
    /**
     * Process user input through the AI agent workflow
     * @param userInput The input text from the user
     * @return The AI agent's response
     */
    @WorkflowMethod
    String processUserInput(String userInput);
}
