package goGame.gui;

/**
 * GoView è la classe che rappresenta l'interfaccia utente del gioco del GO.
 * Gestisce la visualizzazione del tabellone, le interazioni dell'utente
 * e la comunicazione con il controller del gioco.
 */

// Importa le classi per la gestione dei layout e dei colori
// Importa il controller del gioco
import java.awt.*;
// Importa le classi per i componenti dell'interfaccia grafica Swing
import javax.swing.*;
import goGame.controller.GoController;
// Importa l'enumerazione dei colori delle pietre
import goGame.model.Colour;
// Importa il modello del gioco
import goGame.model.GoModel;

public class GoView extends JFrame {
  // Modello del gioco
  private final GoModel model;
  // Controller del gioco
  private final GoController controller;
  // Matrice di pulsanti che rappresentano le posizioni nel tabellone
  private final JButton[][] buttons;

  // Costruttore della classe GoView
  public GoView(GoModel model, GoController controller) {
    this.model = model;
    this.controller = controller;
    this.buttons =
        new JButton[model.getBoard().getSize()][model.getBoard().getSize()];
    // Inizializza l'interfaccia utente
    initializeUI();
  }

  // Inizializza l'interfaccia utente impostando il layout,
  // il titolo della finestra e creando i pulsanti per il tabellone
  private void initializeUI() {
    // Imposta il titolo
    setTitle("Gioco del GO");
    // Imposta la dimensione della finestra
    setSize(600, 600);
    // Chiude l'applicazione
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setLayout(
        // Imposta il layout a griglia
        new GridLayout(model.getBoard().getSize(), model.getBoard().getSize()));
    // Colore dello sfondo
    setBackground(new Color(205, 133, 36));

    // Crea i pulsanti per ogni posizione nel tabellone
    for (int x = 0; x < model.getBoard().getSize(); x++) {
      for (int y = 0; y < model.getBoard().getSize(); y++) {
        // Nuovo pulsante
        JButton button = new JButton();
        // Colore del pulsante
        button.setBackground(new Color(205, 133, 36));
        // Variabili per l'uso dell'espressione  lambda
        int finalX = x;
        int finalY = y;

        // Aggiunge un'azione al pulsante che gestisce il click
        button.addActionListener(e -> handleButtonClick(finalX, finalY));
        // Salva il pulsante nella matrice
        buttons[x][y] = button;
        // Aggiunge il pulsante alla finestra
        add(button);
      }
    }

    // Crea il menù
    setJMenuBar(createMenuBar());
  }

  // Crea e restituisce la barra dei menu per il gioco
  private JMenuBar createMenuBar() {
    // Crea barra del menù
    JMenuBar menuBar = new JMenuBar();
    // Crea il menù di gioco
    JMenu gameMenu = new JMenu("Opzioni");
    // Opzione per avviare una nuova partita
    JMenuItem newGameItem = new JMenuItem("Nuova Partita");
    newGameItem.addActionListener(e -> {
      if (model.isGameFinished()) {
        // Inizia una nuova partita
        controller.startNewGame();
      } else {
        JOptionPane.showMessageDialog(
            this, "Termina la partita corrente prima di iniziarne una nuova.");
      }
    });

    // Aggiunge l'opzione al menù
    gameMenu.add(newGameItem);

    // Opzione per terminare la partita
    JMenuItem finishGameItem = new JMenuItem("Termina Partita");
    finishGameItem.addActionListener(e -> controller.finishGame());
    gameMenu.add(finishGameItem);

    // Opzione per uscire dal gioco
    JMenuItem exitItem = new JMenuItem("Esci");
    exitItem.addActionListener(e -> System.exit(0));
    gameMenu.add(exitItem);

    menuBar.add(gameMenu);
    return menuBar;
  }

  // Gestisce il click del pulsante per effettuare una mossa nel gioco
  private void handleButtonClick(int x, int y) {
    if (!model.isGameFinished() &&
        controller.handleMove(x, y, model.getCurrentPlayer().getColour())) {
      refreshBoard();
    }
  }

  // Aggiorna la visualizzazione del tabellone in base allo stato attuale del
  // modello
  public void refreshBoard() {
    // Itera attraverso tutte le posizioni nel tabellone
    for (int x = 0; x < model.getBoard().getSize(); x++) {
      for (int y = 0; y < model.getBoard().getSize(); y++) {
        // Ottiene il colore della pietra nella posizione attuale
        Colour stone = model.getBoard().getGrid()[x][y];
        // Ottiene il pulsante corrispondente
        JButton button = buttons[x][y];

        // Imposta il colore del pulsante in base al colore della pietra
        if (stone == Colour.BLACK) {
          // Colore nero per pietra nera
          button.setBackground(Color.BLACK);
        } else if (stone == Colour.WHITE) {
          // Colore bianco per pietra bianca
          button.setBackground(Color.WHITE);
        } else {
          // Colore nocciola per cella  vuota
          button.setBackground(new Color(205, 133, 36));
        }
      }
    }
  }
}
