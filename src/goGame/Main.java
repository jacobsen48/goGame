package goGame;

import javax.swing.*;
import goGame.controller.*;
import goGame.gui.*;
import goGame.model.*;

public class Main {

  public static void main(String[] args) {

    GoModel model = new GoModel(19);
    GoController controller = new GoController(model);
    GoView view = new GoView(model, controller);

    controller.setView(view);

    SwingUtilities.invokeLater(() -> { view.setVisible(true); });
  }
}
