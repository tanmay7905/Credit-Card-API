# Credit Card API

The Credit Card API is a Spring Boot application that provides endpoints for managing credit cards and user associations. It allows you to add credit cards, retrieve credit card information, update balance history, and perform various operations related to credit cards and users.

## Prerequisites

Before running the application, ensure that you have the following:

- Java Development Kit (JDK) installed
- Apache Maven or Gradle installed
- A database management system (e.g., MySQL, PostgreSQL) set up and running

## Getting Started

### Clone the repository
```bash
git clone https://github.com/your-username/credit-card-api.git
Configure the database connection
Open the application.properties file located in src/main/resources.
Update the database connection properties (spring.datasource.url, spring.datasource.username, spring.datasource.password) according to your database setup.
Build the application
If using Maven:

bash
Copy code
mvn clean install
If using Gradle:

bash
Copy code
./gradlew clean build
Run the application
If using Maven:

bash
Copy code
mvn spring-boot:run
If using Gradle:

bash
Copy code
./gradlew bootRun
The application will start running on http://localhost:8080.

API Endpoints
The following endpoints are available in the Credit Card API:

POST /credit-card: Add a credit card to a user.
GET /credit-card:all: Get all credit cards of a user.
GET /credit-card:user-id: Get the user ID associated with a credit card.
POST /credit-card:update-balance: Update the balance history of credit cards.
For detailed information on the request and response formats of each endpoint, please refer to the API documentation.

Testing
To test the API endpoints, you can use tools like cURL or Postman. Here's an example of adding a credit card using cURL:

bash
Copy code
curl -X POST -H "Content-Type: application/json" -d '{
  "userId": 1,
  "cardIssuanceBank": "Bank of Example",
  "cardNumber": "1234567890123456"
}' http://localhost:8080/credit-card
Make sure to replace the userId, cardIssuanceBank, and cardNumber with appropriate values.

Contributing
Contributions are welcome! If you find any issues or want to enhance the functionality of the Credit Card API, please feel free to submit a pull request.

