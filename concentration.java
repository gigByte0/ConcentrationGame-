import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import tester.Tester;
import javalib.impworld.*;
import java.awt.Color;
import javalib.worldimages.*;

// represents the World in a game of Concentration
class ConcentrationGame extends World {
  ArrayList<Card> deck;
  ArrayList<ArrayList<Card>> grid;
  int points;
  ArrayList<Card> flipped;
  double time;

  ConcentrationGame() {
    this.deck = new ArrayList<Card>();
    this.grid = new ArrayList<ArrayList<Card>>();
    this.points = 26; // 26 pairs in total at start of game
    this.flipped = new ArrayList<Card>();
    this.time = 0;
    makeDeck(); // Creates deck of 52 cards
    shuffle(); // Shuffles deck
    makeDeckGrid(); // Converts deck into 2-d array
  }

  // EFFECT: if a card is clicked, card is flipped up and added to list of flipped
  // cards
  public void onMouseClicked(Posn pos) {
    for (Card c : this.deck) {
      if ((pos.x <= c.pos.x + 20) && (pos.x >= c.pos.x - 20) && (pos.y <= c.pos.y + 25)
          && (pos.y >= c.pos.y - 25) && (this.flipped.size() < 2) && (!(c.faceUp))) {
        c.flipCard();
        this.flipped.add(c);
      }
    }
  }

  // EFFECT : Checks in the event that two cards are flipped if they match
  // Also, increments the time in game
  public void onTick() {
    if (flipped.size() == 2) {
      // If matched
      if (flippedEqual()) {
        // obtain both card grid positions
        Posn card1Posn = this.flipped.get(0).gridPosn();
        Posn card2Posn = this.flipped.get(1).gridPosn();
        int card1X = card1Posn.x;
        int card1Y = card1Posn.y;
        int card2X = card2Posn.x;
        int card2Y = card2Posn.y;
        removeCard(this.grid.get(card1X).get(card1Y));
        removeCard(this.grid.get(card2X).get(card2Y));
        this.flipped.clear();
        this.points -= 1;
      }
      else if (!flippedEqual()) {
        this.flipped.get(0).flipCard();
        this.flipped.get(1).flipCard();
        this.flipped.clear();
      }
    }
    // ensure player cannot flip more than 2 cards at a time
    if (this.flipped.size() >= 3) {
      this.flipped.clear();
    }
    // increase time
    this.time += 1.5;
    drawTimer();
  }

  // EFFECT : Makes given card matched and unplayable
  public void removeCard(Card c) {
    c.rank = "Matched";
    c.suit = "";
  }

  // compares if both cards in flipped list are equal
  public boolean flippedEqual() {
    return (this.flipped.size() == 2) && (this.flipped.get(0).cardsMatch(flipped.get(1)));
  }

  // EFFECT: creates a list for deck of 52 cards
  void makeDeck() {
    ArrayList<String> vals = new ArrayList<String>(
        Arrays.asList("A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"));
    ArrayList<String> suits = new ArrayList<String>(Arrays.asList("♣", "♦", "♥", "♠"));

    for (int i = 0; i < vals.size(); i += 1) {
      for (int j = 0; j < suits.size(); j += 1) {
        this.deck.add(new Card(vals.get(i), suits.get(j)));
      }
    }
  }

  // EFFECT: shuffles a deck of cards randomly
  void shuffle() {
    Collections.shuffle(this.deck);
  }

  // draws cards onto scene and displays score and time
  public WorldScene makeScene() {
    WorldScene scene = new WorldScene(800, 800);
    for (int i = 0; i < 4; i++) {
      for (int j = 0; j < 13; j++) {
        scene.placeImageXY(this.grid.get(i).get(j).drawCard(), ((j * 60) + 40), ((i * 100) + 40));
      }
    }
    scene.placeImageXY(drawScore(), 400, 500);
    scene.placeImageXY(drawTimer(), 400, 600);
    scene.placeImageXY(drawInstructions(), 10, 550);
    scene.placeImageXY(drawCharacter(), 10, 700);

    return scene;
  }

