package goGame.model;

/**
 * La classe Player rappresenta un giocatore nel gioco del Go,
 * specificando il colore delle sue pietre.
 */
public class Player {

  // Colore del giocatore (può essere Nero o Bianco)
  private final Colour colour;

  // Costruttore della classe Player
  public Player(Colour colour) {
    // Inizializza il colore del giocatore
    this.colour = colour;
  }

  // Restituisce il colore del giocatore
  public Colour getColour() { return colour; }
}
