package goGame.test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import goGame.controller.GoController;
import goGame.model.*;

class MainTest {

  // Test per l'inizializzazione del tabellone: Verifica che il tabellone venga
  // creato con la dimensione corretta e che tutte le caselle siano inzialmente
  // vuote

  @Test
  public void testBoardInizialization() {
    Board board = new Board(19);

    assertEquals(19, board.getSize());
    for (int x = 0; x < 19; x++) {
      for (int y = 0; y < 19; y++) {
        assertTrue(board.isEmpty(x, y));
      }
    }
  }

  // Test per il posizionamento delle pietre: Verifica che le pietre possano
  // essere posizionate correttamente e che la posizione sia aggiornata

  @Test
  public void testPlaceStone() {
    GoModel model = new GoModel(19);
    GoController controller = new GoController(model);
    controller.handleMove(3, 3, Colour.BLACK);
    assertEquals(Colour.BLACK, model.getBoard().colorPosition(3, 3));
  }

  // Test della cattura semplice: Posiziona un gruppo di pietre circondato dal
  // colore avversario, poi verifica che le pietre siano rimosse

  @Test
  public void testCaptureStone() {
    GoModel model = new GoModel(19);
    GoController controller = new GoController(model);
    controller.handleMove(1, 1, Colour.WHITE);
    controller.handleMove(0, 1, Colour.BLACK);
    controller.handleMove(2, 1, Colour.BLACK);
    controller.handleMove(1, 0, Colour.BLACK);
    controller.handleMove(1, 2, Colour.BLACK);

    assertTrue(model.getBoard().isEmpty(1, 1));
  }
  
  // Test della regola del Ko: Simula una situazione di Ko 
  // e verifica che lo stesso stato non possa ripetersi immediatamente.


  @Test
  public void testKoRule() {
    GoModel model = new GoModel(19);
    GoController controller = new GoController(model);
    controller.handleMove(4, 4, Colour.BLACK);
    controller.handleMove(3, 5, Colour.BLACK);
    controller.handleMove(5, 5, Colour.BLACK);
    controller.handleMove(4, 6, Colour.WHITE);

    controller.handleMove(4, 4, Colour.WHITE);

    boolean canPlace = controller.handleMove(4, 6, Colour.BLACK);
    assertFalse(canPlace);
  }
}
