/**
 * @(#)BreakOut.java
 *
 * A simple game of 'Breakout'. To win, the user needs to destroy all the bricks on the screen which a ball and paddle.
 * Upgrades which fall help the user to have more fun and may or may not help them win.

 *@ Author: Ben Cheung
 */


import javax.swing.*;
import java.awt.image.*; 
import java.io.*; 
import javax.imageio.*;
import java.awt.*;
import java.util.*;
import java.awt.event.*;

/*
 * AudioClip only supports .wav, .au, .mid and .aiff
 *
 *
//   http://www.java2s.com/Code/Java/Development-Class/SettingtheVolumeofaSampledAudioPlayer.htm
// http://stackoverflow.com/questions/953598/audio-volume-control-increase-or-decrease-in-java
 */

import java.applet.*;
import javax.sound.sampled.AudioSystem;
import javax.swing.event.ChangeListener;

public class BreakOut extends JFrame implements ActionListener{
	//class for the top menu bar, the borderlayout, buttons, j-slider, music. defines them all and sets timer.
	JMenuBar menuTopBar;
	JMenuItem startGame,theMenu,instructions,completedGame, about, credits, off;
	JMenu menu,menu2,menu3;
	JMenuItem menuItem;
    JButton cButton = new JButton("Center");
    
    JSlider js;
     
    AudioClip music;
    
    javax.swing.Timer myTimer;
	GamePanel game = new GamePanel();
	
    public BreakOut() {
    	//Set the screen size, layout and text on the window
		super("<Break Out>   -By: Ben C");
		setSize(800,625);//screen size
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
		
		//-----This is the formatting for the nice menu bar on the very top-----
		
		//create the Menu; this stores two Menu items as Info and About
		menu = new JMenu("Info");
		menu2 = new JMenu("About");
		menu3 = new JMenu("Music");
		js = new JSlider();
		menu3.add(js);
		
		
		//Create Menu Items
		startGame = new JMenuItem("Start Game");
		theMenu = new JMenuItem("Menu");
		instructions = new JMenuItem("How to Play");
		completedGame = new JMenuItem("Quit");
		about = new JMenuItem("Story");
		credits = new JMenuItem("Credits");
		off = new JMenuItem("Disable");
		
		//Add the Addlistener to initalize the response on click
		instructions.addActionListener(this);
		completedGame.addActionListener(this);
		about.addActionListener(this);
		credits.addActionListener(this);
		startGame.addActionListener(this);
		theMenu.addActionListener(this);
		off.addActionListener(this);
		
		//define music file and loop
		music  = Applet.newAudioClip(getClass().getResource("You_Approaching_Nirvana_.wav"));
		music.loop();
		
		//Add the following items to the specified menu
		menu.add(startGame);
		menu.add(theMenu);
		menu.add(instructions);
		menu.add(completedGame);
		menu2.add(about);
		menu2.add(credits);
		menu3.add(off);
		
		//This adds the menus onto the top bar
		menuTopBar = new JMenuBar();
		menuTopBar.add(menu);
		menuTopBar.add(menu2);
		menuTopBar.add(menu3);
		
		//Places the Game onto the center and the menu bar on the top
		add(game, BorderLayout.CENTER);
		add(menuTopBar,BorderLayout.NORTH);
		
		//J Slider essentials and labels as well as sound control
	    js.setMinorTickSpacing(10);
	    js.setMajorTickSpacing(10);
	    js.setPaintTicks(true);
	    js.setPaintLabels(true);
	    
	
	    // We'll just use the standard numeric labels for now...
	    js.setLabelTable(js.createStandardLabels(10));
	    
		
		myTimer = new javax.swing.Timer(10,this); //triggers the event every 10 ms
		myTimer.start();
		setVisible(true);
		setResizable(false);
    }

