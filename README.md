
# Java Chess Game with Modern GUI

A polished Java Swing chess game with a resizable, high‑DPI friendly UI, clear move feedback, and complete rules including castling, en passant, and promotion.


## Features

- Complete chess rules: legal move enforcement, check, checkmate, stalemate
- Special moves: castling (fully validated), en passant, and promotion
- Promotion dialog: choose Queen/Rook/Bishop/Knight on reaching last rank
- Clear visuals: selected square, legal moves, last move, in‑check king highlight
- Move list in standard SAN notation (paired by move number)
- Undo last move from the Game Information panel
- Modern, resizable board that stays perfectly square; HiDPI friendly
- Smooth rendering with double buffering and size‑aware piece icon caching
- PNG icons loaded from classpath with graceful Unicode fallback
- Clean structure and comments around validation and game state

## Requirements

- Java 8 or higher
- Windows PowerShell (for the provided `run.ps1`) — or run manually

## Project Structure (key files)

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
│           ├── ImageLoader.java      # Piece image loader (classpath PNGs)
│           └── UIConstants.java      # Centralized UI colors/sizes
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

1) From project root, use the provided PowerShell script (recommended):

```powershell
./run.ps1
```

- Compiles sources into `bin/` and launches the app.
- Ensures classpath includes resources under `src/chess/resources`.

2) Optional: Compile without running

```powershell
./run.ps1 -NoRun
```

3) Optional: Manual compile/run (if you don’t want to use the script)

```powershell
# Compile (from project root)
if (!(Test-Path "bin")) { New-Item -ItemType Directory -Path "bin" | Out-Null }
javac -encoding UTF-8 -d bin `
    src\chess\Main.java `
    src\chess\board\*.java `
    src\chess\game\*.java `
    src\chess\gui\*.java `
    src\chess\pieces\*.java

# Run (ensure resources are on classpath)
java -cp "bin;src\chess" Main
```

Note: The code uses package-less `Main` and top-level packages (`board`, `game`, `gui`, `pieces`) as currently structured.

## Controls

- Click a piece (current player) to select; legal moves will be highlighted.
- Click a highlighted square to move. Click the selected square again to deselect.
- On pawn reaching last rank, choose the promotion piece in the dialog (Esc/close defaults to Queen).
- Undo: Click the Undo button in the Game Information panel to revert the last move.
- New Game: via Game menu.

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
-   **Robust Move Validation**: Comprehensive rule checking including special moves; castling rules verified (king/rook unmoved, clear path, not through/into check)
-   **Smart Image System**: Classpath PNG loading with size‑aware caching for sharp rendering at any square size; Unicode fallback
-   **Performance Optimized**: Double buffering and targeted repaints

## Troubleshooting

- Images not showing: run via `./run.ps1` so `src\chess` (resources) is on the classpath.
- Script blocked: start PowerShell as Administrator or use `Set-ExecutionPolicy -Scope Process Bypass` for the session.
- Window is blurry on HiDPI: ensure Java scaling settings are default; icons are scaled smoothly and should be crisp.
- App won’t start: verify Java 8+ is installed (`java -version`).


## License

This project is for educational purposes. Feel free to use and modify as needed.
