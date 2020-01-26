import java.awt.Font;
import java.awt.Color;
import java.awt.image.ImageObserver;
import java.awt.Image;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.io.File;
import java.util.ArrayList;
import java.awt.image.BufferedImage;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.KeyListener;
import javax.swing.JPanel;

// 
// Decompiled by Procyon v0.5.36
// 

class GamePanel extends JPanel implements KeyListener, MouseMotionListener, MouseListener
{
    private int menuOptionSelect;
    private int gunTimeInterval;
    private int gunCountDown;
    private int barx;
    private int bary;
    private int mx;
    private int my;
    private int lives;
    private int gunX;
    private int gunY;
    private int speedCoolDown;
    private int score;
    public boolean inGame;
    private boolean[] keys;
    private boolean increasedSpeed;
    private boolean gunActive;
    private boolean start;
    private boolean win;
    private boolean lose;
    private boolean hasPow;
    private boolean shoot;
    String currentScreen;
    int wait;
    BufferedImage main;
    BufferedImage instructionPic;
    BufferedImage aboutPic;
    BufferedImage creditPic;
    BufferedImage storyPic;
    BufferedImage back;
    BufferedImage gameBallPic;
    BufferedImage gameBrickPic;
    BufferedImage winPic;
    BufferedImage gameOverPic;
    BufferedImage gameScreenSelect;
    BufferedImage instructionScreenSelect;
    BufferedImage lifePic;
    BufferedImage ultPic;
    BufferedImage speedPic;
    BufferedImage slowPic;
    BufferedImage gunPic;
    BufferedImage barBottomPic;
    BufferedImage gamePaddlePic;
    BufferedImage exitScreenSelect;
    BufferedImage bulletPic;
    Ball ball;
    Paddle paddle;
    Brick[] bricks;
    ArrayList<Gun> userGunList;
    
    public GamePanel() {
        this.inGame = true;
        this.currentScreen = "main";
        this.wait = 0;
        this.main = null;
        this.instructionPic = null;
        this.aboutPic = null;
        this.creditPic = null;
        this.storyPic = null;
        this.back = null;
        this.gameBallPic = null;
        this.gameBrickPic = null;
        this.winPic = null;
        this.gameOverPic = null;
        this.gameScreenSelect = null;
        this.instructionScreenSelect = null;
        this.lifePic = null;
        this.ultPic = null;
        this.speedPic = null;
        this.slowPic = null;
        this.gunPic = null;
        this.barBottomPic = null;
        this.gamePaddlePic = null;
        this.exitScreenSelect = null;
        this.bulletPic = null;
        this.ball = new Ball(392, 485);
        this.paddle = new Paddle(400);
        this.bricks = new Brick[56];
        this.userGunList = new ArrayList<Gun>();
        this.addMouseMotionListener(this);
        this.addMouseListener(this);
        this.keys = new boolean[403];
        this.addKeyListener(this);
        try {
            this.main = ImageIO.read(new File("main.png"));
            this.instructionPic = ImageIO.read(new File("instructions.png"));
            this.creditPic = ImageIO.read(new File("credits.png"));
            this.aboutPic = ImageIO.read(new File("story.png"));
            this.back = ImageIO.read(new File("back.png"));
            this.gameScreenSelect = ImageIO.read(new File("gameSelected.png"));
            this.instructionScreenSelect = ImageIO.read(new File("instructionSelected.png"));
            this.exitScreenSelect = ImageIO.read(new File("exitSelected.png"));
            this.winPic = ImageIO.read(new File("win.png"));
            this.gameOverPic = ImageIO.read(new File("gameover.png"));
            this.barBottomPic = ImageIO.read(new File("bar.png"));
            this.gamePaddlePic = ImageIO.read(new File("paddle.png"));
            this.gameBallPic = ImageIO.read(new File("ball.png"));
            this.gameBrickPic = ImageIO.read(new File("brick.png"));
            this.winPic = ImageIO.read(new File("win.png"));
            this.gameOverPic = ImageIO.read(new File("gameover.png"));
            this.bulletPic = ImageIO.read(new File("bullet.png"));
            this.lifePic = ImageIO.read(new File("life.png"));
            this.speedPic = ImageIO.read(new File("speed.png"));
            this.slowPic = ImageIO.read(new File("slow.png"));
            this.gunPic = ImageIO.read(new File("gun.png"));
            this.ultPic = ImageIO.read(new File("ultimate.png"));
        }
        catch (IOException ex) {}
        this.menuOptionSelect = 0;
        this.gunY = 500;
        this.mx = 400;
        this.my = 500;
        this.speedCoolDown = 0;
        this.gunCountDown = 0;
        this.gunTimeInterval = 0;
        this.barx = 375;
        this.bary = 500;
        this.score = 0;
        this.restart();
        this.setSize(800, 600);
    }
    
