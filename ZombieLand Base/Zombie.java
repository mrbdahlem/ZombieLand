import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.awt.Graphics;
import java.awt.FontMetrics;

/**
 * A programmable zombie character.
 *
 * @author bdahlem
 * @version 1.0
 */
public abstract class Zombie extends Actor
{
    private int frame = 0;
    private int deadFrame = 0;
    private int NUM_FRAMES = 4;
    private int moveAngle = 0;
    private GreenfootImage[][] images;
    
    public static final int ZOMBIE_GROAN = 0;
    public static final int ZOMBIE_MED_GROAN = 1;
    public static final int ZOMBIE_LONG_GROAN = 2;
    public static final int ZOMBIE_LOUD_GROAN = 3;
    public static final int ZOMBIE_GRUNT = 4;
    public static final int ZOMBIE_SCREAM = 5;
    public static final int NUM_SOUNDS = 6;
             
    private static GreenfootSound[] sounds;

    private Thread thinker;
    private Object synchro = new Object();

    private volatile int numBrains = 0;

    private volatile boolean undead = true;
    private volatile boolean won = false;

    /**
     * Make a new zombie, you evil person.
     */
    public Zombie() {
        // Prepare the zombie walking animation frames
        images = new GreenfootImage[5][];
        preloadImages();
        preloadSounds();

        thinker = new Thread(new Runnable()
        {
            public void run() {
                plan();
                synchronized (synchro) {
                    try {
                        synchro.wait();
                        if (stillTrying()) {
                           die();
                        }
                    }
                    catch (InterruptedException e) {
                    }
                }
            }
        }
        );

        thinker.start();
    }

    /**
     * Perform one animation step.
     */
    public final void act()
    {
        synchronized (synchro) {
            frame = (frame + 1) % NUM_FRAMES;
            showAnimationFrame();
            if(frame % 2 == 0) {
                synchro.notify();
                
                if (undead && Math.random() < 0.1) {
                    sounds[(int)(Math.random() * 2)].play();
                }
            }
        }
    }

    /**
     * The special thing about this zombie is that has a plan.
     * The zombie's plan is run in a separate thread.  Commands, such as move()
     * and turnRight() wait their turn so that they can happen asynchronously with
     * animations, etc.
     */
    public void plan()
    {
    }

    /**
     * Determine if this zombie is still struggling to make it in this world.
     */
    public boolean stillTrying()
    {
        return undead && !won && !((ZombieLand)getWorld()).isFinished();
    }

    /**
     * Move forward one step.
     */
    public final void move()
    {
        synchronized (synchro) {
            try {
                synchro.wait();
                if (stillTrying()) {
                    boolean success = handleWall();
                    success = success && handleBucket();
                    if (success) {
                        super.move(1);
                    }
                    else {
                        undead = false;
                        die();
                    }
                }
            }
            catch (InterruptedException e) {
            }
        }
    }

    /**
     * Turn 90 degrees to the right.
     */
    public final void turnRight()
    {
        synchronized (synchro) {
            try {
                synchro.wait();
                turn(1);
            }
            catch (InterruptedException e) {
            }
        }
    }

    /**
     * Turn to the right a given number of times
     * @param turns the number of times to turn 90 degrees to the right
     */
    public final void turn(int turns) {
        int degrees = turns * 90;
        synchronized (synchro) {
            if (stillTrying()) {
                getImage().setTransparency(0);
                super.turn(degrees);
                showAnimationFrame();
                getImage().setTransparency(255);
            }
        }
    }

    /**
     * Pick up brains if they exist.  End if not.
     */
    public final void takeBrain()
    {
        synchronized (synchro) {
            try {
                synchro.wait();
                if (stillTrying()) {
                    if (isTouching(Brain.class)) {
                        numBrains++;
                        removeTouching(Brain.class);
                    }
                    else {
                        ((ZombieLand)getWorld()).finish("Zombie no get brain.", false);
                    }
                }
            }
            catch (InterruptedException e) {
            }
        }
    }

