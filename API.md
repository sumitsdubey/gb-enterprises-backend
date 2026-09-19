# GB Enterprises CRM - API Documentation

Base URL: `/api/v1`
Authentication: Bearer Token (JWT) in `Authorization` header for all protected routes.

---

## 1. Authentication (`/auth`)

### 1.1 Login
- **Endpoint**: `POST /auth/login`
- **Auth**: Public
- **Request Body**:
  ```json
  {
    "mobileOrEmail": "string",
    "password": "string"
  }
  ```
- **Response** (200 OK):
  ```json
  {
    "accessToken": "string (JWT)",
    "refreshToken": "string (JWT)",
    "user": {
      "id": 1,
      "name": "string",
      "email": "string",
      "mobile": "string",
      "role": "SUPER_ADMIN | ADMIN | STAFF | USER"
    }
  }
  ```

### 1.2 Refresh Token
- **Endpoint**: `POST /auth/refresh`
- **Auth**: Public
- **Request Body**: `{ "refreshToken": "string" }`
- **Response** (200 OK): Same as Login response.

### 1.3 Forgot Password (Request OTP)
- **Endpoint**: `POST /auth/forgot-password`
- **Auth**: Public
- **Request Body**: `{ "email": "string" }`
- **Response**: `200 OK`

### 1.4 Reset Password (Submit OTP)
- **Endpoint**: `POST /auth/reset-password`
- **Auth**: Public
- **Request Body**:
  ```json
  {
    "email": "string",
    "otp": "string",
    "newPassword": "string"
  }
  ```
- **Response**: `200 OK`

---

## 2. Users (`/users`)

### 2.1 Get Current Profile
- **Endpoint**: `GET /users/me`
- **Auth**: Protected (Any Role)
- **Response** (200 OK): User object.

### 2.2 List Users
- **Endpoint**: `GET /users`
- **Auth**: Protected (SUPER_ADMIN, ADMIN)
- **Query Params**: `search` (optional), `role` (optional), `page` (default 0), `size` (default 50)
- **Response** (200 OK):
  ```json
  {
    "content": [ { "id": 1, "name": "...", "role": "..." } ],
    "totalElements": 10,
    "totalPages": 1,
    "page": 0,
    "size": 50
  }
  ```

### 2.3 Create User
- **Endpoint**: `POST /users`
- **Auth**: Protected (SUPER_ADMIN)
- **Request Body**:
  ```json
  {
    "name": "string",
    "email": "string",
    "mobile": "string",
    "password": "string",
    "role": "ADMIN | STAFF | USER"
  }
  ```
- **Response** (201 Created): User object.

### 2.4 Update User
- **Endpoint**: `PATCH /users/{id}`
- **Auth**: Protected (SUPER_ADMIN, ADMIN, or self)
- **Request Body**: (all fields optional)
  ```json
  {
    "name": "string",
    "mobile": "string",
    "role": "string"
  }
  ```
- **Response** (200 OK): Updated User object.

### 2.5 Change Password
- **Endpoint**: `PATCH /users/{id}/password`
- **Auth**: Protected (self only)
- **Request Body**: `{ "oldPassword": "...", "newPassword": "..." }`
- **Response**: `200 OK`

---

## 3. Leads (`/leads`)

### 3.1 List Leads
- **Endpoint**: `GET /leads`
- **Auth**: Protected (SUPER_ADMIN, ADMIN, STAFF)
- **Query Params**: `name`, `mobile`, `page`, `size`
- **Response** (200 OK): Paginated list of Lead objects.

### 3.2 Create Lead
- **Endpoint**: `POST /leads`
- **Auth**: Protected (SUPER_ADMIN, ADMIN)
- **Request Body**:
  ```json
  {
    "name": "string",
    "mobile": "string",
    "address": "string (optional)",
    "load": "string (optional)",
    "valid": true,
    "alreadyInstalled": false
  }
  ```
- **Response** (201 Created): Lead object.

### 3.3 Update Lead
- **Endpoint**: `PATCH /leads/{id}`
- **Auth**: Protected (SUPER_ADMIN, ADMIN)
- **Request Body**: (all fields optional, including `valid`, `alreadyInstalled`, `load`)
- **Response** (200 OK): Updated Lead object.

