# Computer Shop

Computer shop management backend built with Spring Boot, PostgreSQL, JPA, Flyway, and JWT-ready security.

## Quick start

1. Copy `.env.example` to `.env` and set the database and JWT values.
2. Start the local services with `docker compose up --build`.
3. Run tests with `./mvnw test` (or `mvnw.cmd test` on Windows).

The application listens on port `8083` by default.

## Project layout

- `auth`, `user`, `employee`, `customer`, and `supplier` manage identity and parties.
- `product`, `inventory`, `sales`, `purchase`, and `payment` manage commerce workflows.
- `report`, `audit`, and `notification` contain supporting application capabilities.
- `docs/` contains the database, API, and setup documentation.
