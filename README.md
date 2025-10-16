# PC Parts Webshop

A full-stack pcparts e-commerce platform featuring a Spring Boot REST API and Angular 18 front-end.

## Prerequisites

- Java 21+
- Maven 3.9+
- Node.js 20+
- npm 10+

## Backend setup (Spring Boot)

```bash
cd backend
mvn spring-boot:run
```

The API will be available at `http://localhost:8081`. Swagger UI lives at `http://localhost:8081/swagger-ui`.

## Frontend setup (Angular)

```bash
cd frontend
npm install
ng serve --port 4200 --open
```

The SPA will be served at `http://localhost:4200`.

## Seeded credentials

| Role  | Username | Email                | Password  |
|-------|----------|----------------------|-----------|
| Admin | `admin`  | `admin@pcparts.shop` | `Admin!234` |
| User  | `salah`  | `salah@pcparts.shop` | `User!234`  |

## API Highlights

- JWT authentication (`/api/auth/login`, `/api/auth/register`)
- Product catalog CRUD (`/api/products`)
- Orders & status management (`/api/orders`)
- User profile & password updates (`/api/users`)
- OpenAPI docs with JWT bearer support (`/v3/api-docs`)

## Frontend Features

- Angular Material UI with Tailwind enhancements
- Catalog filtering, sorting, and product detail pages
- Cart and checkout flow tied to API orders
- Profile area with personal data, password updates, and order history
- Admin console for products, orders, and users
- Global JWT interceptor and guards for authenticated/admin routes

## Troubleshooting

- **Dependency downloads blocked**: If working behind a proxy, configure Maven/ npm proxy settings accordingly.
- **Angular CLI not installed**: Use `npm install -g @angular/cli` or execute commands via `npx`.
- **Windows PowerShell execution policy**: run `Set-ExecutionPolicy -Scope CurrentUser RemoteSigned` to enable Angular CLI scripts.
- **TypeScript peer dependency warnings**: resolve using `npm install --legacy-peer-deps` if necessary.
- **CORS errors**: ensure the backend is running on port 8081 and the frontend on 4200; CORS is configured for `http://localhost:4200`.

## Notes

- Orders in checkout apply a flat 21% VAT as a demonstration.
- Product images rely on Unsplash URLs; replace any broken links with valid image URLs.

