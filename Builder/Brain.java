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
    
    /**
     * A disembodied brain.  Zombies love brains.
     */
    public Brain() {
        baseImage = getImage();
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
     * Update the image of this brain with the number of brains occupying this
     * cell.
     */
    private void showNumBrainsHere()
    {
        synchronized(this.getClass())
        {
            try
            {
                //this.getClass().wait();
            
                int numBrains = getIntersectingObjects(Brain.class).size() + 1;
                
                if (numBrains > 1) {
                    String msg = "" + (numBrains);
                    
                    GreenfootImage img = new GreenfootImage(baseImage);
            
                    java.awt.Font f = new java.awt.Font("MONOSPACED", java.awt.Font.BOLD, 28);
                    Graphics g = img.getAwtImage().createGraphics();
                    g.setFont(f);
                    FontMetrics fm = g.getFontMetrics(f);
                    
                    int textWidth = fm.stringWidth(msg);
                    int textHeight = fm.getHeight() + fm.getMaxDescent();
                    int textBottom = textHeight - fm.getMaxDescent();
                        
                    g = img.getAwtImage().createGraphics();
                    g.setColor(java.awt.Color.BLACK);
                    g.setFont(f);   
                    
                    int x = (img.getWidth()-textWidth) / 2;
                    
                    g.drawString(msg, x-1, textBottom-1);
                    g.drawString(msg, x, textBottom-1);
                    g.drawString(msg, x+1, textBottom-1);
                    g.drawString(msg, x-1, textBottom);
                    g.drawString(msg, x+1, textBottom);
                    g.drawString(msg, x-1, textBottom+1);
                    g.drawString(msg, x, textBottom+1);
                    g.drawString(msg, x+1, textBottom+1);
                    
                    g.setColor(java.awt.Color.WHITE);
                    g.drawString(msg, x, textBottom);
                    
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