  // EFFECT: turns shuffled deck into 2-d grid (4 x 13)
  void makeDeckGrid() {
    this.grid = new ArrayList<ArrayList<Card>>();
    for (int i = 0; i < 4; i++) {
      this.grid.add(new ArrayList<Card>());
      for (int j = 0; j < 13; j++) {
        this.grid.get(i).add(this.deck.get(13 * i + j));
        this.grid.get(i).get(j).setPosn(i, j);
      }
    }
  }

  // EFFECT: draw text instructions
  WorldImage drawCharacter() {
    WorldImage head = new CircleImage(25, OutlineMode.SOLID, Color.getHSBColor(40, 100, 94))
        .movePinhole(-70, -20);
    WorldImage eye1 = new CircleImage(3, OutlineMode.SOLID, Color.BLACK).movePinhole(-60, -20);
    WorldImage eye2 = new CircleImage(3, OutlineMode.SOLID, Color.BLACK).movePinhole(-80, -20);
    WorldImage brow1 = new RectangleImage(10, 5, OutlineMode.SOLID, Color.BLACK).movePinhole(-80,
        -10);
    WorldImage brow2 = new RectangleImage(10, 5, OutlineMode.SOLID, Color.BLACK).movePinhole(-60,
        -10);
    WorldImage mouth1 = new RectangleImage(10, 20, OutlineMode.SOLID, Color.RED).movePinhole(-70,
        -40);
    WorldImage mouth2 = new RectangleImage(10, 5, OutlineMode.SOLID, Color.WHITE).movePinhole(-70,
        -30);
    WorldImage body1 = new EllipseImage(30, 40, OutlineMode.SOLID, Color.BLUE).movePinhole(-70,
        -60);
    WorldImage arm1 = new EllipseImage(30, 10, OutlineMode.SOLID, Color.getHSBColor(40, 100, 94))
        .movePinhole(-50, -50);
    WorldImage arm2 = new EllipseImage(30, 10, OutlineMode.SOLID, Color.getHSBColor(40, 100, 94))
        .movePinhole(-90, -50);
    WorldImage leg1 = new EllipseImage(10, 30, OutlineMode.SOLID, Color.getHSBColor(40, 100, 94))
        .movePinhole(-75, -70);
    WorldImage leg2 = new EllipseImage(10, 30, OutlineMode.SOLID, Color.getHSBColor(40, 100, 94))
        .movePinhole(-65, -70);

    return new OverlayImage(eye1, new OverlayImage(eye2, new OverlayImage(brow1, new OverlayImage(
        brow2,
        new OverlayImage(mouth2, new OverlayImage(mouth1, new OverlayImage(head, new OverlayImage(
            body1,
            new OverlayImage(arm2, new OverlayImage(arm1, new OverlayImage(leg1, leg2)))))))))));
  }

  // EFFECT: draw text instructions
  WorldImage drawInstructions() {
    // if player wins
    if (this.points == 0) {
      return new OverlayImage(
          new TextImage("You win!!!", 12, Color.MAGENTA).movePinhole(-107, -100),
          new TextImage("Play Again? Press 'r'.", 12, Color.MAGENTA).movePinhole(-107, -110));
    }

    else {
      WorldImage instructions1 = new TextImage("To play, match pairs by their color and rank!", 9,
          Color.BLACK).movePinhole(-90, -100);
      WorldImage instructions2 = new TextImage(
          "You win when all cards are matched or there are 0 points.", 9, Color.BLACK)
              .movePinhole(-120, -110);
      WorldImage instructions3 = new TextImage("Click 'r' to reset the game.", 9, Color.BLACK)
          .movePinhole(-50, -120);
      return new OverlayImage(instructions1, new OverlayImage(instructions2, instructions3));
    }
  }

  // EFFECT: draw score at each tick
  WorldImage drawScore() {
    String score = "Score: ";
    return new OverlayImage(new TextImage(score.concat(Integer.toString(this.points)), Color.GREEN),
        new RectangleImage(250, 50, OutlineMode.SOLID, Color.darkGray));
  }

  // EFFECT: draw timer on screen
  WorldImage drawTimer() {
    String time = "Time Since Start (Seconds): ";
    return new OverlayImage(new TextImage(time.concat(Double.toString(this.time)), Color.MAGENTA),
        new RectangleImage(250, 50, OutlineMode.SOLID, Color.darkGray));
  }

