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

This guide provides detailed instructions on how to test the various endpoints of the service using Postman.

### Step 1: Create a Special Offer

*   **Purpose:** To add a new special offer to the system.
*   **Method:** `POST`
*   **URL:** `http://localhost:8080/special-offers`
*   **Headers:**
    *   `Content-Type`: `application/json`
*   **Body (raw, JSON):**
    ```json
    {
        "name": "Summer Sale",
        "discountPercentage": 25
    }
    ```
*   **Expected Response:** `200 OK` with the created `SpecialOffer` object in the response body, including its generated `id`.

### Step 2: Create a Recipient

*   **Purpose:** To add a new recipient to the system.
*   **Method:** `POST`
*   **URL:** `http://localhost:8080/recipients`
*   **Headers:**
    *   `Content-Type`: `application/json`
*   **Body (raw, JSON):**
    ```json
    {
        "name": "John Doe",
        "email": "john.doe@example.com"
    }
    ```
*   **Expected Response:** `200 OK` with the created `Recipient` object in the response body, including its generated `id`.

### Step 3: Generate Vouchers

*   **Purpose:** To generate voucher codes for all existing recipients based on a special offer.
*   **Method:** `POST`
*   **URL:** `http://localhost:8080/vouchers/generate`
*   **Query Parameters:**
    *   `specialOfferName`: `Summer Sale`
    *   `expirationDate`: `2026-12-31` (Use a future date in `YYYY-MM-DD` format)
*   **Expected Response:** `200 OK` with an empty response body.

### Step 4: Get Valid Vouchers for a Recipient

*   **Purpose:** To retrieve all valid (unredeemed and unexpired) vouchers for a specific recipient.
*   **Method:** `GET`
*   **URL:** `http://localhost:8080/vouchers/recipient/john.doe@example.com`
*   **Optional Query Parameters (for pagination):**
    *   `page`: `0`
    *   `size`: `10`
    *   `sort`: `expirationDate,desc`
*   **Expected Response:** `200 OK` with a JSON array of `VoucherCode` objects. **Make sure to copy one of the `code` values from the response for the next step.**

### Step 5: Redeem a Voucher

*   **Purpose:** To mark a specific voucher code as redeemed for a recipient.
*   **Method:** `POST`
*   **URL:** `http://localhost:8080/vouchers/redeem`
*   **Query Parameters:**
    *   `code`: *[Paste the voucher code you copied from Step 4]*
    *   `email`: `john.doe@example.com`
*   **Expected Response:** `200 OK` with the updated `VoucherCode` object in the response body, showing `redeemed: true` and a `redeemedAt` timestamp.

### Step 6: Get Redeemed Vouchers for an Offer

*   **Purpose:** To retrieve all redeemed vouchers associated with a specific special offer.
*   **Method:** `GET`
*   **URL:** `http://localhost:8080/vouchers/offer/Summer%20Sale`
*   **Optional Query Parameters (for pagination):**
    *   `page`: `0`
    *   `size`: `10`
    *   `sort`: `redeemedAt,desc`
*   **Expected Response:** `200 OK` with a JSON array of `VoucherCode` objects that have been redeemed for the specified offer.
