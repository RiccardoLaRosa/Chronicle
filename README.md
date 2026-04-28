#  Chronicle

Piattaforma editoriale blog multi-ruolo realizzata con Java e Spring Boot.

##  Funzionalità

- Registrazione e login utenti con Spring Security
- Sistema di ruoli (Admin, Writer, Revisor, User)
- Creazione e pubblicazione articoli
- Sistema di categorie e tag
- Bacheca Revisor per moderazione articoli
- Pannello Admin con gestione utenti e richieste
- Ricerca articoli
- Candidatura come Writer o Revisor
- Home responsive con Thymeleaf e Bootstrap

## 🛠️ Tech Stack

- **Backend:** Java · Spring Boot · Spring Security · Spring Data JPA
- **Frontend:** Thymeleaf · Bootstrap 5 · JavaScript
- **Database:** MySQL
- **Build:** Maven

##  Installazione

```bash
git clone https://github.com/RiccardoLaRosa/Chronicle.git
cd Chronicle
```

Configura il file `application.properties` con le tue credenziali:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/chronicle
spring.datasource.username=TUO_USERNAME
spring.datasource.password=TUA_PASSWORD
spring.jpa.hibernate.ddl-auto=update
```

Poi avvia il progetto con Maven:

```bash
./mvnw spring-boot:run
```

##  Ruoli

| Ruolo | Permessi |
|-------|----------|
| **Admin** | Gestione completa utenti, articoli e richieste |
| **Revisor** | Moderazione e approvazione articoli |
| **Writer** | Creazione e pubblicazione articoli |
| **User** | Lettura articoli e candidatura |

## 📸 Note

Le credenziali del database vanno configurate nel file `application.properties`.
