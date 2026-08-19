# 🧬 PokéCenter — Pokémon Management and Battle System

## 📖 Description

PokéCenter is a desktop application developed in Java as a university project focused on **object-oriented programming, software architecture, and data persistence**.

The system simulates a Pokémon management environment where users can manage Pokémon, Trainers, Types, and Battles through a graphical interface. It also includes a battle engine that determines outcomes based on type advantages and stats.

The project demonstrates practical application of OOP principles, layered architecture, JSON-based persistence, and automated testing.

---

## ⚙️ Main Features

- **Pokémon Management** → Create, edit, list, and remove Pokémon with detailed attributes.
- **Trainer Management** → Register trainers and manage their Pokémon teams.
- **Type System** → Define Pokémon types and their weaknesses.
- **Battle System** → Simulate battles between trainers using type effectiveness and stats.
- **Battle History** → Store and track past battles and results.
- **JSON Persistence** → Local data storage using JSON files.
- **Graphical Interface** → Desktop UI built with Java Swing.
- **Automated Testing** → Unit tests for persistence and DAO operations.
- **Generic DAO Layer** → Reusable data access structure for all entities.

---

## 🧠 Battle System Overview

Battles are resolved based on type advantages and Pokémon stats.

1. Each Pokémon’s types are compared against the opponent’s weaknesses.
2. Type advantages are calculated for both sides.
3. The Pokémon with the highest advantage wins.
4. In case of a tie, stats are used as a tiebreaker.

Trainer battles aggregate individual Pokémon results, and the trainer with the most wins is declared the winner.

---

## 🏗️ Architecture

The project follows a layered structure:

- **Domain Layer** → Core entities such as Pokémon, Trainer, Type, and Battle.
- **Persistence Layer** → Generic DAO implementation with JSON storage.
- **Presentation Layer** → Java Swing-based graphical interface.

This separation ensures modularity and maintainability.

---

## 💾 Data Persistence

All data is stored locally in JSON files:

- `pokemons.json`
- `treinadores.json`
- `tipos.json`
- `batalhas.json`

Data is automatically loaded on startup and saved after modifications.

---

## 🧪 Testing

The project includes **32 JUnit tests**, covering DAO operations such as:

- Create, update, delete
- Data retrieval
- Persistence validation

All tests pass successfully.

---

## 🛠️ Technologies

- Java 17
- Java Swing
- Maven
- Jackson (JSON processing)
- JUnit 5
- Git

---

## 📚 Key Concepts Applied

- Object-Oriented Programming (OOP)
- Inheritance and Polymorphism
- Encapsulation and Abstraction
- Generic Programming
- Layered Architecture
- Data Persistence
- Unit Testing

---

## 🚀 How to Run

### Clone the repository

```bash
git clone https://github.com/jpnetto/PokeCenter.git
cd PokeCenter
```

### Build the project

```bash
mvn clean package
```

### Run the application

```bash
java -jar target/pokemon-app.jar
```

### Run the tests

```bash
mvn test
```

---
