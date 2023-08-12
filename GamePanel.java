import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import javax.swing.JPanel;
public class GamePanel  extends JPanel implements ActionListener {
    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 20;
    static final int GAME_UNITS = (SCREEN_WIDTH* SCREEN_HEIGHT/UNIT_SIZE);
    static final int DELAY = 100;
    final int x[] = new int[GAME_UNITS];
    final int y[] = new int[GAME_UNITS];
    int bodyParts = 6;
    int appleEaten;
    int appleX;
    int appleY;
    char direction = 'R';
    boolean running = false;
    Timer timer;
    Random random;
    GamePanel() {
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.black);
//        this.setFocusable(true);
//        this.requestFocusInWindow(); // Request keyboard focus

        // Define key bindings
        InputMap inputMap = this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = this.getActionMap();

        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "left");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "right");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "up");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "down");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "restart");

        actionMap.put("left", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                if (direction != 'R') {
                    direction = 'L';
                }
            }
        });

        actionMap.put("right", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                if (direction != 'L') {
                    direction = 'R';
                }
            }
        });

        actionMap.put("up", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                if (direction != 'D') {
                    direction = 'U';
                }
            }
        });

        actionMap.put("down", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                if (direction != 'U') {
                    direction = 'D';
                }
            }
        });

        actionMap.put("restart", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                if (!running) {
                    restartGame();
                }
            }
        });

        startGame();
    }

    public void startGame(){
        newApple();
        running = true;
        timer = new Timer(DELAY,this);
        timer.start();
        this.setFocusable(true);
        this.requestFocusInWindow();
    }
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }
    public void draw(Graphics g){
        if(running) {
//            for (int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++) {
//                g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
//                g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);
//            }
            g.setColor(new Color(random.nextInt(255)));
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);
            for (int i = 0; i < bodyParts; i++) {
                if (i == 0) {
                    g.setColor(Color.RED);
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                } else {
                    g.setColor(new Color(199, 30, 30));
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }
        }else{
            gameOver(g);
        }
    }
    public void newApple(){
        appleX = random.nextInt((int)SCREEN_WIDTH/UNIT_SIZE)*UNIT_SIZE;
        appleY = random.nextInt((int)SCREEN_HEIGHT/UNIT_SIZE)*UNIT_SIZE;
    }
    public void move(){
        for(int i = bodyParts;i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }
        switch (direction) {
            case 'U' -> y[0] = y[0] - UNIT_SIZE;
            case 'D' -> y[0] = y[0] + UNIT_SIZE;
            case 'L' -> x[0] = x[0] - UNIT_SIZE;
            case 'R' -> x[0] = x[0] + UNIT_SIZE;
        }
    }
    public void checkApple(){
        if ((x[0] == appleX) && (y[0] == appleY)){
            bodyParts++;
            appleEaten++;
            newApple();
        }
    }
    public void checkCollision() {
        for (int i = bodyParts; i > 0; i--) {
            if ((x[0] == x[i]) && (y[0] == y[i])) {
                running = false;
                break;
            }
        }

        if (x[0] < 0 || x[0] >= SCREEN_WIDTH || y[0] < 0 || y[0] >= SCREEN_HEIGHT) {
            running = false;
        }

        if (!running) {
            timer.stop();
        }
    }

    public void gameOver(Graphics g){
        //Score
        g.setColor(Color.white);
        g.setFont(new Font("Ink Free",Font.BOLD,35));
        g.setColor(Color.white);
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("Score: "+ appleEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: "+ appleEaten))/2, g.getFont().getSize());
        g.setFont(new Font("Ink Free",Font.BOLD,70));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("Game Over", (SCREEN_WIDTH - metrics2.stringWidth("Game Over"))/2, SCREEN_HEIGHT/2); // TO GET GAME OVER IN CENTER
        //restart the game
        g.setFont(new Font("Ink Free", Font.BOLD, 30));
        g.setColor(Color.white);
        g.drawString("Press Enter to Restart", (SCREEN_WIDTH - metrics.stringWidth("Press Enter to Restart")) / 2, (SCREEN_HEIGHT / 2) + 50);
    }
    public void restartGame() {
        bodyParts = 6;
        appleEaten = 0;
        direction = 'R';
        running = false;
        timer.stop();

        // Reset snake position
        for (int i = 0; i < bodyParts; i++) {
            x[i] = 0;
            y[i] = 0;
        }

        startGame();
        repaint();
    }
    @Override
    public void actionPerformed (ActionEvent e){
        if (running){
            move();
            checkApple();
            checkCollision();
        }
        repaint();
    }
}

