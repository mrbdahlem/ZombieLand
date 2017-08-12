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
     * Karl's planned out actions to find the longest line of brains in front of him.
     */
    public void plan()
    {
        nextBrain();
        turnLeft();
        followBrains();
        turnLeft();
        
        while (isFrontClear() && isBrainHere())
        {
            move();
            nextBrain();
            turnLeft();
            if (isBrainHere()) {
                followBrains();
            }
            turnLeft();
        }
        
        //turnLeft();
        nextBrain();
        win();
    }

    public void nextBrain()
    {
        while (!isBrainHere() && isFrontClear())
        {
            move();
        }
    }
    
    public void followBrains()
    {
        while (isBrainHere() && isFrontClear())
        {
            move();
        }
        
        turnAround();
        
        if (!isBrainHere())
        {
            move();
        }
    }
}