    /**
     * Put down a brain if the Zombie has one.  End if not.
     */
    public final void putBrain()
    {
        synchronized (synchro) {
            try {
                synchro.wait();
                if (stillTrying()) {
                    if (numBrains > 0) {
                        numBrains--;
                        getWorld().addObject(new Brain(), getX(), getY());
                    }
                    else {
                        ((ZombieLand)getWorld()).finish("Zombie no have brain.", false);
                    }
                }
            }
            catch (InterruptedException e) {
            }
        }
    }

    /**
     * Put down a brain if the Zombie has one.  End if not.
     */
    public final boolean haveBrains()
    {
        synchronized (synchro) {
            try {
                synchro.wait();
                return numBrains > 0;
            }
            catch (InterruptedException e) {
            }

            return false;
        }
    }

    /**
     * Check if there is a brain where the zombie is standing.
     */
    public final boolean isBrainHere() {
        return (isTouching(Brain.class));
    }

    /**
     * Check if there is a wall or the edge of the world in front of the zombie.
     */
    public final boolean isFrontClear() {
        synchronized (synchro) {
            try {
                synchro.wait();
                return checkFront(Wall.class, 1) == null &&
                        checkFront(null, 1) == null;
            }
            catch (InterruptedException e) {
            }
            return false;
        }
    }

    /**
     * Die, for reals this time.
     */
    public final void die()
    {
        synchronized (synchro) {
            try {
                synchro.wait();
                undead = false;
                sounds[ZOMBIE_SCREAM].play();
            }
            catch (InterruptedException e) {
            }
        }
    }

    /**
     * Play a sound
     */
    public void playSound(int index) 
    {
        sounds[index].play();
    }
    
    /**
     * Check if this zombie is dead, or just undead
     */
    public final boolean isDead()
    {
        return undead == false;
    }

    /**
     * This Zombie has reached its goal in afterlife!
     */
    public void win()
    {
        if (!won) {
            won = true;
            sounds[ZOMBIE_GRUNT].play();

        }
    }
    
    /**
     * Check if this zombie has accomplished everything it could hope for.
     */
    public boolean hasWon()
    {
        return won;
    }
    /**
     * Show the next animation frame based on the direction the zombie is facing.
     */
    private void showAnimationFrame()
    {
        int dir = getRotation() / 90;

        GreenfootImage img = null;

        if (stillTrying()) {
            // If the zombie is carrying brains, add the number to the current frame.
            if (numBrains > 0) {
                img = new GreenfootImage(images[dir][frame]);
                img.drawImage(NumberOverlay(numBrains, 64, 64, getRotation()), 0, 0);
            }
            else {
                img = images[dir][frame];
            }
        }
        else if (!undead) {
            setRotation(0);
            if (deadFrame == 0) {
                deadFrame = dir;
            }
            img = images[4][deadFrame];
        }
        else if (won) {
            setRotation(90);
            img = images[1][1];
        }

        setImage(img);
    }

    private GreenfootImage NumberOverlay(int number, int width, int height, int direction)
    {
        GreenfootImage textImg = new GreenfootImage(width, height);

        String msg = "" + number;

        java.awt.Font f = new java.awt.Font("MONOSPACED", java.awt.Font.BOLD, 14);
        Graphics g = textImg.getAwtImage().createGraphics();
        g.setFont(f);
        FontMetrics fm = g.getFontMetrics(f);

        int textWidth = fm.stringWidth(msg);
        int textHeight = fm.getHeight() + fm.getMaxDescent();
        int textBottom = textHeight - fm.getMaxDescent();

        g.setColor(java.awt.Color.BLACK);
        g.drawString(msg, 2, textBottom-1);
        g.drawString(msg, 3, textBottom-1);
        g.drawString(msg, 4, textBottom-1);
        g.drawString(msg, 2, textBottom);
        g.drawString(msg, 4, textBottom);
        g.drawString(msg, 2, textBottom+1);
        g.drawString(msg, 3, textBottom+1);
        g.drawString(msg, 4, textBottom+1);

        g.setColor(java.awt.Color.WHITE);
        g.drawString(msg, 3, textBottom);

        textImg.rotate(-direction);

        return textImg;
    }

