# Enquiry API Documentation

This document describes the API endpoints for managing Enquiries in the CRM system. All endpoints are prefixed with `/api/v1` (or your configured base path) and all successful responses are wrapped in a standard `ApiResponse` structure.

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
      "mobile": "must not be blank"
    }
  },
  "message": "Request failed"
}
```

---

## Enquiry Flow Overview

An Enquiry represents a potential customer's initial contact or lead generation query (e.g., from a website contact form). 
- **Public Creation**: The POST endpoint allows public submission of enquiries (no authentication required).
- **Secure Retrieval**: Only authorized personnel (`SUPER_ADMIN`, `ADMIN`, `STAFF`) can view the list of submitted enquiries.

---

## API Endpoints

### 1. Create Enquiry
Submits a new enquiry into the system. This endpoint is typically exposed to public frontends (e.g., website contact forms) and does not require authentication.

- **Method**: `POST`
- **Path**: `/enquiries`
- **Request Body**: `EnquiryRequest` (JSON)
  ```json
  {
    "name": "Amit Sharma",             // Required
    "mobile": "9876543210",            // Required
    "city": "Mumbai",                  // Optional
    "consumerType": "HOME_OWNER",      // Optional (HOME_OWNER, BUSINESS, COMMERCIAL)
    "monthlyBill": 2500.50,            // Optional
    "preferredSystem": "ONGRID"        // Optional (ONGRID, OFFGRID, HYBRID, NOT_SURE)
  }
  ```
- **Authorization**: Public (No authentication required).
- **Success Status**: `201 Created`

**Response Data Structure:**
Returns the newly created `EnquiryResponse` object.

---

### 2. Get Enquiry List
Retrieves a paginated list of all submitted enquiries.

- **Method**: `GET`
- **Path**: `/enquiries`
- **Query Parameters**:
  - `page` (int, default=0): Page number (0-indexed).
  - `size` (int, default=50): Page size.
- **Authorization**: Requires authenticated user with roles `SUPER_ADMIN`, `ADMIN`, or `STAFF`.
- **Success Status**: `200 OK`

**Response Data Structure:**
Returns a `PageResponse<EnquiryResponse>` in the `data` field.

---

### 3. Update Enquiry (PATCH)
Updates an existing enquiry. Useful for marking enquiries as handled or correcting data.

- **Method**: `PATCH`
- **Path**: `/enquiries/{id}`
- **Path Parameters**:
  - `id` (Long): The ID of the enquiry to update.
- **Request Body**: `UpdateEnquiryRequest` (JSON)
  Any fields provided will update the enquiry. Omitted fields remain unchanged.
  ```json
  {
    "city": "Pune",
    "consumerType": "BUSINESS"
  }
  ```
- **Authorization**: Requires authenticated user with roles `SUPER_ADMIN`, `ADMIN`, or `STAFF`.
- **Success Status**: `200 OK`

**Response Data Structure:**
Returns the updated `EnquiryResponse` object.

---

### 4. Delete Enquiry
Deletes an enquiry from the system permanently.

- **Method**: `DELETE`
- **Path**: `/enquiries/{id}`
- **Path Parameters**:
  - `id` (Long): The ID of the enquiry to delete.
- **Authorization**: Requires authenticated user with roles `SUPER_ADMIN` or `ADMIN`.
- **Success Status**: `204 No Content`

**Response Data Structure:**
No content is returned.

---

## Data Models

### EnquiryResponse
```json
{
  "id": 1,
  "name": "Amit Sharma",
  "mobile": "9876543210",
  "city": "Mumbai",
  "consumerType": "HOME_OWNER",
  "monthlyBill": 2500.50,
  "preferredSystem": "ONGRID",
  "createdAt": "2026-09-19T10:00:00Z"
}
```

### Enums

**ConsumerType**
`HOME_OWNER`, `BUSINESS`, `COMMERCIAL`

**PreferredSystem**
`ONGRID`, `OFFGRID`, `HYBRID`, `NOT_SURE`
