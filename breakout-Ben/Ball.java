/**
 * @(#)Ball.java
 The Ball class for all functions for the ball such as movement, speed, and it's rectangle
  @author Ben Cheung
 */
import java.awt.Rectangle;
/* Gets the rectangle class and has a height and with (x,y) based on coordinates.
 *A Rectangle specifies an area in a coordinate space that is enclosed 
 *by the Rectangle object's upper-left 
 *point (x,y) in the coordinate space, its width, and its height
 -http://docs.oracle.com/javase/7/docs/api/java/awt/Rectangle.html
 -http://www.geom.uiuc.edu/~addingto/docs/java_a44.htm
 **/

public class Ball {
	public int speed, bx, by, dy,  dx ;
	
    public Ball(int bx, int by) {
    	this.dx = speed;
    	this.dy =- speed;
    	this.by = by;
    	this.bx = bx;
    	this.speed = 3;
    }
    public void setSpeed(int s){
    	this.speed = s;
    }
    public void reset(){//reset ball to initial state
    	this.speed = 3;
    	this.bx = 392;
    	this.by = 485;
    }
    public void setDY(int y){
    	this.dy = y;
    }
    public void setBX(int x){
    	this.bx = x;
    }
    public void setBY(int y){
    	this.by = y;
    }
    public void setDX(int x){
    	this.dx = x;
    }

    public void move(int px,int size){
    	//method for Ball movements and speed
    	bx += dx;
    	by += dy;
    	// The paddle now has 3 sections 	
    	int paddleR = px + size; 
    	int px2 = px + size/3;
    	int px3 = px2 + size/3;
    	
    	
        if (by <= 0) {//increase speed everytime the ball hits the top
        	speed += 1;
        	if(speed >= 5){
        		speed = 5;
        	}
        	setDY(speed);
        } 	
     	if (bx >= 785) {
        	setDX(-speed);
     	}	
    	if (bx <= 0) {
	        setDX(speed);
	     }

        if (bx <= px3 && by + 15 <= 515  &&  bx >= px2 && by + 15 >= 500){
        	setDY(-speed);
        }
        else if (bx <= paddleR && by + 15 <= 515  &&  bx >= px3 && by + 15 >= 500){// condition of the ball hitting the right of the paddle
        	setDY(-speed);
        	setDX(speed);
        }
        else if (bx <= px2 && by + 15 <= 515  &&  bx >= px && by + 15 >= 500){//condition of the ball hitting the left of the paddle
        	setDX(-speed);
        	setDY(-speed);
        	
        }
    }
    Rectangle getRect(){ 
    	//This gets the rectangle of the ball which will be used to check for collision
    	return new Rectangle(bx,by,15,15);
    }
    
    
}