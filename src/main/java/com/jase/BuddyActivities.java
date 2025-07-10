package com.jase;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.azure.ai.openai.OpenAIAsyncClient;
import com.azure.ai.openai.OpenAIClientBuilder;
import com.azure.core.credential.AzureKeyCredential;
import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.aiservices.openai.chatcompletion.OpenAIChatCompletion;
import com.microsoft.semantickernel.orchestration.InvocationContext;
import com.microsoft.semantickernel.orchestration.InvocationReturnMode;
import com.microsoft.semantickernel.orchestration.ToolCallBehavior;
import com.microsoft.semantickernel.plugin.KernelPlugin;
import com.microsoft.semantickernel.plugin.KernelPluginFactory;
import com.microsoft.semantickernel.services.chatcompletion.AuthorRole;
import com.microsoft.semantickernel.services.chatcompletion.ChatCompletionService;
import com.microsoft.semantickernel.services.chatcompletion.ChatHistory;
import com.microsoft.semantickernel.services.chatcompletion.ChatMessageContent;

/**
 * Temporal activities implementation for Buddy AI Agent Integrates with
 * Semantic Kernel for AI processing
 */
public class BuddyActivities implements BuddyActivitiesInterface {

    private static final Logger logger = LoggerFactory.getLogger(BuddyActivities.class);

    // Semantic Kernel components
    private final Kernel kernel;
    private final ChatCompletionService chatCompletionService;
    private final ChatHistory chatHistory;
    private final InvocationContext invocationContext;

    public BuddyActivities() {
        // Initialize Semantic Kernel components

        // Try to create OpenAI client first, fallback to mock mode if not available
        String apiKey = System.getenv("OPENAI_API_KEY");

        if (apiKey != null && !apiKey.trim().isEmpty()) {

            logger.info("Initializing Semantic Kernel with OpenAI");

            // Create OpenAI client
            OpenAIAsyncClient client = new OpenAIClientBuilder()
                    .credential(new AzureKeyCredential(apiKey))
                    .buildAsyncClient();

            // Create chat completion service
            this.chatCompletionService = OpenAIChatCompletion.builder()
                    .withModelId(System.getenv().getOrDefault("MODEL_ID", "gpt-3.5-turbo"))
                    .withOpenAIAsyncClient(client)
                    .build();

        } else {
            logger.warn("No AI service configuration found. Running in mock mode.");
            this.chatCompletionService = null;
        }

        // Create kernel with the chat completion service
        if (this.chatCompletionService != null) {
            // Create the BuddyPlugin
            KernelPlugin buddyPlugin = KernelPluginFactory.createFromObject(new BuddyPlugin(), "BuddyPlugin");

            this.kernel = Kernel.builder()
                    .withAIService(ChatCompletionService.class, chatCompletionService)
                    .withPlugin(buddyPlugin)
                    .build();
        } else {
            this.kernel = null;
        }

        // Initialize chat history
        this.chatHistory = new ChatHistory();

        // Configure invocation context for function calling
        this.invocationContext = new InvocationContext.Builder()
                .withReturnMode(InvocationReturnMode.LAST_MESSAGE_ONLY)
                .withToolCallBehavior(ToolCallBehavior.allowAllKernelFunctions(true))
                .build();

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
            if (chatCompletionService != null && kernel != null) {
                // Add user message to chat history
                chatHistory.addUserMessage(input);

                // Get response from AI service
                List<ChatMessageContent<?>> results = chatCompletionService
                        .getChatMessageContentsAsync(chatHistory, kernel, invocationContext)
                        .block();

                if (results != null && !results.isEmpty()) {
                    for (ChatMessageContent<?> result : results) {
                        // Process assistant responses
                        if (result.getAuthorRole() == AuthorRole.ASSISTANT && result.getContent() != null) {
                            String responseContent = result.getContent();

                            // Add the assistant's response to chat history
                            chatHistory.addMessage(result);

                            logger.info("Generated response using Semantic Kernel");
                            return responseContent;
                        }
                    }
                }

                logger.warn("No valid response from Semantic Kernel, falling back to mock response");
                return generateMockResponse(input);

            } else {
                logger.info("Semantic Kernel not available, using mock response");
                return generateMockResponse(input);
            }
        } catch (Exception e) {
            logger.error("Error generating response with Semantic Kernel", e);
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