### 3.4 Upload Leads (CSV/Excel)
- **Endpoint**: `POST /leads/upload`
- **Auth**: Protected (SUPER_ADMIN, ADMIN)
- **Request Format**: `multipart/form-data` with `file` key (.csv or .xlsx).
- **Format**: Columns: `Name`, `Mobile`, `Address`, `Load` (header ignored).
- **Response** (200 OK): Array of created Lead objects.

---

## 4. Consumers (`/consumers`)

### 4.1 List Consumers
- **Endpoint**: `GET /consumers`
- **Auth**: Protected (All Roles)
- **Query Params**: `name`, `status`, `page`, `size`
- **Response** (200 OK): Paginated list of Consumer objects.

### 4.2 Get Consumer Detail
- **Endpoint**: `GET /consumers/{id}`
- **Auth**: Protected (All Roles)
- **Response** (200 OK): Consumer object (includes `images` and `remarks`).

### 4.3 Create Consumer (From Lead)
- **Endpoint**: `POST /consumers`
- **Auth**: Protected (SUPER_ADMIN, ADMIN, STAFF)
- **Request Body**:
  ```json
  {
    "leadId": 1,
    "status": "INTERESTED",
    "lat": 28.6139,
    "lng": 77.2090,
    "googleMapUrl": "string",
    "load": "string",
    "type": "ONGRID | OFFGRID | HYBRID",
    "nextAction": "CALL | VISIT | DOCUMENT | INSTALL | COMPLETED",
    "expectedActionDate": "YYYY-MM-DD"
  }
  ```
- **Response** (201 Created): Consumer object.

### 4.4 Update Consumer
- **Endpoint**: `PATCH /consumers/{id}`
- **Auth**: Protected (All Roles)
- **Request Body**: (Any fields from create request except `leadId`)
- **Response** (200 OK): Updated Consumer object.

### 4.5 Upload Consumer Images
- **Endpoint**: `POST /consumers/{id}/images`
- **Auth**: Protected (All Roles)
- **Request Format**: `multipart/form-data` with `files` key (multiple files allowed).
- **Response** (200 OK): Array of uploaded image URLs.

### 4.6 Add Remark
- **Endpoint**: `POST /consumers/{id}/remarks`
- **Auth**: Protected (All Roles)
- **Request Body**: `{ "text": "string" }`
- **Response** (201 Created): Remark object (with creator name and timestamp).

### 4.7 Add Feedback (Rating)
- **Endpoint**: `POST /consumers/{id}/feedback`
- **Auth**: Protected (All Roles)
- **Request Body**:
  ```json
  {
    "text": "string",
    "rating": 5
  }
  ```
- **Response** (201 Created): Feedback object.

---

## 5. Enquiries (`/enquiries`)

### 5.1 Create Enquiry (Public Contact Form)
- **Endpoint**: `POST /enquiries`
- **Auth**: Public
- **Request Body**:
  ```json
  {
    "name": "string",
    "mobile": "string",
    "email": "string (optional)",
    "message": "string (optional)",
    "source": "WEBSITE"
  }
  ```
- **Response** (201 Created): Enquiry object.

### 5.2 List Enquiries
- **Endpoint**: `GET /enquiries`
- **Auth**: Protected (SUPER_ADMIN, ADMIN, STAFF)
- **Query Params**: `search`, `status` (e.g. `NEW`, `CONTACTED`, `CLOSED`), `page`, `size`
- **Response** (200 OK): Paginated list of Enquiry objects.

### 5.3 Update Enquiry Status
- **Endpoint**: `PATCH /enquiries/{id}/status`
- **Auth**: Protected (SUPER_ADMIN, ADMIN, STAFF)
- **Request Body**: `{ "status": "CONTACTED" }`
- **Response** (200 OK): Updated Enquiry object.

---

## Error Handling

Standard error response format (RFC 7807 Problem Detail):
```json
{
  "type": "about:blank",
  "title": "Bad Request",
  "status": 400,
  "detail": "Invalid credentials",
  "instance": "/api/v1/auth/login",
  "errors": {
    "email": "must not be blank" // Only present on 400 Validation Errors
  }
}
```
