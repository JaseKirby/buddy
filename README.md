# Buddy AI Agent

A powerful AI-powered assistant built with Java, integrating **Temporal workflows** and **LangChain4j** for intelligent task processing and conversation management.

## Features

- 🤖 **AI-Powered Conversations**: Powered by OpenAI GPT models through LangChain4j
- ⚡ **Temporal Workflows**: Robust, scalable workflow orchestration
- 🔌 **Tool System**: Extensible tool architecture for custom functions
- 🌤️ **Built-in Tools**: Weather, time, calculations, and user preferences
- 🔧 **Configurable**: Supports OpenAI API integration
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
export MODEL_ID="gpt-4o-mini"  # Optional, defaults to gpt-4o-mini
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

## LangChain4j Integration

This project uses **LangChain4j**, a Java library that provides seamless integration with Large Language Models and includes:

### Core Components

- **ChatLanguageModel**: Manages communication with OpenAI models
- **AiServices**: Creates AI assistants with automatic tool integration
- **ChatMemory**: Maintains conversation context across interactions
- **Tool System**: Enables the AI to call custom functions
- **Automatic Function Calling**: Seamless invocation of tools based on user requests

### Built-in Tool Functions

The `BuddyPlugin` provides these tools:
|| Tool | Description | Example Usage |
|----------|-------------|---------------|
| `getCurrentTime` | Returns current date and time | "What time is it?" |
| `getWeather(location)` | Gets weather for a location | "What's the weather in London?" |
| `getUserInfo(infoType)` | Retrieves user information | "What's my name?" |
| `setUserPreference(key, value)` | Sets user preferences | "Set my name to John" |
| `calculate(expression)` | Performs calculations | "Calculate 15 * 7" |
| `getHelp` | Shows available functions | "What can you do?" |
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
├── BuddyActivities.java           # LangChain4j integration
├── BuddyActivitiesInterface.java  # Activities interface
├── BuddyPlugin.java               # Custom plugin with utility functions
└── BuddyConfig.java               # Configuration management
```

### Technology Stack

- **[LangChain4j](https://github.com/langchain4j/langchain4j)**: AI orchestration and tool system
- **[Temporal](https://temporal.io/)**: Workflow orchestration engine
- **[OpenAI API](https://platform.openai.com/)**: Large Language Model integration
- **Maven**: Build and dependency management
- **SLF4J + Logback**: Logging framework

### Key Dependencies

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j</artifactId>
</dependency>
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-open-ai</artifactId>
</dependency>
<dependency>
    <groupId>io.temporal</groupId>
    <artifactId>temporal-sdk</artifactId>
</dependency>
```

## Development

### Creating Custom Tools

To create a new tool, follow this pattern:

```java
public class MyCustomTool {
    
    @Tool("Description of what this tool does")
    public String myTool(String param1) {
        // Your tool logic here
        return "Tool result";
    }
}
```

Then register it in `BuddyActivities.java`:

```java
MyCustomTool myTool = new MyCustomTool();
this.assistant = AiServices.builder(BuddyAssistant.class)
    .chatLanguageModel(chatModel)
    .chatMemory(chatMemory)
    .tools(buddyPlugin, myTool)  // Add your tool
    .build();
```

### Running Tests

```bash
mvn test
```

### Mock Mode

If no API key is configured, Buddy runs in mock mode with predefined responses, perfect for development and testing.

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
- Review the [LangChain4j documentation](https://github.com/langchain4j/langchain4j)
- Check the [Temporal documentation](https://docs.temporal.io/)
- Open an issue in this repository
