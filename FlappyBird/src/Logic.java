import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class Logic implements ActionListener, KeyListener, MouseListener {
    int frameWidth = 360;
    int frameHeight = 640;

    int playerStartPosX = frameWidth / 2 - 30;
    int playerStartPosY = frameHeight / 2;
    int playerWidth = 34;
    int playerHeight = 24;

    int pipeStartPosX = frameWidth;
    int pipeStartPosY = 0;
    int pipeWidth = 64;
    int pipeHeight = 512;

    View view;
    Image birdImage;
    Image backgroundImage;
    Image lowerPipeImage;
    Image upperPipeImage;

    Player player;
    ArrayList<Pipe> pipes;

    Timer gameLoop;
    Timer pipesCooldown;

    int gravity = 1;
    int pipeVelocityX = -2;

    boolean running = false;
    boolean gameOver = false;
    boolean inMenu = true;
    boolean paused = false;
    int score = 0;

    public Rectangle playButtonRect = new Rectangle(80, 310, 200, 40);
    public Rectangle exitButtonRect = new Rectangle(80, 360, 200, 40);

    // tombol pause
    public Rectangle resumeButtonRect;
    public Rectangle restartButtonRect;
    public Rectangle menuButtonRect;

    public Logic() {
        backgroundImage = new ImageIcon(getClass().getResource("assets/background.png")).getImage();
        birdImage = new ImageIcon(getClass().getResource("assets/bird.png")).getImage();
        lowerPipeImage = new ImageIcon(getClass().getResource("assets/lowerPipe.png")).getImage();
        upperPipeImage = new ImageIcon(getClass().getResource("assets/upperPipe.png")).getImage();

        player = new Player(playerStartPosX, playerStartPosY, playerWidth, playerHeight, birdImage);
        pipes = new ArrayList<>();

        pipesCooldown = new Timer(2200, e -> placePipes());
        gameLoop = new Timer(1000 / 60, this);
        gameLoop.start();
    }

    public void setView(View view) {
        this.view = view;
        view.addKeyListener(this);
        view.setFocusable(true);
        view.requestFocusInWindow();
        view.addMouseListener(this);
    }

    public Player getPlayer() { return player; }
    public ArrayList<Pipe> getPipes() { return pipes; }
    public Image getBackgroundImage() { return backgroundImage; }
    public boolean isGameOver() { return gameOver; }
    public int getScore() { return score; }
    public boolean isInMenu() { return inMenu; }
    public boolean isPaused() { return paused; }

    public void startGame() {
        inMenu = false;
        resetGame();
        paused = false;
        running = true;
        gameOver = false;
        pipesCooldown.start();
    }

    public void resetGame() {
        pipes.clear();
        score = 0;
        player.setPosX(playerStartPosX);
        player.setPosY(playerStartPosY);
        player.setVelocityY(0);
    }

    public void placePipes() {
        int randomPosY = (int) (pipeStartPosY - pipeHeight / 4 - Math.random() * (pipeHeight / 2));
        int openingSpace = frameHeight / 4;

        Pipe upperPipe = new Pipe(pipeStartPosX, randomPosY, pipeWidth, pipeHeight, upperPipeImage);
        pipes.add(upperPipe);

        Pipe lowerPipe = new Pipe(pipeStartPosX, randomPosY + openingSpace + pipeHeight, pipeWidth, pipeHeight, lowerPipeImage);
        pipes.add(lowerPipe);
    }

    public void move() {
        if (inMenu || !running || paused) return;

        player.setVelocityY(player.getVelocityY() + gravity);
        player.setPosY(player.getPosY() + player.getVelocityY());
        player.setPosY(Math.max(player.getPosY(), 0));

        for (int i = 0; i < pipes.size(); i++) {
            Pipe pipe = pipes.get(i);
            pipe.setPosX(pipe.getPosX() + pipeVelocityX);
            if (pipe.getPosX() + pipe.getWidth() < 0) {
                pipes.remove(i);
                i--;
            }
        }

        checkCollision();
        checkScore();
    }

    public void checkCollision() {
        if (player.getPosY() + player.getHeight() >= frameHeight - 20) {
            triggerGameOver();
            return;
        }

        Rectangle playerRect = player.getBounds();
        for (Pipe pipe : pipes) {
            if (playerRect.intersects(pipe.getBounds())) {
                triggerGameOver();
                return;
            }
        }
    }

    public void checkScore() {
        for (Pipe pipe : pipes) {
            if (!pipe.isPassed() && !pipe.isTop()) {
                if (player.getPosX() > pipe.getPosX() + pipe.getWidth()) {
                    pipe.setPassed(true);
                    score++;
                }
            }
        }
    }

    public void triggerGameOver() {
        running = false;
        gameOver = true;
        paused = false;
        pipesCooldown.stop();
    }

    public void restart() {
        resetGame();
        startGame();
    }

    public void backToMenu() {
        inMenu = true;
        gameOver = false;
        running = false;
        paused = false;
        pipes.clear();
    }

    public void togglePause() {
        if (running && !gameOver) {
            paused = !paused;
            if (paused) {
                pipesCooldown.stop();
            } else {
                pipesCooldown.start();
            }
        }
    }

    public void actionPerformed(ActionEvent e) {
        move();
        if (view != null) view.repaint();
    }

    public void keyTyped(KeyEvent e) {}
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (running && !gameOver && !paused && key == KeyEvent.VK_SPACE) {
            player.setVelocityY(-10);
        }

        if (running && !gameOver && key == KeyEvent.VK_ESCAPE) {
            togglePause();
        }
    }

    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if (gameOver && key == KeyEvent.VK_R) restart();
        else if (gameOver && key == KeyEvent.VK_ESCAPE) backToMenu();
    }

    public void mouseClicked(MouseEvent e) {
        Point clickPoint = e.getPoint();

        if (inMenu) {
            if (playButtonRect.contains(clickPoint)) startGame();
            else if (exitButtonRect.contains(clickPoint)) System.exit(0);
        }

        if (paused) {
            if (resumeButtonRect != null && resumeButtonRect.contains(clickPoint)) togglePause();
            else if (restartButtonRect != null && restartButtonRect.contains(clickPoint)) restart();
            else if (menuButtonRect != null && menuButtonRect.contains(clickPoint)) backToMenu();
        }
    }

    

    public void mousePressed(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
}
