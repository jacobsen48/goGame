package goGame.model;

/**
 * La classe Board rappresenta il tabellone di gioco per una partita di Go.
 * Gestisce la dimensione del tabellone e la posizione di ogni pedina
 * tramite una griglia bidimensionale, dove ogni cella può essere di colore
 * Nero, Bianco o vuota (null).
 */

public class Board {

  // Dimensione del tabellone (19x19)
  private final int size;
  // Griglia bidimensionale che rappresenta le posizioni delle pedine sul
  // tabellone
  private final Colour[][] grid;

  // Costruttore della classe Board
  public Board(int size) {
    this.size = size;
    this.grid = new Colour[size][size];
  }

  // Resetta il tabellone impostando tutte le celle a vuote (null)
  public void clearBoard() {
    for (int i = 0; i < size; i++) {
      for (int j = 0; j < size; j++) {
        // Imposta ogni cella come vuota
        grid[i][j] = null;
      }
    }
  }

  // Ritorna la griglia bidimensionale che rappresenta le posizioni delle pedine
  public Colour[][] getGrid() { return grid; }

  // Posiziona una pedina di un certo colore in una posizione specifica
  public void placeStone(int x, int y, Colour colour) { grid[x][y] = colour; }

  // Rimuove una pedina dalla posizione specificata, impostandola come vuota
  public void removeStone(int x, int y) { grid[x][y] = null; }

  // Ritorna la dimensione del tabellone
  public int getSize() { return size; }

  // Controlla se una determinata posizione sulla griglia è vuota
  public boolean isEmpty(int x, int y) { return grid[x][y] == null; }

  // Ritorna il colore della pietra inserita in quella posizione
  public Colour colorPosition(int x, int y) { return grid[x][y]; }
}