  // EFFECT : resets the game by resetting each field of this game
  public void onKeyEvent(String key) {
    if (key.equals("r")) {
      ConcentrationGame newGame = new ConcentrationGame();
      this.deck = newGame.deck;
      this.grid = newGame.grid;
      this.points = newGame.points;
      this.flipped = newGame.flipped;
      this.time = newGame.time;
    }
  }
}

class Card {
  String rank;
  String suit;
  boolean faceUp;
  Posn pos;

  // constructor for deck (cards first drawn face down)
  Card(String r, String s) {
    this.rank = r;
    this.suit = s;
    this.faceUp = false;
    this.pos = new Posn(0, 0);
  }

  // EFFECT: flip card up
  void flipCard() {
    this.faceUp = (!(this.faceUp));
  }

  // Draws the card, either face up or face down
  WorldImage drawCard() {
    // Images per card
    // Black Card
    WorldImage faceUpBlack = new OverlayImage(
        new OverlayImage(new TextImage(this.rank.concat(this.suit), 12, Color.BLACK),
            new OverlayImage(new RectangleImage(40, 50, OutlineMode.OUTLINE, Color.BLACK),
                new RectangleImage(30, 40, OutlineMode.OUTLINE, Color.BLACK))),
        new OverlayImage(new TextImage(this.suit, 7, Color.BLACK).movePinhole(11, 14),
            new RotateImage(new TextImage(this.suit, 7, Color.BLACK), 180).movePinhole(-10, -14)));
    // Red Card
    WorldImage faceUpRed = new OverlayImage(
        new OverlayImage(new TextImage(this.rank.concat(this.suit), 12, Color.RED),
            new OverlayImage(new RectangleImage(40, 50, OutlineMode.OUTLINE, Color.BLACK),
                new RectangleImage(30, 40, OutlineMode.OUTLINE, Color.RED))),
        new OverlayImage(new TextImage(this.suit, 7, Color.RED).movePinhole(11, 14),
            new RotateImage(new TextImage(this.suit, 7, Color.RED), 180).movePinhole(-10, -14)));
    // Face Down card
    WorldImage faceDownCard = new OverlayImage(new StarImage(5, OutlineMode.SOLID, Color.WHITE),
        new OverlayImage(new RectangleImage(40, 50, OutlineMode.SOLID, Color.BLUE),
            new RectangleImage(40, 50, OutlineMode.OUTLINE, Color.BLACK)));
    // Matched Card
    WorldImage matchedCard = new RectangleImage(40, 50, OutlineMode.SOLID, Color.WHITE);

    // Return correct image
    if (this.faceUp && (this.suit.equals("♣") || this.suit.equals("♠"))) {
      return faceUpBlack;
    }

    else if (this.faceUp && (this.suit.equals("♥") || this.suit.equals("♦"))) {
      return faceUpRed;
    }

    else if (this.rank.equals("Matched") && this.suit.equals("")) {
      return matchedCard;
    }

    else {
      return faceDownCard;
    }
  }

  // EFFECT: set posn to position in grid, x and y posn's are reversed
  public void setPosn(int i, int j) {
    this.pos.x = (j * 60 + 40);
    this.pos.y = (i * 100 + 40);
  }

  // converts World position of a card to its grid indices and returns
  public Posn gridPosn() {
    int xPos = this.pos.x;
    int yPos = this.pos.y;

    return new Posn(((yPos - 40) / 100), ((xPos - 40) / 60));
  }

  // determines if cards are the same color
  public boolean sameColor(Card other) {
    return (this.suit.equals("♥") && other.suit.equals("♦"))
        || (this.suit.equals("♦") && other.suit.equals("♥"))
        || (this.suit.equals("♣") && other.suit.equals("♠"))
        || (this.suit.equals("♠") && other.suit.equals("♣"));
  }

  // determines if cards have same rank and same color
  public boolean cardsMatch(Card other) {
    return (this.sameColor(other)) && (this.rank.equals(other.rank));
  }
}

