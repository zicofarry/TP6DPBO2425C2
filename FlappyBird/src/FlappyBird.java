import java.io.*;
import java.awt.*;
import java.util.*;
import javax.swing.*;
import javax.swing.Timer;
import java.awt.event.*;

public class FlappyBird extends JPanel implements ActionListener, KeyListener, MouseListener {
    int frameWidth = 360;
    int frameHeight = 640;

    Rectangle menuButtonBounds;
    Rectangle startButtonBounds;
    Rectangle resumeButtonBounds;
    Rectangle restartButtonBounds;

    Image background1Image, background2Image, groundImage;
    Image birdUpImage, birdMidImage, birdDownImage;
    Image lowerPipeImage, upperPipeImage;
    Image gameOverImage;
    Image titleImage;
    float alpha = 1.0f;

    int playerStartPosX = frameWidth/2 - 17;
    int playerStartPosY = frameHeight/2;
    int playerWidth = 34;
    int playerHeight = 24;
    Player player;

    int pipeStartPosX = frameWidth;
    int pipeStartPosY = 0;
    int pipeWidth = 64;
    int pipeHeight = 512;

    Score score = new Score();

    ArrayList<Pipe> pipes;

    Timer gameLoop, pipesCooldown;

    int gravity = 1;

    boolean gameStarted = false;
    boolean gamePaused = false;

