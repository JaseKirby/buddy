package com.jase;

/**
 * Configuration class for Buddy AI Agent
 * Contains constants and configuration settings
 */
public class BuddyConfig {
    
    // Temporal Configuration
    public static final String TASK_QUEUE = "buddy-task-queue";
    public static final String WORKFLOW_ID_PREFIX = "buddy-workflow-";
    
    // AI Model Configuration
    public static final String DEFAULT_MODEL = "gpt-3.5-turbo";
    public static final String OPENAI_API_KEY_ENV = "OPENAI_API_KEY";
    
    // Response Configuration
    public static final int MAX_RESPONSE_LENGTH = 2000;
    public static final int CHAT_HISTORY_LIMIT = 10;
    
    // Timeout Configuration
    public static final int ACTIVITY_TIMEOUT_MINUTES = 2;
    public static final int WORKFLOW_TIMEOUT_MINUTES = 5;
    
    // System Messages
    public static final String SYSTEM_PROMPT = 
        "You are Buddy, a helpful AI assistant. You are friendly, knowledgeable, and " +
        "always try to provide useful and accurate information. Keep your responses " +
        "concise but informative. You are running in a Temporal workflow environment.";
    
    // Error Messages
    public static final String API_KEY_MISSING_MSG = 
        "OpenAI API key not found. Please set the OPENAI_API_KEY environment variable.";
    public static final String GENERIC_ERROR_MSG = 
        "I apologize, but I'm having trouble processing your request right now. Please try again.";
    public static final String EMPTY_RESPONSE_MSG = 
        "I apologize, but I couldn't generate a proper response. Please try asking your question again.";
    
    private BuddyConfig() {
        // Private constructor to prevent instantiation
    }
}