    public void theMenu() {
        if (this.wait > 0) {
            --this.wait;
        }
        if (this.wait == 0) {
            if (this.keys[10]) {
                if (this.menuOptionSelect == 0) {
                    this.currentScreen = "gameON";
                }
                else if (this.menuOptionSelect == 1) {
                    this.currentScreen = "how2play";
                }
                else if (this.menuOptionSelect == 2) {
                    System.exit(0);
                }
            }
            if (this.keys[40]) {
                ++this.menuOptionSelect;
                if (this.menuOptionSelect >= 2) {
                    this.menuOptionSelect = 2;
                }
                this.wait = 11;
            }
            if (this.keys[38]) {
                --this.menuOptionSelect;
                if (this.menuOptionSelect <= 0) {
                    this.menuOptionSelect = 0;
                }
                this.wait = 11;
            }
        }
    }
    
    @Override
    public void addNotify() {
        super.addNotify();
        this.requestFocus();
    }
    
    public void setGunXValue(final int gunX) {
        this.gunX = gunX;
    }
    
    public void move() {
        if (!this.start) {
            this.ball.bx = this.mx - 8;
            this.paddle.move(this.mx);
            final int bx = 795 - this.paddle.size / 2;
            if (this.paddle.px >= 795 - this.paddle.size) {
                this.ball.bx = bx;
                this.paddle.px = 795 - this.paddle.size;
            }
            else if (this.paddle.px <= 0) {
                this.ball.bx = this.paddle.size / 2 - 8;
                this.paddle.px = 0;
            }
        }
        else if (this.start) {
            this.paddle.move(this.mx);
            this.ball.move(this.paddle.px, this.paddle.size);
            if (this.paddle.px >= 795 - this.paddle.size) {
                this.paddle.px = 795 - this.paddle.size;
            }
            if (this.increasedSpeed) {
                this.speedCollision();
            }
            this.checkCollision();
            if (this.ball.by >= 560) {
                --this.lives;
                this.paddle.reset();
                this.ball.reset();
                this.mx = 375;
                this.start = false;
                this.gunTimeInterval = 0;
                this.gunCountDown = 0;
                this.increasedSpeed = false;
                this.speedCoolDown = 0;
                for (int i = 0; i < 56; ++i) {
                    if (this.bricks[i].eliminated) {
                        this.bricks[i].dropped = true;
                        this.bricks[i].powX = -50;
                        this.bricks[i].powY = -50;
                    }
                }
                if (this.lives == 0) {
                    this.lose = true;
                    this.completedGame();
                }
            }
            for (int j = 0; j < 56; ++j) {
                if (this.bricks[j].eliminated && this.bricks[j].hasUpgrade) {
                    this.bricks[j].dropPower();
                }
            }
        }
        if (this.speedCoolDown == 0) {
            this.increasedSpeed = false;
        }
        if (this.speedCoolDown > 0) {
            --this.speedCoolDown;
        }
        if (this.gunTimeInterval > 0) {
            --this.gunTimeInterval;
        }
        if (this.gunCountDown > 0) {
            --this.gunCountDown;
        }
        if (this.gunActive && this.shoot) {
            for (int k = 0; k < this.userGunList.size(); ++k) {
                this.userGunList.get(k).shoot();
                this.gunCollide(this.userGunList.get(k));
            }
        }
    }
    
    public void completedGame() {
        this.inGame = false;
    }
    
