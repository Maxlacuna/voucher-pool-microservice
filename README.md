# Voucher Pool Microservice

This is a Spring Boot microservice for managing voucher codes.

## Database Schema

The service uses three entities:

-   **Recipient**: Stores recipient information.
    -   `id`: UUID (Primary Key)
    -   `name`: String
    -   `email`: String (Unique)
-   **SpecialOffer**: Stores special offer details.
    -   `id`: UUID (Primary Key)
    -   `name`: String
    -   `discountPercentage`: Integer
-   **VoucherCode**: Stores voucher code details.
    -   `id`: UUID (Primary Key)
    -   `code`: String (Unique)
    -   `expirationDate`: LocalDate
    -   `redeemed`: boolean
    -   `redeemedAt`: LocalDateTime
    -   `recipient_id`: Foreign Key to Recipient
    -   `special_offer_id`: Foreign Key to SpecialOffer

## API Documentation

The API is documented using OpenAPI (Swagger). You can access the Swagger UI at `http://localhost:8080/swagger-ui.html`.

### Endpoints

-   **`POST /vouchers/generate`**: Generates voucher codes for all recipients for a given special offer.
    -   Request Parameters:
        -   `specialOfferName`: String
        -   `expirationDate`: LocalDate (format: `yyyy-MM-dd`)
-   **`POST /vouchers/redeem`**: Redeems a voucher code.
    -   Request Parameters:
        -   `code`: String
        -   `email`: String
-   **`GET /vouchers/recipient/{email}`**: Retrieves a paginated list of valid voucher codes for a given recipient.
    -   Path Variable:
        -   `email`: String
    -   Pagination Parameters:
        -   `page`: int (default: 0)
        -   `size`: int (default: 20)
        -   `sort`: string (e.g., `expirationDate,desc`)
-   **`GET /vouchers/offer/{offerName}`**: Retrieves a paginated list of redeemed voucher codes for a given special offer.
    -   Path Variable:
        -   `offerName`: String
    -   Pagination Parameters:
        -   `page`: int (default: 0)
        -   `size`: int (default: 20)
        -   `sort`: string (e.g., `redeemedAt,desc`)

## How to Run the Project

1.  **Prerequisites**:
    -   Java 21
    -   Maven
    -   PostgreSQL
2.  **Database Setup**:
    -   Create a PostgreSQL database named `voucher_db`.
    -   Update the `spring.datasource.username` and `spring.datasource.password` in `src/main/resources/application.properties` if necessary.
3.  **Run the application**:
    ```bash
    mvn spring-boot:run
    ```

## Example Postman Requests

### Generate Vouchers

-   **URL**: `POST http://localhost:8080/vouchers/generate`
-   **Body** (form-data):
    -   `specialOfferName`: `Black Friday Offer`
    -   `expirationDate`: `2025-12-31`

### Redeem Voucher

-   **URL**: `POST http://localhost:8080/vouchers/redeem`
-   **Body** (form-data):
    -   `code`: `your-voucher-code`
    -   `email`: `recipient@example.com`

### Get Valid Vouchers for Recipient

-   **URL**: `GET http://localhost:8080/vouchers/recipient/test@example.com?page=0&size=10`

### Get Redeemed Vouchers for Offer

-   **URL**: `GET http://localhost:8080/vouchers/offer/Black%20Friday%20Offer?page=0&size=10`
