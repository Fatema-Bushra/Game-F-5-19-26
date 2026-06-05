import processing.core.PApplet;
import processing.core.PImage;
import java.util.HashMap;



public class Game extends PApplet {

  // --- Visual Layout Fields ---
HashMap<String, PImage> cardImages;
PImage backOfCardImage;

  PApplet p;
  public static final int APP_WIDTH = 800;
  public static final int APP_HEIGHT = 600;

  public void settings() {
    //SETUP: Match the screen size to the background image size
    size(APP_WIDTH, APP_HEIGHT, JAVA2D);  //request app size from Processing here
    // Allows p variable to be used by other classes to access PApplet methods
    p = this;
    
  }

  
  // 1. Declare the Blackjack backend object
  Blackjack blackjack;
  String bettingInput = "";     // Temporarily holds the numbers the player types
  boolean enteringBet = true;   // Tracks if the player is currently typing a bet
  
  public void setup() {
    // ... your existing setup code ...
    blackjack = new Blackjack(1000);
    cardImages = new HashMap<String, PImage>();

    // Load the card back image (Make sure you have a card back file or substitute name)
    backOfCardImage = loadImage("images/cardBack.png"); 

    // Generate strings matching your exact file names to load all 52 cards
    String[] suits = {"Hearts", "Diamonds", "Clubs", "Spades"};
    String[] ranks = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};

    for (String suit : suits) {
        for (String rank : ranks) {
            // Translate short names to full names if your files use them
            String fullRank = rank;
            //if (rank.equals("A")) fullRank = "Ace";
            //else if (rank.equals("J")) fullRank = "Jack";
            //else if (rank.equals("Q")) fullRank = "Queen";
            //else if (rank.equals("K")) fullRank = "King";

            // Example file path string: "images/2_of_Clubs.png"
            String fileName = "images/" + fullRank + "_of_" + suit + ".png"; 
            
            PImage img = loadImage(fileName);
            if (img != null) {
                // Store using a unique key combination, e.g., "2Clubs" or "AceHearts"
                cardImages.put(rank + suit, img);
            }
        }
    }
<<<<<<< HEAD
    this.imageMode(PConstants.CORNER);    //Set Images to read coordinates at corners
    this.width = APP_WIDTH;
    this.height = APP_HEIGHT;
    this.pixelWidth = APP_WIDTH;
    this.pixelHeight = APP_HEIGHT;
=======
}
>>>>>>> 6ed1ac156c537b3780dc382dc439ae2b820b0344