class ExamplesConcentration {
  // Example cards
  Card jackHeart;
  Card tenDiamond;
  Card threeSpade;
  Card aceClub;
  // Example world
  ConcentrationGame con0;

  // EFFECT : Initiates card variables for testing
  void inItData() {
    // Cards
    jackHeart = new Card("J", "♥");
    tenDiamond = new Card("10", "♦");
    threeSpade = new Card("3", "♠");
    aceClub = new Card("A", "♣");
    // World Game
    con0 = new ConcentrationGame();
  }

  // Testing to check if cards are faced-down and for flipCard() method
  void testFlipCard(Tester t) {
    // Initialize
    this.inItData();

    t.checkExpect(threeSpade.faceUp, false);
    t.checkExpect(aceClub.faceUp, false);

    // Use flipCard()
    threeSpade.flipCard();
    jackHeart.flipCard();
    t.checkExpect(threeSpade.faceUp, true);
    t.checkExpect(aceClub.faceUp, false);
    t.checkExpect(jackHeart.faceUp, true);
  }

  // Testing for the drawCard() method
  void testDrawCard(Tester t) {
    // Initiate data
    this.inItData();

    // Constant image for facedown card
    WorldImage faceDownCard = new OverlayImage(new StarImage(5, OutlineMode.SOLID, Color.WHITE),
        new OverlayImage(new RectangleImage(40, 50, OutlineMode.SOLID, Color.BLUE),
            new RectangleImage(40, 50, OutlineMode.OUTLINE, Color.BLACK)));

    // Cards drawn faced down
    t.checkExpect(jackHeart.faceUp, false);
    t.checkExpect(aceClub.faceUp, false);
    t.checkExpect(tenDiamond.faceUp, false);
    t.checkExpect(jackHeart.drawCard(), faceDownCard);
    t.checkExpect(aceClub.drawCard(), faceDownCard);
    t.checkExpect(tenDiamond.drawCard(), faceDownCard);

    // Flip cards up
    tenDiamond.flipCard();
    threeSpade.flipCard();
    t.checkExpect(tenDiamond.faceUp, true);
    t.checkExpect(threeSpade.faceUp, true);

    // Cards drawn faced up
    t.checkExpect(tenDiamond.drawCard(),
        new OverlayImage(
            new OverlayImage(new TextImage("10♦", 12, Color.RED),
                new OverlayImage(new RectangleImage(40, 50, OutlineMode.OUTLINE, Color.BLACK),
                    new RectangleImage(30, 40, OutlineMode.OUTLINE, Color.RED))),
            new OverlayImage(new TextImage("♦", 7, Color.RED).movePinhole(11, 14),
                new RotateImage(new TextImage("♦", 7, Color.RED), 180).movePinhole(-10, -14))));

    t.checkExpect(threeSpade.drawCard(),
        new OverlayImage(
            new OverlayImage(new TextImage("3♠", 12, Color.BLACK),
                new OverlayImage(new RectangleImage(40, 50, OutlineMode.OUTLINE, Color.BLACK),
                    new RectangleImage(30, 40, OutlineMode.OUTLINE, Color.BLACK))),
            new OverlayImage(new TextImage("♠", 7, Color.BLACK).movePinhole(11, 14),
                new RotateImage(new TextImage("♠", 7, Color.BLACK), 180).movePinhole(-10, -14))));

    // create a matched card
    Card matched = new Card("Matched", "");
    // draw matched card
    t.checkExpect(matched.drawCard(), new RectangleImage(40, 50, OutlineMode.SOLID, Color.WHITE));
  }