	public void actionPerformed(ActionEvent evt){
		Object source = evt.getSource();
		int value = js.getValue();
		
		// This is to set the different screens and calls a specific function to help complete the task
		if(source == credits){
			game.currentScreen = "creditpage";  //credits page 
		}
		if(value == 0){
			music.stop();
		}
		else if(value == 100){
			music.play();
			value = 50;
		}
			
		else if(source == off){
			music.stop();
		}		
		if(source == about){
			game.currentScreen = "about";  //about page
		}
		if(source == instructions){
			game.currentScreen = "how2play";  //instructions page
		}
		if(source == completedGame){
			System.exit(0); //quit
		}
		if(source == startGame){
			game.currentScreen = "gameON"; //game screen
			game.restart();
		}
		if(source == myTimer){
			game.gameScreens(); //Controls which page or game the user is on 
		}
		if(source == theMenu){
			game.currentScreen = "main"; //main page with the 3 options [Start Game, How To play, Quit]
		}

		game.repaint(); 
	}

    public static void main(String[] arguments)throws IOException {
		BreakOut frame = new BreakOut();
    }
}

class GamePanel extends JPanel implements KeyListener, MouseMotionListener, MouseListener {
	/* This is the class which loads all the images, and contains the necessary methods for the screens, screen selections, 
	 *restarting the game, new game, checking collision, moving, upgrades and graphics
	 */
	private int menuOptionSelect, gunTimeInterval, gunCountDown, barx,bary,mx,my,lives, gunX, gunY,speedCoolDown, score; //gunTimeInterval is the time interval between projectiles; gunCountDown is the duration of the upgrade
	public boolean inGame = true;																							
	private boolean []keys;
	private boolean increasedSpeed, gunActive,start, win, lose, hasPow, shoot;//increasedSpeed is a flag for determining if the ball's speed increases due to the speed upgrade
	//gunActive is another flag which determines if the user has the gun upgrade or not
	
	String currentScreen = "main";//Want the default screen to be the main screen
	int wait  = 0;
	
	//images- The BufferedImage subclass describes an Image with an accessible buffer of image data.
	//@ http://docs.oracle.com/javase/7/docs/api/java/awt/image/BufferedImage.html
	BufferedImage main = null;
	BufferedImage instructionPic = null;
	BufferedImage aboutPic = null;
	BufferedImage creditPic = null;
	BufferedImage storyPic = null;
	BufferedImage back = null;
	BufferedImage gameBallPic = null;
	BufferedImage gameBrickPic = null;
	BufferedImage winPic = null;
	BufferedImage gameOverPic = null;
	BufferedImage gameScreenSelect = null;
	BufferedImage instructionScreenSelect = null;
	BufferedImage lifePic = null;
	BufferedImage ultPic = null;
	BufferedImage speedPic = null;
	BufferedImage slowPic = null;
	BufferedImage gunPic = null;
	BufferedImage barBottomPic = null;
	BufferedImage gamePaddlePic = null;
	BufferedImage exitScreenSelect = null;
	BufferedImage bulletPic = null;

		
	Ball ball = new Ball(392,485); //new ball
	Paddle paddle = new Paddle(400); //new paddle
	Brick[] bricks = new Brick[56]; //set bricks
	ArrayList<Gun> userGunList = new ArrayList <Gun>();
	
	public GamePanel(){
		addMouseMotionListener(this);
		addMouseListener(this);
		keys = new boolean[KeyEvent.KEY_LAST+1];
		addKeyListener(this);
		//Load all the images
		try{ 
			// Load Selection Screens, Background, and main pic
			main = ImageIO.read(new File ("main.png"));
			instructionPic = ImageIO.read(new File ("instructions.png"));
			creditPic = ImageIO.read(new File ("credits.png"));  
			aboutPic = ImageIO.read(new File ("story.png"));  
			back = ImageIO.read(new File ("back.png"));
			gameScreenSelect = ImageIO.read(new File ("gameSelected.png"));
			instructionScreenSelect = ImageIO.read(new File ("instructionSelected.png"));
			exitScreenSelect = ImageIO.read(new File ("exitSelected.png"));
			
			
			//Load In Game Pictures
			winPic = ImageIO.read(new File ("win.png"));
			gameOverPic = ImageIO.read(new File ("gameover.png"));		
			barBottomPic = ImageIO.read(new File ("bar.png"));
			gamePaddlePic = ImageIO.read(new File ("paddle.png"));
			gameBallPic = ImageIO.read(new File ("ball.png"));
			gameBrickPic = ImageIO.read(new File ("brick.png"));
			winPic = ImageIO.read(new File ("win.png"));
			gameOverPic = ImageIO.read(new File ("gameover.png"));
			bulletPic = ImageIO.read(new File ("bullet.png"));
			
			//Load all upgrade pics
			lifePic = ImageIO.read(new File ("life.png"));
			speedPic = ImageIO.read(new File ("speed.png"));
			slowPic = ImageIO.read(new File ("slow.png"));
			gunPic = ImageIO.read(new File ("gun.png"));
			ultPic = ImageIO.read(new File ("ultimate.png"));
		}
		catch (IOException e){
		}

		/* This is the menu selection which depends on user input to confirm and switch to which screen
		 * 0 is a new game, 1 would be how to play, and 2 is exit
		 */
		menuOptionSelect = 0;
        gunY = 500;
        mx = 400;
        my = 500;
        speedCoolDown = 0;
        gunCountDown = 0;
        gunTimeInterval = 0;	
	    barx = 375;
        bary = 500;
        score = 0;	
        restart();
        setSize(800,600);
	}

