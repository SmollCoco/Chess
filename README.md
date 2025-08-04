# Java Chess Game with GUI

A fully-featured chess game implementation in Java using Swing for the graphical user interface.

## Features

-   Complete chess game with all standard rules
-   Intuitive graphical user interface
-   Move validation and legal move detection
-   Check and checkmate detection
-   Visual feedback for piece selection and valid moves
-   Turn-based gameplay for two players
-   Game state management

## Requirements

-   Java 8 or higher
-   No external dependencies (uses built-in Swing library)

## Project Structure

```
Chess/
├── src/
│   └── chess/
│       ├── Main.java                 # Entry point
│       ├── game/
│       │   ├── ChessGame.java        # Main game logic controller
│       │   ├── GameState.java        # Game state management
│       │   └── MoveValidator.java    # Move validation logic
│       ├── board/
│       │   ├── ChessBoard.java       # Board representation
│       │   ├── Square.java           # Individual board square
│       │   └── Position.java         # Position coordinates
│       ├── pieces/
│       │   ├── Piece.java            # Abstract base piece class
│       │   ├── Pawn.java             # Pawn piece implementation
│       │   ├── Rook.java             # Rook piece implementation
│       │   ├── Knight.java           # Knight piece implementation
│       │   ├── Bishop.java           # Bishop piece implementation
│       │   ├── Queen.java            # Queen piece implementation
│       │   ├── King.java             # King piece implementation
│       │   └── PieceColor.java       # Enum for piece colors
│       └── gui/
│           ├── ChessGUI.java         # Main GUI window
│           ├── BoardPanel.java       # Chess board visual component
│           ├── SquarePanel.java      # Individual square component
│           └── GameInfoPanel.java    # Game status display
├── resources/
│   └── images/                       # Chess piece images (optional)
├── TECHNICAL_GUIDE.md               # Detailed implementation guide
├── .gitignore
└── README.md
```

## Getting Started

1. **Clone/Download** the project
2. **Follow the technical guide** in `TECHNICAL_GUIDE.md` to implement the game
3. **Compile** the Java files:
    ```bash
    javac -d bin src/chess/*.java src/chess/*/*.java
    ```
4. **Run** the game:
    ```bash
    java -cp bin chess.Main
    ```

## Development Phases

The implementation is broken down into manageable phases:

1. **Phase 1**: Basic project setup and core classes
2. **Phase 2**: Chess piece implementations
3. **Phase 3**: Board representation and management
4. **Phase 4**: Move validation and game rules
5. **Phase 5**: GUI implementation
6. **Phase 6**: Game flow and user interaction
7. **Phase 7**: Testing and refinement

## Technical Highlights

-   **Object-Oriented Design**: Clean separation of concerns with distinct packages
-   **MVC Pattern**: Separation of game logic, data model, and user interface
-   **Polymorphism**: Abstract piece class with concrete implementations
-   **Event-Driven GUI**: Responsive Swing-based interface
-   **Robust Move Validation**: Comprehensive rule checking including special moves

## License

This project is for educational purposes. Feel free to use and modify as needed.
