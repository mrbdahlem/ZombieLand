import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.List;

/**
 * Write a description of class MyWorld here.
 * 
 * @author bdahlem
 * @version 1.0
 */
public class ZombieLand extends World
{
    Actor message = null;
    private boolean done = false;

    /**
     * Constructor for objects of class MyWorld.
     * 
     */
    public ZombieLand()
    {    
        // Create a new world 6 cells wide and 3 cells high with a cell size of 64x64 pixels.
        super(6, 3, 64); 

        // Mark the problem as not finished
        done = false;

        // Determine which objects appear on top of other objects
        setPaintOrder(Actor.class, Zombie.class, ZombieDetector.class);
        
        // Populate the world
        prepare();
    }
    
    /**
     * Prepare the world for the start of the program.
     * That is: create the initial objects and add them to the world.
     */
    private void prepare()
    {
        MyZombie karl = new MyZombie();
        addObject(karl,0,1);
        ZombieGoal zombiegoal = new ZombieGoal();
        addObject(zombiegoal,5,1);
    }

    /**
     * Check the status of the Zombies every frame
     */
    public void act()
    {
        checkZombies();
    }
    
    /**
     * Display a message in the World
     */
    public void showMessage(String msg)
    {
        if (message != null) {
            removeObject(message);
        }
        
        message = new Actor(){public void act(){}};
        
        int xOffset = 0;
        int yOffset = 0;
        
        if (getWidth() % 2 == 0) {
            xOffset = getCellSize() / 2;
        }
        if (getHeight() % 2 == 0) {
            yOffset = getCellSize() / 2;
        }
                
        GreenfootImage img = new GreenfootImage(1,1);
        
        java.awt.Font f = new java.awt.Font(java.awt.Font.SANS_SERIF, java.awt.Font.BOLD, 30);
        java.awt.Image image = img.getAwtImage();
        java.awt.Graphics g = img.getAwtImage().createGraphics();
        g.setFont(f);
        java.awt.FontMetrics fm = g.getFontMetrics(f);
        
        int textWidth = fm.stringWidth(msg);
        int textHeight = fm.getHeight() + fm.getMaxDescent();
        int textBottom = textHeight - fm.getMaxDescent();
        
        img = new GreenfootImage((textWidth  + xOffset)* 2, textHeight + yOffset * 2);
        g = img.getAwtImage().createGraphics();
        g.setColor(java.awt.Color.BLACK);
        g.setFont(f); 
        
        int x = textWidth / 2 ;
        int y = textBottom;
        
        g.drawString(msg, x-1, y-1);
        g.drawString(msg, x, y-1);
        g.drawString(msg, x+1, y-1);
        g.drawString(msg, x-1, y);
        g.drawString(msg, x+1, y);
        g.drawString(msg, x-1, y+1);
        g.drawString(msg, x, y+1);
        g.drawString(msg, x+1, y+1);
        
        g.setColor(java.awt.Color.WHITE);
        g.drawString(msg, x, y);
        
        message.setImage(img);
        addObject(message, getWidth() / 2, getHeight() / 2);
    }

    /**
     * When the mission is ended, stop the world.
     */
    public void finish(boolean success)
    {
        done = true;
    }
    
    /**
     * When the mission is ended, stop the world.
     */
    public void finish(String msg, boolean success)
    {
        showMessage(msg);
        done = true;
    }

    /**
     * Check whether the scenario is complete.
     */
    public boolean isFinished()
    {
        return done;
    }
    
    /**
     * End the world if there aren't any zombies left.
     */
    public void checkZombies()
    {    
        if (!done) {
            List<Zombie> zombies = getObjects(Zombie.class);

            if (zombies.size() == 0) {
                finish("Zombie no more.", false);
            }
            else {
                boolean allWinners = true; // Assume that everyone is a winner, until proven wrong.
                boolean allDead = true;
                for (Zombie z : zombies) {
                    if (!z.hasWon()) {
                        allWinners = false;
                    }
                    if (!z.isDead()) {
                        allDead = false;
                    }
                }
                
                if (allWinners) {
                    finish("Zombie do good.", false);
                }
                else if (allDead) {
                    finish("Zombie dead.", false);
                }
            }
        }
    }

    /**
     * The world has stopped.  Was the mission successful?
     */
    public void stopped()
    {

    }
}