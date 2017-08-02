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
       move();
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
               putBrain();
               turnAround();
               move();
               turnRight();
               move();
               while(isBrainHere()) {
                   turnAround();
                   takeBrain();
                   move();
                   turnLeft();
                   move();
                   putBrain();
                   turnAround();
                   move();
                   turnRight();
                   move();
               }
               
               turnRight();
               move();
               turnLeft();
               move();
               win();
           }
       }
       turnLeft();
       move();
       while(isBrainHere()) {
           turnAround();
           takeBrain();
           move();
           turnRight();
           move();
           putBrain();
           turnAround();
           move();
           turnLeft();
           move();
       }
       
       turnAround();
       move();
       move();
       turnRight();
       move();
       win();
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
