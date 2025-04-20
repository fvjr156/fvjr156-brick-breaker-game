# fvjr156-brick-breaker-game

- A Java game program by **fvjr156**, created April 11-13, 2025.

A classic **Brick Breaker** game built using Java Swing. Players control a paddle at the bottom of the screen to bounce a ball upward, breaking bricks arranged at the top of the playing field. The game features multiple difficulty modes, language support, and responsive controls.

---

## Architecture

The game is structured using the following classes:

1. **Start**: The entry point that initializes the game window
2. **Game**: The main game logic and rendering component
3. **Ball**: A dedicated class representing each ball with position, direction, and rendering
4. **MapGenerator**: Handles the creation and display of brick patterns
5. **Bounds**: An immutable record representing position and dimensions
6. **JFrameBuilder**: A utility class for creating consistent JFrame windows
7. **Strings**: Manages text localization (English, Japanese, and Chinese)
8. **Sound**: Handles audio playback for game events
9. **SettingsWindow**: A configuration panel for customizing game parameters

---

## Class Details

### `Start.java`

- Contains the `main()` method that launches the application.
- Creates a new game window with specified dimensions (710x610).
- Instantiates the `Game` component.
- Makes the window visible to start gameplay.

### Game.java

The central class that manages gameplay, user input, and rendering. It extends `JPanel` and implements `KeyListener` and `ActionListener` interfaces.

#### Key Components:
- **Timer**: Controls the game loop with a configurable delay (12ms default)
- **List\<Ball\>**: Manages multiple balls in play
- **MapGenerator**: Manages the brick layout
- **Game State**: Tracks score, player position, and game status variables

#### Game Modes:
- **EASY**: Simplest ball physics with basic bouncing mechanics
- **NORMAL**: More realistic physics with angle-dependent ball reflection
- **EXPERT**: Enhanced physics with speed variation and more challenging gameplay
- **CRAZY Mode**: Special mode with unlimited balls constantly spawning

#### Key Methods:
- **paintComponent()**: Renders all game elements (bricks, paddle, balls, UI text)
- **actionPerformed()**: Updates game state on each timer tick (ball movement, collisions)
- **keyPressed/keyReleased()**: Handles user input for paddle movement and game controls
- **restartGame()**: Resets the game state for a new game
- **applySettings()**: Updates game configuration based on user settings

#### Controls:
- **LEFT/RIGHT arrows**: Move paddle
- **SHIFT**: Speed boost for paddle movement
- **ENTER**: Start/restart game
- **1/2/3**: Change difficulty mode (when not playing)
- **0**: Open settings window
- **ESC**: Exit CRAZY mode

### Ball.java

A dedicated class representing each ball in the game.

#### Key Components:
- **Position**: (x, y) coordinates
- **Direction**: (xDir, yDir) movement vector
- **Speed**: Controls movement rate
- **Color**: Visual appearance

#### Key Methods:
- **move()**: Updates ball position based on direction and speed
- **reflectX()/reflectY()**: Handles bouncing off surfaces
- **getBounds()**: Returns collision rectangle for interaction detection
- **draw()**: Renders the ball to the screen
- **reset()**: Repositions ball with new parameters

### `MapGenerator.java`

Manages the creation, rendering, and state tracking of the brick layout.

#### Key Components:

- `map[][]`: 2D array representing brick states (`1 = visible`, `0 = broken`).
- `brickWidth`/`brickHeight`: Calculated dimensions for optimal screen fitting.

#### Key Methods:

- `draw()`: Renders the bricks to the screen.
- `setBrickValue()`: Updates the state of an individual brick (used for collision).
- `reset()`: Reconstructs the brick layout when starting a new game or level.

### Bounds.java

An immutable record representing rectangular dimensions (x, y, width, height).

### JFrameBuilder.java

A utility class extending JFrame to simplify window creation with consistent properties.

### Strings.java

Manages text localization with support for English, Japanese, and Chinese.
- Stores arrays of interface strings for each language
- Provides a getter method to retrieve strings based on language code

### Sound.java

Handles sound effects for game events.
- Loads and plays audio files from the classpath
- Currently implements collision sound effects

### SettingsWindow.java

Provides a graphical interface for customizing game parameters.

#### Key Features:
- Language selection
- Brick row and column count adjustment
- Ball count configuration
- CRAZY mode activation

## Game Mechanics

### Physics
The game implements progressively more sophisticated physics across difficulty levels:

1. **EASY**: Simple reflection, ball direction inverted on collision
2. **NORMAL**: Angle-based reflection calculated from impact position
3. **EXPERT**: Enhanced physics with variable ball speed based on impact angle

### Multi-Ball Support
- Game can handle multiple balls simultaneously
- Ball count configurable through settings
- Special "CRAZY Mode" constantly spawns new balls for chaotic gameplay

### Collisions

The game handles three types of collisions:

- **Wall collisions**: Ball bounces off screen borders.
- **Paddle collisions**: Ball direction changes based on where it hits the paddle.
- **Brick collisions**: Bricks disappear when hit, score increases, ball direction changes.

### Sound System
- Audio feedback for brick collisions
- Resource-based sound loading from classpath
- Error handling for systems without audio support

### Game Flow
1. Start screen shows instructions and game mode
2. Player presses ENTER to begin
3. Balls move across screen, player controls paddle to keep them in play
4. Breaking all bricks advances to a new level with reset bricks
5. If all balls fall below paddle, game ends and returns to start screen

### Settings and Customization
- Adjustable brick layout (rows and columns)
- Multiple language support (English, Japanese, Chinese)
- Configurable number of balls in play
- Special gameplay modes

---

## Technical Notes

- Uses **Java Swing** for rendering and user interface.
- Smooth animation is achieved through a **timer-based game loop**.
- Collision detection uses Java's `Rectangle` intersection methods.
- Follows **object-oriented principles** with clear separation of concerns.

---

## Software Used

- Visual Studio Code - Building the game.
- Audacity - Synthesizing and editing the sound file.
- JDK - Running and debugging app, for packaging to JAR.
- VSCode's Extension Pack for Java - Linting, and debugging.

---

A part of the documentation is generated by [claude.ai](https://claude.ai).
Game inspired from a video by [Awaiz Mirza](https://youtu.be/K9qMm3JbOH0?si=jjeLG3BgELYI0FSV).
