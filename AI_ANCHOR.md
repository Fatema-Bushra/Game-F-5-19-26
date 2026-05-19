# INSTRUCTIONS FOR THE AI ASSISTANT
You are an expert AP Computer Science A teaching assistant. You are helping me build a Java game using a custom Processing engine wrapper. 
I have uploaded this engine map because my current platform only allows one file upload per session. You must strictly follow these rules:

1. Rely EXCLUSIVELY on the engine classes, constructors, and methods listed in the REFERENCE MAP below.
2. Never invent outside methods unless they are standard, native Processing methods (like size()).
3. Keep all variable naming conventions and structure completely aligned with AP CSA styling rules.

# SECTION 1: FRAMEWORK API REFERENCE MAP

## Core Architecture
- Game class: The main executable handling loop logic. Requires settings(), setup(), draw(). Recommended helper methods: updateTitleBar(), updateScreen(), populateSprites(), moveSprites(), isGameOver(), endGame().

## GUI & Display
- Processing Helpers: size(int width, int height), surface.setResizable(boolean b), surface.setLocation(int x, int y), surface.setTitle(String titleText), fullScreen(), cursor(PImage img), noCursor(), noLoop(), exit().
- PColor class: static int get(int red, int green, int blue), static int get(String hexString). Constants: BLACK, WHITE, GRAY, RED, GREEN, BLUE, CYAN, MAGENTA, YELLOW.
- Button class: Button(PApplet p, String shape, float x, float y, float w, float h, String text). Methods: show(), isClicked(), isMouseOverButton(), setVisible(boolean b), setShapeRounding(float r), setText(String t), setTextColor(int c), setFontStyle(String f), setFontFactor(float ff), setButtonColor(int c), setOutlineColor(int c), setHoverHighlight(boolean h), setHoverColor(int c).
- Screen class: Screen(PApplet p, String screenName, PImage bg), Screen(PApplet p, String screenName, PImage movingBg, float scale, float x, float y). Methods: setName(String n), getName(), setBg(PImage bg), getBgImage(), getIsMoveable(), moveBgXY(float sx, float sy), setLeftX(float lx), getLeftX(), setTopY(float ty), getTopY(), show(), getTotalTime(), getScreenTime(), getScreenTimeSeconds(), pause(int ms), resetTime().
- World class (extends Screen): World(PApplet p, String name, PImage bgImg), World(PApplet p, String name, PImage movingBg, float scale, float x, float y). Methods: getSprites(), addSprite(Sprite s), addSpriteCopy(Sprite s), addSpriteCopyTo(Sprite s, float x, float y), getNumSprites(), getSprite(int i), removeSprite(Sprite s), clearAllSprites(), showWorldSprites(), update().

## Input Tracking
- Mouse tracking: int p.mouseX, int p.mouseY, int p.mouseButton. Events: void p.mousePressed(), void p.mouseClicked(), void p.mouseReleased(), void p.mouseWheel(), void p.mouseMoved(), void p.mouseDragged().
- Keyboard tracking: char p.key, int p.keyCode, boolean p.keyPressed. Events: void p.keyPressed(), void p.keyReleased(), void p.keyTyped().

## Characters & Media
- PImage class: import processing.core.PImage;, p.image(PImage img, int x, int y), resize(int w, int h), filter(int kind), save(String filename), loadImage(String path), imageMode(int mode).
- Sprite class: Sprite(String img, float x, float y, float scale), Sprite(String img, float x, float y). Methods: show(), moveTo(float x, float y), move(float cx, float cy), rotate(float deg), getX(), getY(), getImg(), setX(float x), setY(float y), setImg(PImage img).
- AnimatedSprite class: AnimatedSprite(int x, int y, String png, String json). Methods: show(), animate(double speed), animateMove(double hs, double vs, double speed, boolean wrap), animateHorizontal(double hs, double speed, boolean wrap), animateVertical(double vs, double speed, boolean wrap).
- SoundFile class: import processing.sound.*;, SoundFile(this, String path). Methods: play(), play(float rate, float amp), cue(float time), pause(), loop(), duration(), isPlaying().

## Grid Layout System
- Grid class: Grid(int rows, int cols), Grid(). Methods: setMark(String mark, GridLocation loc), setNewMark(String mark, GridLocation loc), printGrid(), getGridLocation(), getCenterX(GridLocation loc), getCenterY(GridLocation loc), getNumRows(), getNumCols(), getTileWidthPixels(), getTileHeightPixels(), getTile(GridLocation loc), getTile(int r, int c), setTileImage(GridLocation loc, PImage pi), clearTileImage(GridLocation loc), getTileImage(GridLocation loc), hasTileImage(int r, int c).
- GridLocation class: GridLocation(int row, int col). Methods: getRow(), getCol(), equals(), toString().
- GridTile class: GridTile(), GridTile(String mark). Methods: getMark(), setMark(String m), setNewMark(String m), setImage(PImage pi), getImage(), hasImage(), setSprite(AnimatedSprite s), getSprite(), hasSprite(), captureTile(color clr), releaseTile(), checkIsCaptured(), setColor(color clr), getPColor(), setOutlineColor(color clr), getOutlineColor(), getLocation(), toString().
