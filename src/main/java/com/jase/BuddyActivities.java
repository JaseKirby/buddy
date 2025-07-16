package com.jase;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;

/**
 * Temporal activities implementation for Buddy AI Agent Integrates with
 * LangChain4j for AI processing
 */
public class BuddyActivities implements BuddyActivitiesInterface {

    private static final Logger logger = LoggerFactory.getLogger(BuddyActivities.class);

    // LangChain4j components
    private final OpenAiChatModel chatModel;
    private final ChatMemory chatMemory;
    private final BuddyAssistant assistant;
    private final BuddyPlugin buddyPlugin;

    // AI Assistant interface
    interface BuddyAssistant {
        String chat(String message);
    }

    public BuddyActivities() {
        // Initialize LangChain4j components
        this.buddyPlugin = new BuddyPlugin();
        this.chatMemory = MessageWindowChatMemory.withMaxMessages(20);

        // Try to create OpenAI client first, fallback to mock mode if not available
        String apiKey = System.getenv("OPENAI_API_KEY");

        if (apiKey != null && !apiKey.trim().isEmpty()) {
            logger.info("Initializing LangChain4j with OpenAI");

            // Create OpenAI chat model
            this.chatModel = OpenAiChatModel.builder()
                    .apiKey(apiKey)
                    .modelName(System.getenv().getOrDefault("MODEL_ID", "gpt-3.5-turbo"))
                    .build();

            // Create AI assistant with tools
            this.assistant = AiServices.builder(BuddyAssistant.class)
                    .chatModel(chatModel)
                    .chatMemory(chatMemory)
                    .tools(buddyPlugin)
                    .build();

        } else {
            logger.warn("No AI service configuration found. Running in mock mode.");
            this.chatModel = null;
            this.assistant = null;
        }

        logger.info("Buddy AI Agent activities initialized successfully");
    }

    @Override
    public String preprocessInput(String input) {
        logger.info("Preprocessing input: {}", input);

        if (input == null || input.trim().isEmpty()) {
            return "Hello! How can I help you today?";
        }

        // Basic preprocessing: trim whitespace and normalize
        String processed = input.trim();

        // Add any additional preprocessing logic here
        // For example: spell checking, intent detection, etc.
        logger.info("Input preprocessed successfully");
        return processed;
    }

    @Override
    public String generateResponse(String input) {
        logger.info("Generating response for input: {}", input);

        try {
            if (assistant != null) {
                // Generate response using LangChain4j assistant
                String response = assistant.chat(input);
                logger.info("Generated response using LangChain4j");
                return response;
            } else {
                logger.info("LangChain4j not available, using mock response");
                return generateMockResponse(input);
            }
        } catch (Exception e) {
            logger.error("Error generating response with LangChain4j", e);
            return generateMockResponse(input);
        }
    }

    private String generateMockResponse(String input) {
        // Mock responses for testing without OpenAI API
        if (input.toLowerCase().contains("hello") || input.toLowerCase().contains("hi")) {
            return "Hello! I'm Buddy, your AI assistant. How can I help you today?";
        } else if (input.toLowerCase().contains("how are you")) {
            return "I'm doing well, thank you for asking! I'm here and ready to help you with any questions or tasks you might have.";
        } else if (input.toLowerCase().contains("weather")) {
            return "I don't have access to real-time weather data, but I'd be happy to help you find weather information or discuss weather-related topics!";
        } else if (input.toLowerCase().contains("time")) {
            return "I don't have access to real-time information, but you can check the current time on your device!";
        } else {
            return "That's an interesting question! I'm currently running in demo mode. To get full AI responses, please set your OPENAI_API_KEY environment variable.";
        }
    }

    @Override
    public String postprocessResponse(String response) {
        logger.info("Post-processing response");

        if (response == null || response.trim().isEmpty()) {
            return "I apologize, but I couldn't generate a proper response. Please try asking your question again.";
        }

        // Basic post-processing: trim and ensure proper formatting
        String processed = response.trim();

        // Add any additional post-processing logic here
        // For example: content filtering, response formatting, etc.
        logger.info("Response post-processed successfully");
        return processed;
    }
}
