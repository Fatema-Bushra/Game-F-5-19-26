import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import processing.core.PApplet;


public class Main extends JPanel {

  private static Game gameApp;
  private Timer gameLoopTimer;

  public Main() {
    
    Dimension containerSize = new Dimension(800, 600);
    setPreferredSize(containerSize);
    setMinimumSize(containerSize);
    setMaximumSize(containerSize);

    // 1. Instantiate Game as an anonymous subclass to unlock 'protected' methods
    gameApp = new Game() {

      // Instance Initialization Block runs immediately after the constructor
      {
        this.width = Game.APP_WIDTH;
        this.height = Game.APP_HEIGHT;
        this.p = this;     
        this.g = this.makeGraphics(Game.APP_WIDTH, Game.APP_HEIGHT, PApplet.JAVA2D, null, true);
      }
    };

    // 2. Manually trigger custom setup parameters
    // (Skip settings() entirely here so Processing doesn't throw a lifecycle error)
    gameApp.setup();


    // 3. Setup JavaSwing loop timer running at 30 FPS
    gameLoopTimer = new Timer(33, new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (gameApp != null) {
          gameApp.frameCount++;
          gameApp.draw();
          Main.this.repaint();
        }
      }
    });
      
    gameLoopTimer.start();
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    if (gameApp != null && gameApp.g != null && gameApp.g.image != null) {
      Graphics2D g2d = (Graphics2D) g;
      g2d.drawImage((java.awt.Image) gameApp.g.image, 0, 0, null);
    }
  }

  public static void main(String[] args) {
    boolean isWebswing = System.getProperty("webswing.clientId") != null;

    if (isWebswing) {
      System.out.println("Webswing server verified. Launching standalone Swing graphics loop...");
      System.setProperty("sun.java2d.noddraw", "true");
      System.setProperty("sun.java2d.d3d", "false");

      SwingUtilities.invokeLater(() -> {
        JFrame webFrame = new JFrame("APCSA Java Processing Game WebApp");
        webFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        webFrame.add(new Main());
        webFrame.pack();
        webFrame.setLocationRelativeTo(null);
        webFrame.setVisible(true);
      });
    } else {
      // Local IDE Execution path
      PApplet.main("Game", args);
    }
  }
}