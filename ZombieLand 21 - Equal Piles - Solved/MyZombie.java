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
     * Karl's planned out actions to duplicate the pile of brains in front of him.
     */
    public void plan() 
    {
       move();
       
       while (isBrainHere())
       {
           takeBrain();
           move();
           move();
           putBrain();
           turnAround();
           move();
           move();
           turnAround();
       }
       move();
       move();
       while(isBrainHere())
       {
           turnAround();
           takeBrain();
           move();
           putBrain();
           move();
           putBrain();
           turnAround();
           move();
           move();
       }
       win();
    }
}
