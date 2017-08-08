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
     * Karl's planned out actions to build the triangle of brains in front of him.
     */
    public void plan() 
    {  
       // put down the first brain and face forward
       findCenter();
       putBrain();       
       turnLeft();
       
       // Put down all of the other brains
       addDown();
       
       // go back to the start and finish
       turnRight();       
       while(isFrontClear())
       {
           move();
       }       
       win();
    }
    
    /**
     * Walk to the center by taking two steps forward then turning around and taking one
     * step back.
     */
    public void findCenter()
    {
        // Take two steps forward.  If Karl runs into the wall, turn around and quit
        // moving forward
        if (isFrontClear())
        {
            move();
            if (isFrontClear())
            {
                move();
            }
            else
            {
                turnAround();
                return;
            }
        }
        else
        {
            turnAround();
            return;
        }
        
        // Keep moving forward two steps at a time.
        findCenter();
        
        // Once Karl has found the edge, take one step back for every two steps forward
        move();
    }
    
    /**
     * Add all brains in the tree below the current position
     */
    public void addDown()
    {
        // If the front is the edge of the world, this tree branch is done.
        if (!isFrontClear())
        {
            return;
        }
        
        // Put down all the brains down and to the left
        leftBranch();
        
        // move to the right branch
        turnRight();
        move();
        move();
        turnLeft();
        
        // Put down all brains down and to the right
        rightBranch();
    }
     
    /**
     * Put down brains in the left branch from the current cell
     */
    public void leftBranch()
    {
        // Move to the space down to the left
        move();
        turnLeft();
        move();
        turnRight();
        
        // put down a brain in this cell
        putBrain();
        
        // Put down all brains below this cell
        addDown();
    }
    
    /**
     * Put down brains in the right branch from the current cell
     */
    public void rightBranch()
    {
        // put down a brain in this cell
        putBrain();
        
        // put down all brains below this cell
        addDown();
        
        // Move back to the space above this one
        turnLeft();
        move();
        turnLeft();
        move();
        turnAround();
    }
}
