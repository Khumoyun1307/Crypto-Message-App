# Crypto-Message-App

Crypto-Message-App is a Java-based application for secure, encrypted messaging. It is designed to help users exchange messages with strong privacy guarantees, using modern cryptographic techniques.

## Features

- End-to-end encrypted messaging
- User authentication and registration
- Secure key management
- Simple, intuitive user interface
- Message integrity and confidentiality
- Cross-platform support (runs anywhere Java is available)

## Technologies Used

- Java 8+
- Java Cryptography Architecture (JCA)
- JavaFX (for GUI)
- Maven or Gradle (build tools)

## Getting Started

### Prerequisites

- Java JDK 8 or higher
- Maven or Gradle (for building)
- Git (for cloning the repository)

### Installation

1. **Clone the repository:**
   ```sh
   git clone https://github.com/yourusername/Crypto-Message-App.git
   cd Crypto-Message-App
   ```

2. **Build the project:**
   - With Maven:
     ```sh
     mvn clean install
     ```
   - With Gradle:
     ```sh
     gradle build
     ```

3. **Run the application:**
   - With Maven:
     ```sh
     mvn exec:java
     ```
   - With Gradle:
     ```sh
     gradle run
     ```
   - Or run the JAR directly:
     ```sh
     java -jar target/Crypto-Message-App.jar
     ```

## Usage

1. Register or log in with your credentials.
2. Add contacts by username or email.
3. Send and receive encrypted messages.

## Project Structure

```
Crypto-Message-App/
├── src/                # Java source code
├── config/             # Configuration files
├── resources/          # Static resources (icons, etc.)
├── target/             # Build output
├── README.md           # Project documentation
├── pom.xml / build.gradle
```

## Security

- Uses AES and RSA for encryption
- Passwords are hashed and salted
- No plaintext messages are stored or transmitted

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/your-feature`)
3. Commit your changes
4. Push to your branch
5. Open a Pull Request

## License

This project is licensed under the MIT License. See LICENSE for details.

## Acknowledgements

- Java Cryptography Architecture (JCA)
- JavaFX Community

---

*For questions or support, please open an issue on GitHub.*

## Acknowledgements

- Java Cryptography Architecture (JCA)
- JavaFX Community

---

*For questions or support, please open an issue on GitHub.*

