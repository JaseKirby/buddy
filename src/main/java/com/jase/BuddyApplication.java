package com.jase;

import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.worker.Worker;
import io.temporal.worker.WorkerFactory;

/**
 * Main application class for Buddy AI Agent
 * This application demonstrates how to integrate Temporal workflows with LangChain4j
 */
public class BuddyApplication {
    
    private static final Logger logger = LoggerFactory.getLogger(BuddyApplication.class);
    private static final String TASK_QUEUE = "buddy-task-queue";
    
    public static void main(String[] args) {
        // Configure Logback programmatically
        LogbackConfig.configure();
        
        logger.info("Starting Buddy AI Agent...");
        
        // Create Temporal client
        WorkflowServiceStubs service = WorkflowServiceStubs.newLocalServiceStubs();
        WorkflowClient client = WorkflowClient.newInstance(service);
        
        // Create worker factory
        WorkerFactory factory = WorkerFactory.newInstance(client);
        
        // Create worker
        Worker worker = factory.newWorker(TASK_QUEUE);
        
        // Register workflow and activities
        worker.registerWorkflowImplementationTypes(BuddyWorkflowImpl.class);
        worker.registerActivitiesImplementations(new BuddyActivities());
        
        // Start worker
        factory.start();
        logger.info("Buddy AI Agent worker started successfully!");
        
        // Start interactive session
        startInteractiveSession(client);
        
        // Shutdown
        factory.shutdown();
        service.shutdown();
        logger.info("Buddy AI Agent shutdown complete.");
    }
    
    private static void startInteractiveSession(WorkflowClient client) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("\n=== Welcome to Buddy AI Agent ===");
            System.out.println("Type 'help' for available commands or 'exit' to quit");
            
            while (true) {
                System.out.print("\nBuddy> ");
                String input = scanner.nextLine().trim();
                
                if (input.equalsIgnoreCase("exit")) {
                    break;
                }
                
                if (input.equalsIgnoreCase("help")) {
                    printHelp();
                    continue;
                }
                
                if (input.isEmpty()) {
                    continue;
                }
                
                try {
                    // Create and start workflow
                    BuddyWorkflow workflow = client.newWorkflowStub(
                            BuddyWorkflow.class,
                            WorkflowOptions.newBuilder()
                                    .setTaskQueue(TASK_QUEUE)
                                    .setWorkflowId("buddy-workflow-" + System.currentTimeMillis())
                                    .build()
                    );
                    
                    // Execute the workflow
                    String response = workflow.processUserInput(input);
                    System.out.println("Buddy: " + response);
                    
                } catch (Exception e) {
                    logger.error("Error processing user input", e);
                    System.out.println("Sorry, I encountered an error processing your request: " + e.getMessage());
                }
            }
        }
    }
    
    private static void printHelp() {
        System.out.println("\nAvailable commands:");
        System.out.println("  help  - Show this help message");
        System.out.println("  exit  - Exit the application");
        System.out.println("  Any other text will be processed by the AI agent");
    }
}