@Override
public void draw() {
    background(20, 110, 50); // Poker room green felt felt
    
<<<<<<< HEAD
    //SETUP: Construct Game objects used in All Screens
    runningHorse = new AnimatedSprite(p, "sprites/horse_run.png", "sprites/horse_run.json", 50.0f, 75.0f, 1.0f);

    //SETUP: Construct Splash Screen + objects
    splashScreen = new Screen(p, "splash", blackjackBgFile);

    //SETUP: Construct grid1 Screen + objects
    grid1 = new Grid(p, "Blackjack Board", grid1BgFile, 6, 8);
    //chick = new AnimatedSprite(p, chickFile, chickJson, 0.0f, 0.0f, 0.5f);
    //b1 = new Button(p, "rect", 625, 525, 150, 50, "GoTo Level 2");
    // b1.setFontStyle("fonts/spidermanFont.ttf");
    // b1.setFontStyle("Calibri");
    // b1.setTextColor(PColor.WHITE);
    // b1.setButtonColor(PColor.BLACK);
    // b1.setHoverColor(PColor.get(100,50,200));
    // b1.setOutlineColor(PColor.WHITE);
    String[][] tileMarks = {
      {"R","N","B","Q","K","B","N","R"},
      {"P","P","P","P","P","P","P","P"},
      {"", "", "", "", "", "", "", ""},
      {"", "", "", "", "", "", "", ""},
      {"P","P","P","P","P","P","P","P"},
      {"R","N","B","Q","K","B","N","R"}
    };
    grid1.setAllMarks(tileMarks);
    grid1.startPrintingGridMarks();
    System.out.println("Finished setup for grid1...");
    
    //SETUP: Setup skyWorld constructor and objects
    /*skyWorld = new World(p, "sky", skyWorldBgFile, 4.0f, 0.0f, -600.0f); //moveable World constructor
    zapdos = new Sprite(p, zapdosFile, 0.25f, 300.0F);
    skyWorld.addSprite(zapdos);
    skyWorld.addSpriteCopyTo(runningHorse, 100, 200);  //example Sprite added to a World at a location, with a speed
    skyWorld.printWorldSprites();
    System.out.println("Finieshed setup for skyWorld...");

    // SETUP: Setup brickWorld constructor + objects
    brickWorld = new World(p,"platformer", brickWorldBgFile);
    plat = new Platform(p, PColor.MAGENTA, 500.0f, 100.0f, 200.0f, 20.0f);
    plat.setOutlineColor(PColor.BLACK);
    // plat.startGravity(5.0f); //sets gravity to a rate of 5.0
    brickWorld.addSprite(plat);    
    System.out.println("Finished setup for brickWorld...");

    //SETUP: Setup endScreen constructor + objects
    endScreen = new World(p, "end", endBgFile);
*/
    //SETUP: Set the starting screen for the game
    currentScreen = splashScreen;

    //SETUP: Sound
      // Load a soundfile from the sounds folder of the sketch and play it back
      // song = new SoundFile(p, "sounds/Lenny_Kravitz_Fly_Away.mp3");
      // song.play();

     // Trigger the new initialization phase
     System.out.println("Initial Rendering of Game...");
     initialRender(); 
    
    System.out.println("Game started...");

  } //end setup()


  // Method that starts drawing items on the screen
  // Safe place to load and resize images
  public void initialRender() {

    //RENDER: All Screen Objects
    runningHorse.initialRender();

    //RENDER: splash objects
    splashScreen.initialRender();
    
    //RENDER: grid1 objects
    grid1.initialRender();
    chick.initialRender();
    grid1.setTileSprite(new GridLocation (chickRow, chickCol), chick);
    // grid1.addSprite(b1);
    piece1 = Resource.loadImage(piece1File);
    piece1.resize(grid1.getTileWidth(),grid1.getTileHeight());
    System.out.println("Done intial render of grid1...");

    //RENDER: skyWorld objects
    skyWorld.initialRender();
    zapdos.initialRender();
    zapdos.moveTo(zapdosStartX, zapdosStartY);
    // skyWorld.addSprite(zapdos);
    // skyWorld.addSpriteCopyTo(runningHorse, 100, 200);  //example Sprite added to a World at a location, with a speed
    // skyWorld.printWorldSprites();
    System.out.println("Done intial render of skyWorld...");


    //RENDER: brickWorld objects
    brickWorld.initialRender();
    plat.setOutlineColor(PColor.BLACK);
    // plat.startGravity(5.0f); //sets gravity to a rate of 5.0
    brickWorld.addSprite(plat);    
    System.out.println("Done loading Level 3 (brickWorld)...");

    //RENDER: end objects
    endScreen.initialRender();

  }

  //Required Processing method that automatically loops
  //(Anything drawn on the screen should be called from here)
  public void draw() {

    // DRAW LOOP: Update Screen Visuals
    updateTitleBar();
    updateScreen();

    // DRAW LOOP: Set Timers
    int cycleTime = 1;  //milliseconds
    int slowCycleTime = 300;  //milliseconds
    if(slowCycleTimer == null){
      slowCycleTimer = new CycleTimer(p, slowCycleTime);
=======
    // 1. Render Table UI Information
    textSize(24);
    fill(255);
    text("Bankroll Balance: $" + blackjack.getBalance(), 50, 40);
    text("Status: " + blackjack.getGameMessage(), 50, 80);
    
    // 2. DRAW THE DECK IN THE MIDDLE
    int deckX = 600;
    int deckY = 100;
    if (backOfCardImage != null) {
        // Draw a stacked effect using 3 offset images
        image(backOfCardImage, deckX + 4, deckY + 4, 90, 130);
        image(backOfCardImage, deckX + 2, deckY + 2, 90, 130);
        image(backOfCardImage, deckX, deckY, 90, 130);
    } else {
        fill(40, 40, 40);
        rect(deckX, deckY, 90, 130, 5);
>>>>>>> 6ed1ac156c537b3780dc382dc439ae2b820b0344
    }
    fill(255);
    textSize(14);
    //text("DECK", deckX + 25, deckY + 70);

<<<<<<< HEAD
    // DRAW LOOP: Populate & Move Sprites
    if(slowCycleTimer.isDone()){
      populateSprites();
      moveSprites();
    }

    // DRAW LOOP: Pause Game Cycle
    currentScreen.pause(cycleTime);   // slows down the game cycles

    // DRAW LOOP: Check for end of game
    if(isGameOver()){
      endGame();
    }

  } //end draw()

  //------------------ USER INPUT METHODS --------------------//


  //Known Processing method that automatically will run whenever a key is pressed
  public void keyPressed(){

    //check what key was pressed
    System.out.println("\nKey pressed: " + this.keyCode); //key gives you a character for the key pressed

    //What to do when a key is pressed?
    
    //KEYS FOR LEVEL1
    if(currentScreen == grid1){

      //set [S] key to move the chick down & avoid Out-of-Bounds errors
      if(this.keyCode == 83){        

        //change the field for chickRow
        chickRow++;
      }

      // if the 'n' key is pressed, ask for their name
      if(this.key == 'n'){
        name = Input.getString("What is your name?");
      }

      // if the 't' key is pressed, then toggle the animation on/off
      if(this.key == 't'){
        //Toggle the animation on & off
        doAnimation = !doAnimation;
        System.out.println("doAnimation: " + doAnimation);
      }



    }

    if(currentScreen == brickWorld){
      if(this.key == 'w'){
        plat.jump();
      }
    }

    //CHANGING SCREENS BASED ON KEYS
    //change to level1 if 1 key pressed, level2 if 2 key is pressed
    if(this.key == '1'){
      currentScreen = grid1;
    } else if(this.key == '2'){
      currentScreen = skyWorld;
    } else if(this.key == '3'){
      currentScreen = brickWorld;

      //reset the moving Platform every time the Screen is re-displayed
      plat.moveTo(500.0f, 100.0f);
      plat.setSpeed(0,0);
    }

  }

  // Known Processing method that automatically will run when a mouse click triggers it
  public void mouseClicked(){
    
    // Print coordinates of mouse click
    System.out.println("\nMouse was clicked at (" + this.mouseX + "," + this.mouseY + ")");

    // Display color of pixel clicked
    int color = this.get(this.mouseX, this.mouseY);
    PColor.printPColor(p, color);

    // if the Screen is a Grid, print grid coordinate clicked
    if(currentScreen instanceof Grid){
      System.out.println("Grid location --> " + ((Grid) currentScreen).getGridLocation());
    }

    // if the Screen is a Grid, "mark" the grid coordinate to track the state of the Grid
    if(currentScreen instanceof Grid){
      ((Grid) currentScreen).setMark("X",((Grid) currentScreen).getGridLocation());
    }

    // what to do if clicked? (ex. assign a new location to piece1)
    if(currentScreen == grid1){
      piece1Row = grid1.getGridLocation().getRow();
      piece1Col = grid1.getGridLocation().getCol();
    }
    

  }



  //------------------ CUSTOM  GAME METHODS --------------------//

  // Updates the title bar of the Game
  public void updateTitleBar(){

    if(!isGameOver()) {

      extraText = currentScreen.getName();

      //set the title each loop
      if (surface != null) {
        surface.setTitle(titleText + "\t// CurrentScreen: " + extraText + " \t // Name: " + name + "\t // Health: " + health );
      }
      //adjust the extra text as desired
    
    }
  }

  // Syncs the Player/Dealer Hand ArrayLists with visual Sprites
  public void updateCardSprites() {
    
    // Clear out old sprites to prevent overlapping
    blackjackWorld.clearAllSprites(); 
    blackjackWorld.addSprite(deckSprite);

    // Render Dealer Cards (Top of screen)
    float dealerStartX = 250.0f;
    for (int i = 0; i < dealerHand.getCards().size(); i++) {
      Card c = dealerHand.getCards().get(i);
      
      // Assumes you have images named like "A_of_Spades.png"
      String imgName = "images/" + c.getRank() + "_of_" + c.getSuit() + ".png";
      
      // If it's the hidden second card, you might substitute the image for a card back here
      Sprite cardSprite = new Sprite(this, imgName, dealerStartX + (i * 80), 100.0f);
      blackjackWorld.addSprite(cardSprite);
    }

    // Render Player Cards (Bottom of screen)
    float playerStartX = 250.0f;
    for (int i = 0; i < player.getHand().getCards().size(); i++) {
      Card c = player.getHand().getCards().get(i);
      String imgName = "images/" + c.getRank() + "_of_" + c.getSuit() + ".png";
      Sprite cardSprite = new Sprite(this, imgName, playerStartX + (i * 80), 400.0f);
      blackjackWorld.addSprite(cardSprite);
    }
  }

  // Updates what is drawn on the screen each frame
  public void updateScreen(){

    // UPDATE: first lay down the Background
    currentScreen.showBg();

    // UPDATE: splashScreen
    if(currentScreen == splashScreen){
// UPDATE: Blackjack Screen
    if(currentScreen == blackjackWorld) {
=======
    // --- PHASE 1: Custom Bet Entry Mode ---
    if (enteringBet && blackjack.getBalance() > 0) {
        fill(255, 255, 150);
        textSize(24);
        text("Type your bet amount: $" + bettingInput + "_", 50, 140);
>>>>>>> 6ed1ac156c537b3780dc382dc439ae2b820b0344
        
        textSize(14);
        fill(200);
        text("Type digits [0-9], BACKSPACE to edit, ENTER to confirm deal.", 50, 180);
        text("Press 'R' at any time to request a complete engine profile reset.", 50, 200);
    }
    
    // --- PHASE 2: Bankrupt View ---
    else if (blackjack.getBalance() <= 0 && blackjack.isRoundOver()) {
        fill(255, 50, 50);
        textSize(32);
        text("BANKRUPT!", 50, 140);
        textSize(18);
        fill(255);
        text("Press 'R' to wipe profile and transfer a fresh $1000 balance.", 50, 180);
    }

<<<<<<< HEAD
      // Standard Processing text methods for HUD
      this.fill(PColor.WHITE); 
      this.textSize(24);
      this.textAlign(PConstants.CENTER);

      // Dealer Points (Assuming standard rules: only the first card's value is visible initially)
      if (dealerHand.getCards().size() > 0) {
          int dealerVisiblePts = dealerHand.getCards().get(0).getValue(); 
          this.text("Dealer Shows: " + dealerVisiblePts, APP_WIDTH / 2, 60);
      }

      // Middle Screen: Bet and Balance
      this.text("Current Bet: $" + currentBet, APP_WIDTH / 2, APP_HEIGHT / 2 - 20);
      this.text("Total Balance: $" + player.getBalance(), APP_WIDTH / 2, APP_HEIGHT / 2 + 20);

      // Player Total Points
      int playerTotalPts = player.getHand().getValue();
      this.text("Player Total: " + playerTotalPts, APP_WIDTH / 2, 550);
    }
    }
    
    // UPDATE: skyWorld Screen
    if(currentScreen == skyWorld){

      // Print a '2' in console when skyWorld
      System.out.print("2");

      // Set speed of moving skyWorld background
      skyWorld.moveBgXY(-0.3f, 0f);

    }

    // UPDATE: brickWorld Screen
    if(currentScreen == brickWorld){

      // Print a '3 in console when brickWorld
      System.out.print("3");


    }

    // UPDATE: End Screen
    // if(currentScreen == endScreen){

    // }

    // UPDATE: Any Screen
    if(doAnimation){
      runningHorse.animateHorizontal(0.5f, 1.0f, true);
    }

    // UPDATE: Other built-in to current World/Grid/HexGrid
    currentScreen.show();

=======
    // --- PHASE 3: Active Card Dealing Hands ---
    if (blackjack.getPlayer().getHand().getCards().size() > 0) {
        
        // --- DEALER HAND (Top Region of Table) ---
        fill(255);
        textSize(18);
        if (blackjack.isRoundOver()) {
            text("Dealer Hand (Total: " + blackjack.getDealerHand().getValue() + ")", 50, 250);
        } else {
            text("Dealer Hand (Showing Card value)", 50, 250);
        }
        // Call helper: hide first card if the game state round is actively running
        drawHandVisuals(blackjack.getDealerHand(), 50, 270, true);
        
        // --- PLAYER HAND (Bottom Region of Table) ---
        fill(255);
        textSize(18);
        text("Your Hand (Total: " + blackjack.getPlayer().getHand().getValue() + ")", 50, 420);
        // Call helper: player cards are always fully visible
        drawHandVisuals(blackjack.getPlayer().getHand(), 50, 430, false);
        
        // Display hotkeys prompt while hand is open
        if (!blackjack.isRoundOver()) {
            fill(255, 255, 150);
            textSize(16);
            text("Options: Press 'H' to Hit  |  Press 'S' to Stand", width - 350, height - 40);
        }
    }
}

  // 4. Trigger methods when GUI inputs happen (like keys or custom Button clicks)
  @Override
public void keyPressed() {
    
    // --- CASE A: Player is Bankrupt ---
    if (blackjack.getBalance() <= 0 && blackjack.isRoundOver()) {
        // Only allow resetting the full game
        if (key == 'r' || key == 'R') {
            blackjack = new Blackjack(1000); // Start totally fresh
            bettingInput = "";
            enteringBet = true;
        }
        return; // Block all other keys
    }

    // --- CASE B: Choosing/Typing a Bet ---
    if (enteringBet) {
        if (key >= '0' && key <= '9') {
            bettingInput += key; // Append typed digit to the string
        } 
        else if (key == BACKSPACE && bettingInput.length() > 0) {
            bettingInput = bettingInput.substring(0, bettingInput.length() - 1); // Delete last digit
        } 
        else if (key == ENTER || key == RETURN) {
            if (bettingInput.length() > 0) {
                int customBet = Integer.parseInt(bettingInput);
                
                // Attempt to start the round with this bet
                boolean success = blackjack.startRound(customBet);
                if (success) {
                    enteringBet = false; // Successfully moved to playing phase
                } else {
                    bettingInput = ""; // Reset incorrect bet input (e.g., betting more than owned)
                }
            }
        }
        // Allow restarting a completely fresh game even before bankrupt
        if (key == 'r' || key == 'R') {
            blackjack = new Blackjack(1000);
            bettingInput = "";
        }
    } 
    
    // --- CASE C: Round is Active (Hit or Stand) ---
    else {
        if (key == 'h' || key == 'H') {
            blackjack.hit();
            
            // If hitting caused a bust, round is instantly over
            if (blackjack.isRoundOver()) {
                handleRoundEnding();
            }
        } 
        else if (key == 's' || key == 'S') {
            blackjack.stand();
            handleRoundEnding();
        }
    }
}

/**
 * Helper method to determine where the player goes next after a hand finishes
 */
private void handleRoundEnding() {
    if (blackjack.getBalance() <= 0) {
        // Player is broke! Force them to restart
        enteringBet = false; 
    } else {
        // Player still has money! Transfer balance over and ask for a new custom bet
        bettingInput = "";
        enteringBet = true; 
    }
}

/**
 * Draws a hand of cards horizontally across the screen
 * @param hand The Hand object containing cards
 * @param startX The starting X position on screen
 * @param startY The starting Y position on screen
 * @param hideFirstCard True if this is the dealer's face-down card setup
 */
public void drawHandVisuals(Hand hand, float startX, float startY, boolean hideFirstCard) {
  float xOffset = 0;
  int cardWidth = 90;   // Adjust card scale size to fit your custom screen layout
  int cardHeight = 130;

  for (int i = 0; i < hand.getCards().size(); i++) {
      Card card = hand.getCards().get(i);
      String lookupKey = card.getRank() + card.getSuit();
      PImage cardImg = cardImages.get(lookupKey);

      // Position for this specific card
      float currentX = startX + xOffset;

      // If it's the dealer's first card and the round is still active, draw it face down
      if (i == 0 && hideFirstCard && !blackjack.isRoundOver()) {
          if (backOfCardImage != null) {
              image(backOfCardImage, currentX, startY, cardWidth, cardHeight);
          } else {
              // Fallback rectangle if card back image is missing
              fill(0, 0, 150);
              rect(currentX, startY, cardWidth, cardHeight, 5);
          }
      } else {
          // Draw the real face-up card image asset
          if (cardImg != null) {
              image(cardImg, currentX, startY, cardWidth, cardHeight);
          } else {
              // Fallback text card box if file loading had a typo path
              fill(255);
              stroke(0);
              rect(currentX, startY, cardWidth, cardHeight, 5);
              fill(0);
              textSize(16);
              text(card.getRank() + "\n" + card.getSuit().substring(0,3), currentX + 10, startY + 30);
          }
      }

      // Space out subsequent cards slightly overlapping to look natural
      xOffset += 110; 
>>>>>>> 6ed1ac156c537b3780dc382dc439ae2b820b0344
  }
}

}