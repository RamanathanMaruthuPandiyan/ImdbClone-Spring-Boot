# IMDb Clone

This is an IMDb Clone Project built with **React (frontend)** and **Spring Boot (backend)**. It replicates core IMDb functionalities like browsing, adding, editing movies/persons, importing from IMDb, and managing user ratings.

---

## Features

- **Movie Listings**: Display and manage movies with details like title, genre, and release year.
- **Search & Filter**: Search movies by title, genre, or cast.
- **Movie Details**: View or edit detailed movie information.
- **User Ratings**: Submit and display ratings per movie.
- **Import from IMDb**: Import movie metadata using IMDb ID.
- **Authentication**: OAuth2-based secure access using Keycloak.
- **Role-Based Access Control**: Admin-only access for critical operations.
- **Responsive Design**: Optimized for both desktop and mobile.

---

## Technologies Used

### 🔹 Frontend

- **React**
- **CSS**
- **Axios**

### 🔹 Backend (Spring Boot)

- **Spring Boot**
- **Spring Security (OAuth2, Keycloak)**
- **Spring Data JPA**
- **MapStruct**
- **Lombok**
- **Swagger / OpenAPI**

### 🔹 Database

- **PostgreSQL**  
  _(or replace this if you're using MySQL or another DB)_

---

## Screenshots

#### Home Page
![Home Page](./Screenshots/List%20Movies.png)

#### Add a Movie
![Add a Movie](./Screenshots/Add%20Movie.png)

#### Movies Options Bar
![Options Bar](./Screenshots/Options%20Bar.png)

#### Edit a Movie
![Edit a Movie](./Screenshots/Edit%20Movie.png)

#### List all Persons
![List all Persons](./Screenshots/Persons%20Listing.png)

#### Add Persons
![Add Persons](./Screenshots/Add%20persons.png)

#### Persons Options Bar
![Persons Options Bar](./Screenshots/Persons%20Options%20Bar.png)

#### Edit Persons
![Edit Persons](./Screenshots/Edit%20Persons.png)

#### Import From IMDB Server
![Import From IMDB Server](./Screenshots/Import%20From%20IMDB%20Server.png)

#### Import Process Initiated
![Import Process Initiated](./Screenshots/Import%20Process%20Initiated.png)

#### Imported Movie Listing
![Imported Movie Listing](./Screenshots/Imported%20Movie%20Listing.png)

#### Edit Imported Movie 
![Edit Imported Movie](./Screenshots/Edit%20Imported%20Movie.png)

---

## Installation

### 📦 Clone the Repository

```bash
git clone <repository-url>
cd imdbClone
```

### Navigate to backend folder
```bash
cd backend
```
### Configure application-dev.properties
```bash
spring.datasource.url=jdbc:postgresql://localhost:5432/imdb
spring.datasource.username=your-db-user
spring.datasource.password=your-db-password

spring.jpa.hibernate.ddl-auto=update

spring.security.oauth2.resourceserver.jwt.issuer-uri=http://localhost:8081/realms/imdb
spring.security.oauth2.resourceserver.jwt.jwk-set-uri=http://localhost:8081/realms/imdb/protocol/openid-connect/certs
keycloak.realm-name=imdb
keycloak.base-url=http://localhost:8081
keycloak.client-id=imdbClone
```
### Run the application
```bash
./mvnw spring-boot:run
```

###  Navigate to frontend folder
```bash
cd frontend
```
### Install dependencies
```bash
npm install
```
### Start development server
```bash
npm start
```
### Open in browser
```bash
http://localhost:3000
```

# Folder Structure
```bash
imdbClone/
├── backend/
│   ├── src/
│   ├── pom.xml
│   └── ...
├── frontend/
│   ├── public/
│   ├── src/
│   │   ├── components/
│   │   ├── pages/
│   │   ├── services/
│   │   ├── utils/
│   │   └── App.js
│   ├── package.json
│   └── ...
├── Screenshots/
├── README.md
└── .gitignore
```

## License

This project is licensed under the [MIT License](LICENSE).

## Contact

For any questions or feedback, please reach out to the project maintainer  
M Ramanathan   
+91 8056856956   
rsai48838@gmail.com  