    public void checkCollision() {
        int n = 56;
        this.score = 0;
        for (int i = 0; i < 56; ++i) {
            if (this.ball.getRect().intersects(this.bricks[i].getRect()) && !this.bricks[i].isDestroyed()) {
                final int bx = this.ball.bx;
                final int by = this.ball.by;
                final int n2 = bx + 16;
                final int n3 = by + 16;
                if (this.bricks[i].getRect().contains(n2 + 1, by)) {
                    this.ball.setDX(-this.ball.speed);
                }
                else if (this.bricks[i].getRect().contains(bx - 1, by)) {
                    this.ball.setDX(this.ball.speed);
                }
                else if (this.bricks[i].getRect().contains(bx - 1, n3)) {
                    this.ball.setDX(this.ball.speed);
                }
                else if (this.bricks[i].getRect().contains(n2 + 1, n3)) {
                    this.ball.setDX(-this.ball.speed);
                }
                if (this.bricks[i].getRect().contains(bx, by - 1)) {
                    this.ball.setDY(this.ball.speed);
                }
                else if (this.bricks[i].getRect().contains(n2, by - 1)) {
                    this.ball.setDY(this.ball.speed);
                }
                else if (this.bricks[i].getRect().contains(bx, n3 + 1)) {
                    this.ball.setDY(-this.ball.speed);
                }
                else if (this.bricks[i].getRect().contains(n2, n3 + 1)) {
                    this.ball.setDY(-this.ball.speed);
                }
                this.bricks[i].setDestroyed(true);
            }
            if (this.bricks[i].isDestroyed()) {
                this.score += 10;
                --n;
            }
            if (this.upgradeCollide(this.bricks[i])) {
                if (this.bricks[i].upgrades == "life") {
                    ++this.lives;
                    if (this.lives >= 5) {
                        this.lives = 5;
                    }
                }
                else if (this.bricks[i].upgrades == "gun") {
                    this.gunActive = true;
                    this.gunCountDown = 550;
                }
                else if (this.bricks[i].upgrades == "special") {
                    ++this.lives;
                    this.score += 25;
                    if (this.lives >= 5) {
                        this.lives = 5;
                    }
                    this.ball.setSpeed(3);
                }
                else if (this.bricks[i].upgrades == "speed") {
                    this.ball.setSpeed(5);
                    this.increasedSpeed = true;
                    this.speedCoolDown = 210;
                }
                else if (this.bricks[i].upgrades == "slow") {
                    this.ball.setSpeed(2);
                }
                this.bricks[i].dropped = true;
                this.bricks[i].powY = -50;
                this.bricks[i].powX = -50;
            }
        }
        if (n == 0) {
            this.win = true;
            this.completedGame();
        }
    }
    
    public boolean upgradeCollide(final Brick brick) {
        boolean b = false;
        if (brick.getPowerRect().intersects(this.paddle.getRect())) {
            b = true;
        }
        return b;
    }
    
    public void speedCollision() {
        int n = 56;
        for (int i = 0; i < 56; ++i) {
            if (this.bricks[i].isDestroyed()) {
                --n;
            }
            if (this.ball.getRect().intersects(this.bricks[i].getRect()) && !this.bricks[i].isDestroyed()) {
                this.bricks[i].setDestroyed(true);
            }
        }
        if (n == 0) {
            this.win = true;
            this.completedGame();
        }
    }
    
    public void gunCollide(final Gun o) {
        for (int i = 0; i < 56; ++i) {
            if (!this.bricks[i].isDestroyed() && o.getRect().intersects(this.bricks[i].getRect())) {
                this.userGunList.remove(o);
                this.bricks[i].setDestroyed(true);
            }
        }
    }
    
    @Override
    public void mouseExited(final MouseEvent mouseEvent) {
    }
    
    @Override
    public void mouseReleased(final MouseEvent mouseEvent) {
    }
    
    @Override
    public void mouseEntered(final MouseEvent mouseEvent) {
    }
    
    @Override
    public void mouseClicked(final MouseEvent mouseEvent) {
        this.start = true;
        if (this.gunActive && this.gunCountDown > 0 && this.gunTimeInterval == 0) {
            this.shoot = true;
            this.setGunXValue(this.paddle.px);
            final Gun e = new Gun(this.gunX + this.paddle.size - 5);
            final Gun e2 = new Gun(this.gunX + 5);
            this.userGunList.add(e);
            this.userGunList.add(e2);
            this.gunTimeInterval = 55;
        }
    }
    
    @Override
    public void keyPressed(final KeyEvent keyEvent) {
        this.keys[keyEvent.getKeyCode()] = true;
    }
    
    @Override
    public void keyReleased(final KeyEvent keyEvent) {
        this.keys[keyEvent.getKeyCode()] = false;
    }
    
    @Override
    public void keyTyped(final KeyEvent keyEvent) {
    }
    