	public void theMenu(){
		//control selections in the main menu
		if(wait > 0){//A time between key pressed events to prevent key input spam
			wait --;
		}
		if(wait == 0){
			//users use up down arrow keys to choose options
			if(keys[KeyEvent.VK_ENTER]){ //enter to select
				if(menuOptionSelect == 0){
					currentScreen = "gameON";
				}
				else if(menuOptionSelect == 1){
					currentScreen = "how2play";
				}
				else if(menuOptionSelect == 2 ){
					System.exit(0);
				}
			}
			if(keys[KeyEvent.VK_DOWN]){
				menuOptionSelect ++;
				if(menuOptionSelect >= 2){
					menuOptionSelect = 2;
				}
				wait = 11;
			}
			if (keys[KeyEvent.VK_UP]){
				menuOptionSelect --;
				if(menuOptionSelect <= 0){
					menuOptionSelect = 0;
				}
				wait = 11;
			}
		}
	}

	public void addNotify(){
		super.addNotify();
		requestFocus();
	}
	public void setGunXValue(int x){
		gunX = x;
	}
	public void move(){
		//The method for the paddle's movement and it's restrictions
		if(start == false){//ball is linked with paddle until click to start
			ball.bx = mx-8;//places ball on middle
			paddle.move(mx); 
			int rBorder = 795-paddle.size/2; //this is the location for where the ball is placed if the paddle is on the very right side of the screen
			if(paddle.px >= 795-paddle.size){ //this is if the paddle's position is on the right side touching the edge of the screen
	        	ball.bx = rBorder; //set the ball coordinates to the location	        	
	        	paddle.px = 795-paddle.size; //restricts the paddle to stay on screen
	        }
			else if(paddle.px<=0){ //prevents the paddle from going offscreen
	        	ball.bx = paddle.size/2 - 8;			
	        	paddle.px = 0;
	        }
	        
		}
		else if(start == true){ //this is for the case for when the game has started and the user has left clicked the mouse
			paddle.move(mx);
			ball.move(paddle.px,paddle.size);
			if(paddle.px >= 795-paddle.size){
				//prevents the paddle from going off the screen
	        	paddle.px = 795-paddle.size;
	        }
			if(increasedSpeed == true){
				speedCollision();//speedCollision used for speed increase and it can bypass bricks for a short period of time and destroy them
						
			}
			checkCollision();//normal ball-brick collision, determine which direction the ball bounces back at the specified brick
			

			if(ball.by >= 560){
				//if ball goes to the bottom of the screen
				lives -= 1; //lose a life
				paddle.reset(); //reset paddle and ball
				ball.reset();
				mx = 375;
				start = false;
				gunTimeInterval = 0;
				gunCountDown = 0;
				increasedSpeed = false; //get rid of all status changes to the ball from previous game
				speedCoolDown = 0;
				
				for(int i = 0; i < 56; i ++){ //there are 56 blocks , this checks every block
					if(bricks[i].eliminated){
						bricks[i].dropped = true; 
						//remove all upgrades that are dropped when the player loses
						bricks[i].powX =- 50;   
						bricks[i].powY =- 50;
					}
				}
				if(lives == 0){
					lose = true;
					completedGame();
				}
			}
			for(int i = 0;i < 56; i++){
				if(bricks[i].eliminated && bricks[i].hasUpgrade){
					bricks[i].dropPower(); 
						//checks for upgrades upon destroying a brick and drops an upgrade
				}			
			}	
		}
	
    	if(speedCoolDown == 0){
    		increasedSpeed = false;
    	}
		if(speedCoolDown > 0){//cooldown for speed - this is the timelimit that the upgrade takes effect
    		speedCoolDown --;
    	}

    	if(gunTimeInterval  >0){//cooldown for bullet shot- this is the timelimit that the upgrade takes effect
    		gunTimeInterval --;
    	}
    	if(gunCountDown > 0){//cooldown for bullet upgrades
    		gunCountDown --;
    	}
		if(gunActive){
			if(shoot){
				for(int i = 0; i < userGunList.size(); i++){
					userGunList.get(i).shoot();
					gunCollide(userGunList.get(i));
					//checks for gun shot collision with bricks
				}
			}				
		}
		
	}
	public void completedGame(){
		//method for the completed game condition
		inGame = false;
	}

