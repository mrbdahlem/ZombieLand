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
     * Karl's planned out actions
     */
    public void plan() 
    {
        buildWall();
        
        while(isFrontClear())
        {
            move();
            if (isFrontClear())
            {
                move();
                buildWall();
            }
        }
        win();
    }
    
    public void buildWall()
    {
        turnLeft();
        move();
        takeBrain();
        move();
        takeBrain();
        move();
        takeBrain();
        turnAround();
        move();
        move();
        move();
        turnLeft();
        
    }
}
