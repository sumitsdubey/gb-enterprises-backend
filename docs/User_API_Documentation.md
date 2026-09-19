# User API Documentation

This document provides detailed information about the User Management and Profile APIs.
Base path for all endpoints: `/`

---

## Admin: User Management

### 1. List Users
Retrieve a paginated list of users.

- **URL:** `/users`
- **Method:** `GET`
- **Purpose:** Retrieve a list of all users in the system with pagination support.
- **Authorization:** Bearer Token required. Must have `SUPER_ADMIN` or `ADMIN` role.
- **Headers:**
  - `Authorization`: `Bearer <token>`

#### Request Parameters (Query)
| Parameter | Type | Default | Description |
| :--- | :--- | :--- | :--- |
| `page` | Integer | `0` | Page number to retrieve (0-indexed) |
| `size` | Integer | `20` | Number of records per page |

#### Response: `200 OK`
**Content-Type:** `application/json`
```json
{
  "content": [
    {
      "id": 1,
      "name": "John Doe",
      "email": "john@example.com",
      "mobile": "1234567890",
      "role": "ADMIN",
      "active": true,
      "createdAt": "2023-10-27T10:00:00Z"
    }
  ],
  "totalElements": 50,
  "totalPages": 3,
  "page": 0,
  "size": 20
}
```

---

### 2. Create User
Create a new user in the system.

- **URL:** `/users`
- **Method:** `POST`
- **Purpose:** Admin endpoint to create a new user account.
- **Authorization:** Bearer Token required. Must have `SUPER_ADMIN` or `ADMIN` role.
- **Headers:**
  - `Authorization`: `Bearer <token>`
  - `Content-Type`: `application/json`

#### Request Body
| Field | Type | Validation | Description |
| :--- | :--- | :--- | :--- |
| `name` | String | `@NotBlank` | Full name of the user |
| `email` | String | Optional | Email address |
| `mobile` | String | Optional | Mobile number |
| `password` | String | `@NotBlank` | User's initial password |
| `role` | String | Optional | Role of the user (e.g., `SUPER_ADMIN`, `ADMIN`, `USER`) |

**Example:**
```json
{
  "name": "Jane Smith",
  "email": "jane.smith@example.com",
  "mobile": "9876543210",
  "password": "securepassword123",
  "role": "ADMIN"
}
```

#### Response: `201 Created`
**Content-Type:** `application/json`
```json
{
  "id": 2,
  "name": "Jane Smith",
  "email": "jane.smith@example.com",
  "mobile": "9876543210",
  "role": "ADMIN",
  "active": true,
  "createdAt": "2023-10-27T10:30:00Z"
}
```

---

### 3. Update User
Update an existing user's details.

- **URL:** `/users/{id}`
- **Method:** `PATCH`
- **Purpose:** Update specific fields of an existing user.
- **Authorization:** Bearer Token required. Must have `SUPER_ADMIN` or `ADMIN` role.
- **Headers:**
  - `Authorization`: `Bearer <token>`
  - `Content-Type`: `application/json`

#### Request Parameters (Path)
| Parameter | Type | Description |
| :--- | :--- | :--- |
| `id` | Long | ID of the user to update |

#### Request Body
*Only fields provided in the request will be updated.*

| Field | Type | Description |
| :--- | :--- | :--- |
| `name` | String | Full name of the user |
| `email` | String | Email address |
| `mobile` | String | Mobile number |
| `role` | String | Role of the user |
| `active` | Boolean | Account status (true = active, false = disabled) |

**Example:**
```json
{
  "name": "Jane Doe Smith",
  "active": false
}
```

#### Response: `200 OK`
**Content-Type:** `application/json`
```json
{
  "id": 2,
  "name": "Jane Doe Smith",
  "email": "jane.smith@example.com",
  "mobile": "9876543210",
  "role": "ADMIN",
  "active": false,
  "createdAt": "2023-10-27T10:30:00Z"
}
```

---

### 4. Delete User
Delete a user from the system.

- **URL:** `/users/{id}`
- **Method:** `DELETE`
- **Purpose:** Permanently remove a user from the database.
- **Authorization:** Bearer Token required. Must have `SUPER_ADMIN` role.
- **Headers:**
  - `Authorization`: `Bearer <token>`

#### Request Parameters (Path)
| Parameter | Type | Description |
| :--- | :--- | :--- |
| `id` | Long | ID of the user to delete |

#### Response: `204 No Content`
*(No response body is returned on successful deletion)*

---

## Self: Profile

### 5. Get Current Profile
Retrieve the authenticated user's own profile.

- **URL:** `/me`
- **Method:** `GET`
- **Purpose:** Fetch details of the currently logged-in user.
- **Authorization:** Bearer Token required.
- **Headers:**
  - `Authorization`: `Bearer <token>`

#### Response: `200 OK`
**Content-Type:** `application/json`
```json
{
  "id": 1,
  "name": "John Doe",
  "email": "john@example.com",
  "mobile": "1234567890",
  "role": "ADMIN",
  "active": true,
  "createdAt": "2023-10-27T10:00:00Z"
}
```

---

### 6. Update Profile
Update the authenticated user's own profile details.

- **URL:** `/me`
- **Method:** `PATCH`
- **Purpose:** Allow the current user to update their own profile information.
- **Authorization:** Bearer Token required.
- **Headers:**
  - `Authorization`: `Bearer <token>`
  - `Content-Type`: `application/json`

#### Request Body
| Field | Type | Validation | Description |
| :--- | :--- | :--- | :--- |
| `name` | String | `@NotBlank` | Updated full name |
| `email` | String | Optional | Updated email address |
| `mobile` | String | Optional | Updated mobile number |

**Example:**
```json
{
  "name": "Johnathan Doe",
  "mobile": "0987654321"
}
```

#### Response: `200 OK`
**Content-Type:** `application/json`
```json
{
  "id": 1,
  "name": "Johnathan Doe",
  "email": "john@example.com",
  "mobile": "0987654321",
  "role": "ADMIN",
  "active": true,
  "createdAt": "2023-10-27T10:00:00Z"
}
```

---

### 7. Change Password
Change the authenticated user's password.

- **URL:** `/me/change-password`
- **Method:** `POST`
- **Purpose:** Update the current user's login password.
- **Authorization:** Bearer Token required.
- **Headers:**
  - `Authorization`: `Bearer <token>`
  - `Content-Type`: `application/json`

#### Request Body
| Field | Type | Validation | Description |
| :--- | :--- | :--- | :--- |
| `currentPassword` | String | `@NotBlank` | The user's current password |
| `newPassword` | String | `@NotBlank` | The new desired password |

**Example:**
```json
{
  "currentPassword": "oldPassword123",
  "newPassword": "newSecurePassword456!"
}
```

#### Response: `200 OK`
*(No response body is returned on successful password change)*