	public void checkCollision(){
		//method which checks if the ball collides with the brick
		int count = 56; //total number of bricks to be able to be broken
		score = 0;//keeps count in the score
		for(int i = 0; i < 56; i++){
            if ((ball.getRect()).intersects(bricks[i].getRect())) {
                if (!bricks[i].isDestroyed()) {
    				// This is the position of the brick. It dictates which direction the ball will bounce back
                	int ballLeft = ball.bx; //left
			        int ballTop = ball.by; //top
			        int ballRight = ballLeft+16; //right
			        int ballBottom = ballTop+16; //bottom
	
                    if (bricks[i].getRect().contains(ballRight + 1, ballTop)) {//If the rectangle of the brick contains that specific point:
                        ball.setDX(-ball.speed);					  //means it hits top right portion of the ball -> left of brick
                    }
                    else if (bricks[i].getRect().contains(ballLeft - 1, ballTop)) {//It hits top left portion of the ball -> right of brick
                        ball.setDX(ball.speed);
                    }
					else if(bricks[i].getRect().contains(ballLeft - 1,ballBottom)){//It hits bottom left of ball -> top right of brick
						ball.setDX(ball.speed);
					}

                    else if(bricks[i].getRect().contains(ballRight + 1,ballBottom)){//hits bottom right of ball
                    	ball.setDX(-ball.speed);
                    }

                    if (bricks[i].getRect().contains(ballLeft, ballTop - 1)) {//hits top left portion of the ball -> bottom of brick
                        ball.setDY(ball.speed);
                    }
					else if(bricks[i].getRect().contains(ballRight,ballTop - 1)){//hits top right portion of the ball -> bottom of brick
						ball.setDY(ball.speed);
					}
                    else if (bricks[i].getRect().contains(ballLeft, ballBottom + 1)) {//hits bottom left of the ball -> top of brick
                        ball.setDY(-ball.speed);
                    }
                    else if(bricks[i].getRect().contains(ballRight,ballBottom + 1)){//hits bottom right of the ball -> top left of brick
                    	ball.setDY(-ball.speed);
                    }
                    bricks[i].setDestroyed(true);//destroy brick once hit by ball
                    
                }
				
			}
			if(bricks[i].isDestroyed()){
				score += 10;
				count --;//counts bricks left; this is used for the win game condition
			}
			if(upgradeCollide(bricks[i])){//if upgrade rectangle collides with paddle rectangle
			//determines what each upgrade does and it's effects in game[speed,life, etc.]
				if(bricks[i].upgrades == "life"){	                    	
	            	lives ++; //user gains 1 more health to a max of 5
	            	if(lives >= 5){
	            		lives = 5;
	            	}
	            }
   				else if(bricks[i].upgrades == "gun"){   					
   					gunActive = true;
   					gunCountDown = 550; //timer for how long the ability occurs
   				} 
   				else if(bricks[i].upgrades == "special"){   					
	            	lives ++;//user gains 1 more health to a max of 5
	            	score += 25; //and gets 25 score added
	            	if(lives >= 5){
	            		lives = 5;
	            	}
	            	ball.setSpeed(3); //ball is a little bit faster
   				}
	            else if(bricks[i].upgrades == "speed"){
	            	ball.setSpeed(5); //ball is very fast
	            	increasedSpeed = true;
	            	speedCoolDown = 210; //timer for how long the ability occurs
	            }
	            else if(bricks[i].upgrades == "slow"){
	            	ball.setSpeed(2); //slower speed
   				}

   				bricks[i].dropped = true;
   				bricks[i].powY =- 50;
   				bricks[i].powX =- 50;	
			}

		}
		if(count == 0){//Means that all bricks are destroyed
			win = true;
			completedGame();	
		}//==========================================================================================================
	}
	public boolean upgradeCollide(Brick brick){
		//This sees if the user gains the upgrade or not 
		boolean collide = false;
		if(brick.getPowerRect().intersects(paddle.getRect())){
			collide = true;
		}
		return collide;
	}
	public void speedCollision(){
		// Speed upgrade collision-Direction it bounces back does not matter
		int count = 56;
		for(int i = 0; i < 56; i++){
			if(bricks[i].isDestroyed()){
				count --;
			}
            if ((ball.getRect()).intersects(bricks[i].getRect())) {
                if (!bricks[i].isDestroyed()) {
                	bricks[i].setDestroyed(true);
                }
            }
		}
		if(count == 0){ //in the case of the brick using the speed upgrade winning the game
			win = true;
			completedGame();	
		}
	}
	public void gunCollide(Gun userGun){
		// a method that checks to see if gun bullets hits brick(s)
		for(int j = 0; j < 56; j ++){
			if(!bricks[j].isDestroyed()){
				if(userGun.getRect().intersects(bricks[j].getRect())){
					userGunList.remove(userGun);
					bricks[j].setDestroyed(true);
				}
			}				
		}
	}
//==================================================================
	//MouseListener
	public void mouseExited(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {} 
	public void mouseEntered(MouseEvent e) {}

    public void mouseClicked(MouseEvent e){
    	start = true; //starts the game
    	if(gunActive  && gunCountDown > 0 && gunTimeInterval == 0){//if has gun has the upgrade
    		shoot = true;
    		setGunXValue(paddle.px);
    		Gun gunR = new Gun(gunX + paddle.size  -5);
    		Gun gunL = new Gun(gunX + 5); //gun on each side of paddle
    		userGunList.add(gunR);
    		userGunList.add(gunL);	
    		gunTimeInterval = 55;//delay in shooting so user cannot spam the shooting key
    		
    	}
	}  

    
    //Key Listener
    public void keyPressed(KeyEvent e){
    	keys[e.getKeyCode()] = true;
    }
    public void keyReleased(KeyEvent e){
    	keys[e.getKeyCode()] = false;
    }
    public void keyTyped(KeyEvent e){}
	public void paintComponent(Graphics g){
		if(currentScreen.equals("main")){ //user selects main menu screen
			g.drawImage(main,0,0,this);
			if(menuOptionSelect == 0){//default option is on new game option
				g.drawImage(gameScreenSelect,0,0,this);
			}
			else if(menuOptionSelect == 1){//instruction selection
				g.drawImage(instructionScreenSelect,0,0,this);
			}
			else if(menuOptionSelect ==2){//exit selection
				g.drawImage(exitScreenSelect,0,0,this);
			}
		}
		if(currentScreen.equals("gameON")){
			g.drawImage(back,0,0,this);//draws the background
		
			for (int i = 0; i < 56;i ++){ //this draws the bricks
	     		if(bricks[i].eliminated == false){
	         		g.drawImage(gameBrickPic,bricks[i].getX(),bricks[i].getY(),this);
	         	}
	         }
			if(inGame){
				g.drawImage(gameBallPic,ball.bx,ball.by,this);//draws the ball
				g.drawImage(gamePaddlePic,paddle.px,500,this);//draws the paddle

	         	for(int i = 0; i < 56 ; i ++){//draw upgrades upon destruction of a brick if there are any
		         	if(bricks[i].dropped == false && bricks[i].hasUpgrade && bricks[i].eliminated){
		         			         		
		         		if(bricks[i].upgrades.equals("speed")){
		         			g.drawImage(speedPic,bricks[i].powX,bricks[i].powY,this);
		         		}
		         		else if(bricks[i].upgrades.equals("gun")){
		         			g.drawImage(gunPic,bricks[i].powX,bricks[i].powY,this);
		         		}
		         		else if(bricks[i].upgrades == "life"){
		         			g.drawImage(lifePic,bricks[i].powX,bricks[i].powY,this);
		         		}
		         		else if(bricks[i].upgrades.equals("special")){
		         			g.drawImage(ultPic,bricks[i].powX,bricks[i].powY,this);
		         		}	   
		         		else if(bricks[i].upgrades.equals("slow")){
		         			g.drawImage(slowPic,bricks[i].powX,bricks[i].powY,this);
		         		}
		         	}	
		         }
		         g.setColor(new Color(0,255,0));
		         if(shoot){//draw the projectile
		         	for(int i = 0; i < userGunList.size(); i ++){
		         		g.drawImage(bulletPic,userGunList.get(i).gunX,userGunList.get(i).gunY,this);
		         	
		         	}
		         }
	         	g.drawImage(barBottomPic,0,530,this);//the bottom bar for the stats and score
	         	
	         	//draws font for score and lives
	         	g.setFont(new Font("Modern",Font.BOLD,35));
	         	g.setColor(new Color(255,0,0));
	         	g.drawString(String.format("%d",score),720,568);
	         	g.drawString(String.format("%d",lives),120,568);
			}
			else{ //not in game anymore
				if(win){
					g.drawImage(winPic,0,0,this); //win pic
				}
				else{
					g.drawImage(gameOverPic,0,0,this); //lose pic
				}
				
			}
		}
		//draws all other screen
		if(currentScreen.equals("about")){
			g.drawImage(aboutPic,0,0,this);
		}
		if(currentScreen.equals("how2play")){
			g.drawImage(instructionPic,0,0,this);
		}
		if(currentScreen.equals("creditpage")){
			g.drawImage(creditPic,0,0,this);
		}

         
    }

	public void gameScreens(){
		//controls current page or screen the program is on
		if(currentScreen.equals("gameON")){
			move();
		}
		else if(currentScreen.equals("how2play")){
			instructions();
		}
		else if(currentScreen.equals("main")){
			theMenu();
		}
		else if(currentScreen.equals("creditpage")){
			credits();
		}		
		else if(currentScreen.equals("about")){
			credits();
		}	
	}
	//These are methods that changes the screen to the main page if the user presses the escape key
	public void instructions(){
		//This is a method that changes the screen to the main page if the user presses the escape key
		 
		if (keys[KeyEvent.VK_ESCAPE]){
			currentScreen = "main";
		}
	}
	public void credits(){
		if (keys[KeyEvent.VK_ESCAPE]){
			currentScreen = "main";
		}
	}

    public void mousePressed(MouseEvent e){}
    
	//Mouse Motion Listener
	
	public void mouseDragged(MouseEvent e){}
    public void mouseMoved(MouseEvent e){
    	my = e.getY();
    	mx = e.getX();
		
	}

	public void restart(){ 
		//Method for reseting all game components and having a new game
		start  = false;
    	int brickCount = 0;
        for(int m = 0; m < 7; m ++){
        	for(int n = 0 ; n < 8 ; n ++){
        		bricks[brickCount] = new Brick(n*80+80,m*50+30);//This sets the brick location and space between the brick
        		brickCount ++;
        	}
        }
		//reset all variables to default
        speedCoolDown  = 0;
        gunCountDown = 0;
        gunTimeInterval = 0;
        score = 0;
        lose = false;
        win = false;
        inGame = true;
        increasedSpeed=false;
		gunActive=false;
		lives = 3;
		paddle.reset();
        ball.reset();
    }
}