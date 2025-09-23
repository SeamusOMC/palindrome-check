# 🟣 Palindrome Check API

A **Spring Boot** REST API to check whether a string is a palindrome, with **caching** and **history persistence**.

---

## 📋 Features

- Check if a string is a palindrome 
- **Caching** with Caffeine to avoid repeated computation  
- **History** of all checked strings stored in `data/processed_palindromes.jsonl`.  
- Simple REST endpoints for integration or testing.  

---

## 🚀 Getting Started

### Prerequisites

- Java 17+
- Maven 3+
- (Preferational ) IntelliJ IDEA, VS Code, or any IDE for easier code reviewal and ammendments

### Build & Run

Clone the repository:

```bash
git clone https://github.com/SeamusOMC/palindrome-check.git
cd palindrome-check
```
Now you are inside the project root simply  build the project and then run it 
```bash 
./mvnw clean package
./mvnw spring-boot:run
```
### How to use

Now that you've cloned and built the project, you can test out the API endpoints for yourself.  

The endpoints are:

#### Check a Palindrome

```
POST /api/palindrome/check
Content-Type: application/json
```

**Request Body Example:**

```json
{
  "username": "Seamus",
  "text": "racecar"
}
```

**Response Example:**

```json
{
  "username": "Seamus",
  "text": "racecar",
  "palindrome": true,
  "cached": false,
  "timestamp": "2025-09-22T12:34:56Z"
}
```

#### Get History

```
GET /api/palindrome/history
```

- Returns a list of all previously checked palindromes.  
- Useful to see what has been stored in `data/processed_palindromes.jsonl`.

To quickly test functionality through a bash curl feel free to use the below command
```bash
 curl -X POST http://localhost:8080/api/palindrome/check   -H "Content-Type: application/json"   -d '{"username":"testUser","text":"cIvic"}'
```

## 🌳Project Tree 
```
src/
├─ main/java/com/example/palindrome_check
│  ├─ controller
│  ├─ model
│  ├─ repository
│  ├─ service
│  ├─ startup
│  └─ config
└─ resources
   ├─ static
   └─ templates
```

To find out more about the Project, its implementation and the testing process as it develops, please see link provided below

[Confluence Page](https://seamusg.atlassian.net/wiki/external/MDE5MjM5MGE5ZWU4NGU5ZTlmY2FjMGNmYjZhODU3ZGE)