    public FlappyBird() {
        setPreferredSize(new Dimension(frameWidth, frameHeight));
        setFocusable(true);
        addKeyListener(this);
        addMouseListener(this);

        background1Image = new ImageIcon(Objects.requireNonNull(getClass().getResource("assets/backgroundDay.png"))).getImage();
        background2Image = new ImageIcon(Objects.requireNonNull(getClass().getResource("assets/backgroundNight.png"))).getImage();
        titleImage = new ImageIcon(Objects.requireNonNull(getClass().getResource("assets/title.png"))).getImage();
        groundImage = new ImageIcon(Objects.requireNonNull(getClass().getResource("assets/ground.png"))).getImage();
        birdUpImage = new ImageIcon(Objects.requireNonNull(getClass().getResource("assets/birdUp.png"))).getImage();
        birdMidImage = new ImageIcon(Objects.requireNonNull(getClass().getResource("assets/birdMid.png"))).getImage();
        birdDownImage = new ImageIcon(Objects.requireNonNull(getClass().getResource("assets/birdDown.png"))).getImage();
        lowerPipeImage = new ImageIcon(Objects.requireNonNull(getClass().getResource("assets/lowerPipe.png"))).getImage();
        upperPipeImage = new ImageIcon(Objects.requireNonNull(getClass().getResource("assets/upperPipe.png"))).getImage();
        gameOverImage = new ImageIcon(Objects.requireNonNull(getClass().getResource("assets/gameOver.png"))).getImage();

        player = new Player(playerStartPosX, playerStartPosY, playerWidth, playerHeight, birdMidImage);
        pipes = new ArrayList<>();
        
        pipesCooldown = new Timer(1500, _ -> placePipes());
        
        gameLoop = new Timer(1000/60, _ -> {
            if (gameStarted && !isGameOver() && !gamePaused) {
                checkScore();
                move();
            } repaint();
        });
        gameLoop.start();
    }
    
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }
    
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        Composite originalComposite = g2d.getComposite();

        if (score.now() < 10) {
            g2d.drawImage(background1Image, 0, 0, frameWidth, frameHeight, null);
            alpha = 1.0f;
        } else if (score.now() % 100 / 10 % 2 == 1) {
            if (alpha > 0.0f) {
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
                g2d.drawImage(background1Image, 0, 0, frameWidth, frameHeight, null);
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1 - alpha));
                g2d.drawImage(background2Image, 0, 0, frameWidth, frameHeight, null);
                if (!gamePaused && !isGameOver()) alpha -= 0.01f;
                if (alpha <= 0.0f) alpha = 0.0f;
            } else g2d.drawImage(background2Image, 0, 0, frameWidth, frameHeight, null);
        } else if (score.now() % 100 / 10 % 2 == 0) {
            if (alpha < 1.0f) {
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
                g2d.drawImage(background1Image, 0, 0, frameWidth, frameHeight, null);
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1 - alpha));
                g2d.drawImage(background2Image, 0, 0, frameWidth, frameHeight, null);
                if (!gamePaused && !isGameOver()) alpha += 0.01f;
                if (alpha >= 1.0f) alpha = 1.0f;
            } else g2d.drawImage(background1Image, 0, 0, frameWidth, frameHeight, null);
        }

        g2d.setComposite(originalComposite);

        if (!gameStarted) {
            Font customFont;
            try {
                customFont = Font.createFont(Font.TRUETYPE_FONT, Objects.requireNonNull(getClass().getResourceAsStream("assets/_font.ttf")));
            } catch (FontFormatException | IOException e) {
                customFont = new Font("Arial", Font.PLAIN, 70); // Fallback font
            }
            g.setFont(customFont.deriveFont(30f));
            g.setColor(Color.DARK_GRAY);
            g.drawString("Click Button to Start!", (frameWidth - g.getFontMetrics().stringWidth("Click Button to Start!"))/2 - 1, frameHeight/2 + 75);
            g.setColor(Color.WHITE);
            g.drawString("Click Button to Start!", (frameWidth - g.getFontMetrics().stringWidth("Click Button to Start!"))/2 + 1, frameHeight/2 + 72);

            g.setColor(new Color(86, 206, 102));
            startButtonBounds = new Rectangle(player.getPosX() - 40, player.getPosY() - 15, player.getWidth() + 80, player.getHeight() + 30);
            g.fillRoundRect(startButtonBounds.x, startButtonBounds.y, startButtonBounds.width, startButtonBounds.height, 5, 5);
            g.setColor(Color.BLACK);
            g.drawRoundRect(startButtonBounds.x, startButtonBounds.y, startButtonBounds.width, startButtonBounds.height, 5, 5);

            g.drawImage(titleImage, (frameWidth - (int)(titleImage.getWidth(null) * 1.5)) / 2, (frameHeight - (int)(titleImage.getHeight(null) * 1.5)) / 8, (int)(titleImage.getWidth(null) * 1.5), (int)(titleImage.getHeight(null) * 1.5), null);
        }

        g.drawImage(player.getImage(), player.getPosX(), player.getPosY(), player.getWidth(), player.getHeight(), null);
        for (Pipe pipe : pipes) g.drawImage(pipe.getImage(), pipe.getPosX(), pipe.getPosY(), pipe.getWidth(), pipe.getHeight(), null);
        g.drawImage(groundImage, 0, frameHeight - groundImage.getHeight(null) + 50, frameWidth, groundImage.getHeight(null), null);

        if (gameStarted) {
            String scoreStr = String.valueOf(score.now());
            int digitWidth = 20, digitHeight = 30;
            int startX = (frameWidth - (scoreStr.length() * digitWidth)) / 2;

            for (int i = 0; i < scoreStr.length(); i++) {
                char digit = scoreStr.charAt(i);
                Image digitImage = new ImageIcon(Objects.requireNonNull(getClass().getResource("assets/score/" + digit + ".png"))).getImage();
                g.drawImage(digitImage, startX + (i * digitWidth), 50, digitWidth, digitHeight, null);
            }

            if (!gamePaused && !isGameOver() && player.getPosX() > frameWidth / 8) player.setPosX(player.getPosX() - 1);
        }

        if (gamePaused) {
            g.setColor(new Color(0, 0, 0, 50));
            g.fillRect(0, 0, frameWidth, frameHeight);
            
            g.setColor(new Color(86, 206, 102));
            resumeButtonBounds = new Rectangle(frameWidth / 2 - 100, frameHeight / 2 - 100, 200, 50);
            restartButtonBounds = new Rectangle(frameWidth / 2 - 100, frameHeight / 2 - 25, 200, 50);
            menuButtonBounds = new Rectangle(frameWidth / 2 - 100, frameHeight / 2 + 50, 200, 50);
            g.fillRoundRect(resumeButtonBounds.x, resumeButtonBounds.y, resumeButtonBounds.width, resumeButtonBounds.height, 20, 20);
            g.fillRoundRect(restartButtonBounds.x, restartButtonBounds.y, restartButtonBounds.width, restartButtonBounds.height, 20, 20);
            g.fillRoundRect(menuButtonBounds.x, menuButtonBounds.y, menuButtonBounds.width, menuButtonBounds.height, 20, 20);
            
            g.setColor(Color.BLACK);
            g.drawRoundRect(resumeButtonBounds.x, resumeButtonBounds.y, resumeButtonBounds.width, resumeButtonBounds.height, 20, 20);
            g.drawRoundRect(restartButtonBounds.x, restartButtonBounds.y, restartButtonBounds.width, restartButtonBounds.height, 20, 20);
            g.drawRoundRect(menuButtonBounds.x, menuButtonBounds.y, menuButtonBounds.width, menuButtonBounds.height, 20, 20);

            Font customFont;
            try {
                customFont = Font.createFont(Font.TRUETYPE_FONT, Objects.requireNonNull(getClass().getResourceAsStream("assets/_font.ttf")));
            } catch (FontFormatException | IOException e) {
                customFont = new Font("Arial", Font.PLAIN, 70); // Fallback font
            }
            g.setFont(customFont.deriveFont(70f));
            g.setColor(Color.DARK_GRAY);
            g.drawString("Game Paused", (frameWidth - g.getFontMetrics().stringWidth("Game Paused"))/2, frameHeight/2 - 150);
            g.setColor(Color.WHITE);
            g.drawString("Game Paused", (frameWidth - g.getFontMetrics().stringWidth("Game Paused"))/2 + 3, frameHeight/2 - 155);
            
            g.setFont(customFont.deriveFont(30f));
            g.setColor(Color.BLACK);
            g.drawString("Resume", resumeButtonBounds.x + (resumeButtonBounds.width - g.getFontMetrics().stringWidth("Resume"))/2, resumeButtonBounds.y + resumeButtonBounds.height/2 + 9);
            g.drawString("Restart", restartButtonBounds.x + (restartButtonBounds.width - g.getFontMetrics().stringWidth("Restart"))/2, restartButtonBounds.y + restartButtonBounds.height/2 + 9);
            g.drawString("Main Menu", menuButtonBounds.x + (menuButtonBounds.width - g.getFontMetrics().stringWidth("Main Menu"))/2, menuButtonBounds.y + menuButtonBounds.height / 2 + 9);
            g.setColor(Color.WHITE);
            g.drawString("Resume", resumeButtonBounds.x + (resumeButtonBounds.width - g.getFontMetrics().stringWidth("Resume"))/2 + 1, resumeButtonBounds.y + resumeButtonBounds.height/2 + 7);
            g.drawString("Restart", restartButtonBounds.x + (restartButtonBounds.width - g.getFontMetrics().stringWidth("Restart"))/2 + 1, restartButtonBounds.y + restartButtonBounds.height/2 + 7);
            g.drawString("Main Menu", menuButtonBounds.x + (menuButtonBounds.width - g.getFontMetrics().stringWidth("Main Menu"))/2 + 1, menuButtonBounds.y + menuButtonBounds.height / 2 + 7);
        }

        if (isGameOver()) {
            g.setColor(new Color(0, 0, 0, 50));
            g.fillRect(0, 0, frameWidth, frameHeight);
            g.drawImage(gameOverImage, (frameWidth - gameOverImage.getWidth(null)) / 2, frameHeight/ 2 - gameOverImage.getHeight(null), null);

            Font customFont;
            try {
                customFont = Font.createFont(Font.TRUETYPE_FONT, Objects.requireNonNull(getClass().getResourceAsStream("assets/_font.ttf")));
            } catch (FontFormatException | IOException e) {
                customFont = new Font("Arial", Font.PLAIN, 70); // Fallback font
            }
            g.setFont(customFont.deriveFont(30f));
            g.setColor(Color.WHITE);
            g.drawString("Score: " + score.now(), (frameWidth - g.getFontMetrics().stringWidth("Score: " + score.now()))/2, frameHeight/ 2 + 30);
            g.drawString("Press R to Restart!", (frameWidth - g.getFontMetrics().stringWidth("Press R to Restart!"))/2, 3 * frameHeight/4);
        }
    }
    
    public void move() {
        player.setVelocityY(player.getVelocityY() + gravity);
        player.setPosY(player.getPosY() + player.getVelocityY());
        player.setPosY(Math.max(player.getPosY(), 0));

        if (player.getVelocityY() < 0) {
            player.setImage(birdUpImage);
        } else if (player.getVelocityY() > 0 && player.getVelocityY() < 5) {
            player.setImage(birdMidImage);
        } else {
            player.setImage(birdDownImage);
        }

        for (Pipe pipe : pipes) {
            pipe.setPosX(pipe.getPosX() + pipe.getVelocityX());
        }
    }
    
    public void placePipes() {
        int randomPosY = (int) (pipeStartPosY - (double) pipeHeight /4 - Math.random() * ((double) pipeHeight /2));
        int openingSpace = frameHeight/4;

        Pipe upperPipe = new Pipe(pipeStartPosX, randomPosY, pipeWidth, pipeHeight, upperPipeImage);
        pipes.add(upperPipe);
        
        Pipe lowerPipe = new Pipe(pipeStartPosX, (randomPosY + openingSpace + pipeHeight), pipeWidth, pipeHeight, lowerPipeImage);
        pipes.add(lowerPipe);
    }

    public void checkScore() {
        for (Pipe pipe : pipes) {
            if (pipe.getPosX() + pipe.getWidth() < player.getPosX() && !pipe.passed && pipe.getImage() == upperPipeImage) {
                score.increment();
                pipe.passed = true;
            }
        }
    }
    
    public boolean isGameOver() {
        for (Pipe pipe : pipes) {
            if (player.getPosX() < pipe.getPosX() + pipe.getWidth() && player.getPosX() + player.getWidth() > pipe.getPosX() &&
                player.getPosY() < pipe.getPosY() + pipe.getHeight() && player.getPosY() + player.getHeight() > pipe.getPosY()) {
                return true;
            }
        }

        if (player.getPosY() + player.getHeight() * 1.5 >= frameHeight - groundImage.getHeight(null) + 50) return true;
        return player.getPosY() == 0;
    }

    public void pauseGame() {
        gamePaused = true;
        pipesCooldown.stop();
    }

    public void restartGame() {
        player.setPosX(gameStarted ? playerStartPosX/4 : playerStartPosX);
        player.setPosY(playerStartPosY);
        player.setVelocityY(0);
        pipes.clear();
        score.reset();
        if (gameStarted) pipesCooldown.restart();
        else pipesCooldown.stop();
        gameLoop.restart();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {
        if (isGameOver() && e.getKeyChar() == 'r') {
            player.setPosX(playerStartPosX/4);
            restartGame();
        }
    }
    
    @Override
    public void keyPressed(KeyEvent e) {
        if (gameStarted && !isGameOver() && !gamePaused) {
            if (e.getKeyCode() == KeyEvent.VK_SPACE) player.setVelocityY(-10);
            if (e.getKeyCode() == KeyEvent.VK_ESCAPE) pauseGame();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (!gameStarted) {
            Point clickedPoint = e.getPoint();
            if (startButtonBounds.contains(clickedPoint)) {
                gameStarted = true;
                pipesCooldown.start();
                player.setVelocityY(-10);
            }
        }

        if (gamePaused) {
            Point clickedPoint = e.getPoint();
            if (resumeButtonBounds.contains(clickedPoint)) {
                gamePaused = false;
                pipesCooldown.start();
            } else if (restartButtonBounds.contains(clickedPoint)) {
                restartGame();
                gamePaused = false;
            } else if (menuButtonBounds.contains(clickedPoint)) {
                gameStarted = false;
                restartGame();
                gamePaused = false;
            }
        }
    }

    @Override public void keyReleased(KeyEvent e) { }
    @Override public void mousePressed(MouseEvent e) { }
    @Override public void mouseReleased(MouseEvent e) { }
    @Override public void mouseEntered(MouseEvent e) { }
    @Override public void mouseExited(MouseEvent e) { }
}