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
     * Karl's planned out actions to multiply the piles of brains in front of him.
     */
    public void plan() 
    {
       move();
       while(isBrainHere())
       {
           takeBrain();
           move();
           turnLeft();
           move();
           putBrain();
           turnLeft();
           move();
           turnLeft();
           move();
           turnLeft();
       }
       move();
       while(isBrainHere())
       {
           takeBrain();
           turnLeft();
           move();
           turnLeft();
           while(isBrainHere())
           {
               takeBrain();
               move();
               putBrain();
               turnLeft();
               move();
               putBrain();
               turnLeft();
               move();
               turnLeft();
               move();
               turnLeft();
           }
           move();
           while(isBrainHere()) {
               takeBrain();
               turnAround();
               move();
               putBrain();
               turnAround();
               move();
           }
           turnLeft();
           move();
           turnLeft();
           move();
       }
       turnLeft();
       move();
       while(isBrainHere()) {
           takeBrain();
       }
       turnLeft();
       move();
       move();
       turnLeft();
       move();
       win();
    }
}
