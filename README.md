# Buddy AI Agent

A powerful AI-powered assistant built with Java, integrating **Temporal workflows** and **Microsoft Semantic Kernel** for intelligent task processing and conversation management.

## Features

- 🤖 **AI-Powered Conversations**: Powered by OpenAI GPT models through Semantic Kernel
- ⚡ **Temporal Workflows**: Robust, scalable workflow orchestration
- 🔌 **Plugin System**: Extensible plugin architecture for custom functions
- 🌤️ **Built-in Functions**: Weather, time, calculations, and user preferences
- 🔧 **Configurable**: Supports both OpenAI and Azure OpenAI services
- 📝 **Conversation History**: Maintains context across interactions

## Quick Start

### Prerequisites

- Java 17 or higher
- Maven 3.6+
- OpenAI API key or Azure OpenAI service

### Environment Setup

Configure your AI service by setting one of the following environment variable sets:

#### Option 1: OpenAI
```bash
export OPENAI_API_KEY="your-openai-api-key"
export MODEL_ID="gpt-3.5-turbo"  # Optional, defaults to gpt-3.5-turbo
```

#### Option 2: Azure OpenAI
```bash
export AZURE_OPENAI_API_KEY="your-azure-openai-key"
export AZURE_OPENAI_ENDPOINT="https://your-resource.openai.azure.com/"
export MODEL_ID="gpt-35-turbo"  # Optional, defaults to gpt-35-turbo
```

### Build and Run

1. **Clone and build the project:**
   ```bash
   git clone <repository-url>
   cd buddy
   mvn clean compile
   ```

2. **Start Temporal server** (in a separate terminal):
   ```bash
   temporal server start-dev
   ```

3. **Run the Buddy AI Agent:**
   ```bash
   mvn exec:java -Dexec.mainClass="com.jase.BuddyApplication"
   ```

## Semantic Kernel Integration

This project follows the [Microsoft Semantic Kernel Java quick start guide](https://learn.microsoft.com/en-us/semantic-kernel/get-started/quick-start-guide?tabs=Java&pivots=programming-language-java) and includes:

### Core Components

- **Kernel**: The central orchestrator that manages AI services and plugins
- **Chat Completion Service**: Handles conversations with OpenAI/Azure OpenAI
- **Plugin System**: Enables the AI to call custom functions
- **Function Calling**: Automatic invocation of functions based on user requests

### Built-in Plugin Functions

The `BuddyPlugin` provides these functions:

| Function | Description | Example Usage |
|----------|-------------|---------------|
| `get_current_time` | Returns current date and time | "What time is it?" |
| `get_weather(location)` | Gets weather for a location | "What's the weather in London?" |
| `get_user_info(info_type)` | Retrieves user information | "What's my name?" |
| `set_user_preference(key, value)` | Sets user preferences | "Set my name to John" |
| `calculate(expression)` | Performs calculations | "Calculate 15 * 7" |
| `help` | Shows available functions | "What can you do?" |

### Example Interactions

```
User> What time is it?
Buddy: The current time is 2025-07-09 23:15:30

User> What's the weather in New York?
Buddy: The weather in New York is currently sunny with a temperature of 22°C (72°F). It's a beautiful day with light winds and clear skies.

User> Calculate 25 * 4
Buddy: 25.00 * 4.00 = 100.00

User> Set my name to Alice
Buddy: User preference 'name' has been set to 'Alice'
```

## Architecture

### Project Structure

```
src/main/java/com/jase/
├── BuddyApplication.java          # Main application entry point
├── BuddyWorkflow.java             # Temporal workflow interface
├── BuddyWorkflowImpl.java         # Temporal workflow implementation
├── BuddyActivities.java           # Semantic Kernel integration
├── BuddyActivitiesInterface.java  # Activities interface
├── BuddyPlugin.java               # Custom plugin with utility functions
└── BuddyConfig.java               # Configuration management
```

### Technology Stack

- **[Microsoft Semantic Kernel](https://learn.microsoft.com/en-us/semantic-kernel/)**: AI orchestration and plugin system
- **[Temporal](https://temporal.io/)**: Workflow orchestration engine
- **[OpenAI API](https://platform.openai.com/)**: Large Language Model integration
- **Maven**: Build and dependency management
- **SLF4J + Logback**: Logging framework

### Key Dependencies

```xml
<dependency>
    <groupId>com.microsoft.semantic-kernel</groupId>
    <artifactId>semantickernel-api</artifactId>
</dependency>
<dependency>
    <groupId>com.microsoft.semantic-kernel</groupId>
    <artifactId>semantickernel-aiservices-openai</artifactId>
</dependency>
<dependency>
    <groupId>io.temporal</groupId>
    <artifactId>temporal-sdk</artifactId>
</dependency>
```

## Development

### Creating Custom Plugins

To create a new plugin, follow this pattern:

```java
public class MyCustomPlugin {
    
    @DefineKernelFunction(name = "my_function", description = "Description of what this function does")
    public String myFunction(
        @KernelFunctionParameter(name = "param1", description = "Description of parameter") String param1) {
        // Your function logic here
        return "Function result";
    }
}
```

Then register it in `BuddyActivities.java`:

```java
KernelPlugin myPlugin = KernelPluginFactory.createFromObject(new MyCustomPlugin(), "MyPlugin");
this.kernel = Kernel.builder()
    .withAIService(ChatCompletionService.class, chatCompletionService)
    .withPlugin(buddyPlugin)
    .withPlugin(myPlugin)  // Add your plugin
    .build();
```

### Running Tests

```bash
mvn test
```

### Mock Mode

If no API keys are configured, Buddy runs in mock mode with predefined responses, perfect for development and testing.

## Configuration

### Application Properties

Configure logging and other settings in `src/main/resources/application.properties`:

```properties
# Application settings
app.name=Buddy AI Agent
app.version=1.0-SNAPSHOT

# Add your custom configuration here
```

### Logging Configuration

Customize logging behavior in `src/main/resources/logback.xml`.

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Support

For questions and support:
- Review the [Semantic Kernel documentation](https://learn.microsoft.com/en-us/semantic-kernel/)
- Check the [Temporal documentation](https://docs.temporal.io/)
- Open an issue in this repository
