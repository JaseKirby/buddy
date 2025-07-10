package com.jase;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.microsoft.semantickernel.semanticfunctions.annotations.DefineKernelFunction;
import com.microsoft.semantickernel.semanticfunctions.annotations.KernelFunctionParameter;

/**
 * Buddy AI Plugin providing useful functions for the AI assistant
 * This demonstrates how to create native plugins for Semantic Kernel
 */
public class BuddyPlugin {
    
    private static final Logger logger = LoggerFactory.getLogger(BuddyPlugin.class);
    
    // Mock data for demonstration
    private final Map<String, String> userData;
    
    public BuddyPlugin() {
        this.userData = new HashMap<>();
        // Initialize with some sample data
        userData.put("user_name", "User");
        userData.put("user_preferences", "Friendly and helpful responses");
        logger.info("BuddyPlugin initialized");
    }
    
    @DefineKernelFunction(name = "get_current_time", description = "Gets the current date and time")
    public String getCurrentTime() {
        logger.info("Getting current time");
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return now.format(formatter);
    }
    
    @DefineKernelFunction(name = "get_weather", description = "Gets weather information for a location")
    public String getWeather(
        @KernelFunctionParameter(name = "location", description = "The location to get weather for") String location) {
        logger.info("Getting weather for location: {}", location);
        
        // Mock weather response - in a real implementation, this would call a weather API
        return String.format("The weather in %s is currently sunny with a temperature of 22°C (72°F). " +
            "It's a beautiful day with light winds and clear skies.", location);
    }
    
    @DefineKernelFunction(name = "get_user_info", description = "Gets information about the current user")
    public String getUserInfo(
        @KernelFunctionParameter(name = "info_type", description = "The type of user information to retrieve (name, preferences, etc.)") String infoType) {
        logger.info("Getting user info for type: {}", infoType);
        
        String result = userData.get(infoType.toLowerCase());
        if (result != null) {
            return result;
        } else {
            return "Information not available for: " + infoType;
        }
    }
    
    @DefineKernelFunction(name = "set_user_preference", description = "Sets a user preference")
    public String setUserPreference(
        @KernelFunctionParameter(name = "preference_key", description = "The preference key to set") String key,
        @KernelFunctionParameter(name = "preference_value", description = "The preference value to set") String value) {
        logger.info("Setting user preference: {} = {}", key, value);
        
        userData.put(key.toLowerCase(), value);
        return String.format("User preference '%s' has been set to '%s'", key, value);
    }
    
    @DefineKernelFunction(name = "calculate", description = "Performs simple mathematical calculations")
    public String calculate(
        @KernelFunctionParameter(name = "expression", description = "The mathematical expression to evaluate (e.g., '2 + 2', '10 * 5')") String expression) {
        logger.info("Calculating expression: {}", expression);
        
        try {
            // Simple calculator for demonstration - handles basic operations
            String[] parts = expression.trim().split("\\s+");
            if (parts.length == 3) {
                double a = Double.parseDouble(parts[0]);
                String operator = parts[1];
                double b = Double.parseDouble(parts[2]);
                
                double result = switch (operator) {
                    case "+" -> a + b;
                    case "-" -> a - b;
                    case "*" -> a * b;
                    case "/" -> a / b;
                    default -> throw new IllegalArgumentException("Unsupported operator: " + operator);
                };
                
                return String.format("%.2f %s %.2f = %.2f", a, operator, b, result);
            } else {
                return "Invalid expression format. Please use format like '2 + 2' or '10 * 5'";
            }
        } catch (IllegalArgumentException e) {
            logger.error("Error calculating expression: {}", expression, e);
            return "Error calculating expression: " + e.getMessage();
        }
    }
    
    @DefineKernelFunction(name = "help", description = "Provides help information about available functions")
    public String getHelp() {
        logger.info("Providing help information");
        
        return """
            Available Buddy AI functions:
            
            🕒 get_current_time - Get the current date and time
            🌤️ get_weather(location) - Get weather information for a location
            👤 get_user_info(info_type) - Get user information (name, preferences, etc.)
            ⚙️ set_user_preference(key, value) - Set a user preference
            🧮 calculate(expression) - Perform mathematical calculations (e.g., '2 + 2')
            ❓ help - Show this help message
            
            I can combine these functions to help you with various tasks!
            """;
    }
}
