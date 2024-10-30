package goGame.controller;

/**
 * La classe GoController funge da intermediario tra il modello (GoModel) e la
 * vista (GoView). Gestisce la logica del gioco, le mosse dei giocatori e le
 * catture delle pietre, e aggiorna l'interfaccia utente di conseguenza.
 */

// Importa la classe Point per rappresentare coordinate x e y sulla scacchiera
import java.awt.Point;
// Importa HashSet per gestire collezioni di punti catturati senza duplicati
import java.util.HashSet;
// Importa Set per definire un insieme di punti unici
import java.util.Set;
// Importa JOptionPane per mostrare finestre di dialogo per interazioni con
// l'utente
import javax.swing.JOptionPane;
// Importa GoView, la classe che gestisce l'interfaccia grafica del gioco
import goGame.gui.GoView;
// Importa Colour
import goGame.model.Colour;
// Importa GoModel
import goGame.model.GoModel;

public class GoController {
  // Modello del gioco che contiene la logica e lo stato del gioco
  private GoModel model;
  // Vista del gioco per l'interfaccia utente
  private GoView view;
  // Posizione dell'ultima pietra mossa per gestire la regola del ko
  public Point position;

  // Costruttore della classe GoController
  public GoController(GoModel model) { this.model = model; }

  // Imposta la vista del gioco
  public void setView(GoView view) { this.view = view; }

  // Gestisce una mossa del giocatore
  public boolean handleMove(int x, int y, Colour colour) {
    // Crea un oggetto Point con le coordinate della mossa
    Point np = new Point(x, y);

    // Controlla se la partita è già terminata
    if (model.isGameFinished())
      return false;
    // Ottiene il colore della pietra del giocatore corrente
    // Colour colour = model.getCurrentPlayer().getColour();
    // Controlla se la posizione è già stata utilizzata nella mossa precedente
    // (regola del ko)
    if (position != null && position.equals(np)) {
      JOptionPane.showMessageDialog(view, "Mossa non valida, regola del ko.");
      return false;
    }

    // Controlla se la casella selezionata è vuota
    if (model.getBoard().isEmpty(x, y)) {
      // Posiziona la pietra del giocatore nella casella selezionata
      model.getBoard().placeStone(x, y, colour);
      // Controlla se ci sono pietre avversarie da catturare
      captureStones(x, y, colour);
      // Passa al turno del prossimo giocatore
      model.nextTurn();
      return true;
    }
    JOptionPane.showMessageDialog(view, "Mossa non valida.");
    return false;
  }

  // Inizia una nuova partita, resettando il modello e aggiornando la vista
  public void startNewGame() {
    model.startNewGame();
    view.refreshBoard();
  }

  // Termina la partita e mostra i punteggi finali e il vincitore
  public void finishGame() {
    model.finishGame();
    int blackScore = model.calculateScore(Colour.BLACK);
    int whiteScore = model.calculateScore(Colour.WHITE);
    if (blackScore < whiteScore) {
      JOptionPane.showMessageDialog(view, "Bianco Vince: " + whiteScore);
    } else if (whiteScore < blackScore) {
      JOptionPane.showMessageDialog(view, "Nero Vince: " + blackScore);
    } else {
      JOptionPane.showMessageDialog(view, "Pareggio :\nNero: " + blackScore +
                                              "\nBianco: " + whiteScore);
    }
  }

  // Gestisce la cattura delle pietre avversarie attorno alla posizione della
  // mossa
  private void captureStones(int x, int y, Colour colour) {
    // Determina il colore dell'avversario
    Colour opponent = (colour == Colour.BLACK) ? Colour.WHITE : Colour.BLACK;

    // Insieme per memorizzare i punti delle pietre catturate
    Set<Point> capturedPoints = new HashSet<>();

    // Controlla le pietre adiacenti (a destra, sinistra, sopra, sotto) per
    // vedere se possono essere catturate Controlla a  destra
    capturedPoints.addAll(removeIfCaptured(x + 1, y, opponent));
    // Controlla a sinistra
    capturedPoints.addAll(removeIfCaptured(x - 1, y, opponent));
    // Controlla sopra
    capturedPoints.addAll(removeIfCaptured(x, y + 1, opponent));
    // Controlla sotto
    capturedPoints.addAll(removeIfCaptured(x, y - 1, opponent));

    // Se è stata catturata una pietra avversaria
    if (capturedPoints.size() == 1) {
      // Imposta la posizione della pietra catturata per gestire la regola del
      // ko
      model.setKoPosition(capturedPoints.iterator().next());
      // Aggiorna la posizione dell'ultima pietra mossa
      position = model.getKoPosition();
    } else {
      // Nessuna pietra da catturare o più di una pietra catturata, resetta la
      // posizione
      position = null;
    }
  }

