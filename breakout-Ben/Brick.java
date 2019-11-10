/**
 * @(#)Brick.java
 Class for the bricks which is used in the Breaout main class.
  Upgrades droppings, power changes of the ball and 
  it getting destroyed are all specified here.
  @author Ben Cheung
 */
import java.util.*;
import java.awt.Rectangle; //needs this to check for collisions

public class Brick {
	public String upgrades;
	public int powX, powY, y, x ;
	public boolean eliminated, dropped, hasUpgrade;
	
    public Brick(int x, int y) {
    	this.y = y;
    	this.upgrades = null;	
    	this.x = x;
		dropped = false;
    	eliminated = false;
    		
    	if(hasPow()){
    		powX  = x + 16;
    		powY = y + 25;
    		
    		Random rand = new Random();
    		int pow = rand.nextInt(7);
    		if(pow == 0){//low chance of getting life
    			upgrades = "life";
    		}
    		else if(pow == 2 || pow == 1){
    			upgrades = "slow";		
    		}
    		else if(pow == 4 || pow == 3){
    			upgrades = "speed";
    		}
    		if(pow == 5){
    			upgrades = "gun";
    		}
    		if(pow == 6){
    			upgrades ="special";
    		}
    	}
    }
    public boolean hasPow(){
    	//checks if brick has an upgrade or not
    	hasUpgrade = false;
    	Random rand = new Random();
    	int num = rand.nextInt(8);// 1 of 8 chance of an upgrade appearing
    	if(num == 0){
    		hasUpgrade = true;
    	}
    	return hasUpgrade;
    }
    public boolean isDestroyed(){
    	return eliminated;
    }
    public int getY(){
    	return y;
    }
    public int getX(){
    	return x;
    }
    public void setDestroyed(boolean eliminated){
    	this.eliminated = eliminated;
    }
    public void dropPower(){
    	powY += 1;
    }
    Rectangle getPowerRect(){//get rectangle for the upgrade
    	return new Rectangle(powX,powY,35,35);
    }
    Rectangle getRect(){//Obtain the rectangle of the brick
    	return new Rectangle(x,y,70,25);
    }   
}