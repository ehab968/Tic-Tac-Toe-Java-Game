# XO Game (Tic Tac Toe)
![Java](https://img.shields.io/badge/Java-17+-orange)
![JavaFX](https://img.shields.io/badge/JavaFX-UI-blue)
![TCP](https://img.shields.io/badge/Networking-TCP-green)

A full-featured Tic Tac Toe desktop game with offline, online, and AI modes built using JavaFX and Client-Server architecture.


## Description
This project implements the classic Tic Tac Toe game logic:
- 3 × 3 grid  
- Two players (X and O)  
- Three types of game modes  
- Automatic win and draw detection  


## Features
- Local multiplayer  
- Online multiplayer with invitations  
- Player vs Computer with difficulty levels  
- User authentication  
- Real-time gameplay  
- Score tracking  
- Game replay  
- Restart & exit support  
- Choose score win before playing  
- Leaderboard support  


## 📸 Screenshots

### 🏠 Home Screen & 🔐 Login
<img src="https://github.com/user-attachments/assets/1242fdae-1b1d-4180-8fd5-e81ef3a3e9b5" width="450" />
<img src="https://github.com/user-attachments/assets/b483d8df-ab83-404f-b39b-77a8dc820bf6" width="450" />

<br><br>

### 📝 Register & 👥 Online Users
<img src="https://github.com/user-attachments/assets/edd03a99-27ab-4596-acd3-9cee9d7d4d63" width="450" />
<img src="https://github.com/user-attachments/assets/092899a3-6646-48a1-88ff-6c94e0ff946a" width="450" />

<br><br>

### 🎮 Online Game & 🤖 AI Mode
<img src="https://github.com/user-attachments/assets/f903b66c-00fc-4b04-b4de-945096ced237" width="450" />
<img src="https://github.com/user-attachments/assets/d3bd5bbd-c6ff-4cc1-9f9d-d664913afcd4" width="450" />

<br><br>

### 🏁 Choose Score & 🏆 Leaderboard
<img src="https://github.com/user-attachments/assets/44f378e6-b249-4260-bdd7-b769d3caa45e" width="450" />
<img src="https://github.com/user-attachments/assets/87b56404-f205-4241-9856-6adaeb8d28e3" width="450" />



## Technologies Used
- Java  
- JavaFX  
- TCP Sockets  
- Multithreading  
- Apache Derby Database  


## System Architecture
- Client-Server architecture  

### Server handles:
- User authentication  
- Online users management  
- Game sessions  
- Score updates  

**Two sockets are used:**
- Authentication Socket  
- Game Stream Socket  

### Client handles:
- UI rendering (JavaFX)  
- User interactions  
- Real-time updates via sockets  


## How to Run

### Requirements
- Java 17 or higher  
- JavaFX  
- Apache Derby Database  

### Steps
1. Run the server application.  
2. Start the database.  
3. Run the client application.  
4. Login or register a new user.  
5. Choose a game mode.  

---

## AI Difficulty Levels
- Easy: Random moves  
- Medium: Semi-optimal moves  
- Hard: Minimax algorithm (unbeatable)  

---

## Error Handling
- Handles server disconnection gracefully  
- Prevents invalid moves  
- Handles unexpected client disconnects  

---

## Contributors
- Ehab Salah  
- Mahmoud ELDemerdash  
- Ahmed Sayed  
- Shahd Ashraf  
