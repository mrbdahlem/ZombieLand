import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.awt.Graphics;
import java.awt.FontMetrics;

/**
 * Write a description of class Message here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Message extends Actor
{
    public Message(String msg)
    {
        if (msg.length()> 0) {
            java.awt.Font f = new java.awt.Font("MONOSPACED", java.awt.Font.BOLD, 30);
            GreenfootImage img = getImage();
            Graphics g = img.getAwtImage().createGraphics();
            g.setFont(f);
            FontMetrics fm = g.getFontMetrics(f);
            
            int textWidth = fm.stringWidth(msg);
            int textHeight = fm.getHeight() + fm.getMaxDescent();
            int textBottom = textHeight - fm.getMaxDescent();
            
            img = new GreenfootImage(textWidth * 2 , textHeight);
            g = img.getAwtImage().createGraphics();
            g.setColor(java.awt.Color.BLACK);
            g.setFont(f);   
            
            g.drawString(msg, textWidth-1, textBottom-1);
            g.drawString(msg, textWidth, textBottom-1);
            g.drawString(msg, textWidth+1, textBottom-1);
            g.drawString(msg, textWidth-1, textBottom);
            g.drawString(msg, textWidth+1, textBottom);
            g.drawString(msg, textWidth-1, textBottom+1);
            g.drawString(msg, textWidth, textBottom+1);
            g.drawString(msg, textWidth+1, textBottom+1);
            
            g.setColor(java.awt.Color.WHITE);
            g.drawString(msg, textWidth, textBottom);
            
            setImage(img);
        }
        else {
            GreenfootImage img = new GreenfootImage(1,1);
            setImage(img);
        }
    }
}