  // Testing for creating a deck of 52 cards
  void testMakeDeck(Tester t) {
    // Initiate data
    this.inItData();
    // Create empty deck
    ArrayList<Card> deck = new ArrayList<Card>();
    // Create suits and ranks
    ArrayList<String> vals = new ArrayList<String>(
        Arrays.asList("A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"));
    ArrayList<String> suits = new ArrayList<String>(Arrays.asList("♣", "♦", "♥", "♠"));

    // Create deck
    for (int i = 0; i < vals.size(); i += 1) {
      for (int j = 0; j < suits.size(); j += 1) {
        deck.add(new Card(vals.get(i), suits.get(j)));
      }
    }
    // Check if first four cards are made in order
    t.checkExpect(deck.get(0), new Card("A", "♣"));
    t.checkExpect(deck.get(1), new Card("A", "♦"));
    t.checkExpect(deck.get(2), new Card("A", "♥"));
    t.checkExpect(deck.get(3), new Card("A", "♠"));

    // Testing for creating the 2-d array grid
    // Create grid
    ArrayList<ArrayList<Card>> grid = new ArrayList<ArrayList<Card>>();
    for (int k = 0; k < 4; k++) {
      grid.add(new ArrayList<Card>());
      for (int h = 0; h < 13; h++) {
        grid.get(k).add(deck.get(13 * k + h));
      }
    }

    // Check if all cards are made
    // first row
    t.checkExpect(grid.get(0).get(0), new Card("A", "♣"));
    t.checkExpect(grid.get(0).get(1), new Card("A", "♦"));
    t.checkExpect(grid.get(0).get(2), new Card("A", "♥"));
    t.checkExpect(grid.get(0).get(3), new Card("A", "♠"));
    t.checkExpect(grid.get(0).get(4), new Card("2", "♣"));
    t.checkExpect(grid.get(0).get(5), new Card("2", "♦"));
    t.checkExpect(grid.get(0).get(6), new Card("2", "♥"));
    t.checkExpect(grid.get(0).get(7), new Card("2", "♠"));
    t.checkExpect(grid.get(0).get(8), new Card("3", "♣"));
    t.checkExpect(grid.get(0).get(9), new Card("3", "♦"));
    t.checkExpect(grid.get(0).get(10), new Card("3", "♥"));
    t.checkExpect(grid.get(0).get(11), new Card("3", "♠"));
    t.checkExpect(grid.get(0).get(12), new Card("4", "♣"));
    // second row
    t.checkExpect(grid.get(1).get(0), new Card("4", "♦"));
    t.checkExpect(grid.get(1).get(1), new Card("4", "♥"));
    t.checkExpect(grid.get(1).get(2), new Card("4", "♠"));
    t.checkExpect(grid.get(1).get(3), new Card("5", "♣"));
    t.checkExpect(grid.get(1).get(4), new Card("5", "♦"));
    t.checkExpect(grid.get(1).get(5), new Card("5", "♥"));
    t.checkExpect(grid.get(1).get(6), new Card("5", "♠"));
    t.checkExpect(grid.get(1).get(7), new Card("6", "♣"));
    t.checkExpect(grid.get(1).get(8), new Card("6", "♦"));
    t.checkExpect(grid.get(1).get(9), new Card("6", "♥"));
    t.checkExpect(grid.get(1).get(10), new Card("6", "♠"));
    t.checkExpect(grid.get(1).get(11), new Card("7", "♣"));
    t.checkExpect(grid.get(1).get(12), new Card("7", "♦"));
    // third row
    t.checkExpect(grid.get(2).get(0), new Card("7", "♥"));
    t.checkExpect(grid.get(2).get(1), new Card("7", "♠"));
    t.checkExpect(grid.get(2).get(2), new Card("8", "♣"));
    t.checkExpect(grid.get(2).get(3), new Card("8", "♦"));
    t.checkExpect(grid.get(2).get(4), new Card("8", "♥"));
    t.checkExpect(grid.get(2).get(5), new Card("8", "♠"));
    t.checkExpect(grid.get(2).get(6), new Card("9", "♣"));
    t.checkExpect(grid.get(2).get(7), new Card("9", "♦"));
    t.checkExpect(grid.get(2).get(8), new Card("9", "♥"));
    t.checkExpect(grid.get(2).get(9), new Card("9", "♠"));
    t.checkExpect(grid.get(2).get(10), new Card("10", "♣"));
    t.checkExpect(grid.get(2).get(11), new Card("10", "♦"));
    t.checkExpect(grid.get(2).get(12), new Card("10", "♥"));
    // fourth row
    t.checkExpect(grid.get(3).get(0), new Card("10", "♠"));
    t.checkExpect(grid.get(3).get(1), new Card("J", "♣"));
    t.checkExpect(grid.get(3).get(2), new Card("J", "♦"));
    t.checkExpect(grid.get(3).get(3), new Card("J", "♥"));
    t.checkExpect(grid.get(3).get(4), new Card("J", "♠"));
    t.checkExpect(grid.get(3).get(5), new Card("Q", "♣"));
    t.checkExpect(grid.get(3).get(6), new Card("Q", "♦"));
    t.checkExpect(grid.get(3).get(7), new Card("Q", "♥"));
    t.checkExpect(grid.get(3).get(8), new Card("Q", "♠"));
    t.checkExpect(grid.get(3).get(9), new Card("K", "♣"));
    t.checkExpect(grid.get(3).get(10), new Card("K", "♦"));
    t.checkExpect(grid.get(3).get(11), new Card("K", "♥"));
    t.checkExpect(grid.get(3).get(12), new Card("K", "♠"));
  }

