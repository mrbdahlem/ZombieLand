import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Help Karl collect all of the brains.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class MyZombie extends UltraZombie
{
    /**
     * Karl's planned out actions to swap the piles of brains in front of him.
     */
    public void plan() 
    {
       for (int i = 0; i < getWorld().getWidth(); i++) {
           while (isFrontClear()) {
               arrange();
           }
           
           turnAround();
           
           while(isFrontClear()) {
               move();
           }
           turnAround();
       }
       win();
    }
    
    public void arrange()
    {
       turnLeft();
       moveBrains();
       turnRight();
       move();
       turnLeft();
       moveBrains();
       move();
       
       while(isBrainHere())
       {
           takeBrain();
           turnLeft();
           move();
           if(isBrainHere())
           {
               takeBrain();
               turnLeft();
               move();
               putBrain();
               turnLeft();
               move();
               putBrain();
               turnLeft();
               move();
           }
           else
           {
               turnLeft();
               move();
               turnLeft();
               move();
               putBrain();
               turnLeft();
               move();
               turnAround();
               moveBrains();                       
               move();
               turnLeft();
               return;
           }
       }
       
       turnLeft();
       move();
       turnAround();
       moveBrains();
       move();
       turnRight();
       moveBrains();
       move();
       turnLeft();
    }
    
    public void moveBrains()
    {
        while(isBrainHere())
        {
            takeBrain();
            move();
            putBrain();
            turnAround();
            move();
            turnAround();
        }
    }
}
