/**
 * @Paddle.java
	A class that contains all the methods for the paddle which is necessary
	for the breakout main class. This class has movement, the paddle rectangle and
	the location of reset.
  @author Ben Cheung
 */

import java.awt.Rectangle;

public class Paddle {
	public int px,size, mx,my;
    public Paddle(int mx) {
    	this.size = 70;
    	this.px = 375;	
    }
    public void reset(){
    	//location for paddle reset
    	px = 375;
    }
    public void move(int mx){
    	//Paddle movement
    	px = mx - size/2;
    	if(px >= 745){
        	px = 745;
        }
		else if(px <= 0){
        	px = 0;
        }
    }
    Rectangle getRect(){
    	return new Rectangle(px,500,70,15); //retangle of paddle
    }
    
    
}