import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.awt.Graphics;
import java.awt.FontMetrics;

/**
 * Write a description of class Brain here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Brain extends Actor
{
    private GreenfootImage baseImage;
    private int numBrains;
    
    /**
     * A disembodied brain.  Zombies love brains.
     */
    public Brain() {
        baseImage = getImage();
        numBrains = 1;
    }
    
    /**
     * Contemplates the meaning of existance; and display the number of brains
     * in this cell.
     */
    public void act() 
    {
        showNumBrainsHere();
    }    
    
    /**
     * Add one to the number of brains in this pile
     */
    public void addBrain()
    {
        synchronized(this.getClass())
        {
            try
            {
                this.numBrains++;
            }
            catch (Exception e)
            {
            }
        }
    }
    
    /**
     * Set the number of brains in this pile
     */
    public void setNum(int num)
    {
        synchronized(this.getClass())
        {
            try
            {
                this.numBrains = num;
            }
            catch (Exception e)
            {
            }
        }
    }
    
    /**
     * Remove one brain from this pile. If this is the last brain,
     * remove the pile from the world.
     */
    public void removeBrain()
    {
        synchronized(this.getClass())
        {
            try
            {
                this.numBrains--;
            
                // If there are no more brains in the pile
                if (this.numBrains < 1) {
                    World w = getWorld();
                    w.removeObject(this);
                }
            }
            catch (Exception e)
            {
            }
        }
    }
    
    /**
     * Update the image of this brain with the number of brains occupying this
     * cell.
     */
    private void showNumBrainsHere()
    {
        synchronized(this.getClass())
        {
            try
            {
                // Deal with old zombies dropping extra brains the old way
                //int numBrains = this.numBrains + getIntersectingObjects(Brain.class).size();
                
                if (numBrains > 1) {
                    
                    String msg = "" + (numBrains);
                    
                    GreenfootImage img = new GreenfootImage(baseImage);
                    GreenfootImage num = new GreenfootImage(msg, 28, Color.WHITE, 
                        new Color(0,0,0,0), new Color(0,0,0,0));
                        
                    int x = (img.getWidth() - num.getWidth()) / 2;
                    int y = img.getHeight() - num.getHeight();
                    img.drawImage(num, x, y);
                    
                    setImage(img);
                }
                else {
                    setImage(baseImage);
                }
                
                //this.getClass().notify();
            }
            catch (Exception e)
            {
            }
        }
    }
}
