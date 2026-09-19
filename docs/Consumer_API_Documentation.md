# Consumer API Documentation

This document describes the API endpoints for managing Consumers in the CRM system. All endpoints are prefixed with `/api/v1` (or your configured base path) and all successful responses are wrapped in a standard `ApiResponse` structure.

---

## Base Response Structure

All endpoints return a standardized JSON structure:

**Success Response:**
```json
{
  "success": true,
  "data": { ... },
  "message": "Operation successful"
}
```

**Error Response:**
```json
{
  "success": false,
  "error": {
    "code": "VALIDATION_ERROR",
    "message": "Validation failed",
    "validationErrors": {
      "mobile": "must not be null"
    }
  },
  "message": "Request failed"
}
```

---

## Consumer Flow Overview

1. **Independent Entity**: A Consumer is an independent entity containing its own `name`, `mobile`, and `address`. It does not require a Lead ID.
2. **Creation**: When creating a Consumer, if a Lead already exists in the system, the frontend can query the Lead API to auto-fill the Consumer's `name`, `mobile`, `address`, and `load`. However, the Consumer is saved completely independently.
3. **Tracking**: The Consumer can be updated with tracking statuses, map coordinates, solar system type, remarks, expected action dates, and site images.
4. **Ordering**: Consumer lists are returned ordered by `expectedActionDate` in ascending order, meaning upcoming and overdue actions are prioritized at the top of the list.

---

## API Endpoints

### 1. Get Consumer List (Search)
Retrieves a paginated list of consumers with optional filters. Results are ordered by `expectedActionDate` ascending.

- **Method**: `GET`
- **Path**: `/consumers`
- **Query Parameters**:
  - `name` (String, optional): Filter by consumer name (partial match, case-insensitive).
  - `mobile` (String, optional): Filter by mobile number (partial match).
  - `status` (String, optional): Filter by exact status (e.g., `INTERESTED`).
  - `page` (int, default=0): Page number.
  - `size` (int, default=50): Page size.
- **Authorization**: Requires authenticated user.

**Response Data Structure:**
Returns a `PageResponse<ConsumerResponse>` in the `data` field.

---

### 2. Get Single Consumer
Retrieves detailed information for a specific consumer.

- **Method**: `GET`
- **Path**: `/consumers/{id}`
- **Path Parameters**:
  - `id` (Long): Consumer ID.
- **Authorization**: Requires authenticated user.

**Response Data Structure:**
Returns a `ConsumerResponse` object.

---

### 3. Create Consumer
Creates a new consumer record.

- **Method**: `POST`
- **Path**: `/consumers`
- **Request Body**: `CreateConsumerRequest` (JSON)
  ```json
  {
    "name": "Ramesh Kumar",         // Required
    "mobile": "9876543210",         // Required
    "address": "123 Solar Street",
    "status": "INTERESTED",         // Defaults to INTERESTED if omitted
    "lat": 28.7041,
    "lng": 77.1025,
    "googleMapUrl": "https://maps.google.com/...",
    "load": 5.0,
    "type": "ONGRID",               // ONGRID, OFFGRID, HYBRID
    "nextAction": "CALL",           // CALL, VISIT, DOCUMENT, INSTALL, COMPLETED
    "expectedActionDate": "2026-10-01"
  }
  ```
- **Authorization**: Requires authenticated user.
- **Success Status**: `201 Created`

**Response Data Structure:**
Returns the newly created `ConsumerResponse` object.

---

### 4. Update Consumer (PATCH)
Partially updates an existing consumer.

- **Method**: `PATCH`
- **Path**: `/consumers/{id}`
- **Path Parameters**:
  - `id` (Long): Consumer ID.
- **Request Body**: `UpdateConsumerRequest` (JSON)
  Any fields provided will update the consumer. Omitted fields remain unchanged.
  ```json
  {
    "name": "Ramesh Updated",
    "mobile": "9876543211",
    "status": "VISITED",
    "expectedActionDate": "2026-10-05"
  }
  ```
- **Authorization**: Requires authenticated user.

**Response Data Structure:**
Returns the updated `ConsumerResponse` object.

---

### 5. Upload Consumer Images
Uploads site survey or installation images for a consumer.

- **Method**: `POST`
- **Path**: `/consumers/{id}/images`
- **Content-Type**: `multipart/form-data`
- **Form Data**:
  - `files`: One or more image files.
- **Authorization**: Requires authenticated user.

**Response Data Structure:**
Returns a list of image URLs (List of Strings).

---

### 6. Add Remark
Adds a remark/note to the consumer's timeline.

- **Method**: `POST`
- **Path**: `/consumers/{id}/remarks`
- **Request Body**: `AddRemarkRequest` (JSON)
  ```json
  {
    "text": "Visited the site, roof requires minor repairs before installation."
  }
  ```
- **Authorization**: Requires authenticated user.
- **Success Status**: `201 Created`

**Response Data Structure:**
Returns the newly created `RemarkResponse` object.

---

### 7. Get Consumer Feedback
Retrieves paginated feedback entries for a consumer.

- **Method**: `GET`
- **Path**: `/consumers/{id}/feedback`
- **Query Parameters**:
  - `page` (int, default=0): Page number.
  - `size` (int, default=20): Page size.
- **Authorization**: Requires authenticated user.

**Response Data Structure:**
Returns a `PageResponse<FeedbackResponse>` in the `data` field.

---

### 8. Add Feedback
Adds feedback and a rating for a consumer.

- **Method**: `POST`
- **Path**: `/consumers/{id}/feedback`
- **Request Body**: `FeedbackRequest` (JSON)
  ```json
  {
    "text": "Excellent service and quick installation.",
    "rating": 5
  }
  ```
- **Authorization**: Requires authenticated user.
- **Success Status**: `201 Created`

**Response Data Structure:**
Returns the newly created `FeedbackResponse` object.

---

## Data Models

### ConsumerResponse
```json
{
  "id": 1,
  "name": "Ramesh Kumar",
  "mobile": "9876543210",
  "address": "123 Solar Street",
  "status": "INTERESTED",
  "lat": 28.7041,
  "lng": 77.1025,
  "googleMapUrl": "https://maps.google.com/...",
  "load": 5.0,
  "type": "ONGRID",
  "nextAction": "CALL",
  "expectedActionDate": "2026-10-01",
  "images": [
    "/uploads/consumers/1/uuid_image1.jpg"
  ],
  "remarks": [
    {
      "id": 1,
      "text": "Visited the site...",
      "createdBy": "Admin User",
      "createdAt": "2026-09-19T10:00:00Z"
    }
  ],
  "createdBy": "Admin User",
  "updatedBy": "Admin User",
  "createdAt": "2026-09-19T09:00:00Z",
  "updatedAt": "2026-09-19T10:00:00Z"
}
```

### Enums

**Status**
`INTERESTED`, `NEED_VISIT`, `VISITED`, `APPLIED`, `LOAN_PROCESSING`, `DOCUMENTATION_PROCESSING`, `READY_TO_INSTALL`, `INSTALLED`

**SystemType**
`ONGRID`, `OFFGRID`, `HYBRID`

**NextAction**
`CALL`, `VISIT`, `DOCUMENT`, `INSTALL`, `COMPLETED`