  // Testing for Card Helpers
  // Testing for setPosn, setting a card's position on the WorldScene
  void testSetPosn(Tester t) {
    // Initiate data
    this.inItData();
    t.checkExpect(this.jackHeart.pos, new Posn(0, 0));
    t.checkExpect(this.tenDiamond.pos, new Posn(0, 0));
    this.jackHeart.setPosn(1, 1);
    this.tenDiamond.setPosn(2, 2);
    t.checkExpect(this.jackHeart.pos, new Posn(100, 140));
    t.checkExpect(this.tenDiamond.pos, new Posn(160, 240));
  }

  // Testing for cardsMatch, determining when two cards match in rank and color
  void testCardsMatch(Tester t) {
    // 1. Initiate data
    this.inItData();

    // 2. Testing helper method, sameColor()
    t.checkExpect(this.jackHeart.sameColor(this.tenDiamond), true); // heart & diamond
    t.checkExpect(this.tenDiamond.sameColor(this.jackHeart), true); // diamond & heart
    t.checkExpect(this.threeSpade.sameColor(this.aceClub), true); // spade & club
    t.checkExpect(this.aceClub.sameColor(this.threeSpade), true); // club & spade
    t.checkExpect(this.jackHeart.sameColor(this.threeSpade), false); // heart & spade
    t.checkExpect(this.jackHeart.sameColor(this.aceClub), false); // heart & club
    t.checkExpect(this.aceClub.sameColor(this.tenDiamond), false); // club & diamond

    // 3. Testing method cardsMatch()
    t.checkExpect(this.jackHeart.cardsMatch(this.tenDiamond), false);
    t.checkExpect(this.jackHeart.cardsMatch(new Card("J", "♠")), false);
    t.checkExpect(this.aceClub.cardsMatch(new Card("A", "♠")), true);
    t.checkExpect(this.tenDiamond.cardsMatch(new Card("10", "♥")), true);
    // impossible to have a duplicate card
    t.checkExpect(this.tenDiamond.cardsMatch(this.tenDiamond), false);
  }

  // Testing for gridPosn, converting a card's
  // Posn from WorldScene coordinates to grid indices
  void testGridPosn(Tester t) {
    // Initiate data
    this.inItData();
    t.checkExpect(this.jackHeart.pos, new Posn(0, 0));
    t.checkExpect(this.tenDiamond.pos, new Posn(0, 0));
    // Set tenDiamond to a new position
    this.tenDiamond.pos = new Posn(240, 160);
    t.checkExpect(this.tenDiamond.pos, new Posn(240, 160));
    // Check results
    t.checkExpect(this.jackHeart.gridPosn(), new Posn(0, 0));
    t.checkExpect(this.tenDiamond.gridPosn(), new Posn(1, 3));
  }

  // Testing for drawing score
  void testScore(Tester t) {
    // Initiate data
    this.inItData();
    t.checkExpect(this.con0.points, 26);
    t.checkExpect(con0.drawScore(), new OverlayImage(new TextImage("Score: 26", Color.GREEN),
        new RectangleImage(250, 50, OutlineMode.SOLID, Color.darkGray)));
    // draw score at 18
    con0.points = 18;
    t.checkExpect(con0.drawScore(), new OverlayImage(new TextImage("Score: 18", Color.GREEN),
        new RectangleImage(250, 50, OutlineMode.SOLID, Color.darkGray)));
  }

