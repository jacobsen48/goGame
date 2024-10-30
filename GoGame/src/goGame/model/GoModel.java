package goGame.model;

/**
 * La classe GoModel rappresenta il modello di gioco per una partita di Go.

 * Essa gestisce lo stato generale della partita, inclusi:
 *  - Il tabellone (Board) e le sue dimensioni.
 *  - Il giocatore corrente, alternando tra Nero e Bianco.
 *  - Lo stato di fine partita e la gestione del turno.
 *  - La regola del ko, utilizzata per evitare ripetizioni immediate.
 *  - Calcolo del punteggio basato sulle posizioni delle pedine.
 *
 * Fornisce metodi per avviare una nuova partita, cambiare turno,
 * controllare lo stato della partita e calcolare il punteggio
 * per ciascun colore (Nero e Bianco).
 */

// Importa la classe Point, usata per rappresentare coordinate 2D (x, y) sul
// tabellone di gioco
import java.awt.Point;

public class GoModel {
  // Rappresenta il tabellone di gioco
  private final Board board;
  // Giocatore corrente (colore Nero o Bianco)
  private Player currentPlayer;
  // Flag che indica se il gioco è finito
  private boolean gameFinished;
  // Posizione di "ko" per evitare situazioni di ripetizione immediata
  private Point koPosition;

  // Costruttore della classe GoModel
  public GoModel(int size) {
    // Inizializza il tabellone
    board = new Board(size);
    // Il primo giocatore è il Nero
    currentPlayer = new Player(Colour.BLACK);
    // Il gioco è attivo all'inizio
    gameFinished = false;
    // Non c'è posizione di ko all'inizio
    koPosition = null;
  }

  // Ritorna il tabellone di gioco
  public Board getBoard() { return board; }

  // Ritorna il giocatore corrente
  public Player getCurrentPlayer() { return currentPlayer; }

  // Passa il turno al prossimo giocatore e resetta la posizione di ko
  public void nextTurn() {
    // Cambia giocatore: se Nero, diventa Bianco; se Bianco, diventa Nero
    currentPlayer = (currentPlayer.getColour() == Colour.BLACK)
                        ? new Player(Colour.WHITE)
                        : new Player(Colour.BLACK);
    // Resetta la posizione di ko alla fine del turno
    resetKoPosition();
  }

  // Controlla se il gioco è finito
  public boolean isGameFinished() { return gameFinished; }

  // Imposta il gioco come terminato
  public void finishGame() { gameFinished = true; }

  // Avvia una nuova partita resettando il tabellone, il giocatore corrente e lo
  // stato del gioco
  public void startNewGame() {
    // Pulisce il tabellone
    board.clearBoard();
    // Inizia con il giocatore Nero
    currentPlayer = new Player(Colour.BLACK);
    // Imposta il gioco come attivo
    gameFinished = false;
  }

  // Ritorna la posizione di ko attuale, se esiste
  public Point getKoPosition() { return this.koPosition; }

  // Imposta una nuova posizione di ko
  public void setKoPosition(Point koPosition) { this.koPosition = koPosition; }

  // Resetta la posizione di ko (null)
  public void resetKoPosition() { this.koPosition = null; }

  // Calcola il punteggio per un colore specifico (Bianco o Nero) contando le
  // pedine sul tabellone
  public int calculateScore(Colour colour) {
    int score = 0;
    // Scorre ogni posizione della griglia
    for (int x = 0; x < board.getSize(); x++) {
      for (int y = 0; y < board.getSize(); y++) {
        // Incrementa il punteggio per ogni cella del colore specificato
        if (board.getGrid()[x][y] == colour) {
          score++;
        }
      }
    }
    // Ritorna il punteggio finale per il colore specificato
    return score;
  }
}
