#ChessAI
ChessAI is a Java program that allows you to play chess against an AI opponent that uses the Minimax algorithm with Alpha-Beta pruning to make its moves. The program has a simple graphical user interface that allows you to move pieces with a mouse click, and also features a move history display, undo/redo functionality, and the ability to save and load game states.

#Getting Started
To get started with ChessAI, follow these steps:

- Clone the repository using git clone https://github.com/jaceg18/ChessAI.git.
- Open the project in your preferred Java IDE.
- Run the program.

#Gameplay
The game is played on a standard 8x8 chess board, and the AI opponent is black while the human player is white. The human player moves first, and can make moves by clicking on a piece and then clicking on the square they want to move the piece to.

The AI opponent will then make its move, using the Minimax algorithm with Alpha-Beta pruning to evaluate all possible moves and select the one that maximizes its chance of winning while minimizing the human player's chance of winning.

The game continues until one player is checkmated, resigns, or the game is drawn.

#Features
- Human vs. AI gameplay with Minimax algorithm and Alpha-Beta pruning.
- Customizable board and piece themes.
- Game state saving and loading.
- Move history and undo/redo functionality.
- Game end detection (checkmate, stalemate, draw).

#Code Structure
The code for ChessAI is organized into the following packages:

com.jaceg.chess.Logic: Contains the main game logic and user interface.
com.jaceg.chess.Engine: Contains the Minimax algorithm with Alpha-Beta pruning for the AI opponent.
com.jaceg.chess.Board: Contains the board representation and move validation logic.
com.jaceg.chess.Pieces: Contains the piece classes and their specific move validation logic.
com.jaceg.chess.View: Contains the graphical user interface components.

Contributions to ChessAI are welcome! If you find a bug, want to request a feature, or want to contribute code, please submit an issue or pull request.

#License
This project is licensed under the MIT License. See the LICENSE file for details.

#Credits
This project was developed by Jace Grant.
