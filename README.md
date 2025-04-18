# fvjr156-brick-breaker-game

- A Java game program by **fvjr156**, created April 11-13, 2025.

A classic **Brick Breaker** game built using Java Swing. Players control a paddle at the bottom of the screen to bounce a ball upward, breaking bricks arranged at the top of the playing field. The game features multiple difficulty modes, language support, and responsive controls.

---

## Architecture

The game is structured using the following classes:

- **Start**: The entry point that initializes the game window.
- **Game**: The main game logic and rendering component.
- **MapGenerator**: Handles the creation and display of brick patterns.
- **BallPos**: A mutable class representing the ball's position and direction.
- **Bounds**: An immutable record representing position and dimensions.
- **JFrameBuilder**: A utility class for creating consistent JFrame windows.
- **Strings**: Manages text localization (English and Japanese).
- **SettingsWindow**: Allows the user to set language, and number of brick rows and columns.

---

## Class Details

### `Start.java`

- Contains the `main()` method that launches the application.
- Creates a new game window with specified dimensions (710x610).
- Instantiates the `Game` component.
- Makes the window visible to start gameplay.

---

### `Game.java`

The central class that manages gameplay, user input, and rendering. It extends `JPanel` and implements `KeyListener` and `ActionListener`.

#### Key Components:

- **Timer**: Controls the game loop with a configurable delay (8ms default).
- **BallPos**: Tracks the ball's position and movement direction.
- **MapGenerator**: Manages the brick layout.
- **Game State**: Tracks score, player position, and game status variables.

#### Game Modes:

- `EASY`: Simplest ball physics with basic bouncing mechanics.
- `NORMAL`: More realistic physics with angle-dependent ball reflection.
- `EXPERT`: Enhanced physics with speed variation and more challenging gameplay.

#### Key Methods:

- `paintComponent()`: Renders all game elements (bricks, paddle, ball, UI text).
- `actionPerformed()`: Updates game state on each timer tick (ball movement, collisions).
- `keyPressed()/keyReleased()`: Handles user input for paddle movement and game controls.
- `restartGame()`: Resets the game state for a new game.

#### Controls:

- `LEFT/RIGHT` arrows: Move paddle.
- `SHIFT`: Speed boost for paddle movement.
- `ENTER`: Start/restart game.
- `1/2/3`: Change difficulty mode (when not playing).
- `0`: Open SettingsWindow

---

### `MapGenerator.java`

Manages the creation, rendering, and state tracking of the brick layout.

#### Key Components:

- `map[][]`: 2D array representing brick states (`1 = visible`, `0 = broken`).
- `brickWidth`/`brickHeight`: Calculated dimensions for optimal screen fitting.

#### Key Methods:

- `draw()`: Renders the bricks to the screen.
- `setBrickValue()`: Updates the state of an individual brick (used for collision).
- `reset()`: Reconstructs the brick layout when starting a new game or level.

---

### `Models.java`

Contains two data structures:

- **BallPos**: A mutable class tracking ball position (`x`, `y`) and direction (`xDir`, `yDir`).
- **Bounds**: An immutable record representing rectangular dimensions (`x`, `y`, `width`, `height`).

---

### `JFrameBuilder.java`

A utility class extending `JFrame` to simplify window creation with consistent properties.

---

### `Strings.java`

Manages text localization with support for English and Japanese.

- Stores arrays of interface strings for each language.
- Provides a getter method to retrieve strings based on language code.

---

### `SettingsWindow.java`

Allows user to set game values.

- JFrame window which allows user to set language (to either EN or JP)
- Allows user to set the number of rows and columns of the bricks.
- When save button is clicked, game UI is rerendered to reflect overwritten values.
- Values doesn't persist across game instances, and program starts.

---

## Game Mechanics

### Physics

The game implements progressively more sophisticated physics across difficulty levels:

- **EASY**: Simple reflection, ball direction inverted on collision.
- **NORMAL**: Angle-based reflection calculated from impact position.
- **EXPERT**: Enhanced physics with variable ball speed based on impact angle.

### Collisions

The game handles three types of collisions:

- **Wall collisions**: Ball bounces off screen borders.
- **Paddle collisions**: Ball direction changes based on where it hits the paddle.
- **Brick collisions**: Bricks disappear when hit, score increases, ball direction changes.

---

## Game Flow

1. Start screen shows instructions and game mode.
2. Player presses `ENTER` to begin.
3. Ball moves across screen, player controls paddle to keep it in play.
4. Breaking all bricks advances to a new level with reset bricks.
5. If ball falls below paddle, game ends and returns to start screen.

## Technical Notes

- Uses **Java Swing** for rendering and user interface.
- Smooth animation is achieved through a **timer-based game loop**.
- Collision detection uses Java's `Rectangle` intersection methods.
- Follows **object-oriented principles** with clear separation of concerns.

---

A part of the documentation is generated by [claude.ai](https://claude.ai).
Game inspired from a video by [Awaiz Mirza](https://youtu.be/K9qMm3JbOH0?si=jjeLG3BgELYI0FSV).