  // Testing for timer
  void testTimer(Tester t) {
    // Initiate data
    this.inItData();
    // initial time
    t.checkExpect(this.con0.time, 0.0);
    t.checkExpect(this.con0.drawTimer(),
        new OverlayImage(new TextImage("Time Since Start (Seconds): 0.0", Color.MAGENTA),
            new RectangleImage(250, 50, OutlineMode.SOLID, Color.darkGray)));
    // draw timer at 2 seconds
    con0.time = 2.0;
    t.checkExpect(this.con0.drawTimer(),
        new OverlayImage(new TextImage("Time Since Start (Seconds): 2.0", Color.MAGENTA),
            new RectangleImage(250, 50, OutlineMode.SOLID, Color.darkGray)));
  }

  // Testing for onMouse(), when a cursor clicks on a card
  void testOnClick(Tester t) {
    // Initiate data
    this.inItData();
    // World's first card in its initial state
    Card firstCardCon0 = con0.grid.get(0).get(0);
    // If cursor is not on a card, no card should be flipped or added to flipped
    // list
    con0.onMouseClicked(new Posn(800, 800));
    t.checkExpect(!firstCardCon0.faceUp, true);
    t.checkExpect(con0.flipped.size(), 0);
    // If cursor is on the first card, that card should be flipped
    con0.onMouseClicked(new Posn(20, 20));
    t.checkExpect(!firstCardCon0.faceUp, false); // flipped
    // Card should also be added to list of flipped cards
    t.checkExpect(con0.flipped.get(0), firstCardCon0);
  }

  // Testing for onTick(), events after every tick
  void testOnTick(Tester t) {
    // 1. Initiate data
    this.inItData();
    // 2. World should initially have a time of 0 and
    // an empty list for flipped cards and point total of 26
    t.checkExpect(con0.time, 0.0);
    t.checkExpect(con0.flipped, new ArrayList<Card>());
    t.checkExpect(con0.points, 26);
    // 3. After one tick
    con0.onTick();
    // 4. Time should now be incremented by 1.5 seconds
    t.checkExpect(con0.time, 1.5);
    t.checkExpect(con0.flipped, new ArrayList<Card>());
    t.checkExpect(con0.points, 26);
    // 5. Second tick
    con0.onTick();
    t.checkExpect(con0.time, 3.0);
    t.checkExpect(con0.flipped, new ArrayList<Card>());
    t.checkExpect(con0.points, 26);
    // 6. Add two matching cards to flipped list of World
    con0.flipped.add(new Card("J", "♠"));
    con0.flipped.add(new Card("J", "♣"));
    t.checkExpect(con0.flipped.size(), 2);
    // 7. Third tick, cards are matched, time increases by 1.5,
    // flipped is emptied, and point decreases by 1.
    con0.onTick();
    t.checkExpect(con0.time, 4.5);
    t.checkExpect(con0.flipped.size(), 0); // flipped is emptied
    t.checkExpect(con0.points, 25);
  }

  // Testing for drawing instructions
  void testDrawInstructions(Tester t) {
    // Initiate data
    this.inItData();
    // Test drawInstructions()
    t.checkExpect(con0.drawInstructions(), new OverlayImage(
        new TextImage("To play, match pairs by their color and rank!", 9, Color.BLACK)
            .movePinhole(-90, -100),
        new OverlayImage(
            new TextImage("You win when all cards are matched or there are 0 points.", 9,
                Color.BLACK).movePinhole(-120, -110),
            new TextImage("Click 'r' to reset the game.", 9, Color.BLACK).movePinhole(-50, -120))));
    // Set points to 0, or win game
    con0.points = 0;
    t.checkExpect(con0.drawInstructions(),
        new OverlayImage(new TextImage("You win!!!", 12, Color.MAGENTA).movePinhole(-107, -100),
            new TextImage("Play Again? Press 'r'.", 12, Color.MAGENTA).movePinhole(-107, -110)));
  }

  void testBigBang(Tester t) {
    ConcentrationGame world = new ConcentrationGame();
    int worldWidth = 800;
    int worldHeight = 800;
    double speed = 1.5;
    world.bigBang(worldWidth, worldHeight, speed);
  }
}