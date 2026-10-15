# API documentation

The REST API is organized by business capability. Controllers live in each capability's `controller` package and should expose versioned resource endpoints under `/api`.

Authentication endpoints belong under `/api/auth`. Protected endpoints use the bearer JWT issued by the authentication flow.

## Authentication

`POST /api/auth/login` is public:

```json
{
  "username": "admin",
  "password": "your-password"
}
```

Use the returned token as `Authorization: Bearer <token>` on protected requests.

## Products

- `GET /api/products` — ADMIN or STAFF
- `GET /api/products/{id}` — ADMIN or STAFF
- `POST /api/products` — ADMIN
- `PUT /api/products/{id}` — ADMIN
- `DELETE /api/products/{id}` — ADMIN

## Inventory

Inventory is stored in `products.quantity_on_hand` and exposed through a dedicated stock API.

- `GET /api/inventory` — ADMIN or STAFF
- `GET /api/inventory/{productId}` — ADMIN or STAFF
- `POST /api/inventory` — ADMIN
- `PUT /api/inventory/{productId}` — ADMIN
- `DELETE /api/inventory/{productId}` — ADMIN; sets stock to zero and keeps the product

Create or set inventory for an existing product:

```json
{
  "productId": 10,
  "quantityOnHand": 25
}
```

For `PUT`, send only `quantityOnHand`:

```json
{
  "quantityOnHand": 40
}
```

## Sales process

`POST /api/sales` accepts a customer and product quantities:

```json
{
  "customerId": 1,
  "items": [
    { "productId": 10, "quantity": 2 },
    { "productId": 11, "quantity": 1 }
  ]
}
```

The authenticated employee is resolved from the JWT account. The service then validates the
customer, locks product rows with a pessimistic write lock, reads prices from the database,
checks stock, creates the sale and line items, decreases inventory, and commits everything in
one transaction. Any validation or persistence failure rolls back the sale and stock changes.

## First administrator

Set `ADMIN_ENABLED=true`, `ADMIN_USERNAME`, `ADMIN_PASSWORD`, and `ADMIN_EMAIL` before the first
startup. The application hashes and creates the administrator only when that username does not
already exist. Disable `ADMIN_ENABLED` after initialization if the one-time bootstrap is no
longer needed.