    /**
     * Handle a wall in front of the zombie.  Everything ends if we crash into a wall.
     */
    private boolean handleWall()
    {
        if (checkFront(Wall.class, 1) != null || checkFront(null, 1) != null) {
            ((ZombieLand)getWorld()).finish("Zombie hit wall.", false);
            return false;
        }
        return true;
    }

    /**
     * Handle a bucket in front of the zombie.  Running into a bucket tries to push it.  If it can't be pushed,
     * everything ends.
     */
    private boolean handleBucket()
    {
        Bucket bucket = (Bucket)checkFront(Bucket.class, 1);
        if (bucket != null)
        {
            if (tryPush(bucket, getRotation()) == false) {
                ((ZombieLand)getWorld()).finish("Bucket no move.", false);
                return false;
            }
        }
        return true;
    }

    /**
     * Attempt to push an object in a given direction
     */
    private boolean tryPush(Actor item, int dir)
    {
        dir = dir / 90;
        int dx = 0;
        int dy = 0;

        if (dir == 0) {
            dx = 1;
        }
        else if (dir == 1) {
            dy = 1;
        }
        else if (dir == 2) {
            dx = -1;
        }
        else {
            dy = -1;
        }

        if (checkFront(Wall.class, 2) == null && checkFront(null, 2) == null) {
            item.setLocation(item.getX()+dx, item.getY()+dy);
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Check for an object of a particular class in front of the zombie or if that distance is beyond the
     * edge of the world
     *
     * @param clazz The class to check for.  If null, look for the edge of the world
     * @param distance The distance (in cells) to the front to look for the object/edge
     * @return The object at the distance, or a Boolean(false) if the front is off the edge of the world
     *         null if no object of the given class is at that distance or the world does not end within
     *         that distance.
     */
    private Object checkFront(Class clazz, int distance)
    {
        int dir = getRotation() / 90;
        int dx = 0;
        int dy = 0;

        if (dir == 0) {
            dx = 1;
        }
        else if (dir == 1) {
            dy = 1;
        }
        else if (dir == 2) {
            dx = -1;
        }
        else {
            dy = -1;
        }

        dx *= distance;
        dy *= distance;

        if (clazz != null) {
            return getOneObjectAtOffset(dx, dy, clazz);
        }
        else {
            int nextX = getX() + dx;
            int nextY = getY() + dy;
            if ((nextX >= 0 && nextX < getWorld().getWidth()) &&
                (nextY >= 0 && nextY < getWorld().getHeight())) {
                return null;
            }
            else {
                return new Boolean(false);
            }
        }
    }


    /**
     * Load the zombie walking images for faster shambling animations.
     */
    private void preloadImages()
    {
        images[0] = loadImages("zombie-right");
        images[1] = loadImages("zombie-down");
        images[2] = loadImages("zombie-left");
        images[3] = loadImages("zombie-up");

        images[4] = loadImages("zombie-dead");
    }

    /**
     * Create and fill an array of Greenfoot images by loading the files with
     * a given name followed by frame numbers
     */
    private GreenfootImage[] loadImages(String name)
    {
        GreenfootImage[] imageArr = new GreenfootImage[NUM_FRAMES];

        for (int i = 0; i < NUM_FRAMES; i++) {
            imageArr[i] = new GreenfootImage(name + "-" + i + ".png");
        }

        return imageArr;
    }

    /**
     * Load the zombie sounds for faster playback.
     */
     private void preloadSounds()
     {
         if (sounds == null) {
             sounds = new GreenfootSound[NUM_SOUNDS];
             sounds[ZOMBIE_GROAN] = new GreenfootSound("ZombieGroan.wav");
             sounds[ZOMBIE_MED_GROAN] = new GreenfootSound("ZombieMedGroan.wav");
             sounds[ZOMBIE_LONG_GROAN] = new GreenfootSound("ZombieLongGroan.wav");
             sounds[ZOMBIE_LOUD_GROAN] = new GreenfootSound("ZombieLoudGroan.wav");
             sounds[ZOMBIE_GRUNT] = new GreenfootSound("ZombieGrunt.wav");
             sounds[ZOMBIE_SCREAM] = new GreenfootSound("ZombieScream.wav");
            }
        }
}
