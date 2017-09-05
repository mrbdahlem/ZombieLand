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
        while(!hasWon()){
            while(isFrontClear() || isBrainHere()){
                nextBrain();
                turnLeft();
                followBrains();
            }
            turnAround();
            turnRight();
            move();
            turnLeft();
            nextBrain();
            win();
            
        }
    }
    public void nextBrain(){
        move();
        while(isFrontClear() && !isBrainHere()){
             move();
        }
    }
    public void followBrains(){
        while(isBrainHere() && isFrontClear()){
            move();
        }
        turnAround();
        move();
        turnLeft();
    }
}