    public void paintComponent(final Graphics graphics) {
        if (this.currentScreen.equals("main")) {
            graphics.drawImage(this.main, 0, 0, this);
            if (this.menuOptionSelect == 0) {
                graphics.drawImage(this.gameScreenSelect, 0, 0, this);
            }
            else if (this.menuOptionSelect == 1) {
                graphics.drawImage(this.instructionScreenSelect, 0, 0, this);
            }
            else if (this.menuOptionSelect == 2) {
                graphics.drawImage(this.exitScreenSelect, 0, 0, this);
            }
        }
        if (this.currentScreen.equals("gameON")) {
            graphics.drawImage(this.back, 0, 0, this);
            for (int i = 0; i < 56; ++i) {
                if (!this.bricks[i].eliminated) {
                    graphics.drawImage(this.gameBrickPic, this.bricks[i].getX(), this.bricks[i].getY(), this);
                }
            }
            if (this.inGame) {
                graphics.drawImage(this.gameBallPic, this.ball.bx, this.ball.by, this);
                graphics.drawImage(this.gamePaddlePic, this.paddle.px, 500, this);
                for (int j = 0; j < 56; ++j) {
                    if (!this.bricks[j].dropped && this.bricks[j].hasUpgrade && this.bricks[j].eliminated) {
                        if (this.bricks[j].upgrades.equals("speed")) {
                            graphics.drawImage(this.speedPic, this.bricks[j].powX, this.bricks[j].powY, this);
                        }
                        else if (this.bricks[j].upgrades.equals("gun")) {
                            graphics.drawImage(this.gunPic, this.bricks[j].powX, this.bricks[j].powY, this);
                        }
                        else if (this.bricks[j].upgrades == "life") {
                            graphics.drawImage(this.lifePic, this.bricks[j].powX, this.bricks[j].powY, this);
                        }
                        else if (this.bricks[j].upgrades.equals("special")) {
                            graphics.drawImage(this.ultPic, this.bricks[j].powX, this.bricks[j].powY, this);
                        }
                        else if (this.bricks[j].upgrades.equals("slow")) {
                            graphics.drawImage(this.slowPic, this.bricks[j].powX, this.bricks[j].powY, this);
                        }
                    }
                }
                graphics.setColor(new Color(0, 255, 0));
                if (this.shoot) {
                    for (int k = 0; k < this.userGunList.size(); ++k) {
                        graphics.drawImage(this.bulletPic, this.userGunList.get(k).gunX, this.userGunList.get(k).gunY, this);
                    }
                }
                graphics.drawImage(this.barBottomPic, 0, 530, this);
                graphics.setFont(new Font("Modern", 1, 35));
                graphics.setColor(new Color(255, 0, 0));
                graphics.drawString(String.format("%d", this.score), 720, 568);
                graphics.drawString(String.format("%d", this.lives), 120, 568);
            }
            else if (this.win) {
                graphics.drawImage(this.winPic, 0, 0, this);
            }
            else {
                graphics.drawImage(this.gameOverPic, 0, 0, this);
            }
        }
        if (this.currentScreen.equals("about")) {
            graphics.drawImage(this.aboutPic, 0, 0, this);
        }
        if (this.currentScreen.equals("how2play")) {
            graphics.drawImage(this.instructionPic, 0, 0, this);
        }
        if (this.currentScreen.equals("creditpage")) {
            graphics.drawImage(this.creditPic, 0, 0, this);
        }
    }
    
    public void gameScreens() {
        if (this.currentScreen.equals("gameON")) {
            this.move();
        }
        else if (this.currentScreen.equals("how2play")) {
            this.instructions();
        }
        else if (this.currentScreen.equals("main")) {
            this.theMenu();
        }
        else if (this.currentScreen.equals("creditpage")) {
            this.credits();
        }
        else if (this.currentScreen.equals("about")) {
            this.credits();
        }
    }
    
    public void instructions() {
        if (this.keys[27]) {
            this.currentScreen = "main";
        }
    }
    
    public void credits() {
        if (this.keys[27]) {
            this.currentScreen = "main";
        }
    }
    
    @Override
    public void mousePressed(final MouseEvent mouseEvent) {
    }
    
    @Override
    public void mouseDragged(final MouseEvent mouseEvent) {
    }
    
    @Override
    public void mouseMoved(final MouseEvent mouseEvent) {
        this.my = mouseEvent.getY();
        this.mx = mouseEvent.getX();
    }
    
    public void restart() {
        this.start = false;
        int n = 0;
        for (int i = 0; i < 7; ++i) {
            for (int j = 0; j < 8; ++j) {
                this.bricks[n] = new Brick(j * 80 + 80, i * 50 + 30);
                ++n;
            }
        }
        this.speedCoolDown = 0;
        this.gunCountDown = 0;
        this.gunTimeInterval = 0;
        this.score = 0;
        this.lose = false;
        this.win = false;
        this.inGame = true;
        this.increasedSpeed = false;
        this.gunActive = false;
        this.lives = 3;
        this.paddle.reset();
        this.ball.reset();
    }
}