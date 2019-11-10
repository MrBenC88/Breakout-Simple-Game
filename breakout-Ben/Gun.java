/**
 *Gun.java
 The Gun class for the breakout main class. This class has all the components and methods
 for the gun upgrade
  @author Ben Cheung
 */

import java.awt.Rectangle;
public class Gun {
	public boolean hit;
	public int gunY, gunX;
    public void shoot(){
    	//shoot method- moves the y coordinate of the gun's projectile
    	gunY--;
    }
    public Gun(int gunX) {
     	this.gunY = 500;
    	this.gunX = gunX;
    }
    Rectangle getRect(){
    	//get rectangle for each individual projectile
    	//- used for collison check
    	return new Rectangle(gunX,gunY,6,10);
    }
    
    
}