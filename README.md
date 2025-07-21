# Crypto Message App

A simple Java console-based application for secure messaging using hybrid encryption (AES-GCM + RSA) and digital signatures.

## Table of Contents

- [Features](#features)
- [Prerequisites](#prerequisites)
- [Getting Started](#getting-started)
  - [Clone the Repository](#clone-the-repository)
  - [Compile](#compile)
  - [Run](#run)
- [Usage](#usage)
- [Project Structure](#project-structure)
- [Configuration](#configuration)
- [Cryptography Details](#cryptography-details)
- [Testing](#testing)
- [Contributing](#contributing)
- [License](#license)

## Features

- **Hybrid Encryption**: Combines AES-GCM for symmetric encryption and RSA for key exchange.
- **Digital Signatures**: Ensures message integrity and authenticity.
- **Console Interface**: Simple UI for sending and receiving encrypted messages.
- **Reusable Core Library**: The `cryptoLib` module serves as a standalone JAR for integration in other projects.

## Prerequisites

- Java 11 or higher installed.
- Maven or Gradle for dependency management.
- Git for cloning the repository.

## Getting Started

### Clone the Repository

```bash
git clone https://github.com/yourusername/crypto-message-app.git
cd crypto-message-app
```

### Compile

Using Maven:

```bash
mvn clean package
```

Or using Gradle:

```bash
gradle build
```

### Run

```bash
java -jar cryptoMessageApp/target/crypto-message-app-1.0.jar
```

## Usage

1. Generate or provide RSA key pairs for sender and receiver.
2. Enter the plaintext message when prompted.
3. The app encrypts the message using AES-GCM, encrypts the AES key with RSA, and signs the ciphertext.
4. On the receiving side, the app verifies the signature, decrypts the AES key, and retrieves the original plaintext.

## Project Structure

- `cryptoMessageApp` – the console-based UI and application entry point
- `cryptoLib` – the core domain engine module, provided as a standalone JAR for reuse in other projects
- `tests` – unit and integration tests for both `cryptoMessageApp` and `cryptoLib`

## Configuration

Edit `config.properties` to set:
- AES key length
- RSA key size
- Preferred cipher transformation

## Cryptography Details

- **AES-GCM**: Provides confidentiality and integrity with minimal overhead.
- **RSA**: Used to securely exchange the AES session key.
- **SHA-256**: Hash function for generating digital signatures.

## Testing

Run unit tests:

```bash
mvn test
```

Or:

```bash
gradle test
```

## Contributing

Contributions are welcome! Please open issues or submit pull requests.

## License

This project is licensed under the MIT License. See [LICENSE](LICENSE) for details.
