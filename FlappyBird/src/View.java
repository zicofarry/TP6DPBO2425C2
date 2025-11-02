import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class View extends JPanel {
    int width = 360;
    int height = 640;
    private Logic logic;
    private Image flappyTitleImage;
    private Image gameOverImage;

    public View(Logic logic) {
        this.logic = logic;
        setPreferredSize(new Dimension(width, height));
        setBackground(Color.cyan);
        setFocusable(true);
        addKeyListener(logic);
        flappyTitleImage = new ImageIcon(getClass().getResource("assets/title.png")).getImage();
        gameOverImage = new ImageIcon(getClass().getResource("assets/gameOver.png")).getImage();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    private void drawCenteredString(Graphics g, String text, Font font, Color color, int y, int panelWidth) {
        FontMetrics metrics = g.getFontMetrics(font);
        int x = (panelWidth - metrics.stringWidth(text)) / 2;
        g.setFont(font);
        g.setColor(color);
        g.drawString(text, x, y);
    }

    public void draw(Graphics g) {
        int w = getWidth(), h = getHeight();

        g.drawImage(logic.getBackgroundImage(), 0, 0, w, h, null);

        if (logic.isInMenu()) {
            int titleW = (int)(flappyTitleImage.getWidth(null) * 1.3);
            int titleH = (int)(flappyTitleImage.getHeight(null) * 1.3);
            g.drawImage(flappyTitleImage, (w - titleW)/2, 100, titleW, titleH, null);

            g.setColor(Color.GREEN);
            g.fillRect(logic.playButtonRect.x, logic.playButtonRect.y, logic.playButtonRect.width, logic.playButtonRect.height);
            g.setColor(Color.RED);
            g.fillRect(logic.exitButtonRect.x, logic.exitButtonRect.y, logic.exitButtonRect.width, logic.exitButtonRect.height);

            g.setFont(new Font("Arial", Font.BOLD, 24));
            g.setColor(Color.BLACK);
            FontMetrics fm = g.getFontMetrics();
            g.drawString("PLAY", logic.playButtonRect.x + (logic.playButtonRect.width - fm.stringWidth("PLAY"))/2, logic.playButtonRect.y + 28);
            g.drawString("EXIT", logic.exitButtonRect.x + (logic.exitButtonRect.width - fm.stringWidth("EXIT"))/2, logic.exitButtonRect.y + 28);
            return;
        }

        for (Pipe p : logic.getPipes())
            g.drawImage(p.getImage(), p.getPosX(), p.getPosY(), p.getWidth(), p.getHeight(), null);

        Player pl = logic.getPlayer();
        g.drawImage(pl.getImage(), pl.getPosX(), pl.getPosY(), pl.getWidth(), pl.getHeight(), null);

        drawCenteredString(g, String.valueOf(logic.getScore()), new Font("Arial Black", Font.BOLD, 26), Color.white, 40, w);

        // === PAUSED MENU ===
        if (logic.isPaused()) {
            g.setColor(new Color(0, 0, 0, 150));
            g.fillRect(0, 0, w, h);

            int btnW = 200, btnH = 50;
            int centerX = w/2 - btnW/2;
            int startY = h/2 - 100;

            logic.resumeButtonRect = new Rectangle(centerX, startY, btnW, btnH);
            logic.restartButtonRect = new Rectangle(centerX, startY + 75, btnW, btnH);
            logic.menuButtonRect = new Rectangle(centerX, startY + 150, btnW, btnH);

            g.setColor(new Color(86, 206, 102, 230));
            g.fillRoundRect(centerX, startY, btnW, btnH, 20, 20);
            g.fillRoundRect(centerX, startY + 75, btnW, btnH, 20, 20);
            g.fillRoundRect(centerX, startY + 150, btnW, btnH, 20, 20);

            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial Black", Font.BOLD, 36));
            drawCenteredString(g, "PAUSED", g.getFont(), Color.WHITE, h/2 - 150, w);

            g.setFont(new Font("Arial", Font.BOLD, 22));
            drawCenteredString(g, "Resume", g.getFont(), Color.WHITE, startY + 32, w);
            drawCenteredString(g, "Restart", g.getFont(), Color.WHITE, startY + 107, w);
            drawCenteredString(g, "Main Menu", g.getFont(), Color.WHITE, startY + 182, w);
            return;
        }

        // === GAME OVER ===
        if (logic.isGameOver()) {
            g.setColor(new Color(0, 0, 0, 150));
            g.fillRect(0, 0, w, h);
            int titleW = (int)(gameOverImage.getWidth(null) * 1.2);
            int titleH = (int)(gameOverImage.getHeight(null) * 1.2);
            g.drawImage(gameOverImage, (w - titleW)/2, h/2 - titleH - 10, titleW, titleH, null);

            drawCenteredString(g, "Score: " + logic.getScore(), new Font("Arial", Font.BOLD, 24), Color.WHITE, h/2 + 20, w);
            drawCenteredString(g, "Press R to Restart", new Font("Arial", Font.BOLD, 18), Color.white, h/2 + 50, w);
        }
    }
}
