# Lead API Documentation

This document describes the API endpoints for managing Leads within the CRM application.
All responses follow a standard format wrapped in an `ApiResponse` object.

## Standard API Response Format
All successful responses will be returned as:
```json
{
  "success": true,
  "message": "Operation successful",
  "data": { ... } // Varies by endpoint
}
```
All error responses will be returned as:
```json
{
  "success": false,
  "error": {
    "code": "ERROR_CODE",
    "message": "Human readable error message",
    "validationErrors": { ... } // Optional map of field errors
  }
}
```

---

## Endpoints

### 1. List Leads
Retrieves a paginated list of leads with optional filtering.

- **Method**: `GET`
- **Path**: `/leads`
- **Query Parameters**:
  - `name` (String, optional): Filter leads by partial name match.
  - `mobile` (String, optional): Filter leads by partial mobile number match.
  - `valid` (Boolean, optional): Filter leads by validity (`true` or `false`).
  - `alreadyInstalled` (Boolean, optional): Filter leads by installation status (`true` or `false`).
  - `page` (Integer, default `0`): Page index for pagination.
  - `size` (Integer, default `50`): Number of items per page.
- **Authorization**: Requires authenticated user.

**Response Data Structure:**
Returns a `PageResponse<LeadResponse>` in the `data` field.

---

### 2. Get Single Lead
Retrieves details of a specific lead by its ID.

- **Method**: `GET`
- **Path**: `/leads/{id}`
- **Path Parameters**:
  - `id` (Long): The unique identifier of the lead.
- **Authorization**: Requires authenticated user.

**Response Data Structure:**
Returns a `LeadResponse` object.

---

### 3. Create Lead
Creates a new lead.

- **Method**: `POST`
- **Path**: `/leads`
- **Request Body**: `CreateLeadRequest` (JSON)
  ```json
  {
    "name": "John Doe",           // Required
    "mobile": "9876543210",       // Required
    "address": "123 Solar Street",
    "load": "5kW",
    "valid": true,                // Defaults to true
    "alreadyInstalled": false     // Defaults to false
  }
  ```
- **Authorization**: Requires authenticated user.
- **Success Status**: `201 Created`

**Response Data Structure:**
Returns the newly created `LeadResponse` object.

---

### 4. Update Lead (PATCH)
Partially updates an existing lead. Can be used to mark a lead as invalid or already installed.

- **Method**: `PATCH`
- **Path**: `/leads/{id}`
- **Path Parameters**:
  - `id` (Long): The unique identifier of the lead.
- **Request Body**: `UpdateLeadRequest` (JSON, all fields optional)
  ```json
  {
    "valid": false,
    "alreadyInstalled": true,
    "name": "Jane Doe",
    "mobile": "9876543210",
    "address": "New Address",
    "load": "10kW"
  }
  ```
- **Authorization**: Requires authenticated user.

**Response Data Structure:**
Returns the updated `LeadResponse` object.

---

### 5. Delete Lead
Deletes a specific lead from the system.

- **Method**: `DELETE`
- **Path**: `/leads/{id}`
- **Path Parameters**:
  - `id` (Long): The unique identifier of the lead.
- **Authorization**: **Requires `ADMIN` or `SUPER_ADMIN` role.**
- **Success Status**: `204 No Content` (No data returned).

---

### 6. Upload Leads Bulk (Excel/CSV)
Uploads a bulk file of leads.

- **Method**: `POST`
- **Path**: `/leads/upload`
- **Headers**: `Content-Type: multipart/form-data`
- **Form Data**:
  - `file`: The `.xlsx` or `.csv` file.
- **Authorization**: **Requires `ADMIN` or `SUPER_ADMIN` role.**

**Response Data Structure:**
Returns a list of created `LeadResponse` objects.