  // Controlla se una pietra avversaria è circondata e la cattura se necessario
  private Set<Point> removeIfCaptured(int x, int y, Colour colour) {
    Set<Point> capturedPoints = new HashSet<>();

    if (x < 0 || y < 0 || x >= model.getBoard().getSize() ||
        y >= model.getBoard().getSize()) {
      // Esci se le coordinate sono fuori dal tabellone
      return capturedPoints;
    }

    if (model.getBoard().getGrid()[x][y] != colour) {
      // Esci se non è del colore avversario
      return capturedPoints;
    }
    if (isSurrounded(x, y, colour)) {
      // Raccoglie le pietre catturate
      collectGroup(x, y, colour, capturedPoints);
      for (Point p : capturedPoints) {
        // Rimuove le pietre catturate dalla scacchiera
        model.getBoard().removeStone(p.x, p.y);
      }
    }

    return capturedPoints;
  }

  // Controlla se una pietra è circondata dalle pietre avversarie
  private boolean isSurrounded(int x, int y, Colour colour) {
    Set<Point> visited = new HashSet<>();
    return isSurroundedHelper(x, y, colour, visited);
  }

  // Funzione ricorsiva per verificare se una pietra è circondata
  private boolean isSurroundedHelper(int x, int y, Colour colour,
                                     Set<Point> visited) {
    // Raggiunge un bordo, il gruppo non è circondato
    if (x < 0 || y < 0 || x >= model.getBoard().getSize() ||
        y >= model.getBoard().getSize()) {
      return false;
    }

    // Già visitato
    if (visited.contains(new Point(x, y))) {
      return true;
    }

    visited.add(new Point(x, y));

    // C'è uno spazio vuoto, quindi il gruppo non è circondato
    if (model.getBoard().getGrid()[x][y] == null) {
      return false;
    }

    // Non è lo stesso colore e non influisce sul controllo
    if (model.getBoard().getGrid()[x][y] != colour) {
      return true;
    }

    // Ricorsione per esplorare tutte le direzioni
    boolean surrounded = true;
    // Controlla la posizione a destra
    surrounded &= isSurroundedHelper(x + 1, y, colour, visited);
    // Controlla la posizione a sinistra
    surrounded &= isSurroundedHelper(x - 1, y, colour, visited);
    // Controlla la posizione a sopra
    surrounded &= isSurroundedHelper(x, y + 1, colour, visited);
    // Controlla la posizione a sotto
    surrounded &= isSurroundedHelper(x, y - 1, colour, visited);
    // Ritorna se è circondato
    return surrounded;
  }

  // Funzione per raccogliere tutte le pietre in un gruppo
  private void collectGroup(int x, int y, Colour colour, Set<Point> group) {
    // Esci se le coordinate sono fuori dal tabellone
    if (x < 0 || y < 0 || x >= model.getBoard().getSize() ||
        y >= model.getBoard().getSize()) {
      return;
    }

    Point point = new Point(x, y);
    // Esci se il punto è già stato raccolto o non è dello stesso colore
    if (group.contains(point) || model.getBoard().getGrid()[x][y] != colour) {
      return;
    }
    // Aggiungi il punto al gruppo
    group.add(point);

    // Ricorsione per esplorare tutte le direzioni e aggiungere pietre del
    // gruppo
    // Esplora a destra
    collectGroup(x + 1, y, colour, group);
    // Esplora a sinistra
    collectGroup(x - 1, y, colour, group);
    // Esplora a sopra
    collectGroup(x, y + 1, colour, group);
    // Esplora a sotto
    collectGroup(x, y - 1, colour, group);
  }
}
