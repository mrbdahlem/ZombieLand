import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class UltraZombie here.
 * 
 * @author bdahlem 
 * @version 7/26/2017
 */
public class UltraZombie extends Zombie
{
    public static final int EAST = 0;
    public static final int SOUTH = 90;
    public static final int WEST = 180;
    public static final int NORTH = 270;
    
    /**
     * Determine which direction the UltraZombie is facing.
     * 
     * @return a value of EAST,SOUTH,WEST, or NORTH
     */
    public final int isFacing()
    {
       return (getRotation() / 90) * 90;
    }
    
    /**
     * Turn 90 degrees to the left
     */
    public final void turnLeft()
    {
        turn(-1);
    }
    
    /**
     * Turn 180 degrees
     */
    public final void turnAround()
    {
        turn(2);
    }
}
