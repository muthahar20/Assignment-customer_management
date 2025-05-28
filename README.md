# Assignment-customer_management
customer management API


## How to Run
- Build: `mvn clean install`
- Run: `mvn spring-boot:run`

## H2 Console
- URL: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:customerdb`
- User: `sa`, Password: (empty)

## Sample Requests
- `POST /customers` to create
- `GET /customers/{id}` to fetch
- `PUT /customers/{id}` to update
- `DELETE /customers/{id}` to delete
