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
    private Message message;
    private boolean done;

    /**
     * Constructor for objects of class MyWorld.
     * 
     */
    public ZombieLand()
    {    
        // Create a new world with 600x400 cells with a cell size of 1x1 pixels.
        super(7, 7, 64); 

        done = false;

        setPaintOrder(Message.class, Fire.class, Zombie.class, Bucket.class, Brain.class, Wall.class);
        message = new Message("");
        addObject(message, 0, 0);
        prepare();
    }
    
    /**
     * Prepare the world for the start of the program.
     * That is: create the initial objects and add them to the world.
     */
    private void prepare()
    {
        MyZombie myzombie = new MyZombie();
        addObject(myzombie,0,1);
        ZombieGoal zombiegoal = new ZombieGoal();
        addObject(zombiegoal,4,1);
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
    public Message showMessage(String msg)
    {
        removeObject(message);
        message = new Message(msg);
        addObject(message, 0, 0);

        return message;
    }

    /**
     * When the mission is ended, stop the world.
     */
    public void finish()
    {
        done = true;
        Greenfoot.stop();
    }

    /**
     * End the world if there aren't any zombies left.
     */
    public void checkZombies()
    {    
        if (!done) {
            List<Zombie> zombies = getObjects(Zombie.class);

            if (!done && zombies.size() == 0) {
                showMessage("Zombie no more.");
                finish();
            }
            
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
                showMessage("Zombie do good.");
                finish();
            }
            else if (allDead) {
                showMessage("Zombie dead.");
                finish();
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