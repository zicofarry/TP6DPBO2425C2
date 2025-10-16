import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Logic implements ActionListener, KeyListener{
    int frameWidth = 360;
    int frameHeight = 640;

    int playerStartPosX = frameWidth / 2;
    int playerStartPosY = frameHeight / 2;
    int playerWidth = 34;
    int playerHeight = 24;

    View view;
    Image birdImage;
    Player player;
    Timer gameLoop;
    int gravity = 1;

    public Logic(){
        birdImage = new ImageIcon(getClass().getResource("assets/bird.png")).getImage();
        player = new Player(playerStartPosX, playerStartPosY, playerWidth, playerHeight, birdImage);

        gameLoop = new Timer(1000/60, this);
        gameLoop.start();
    }

    public void setView(View view){
        this.view = view;
    }

    public Player getPlayer(){
        return player;
    }

    public void move(){
        player.setVelocityY(player.getVelocityY() + gravity);
        player.setPosY(player.getPosY() + player.getVelocityY());
        // batas atas
        player.setPosY(Math.max(player.getPosY(), 0));

        // batas bawah
        int groundLevel = frameHeight - player.getHeight();
        if(player.getPosY() > groundLevel){
            player.setPosY(groundLevel);
            player.setVelocityY(0); // supaya gak nembus
        }
    }

    @Override
    public void actionPerformed(ActionEvent e){
        move();
        if(view != null){
            view.repaint();
        }
    }

    @Override
    public void keyTyped(KeyEvent e){}
    public void keyPressed(KeyEvent e){
        if(e.getKeyCode() == KeyEvent.VK_SPACE){
            player.setVelocityY(-10);
        }
    }
    public void keyReleased(KeyEvent e){}
}
