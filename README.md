
# Java Chess Game with Modern GUI

A fully-featured chess game in Java using Swing, with a modern, resizable interface and visual move highlighting.


## Features

-   Complete chess game with all standard rules
-   Modern, resizable graphical user interface
-   Move validation and legal move detection
-   Check and checkmate detection
-   Visual feedback for piece selection and all legal moves (highlighted on board)
-   Turn-based gameplay for two players
-   Game state management
-   Board always stays perfectly square, even when window is resized
-   Improved piece and label styling for a professional look
-   Chess piece icons (PNG images) with automatic fallback to Unicode symbols
-   Smart image loading and caching system

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
│           ├── GameInfoPanel.java    # Game status display
│           └── ImageLoader.java      # Chess piece image loader
│       └── resources/
│           └── images/               # Chess piece icons (PNG format)
│               ├── white_king.png    # White king piece icon
│               ├── white_queen.png   # White queen piece icon
│               ├── white_rook.png    # White rook piece icon
│               ├── white_bishop.png  # White bishop piece icon
│               ├── white_knight.png  # White knight piece icon
│               ├── white_pawn.png    # White pawn piece icon
│               ├── black_king.png    # Black king piece icon
│               ├── black_queen.png   # Black queen piece icon
│               ├── black_rook.png    # Black rook piece icon
│               ├── black_bishop.png  # Black bishop piece icon
│               ├── black_knight.png  # Black knight piece icon
│               └── black_pawn.png    # Black pawn piece icon
├── .gitignore
└── README.md
```


## Getting Started

1. **Clone/Download** the project
2. **Compile** all Java files (from the project root):
    ```powershell
    # Create bin directory if it doesn't exist
    if (!(Test-Path "bin")) { New-Item -ItemType Directory -Path "bin" }
    
    # Compile all Java files
    cd .\src
    javac -d ..\bin chess\*.java chess\board\*.java chess\game\*.java chess\gui\*.java chess\pieces\*.java
    ```
3. **Run** the game:
    ```powershell
    cd .\chess
    java -cp bin Main.java
    ```

   **Alternative (Simpler)**: Use the provided PowerShell script:
    ```powershell
    cd .\src\chess
    .\run.ps1
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
-   **Visual Move Highlighting**: All legal moves for a selected piece are shown on the board
-   **Resizable, Modern UI**: Board and info panel scale with the window, always keeping the board square
-   **Robust Move Validation**: Comprehensive rule checking including special moves
-   **Smart Image System**: Automatic loading of chess piece PNG icons with graceful fallback to Unicode symbols
-   **Performance Optimized**: Image caching and efficient board rendering


## License

This project is for educational purposes. Feel free to use and modify as needed.
