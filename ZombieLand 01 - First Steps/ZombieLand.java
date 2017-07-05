import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.List;

import java.io.File;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.net.URL;
import java.net.URLClassLoader;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.List;
import java.util.ArrayList;

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
    private List<GoalObject> goal;

    /**
     * Load the world description file and initialize the world
     */
    public ZombieLand()
    {   
        // Create a temporary world;
        super(1,1,1);
        
        try {
            // Create a Classloader to load the actors for the world
            URL url = (new File(".")).toURL();
            URL[] urls = new URL[]{url};
            ClassLoader cl = new URLClassLoader(urls, this.getClass().getClassLoader());
            
            // Open and parse the world description XML File
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new File("world.xml"));
            doc.getDocumentElement().normalize();
            
            // Get a handle to the root of the world description
            Element root = doc.getDocumentElement();
            
            // Set the world width and height
            int width = Integer.parseInt(root.getAttribute("width"));
            int height = Integer.parseInt(root.getAttribute("height"));
            ZombieLand realWorld = new ZombieLand(width, height, 64);
            Greenfoot.setWorld(realWorld);

            // Get handles to the initial and objective description nodes
            Node initial = root.getElementsByTagName("initial").item(0);
            Node objective = root.getElementsByTagName("objective").item(0);
                       
            // Load and place initial objects
            NodeList initialObjects = ((Element)initial).getElementsByTagName("object");
            for (int i = 0; i < initialObjects.getLength(); i++) {
                Element obj = (Element)initialObjects.item(i);
                String className = obj.getAttribute("classname");
                
                Class objClass = cl.loadClass(className);
               
                NodeList locations = obj.getElementsByTagName("location");
                for (int j = 0; j < locations.getLength(); j++) {
                    Element pos = (Element)locations.item(j);
                    
                    int x = Integer.parseInt(pos.getAttribute("x"));
                    int y = Integer.parseInt(pos.getAttribute("y"));
                    
                    int count = 1;
                    
                    if (pos.hasAttribute("count")) {
                        count = Integer.parseInt(pos.getAttribute("count"));
                    }
                    Constructor constructor = objClass.getConstructor();
                    
                    for (; count > 0; count--) {
                        realWorld.addObject((Actor)constructor.newInstance(), x, y);
                    }
                }
            }
            
            realWorld.goal = new ArrayList<GoalObject>();
            
            NodeList goalObjects = ((Element)objective).getElementsByTagName("object");
            for (int i = 0; i < goalObjects.getLength(); i++) {
                Element gEl = (Element)goalObjects.item(i);
                
                String classname = gEl.getAttribute("classname");
                
                NodeList locations = gEl.getElementsByTagName("location");
                for (int j = 0; j < locations.getLength(); j++) {
                    Element pos = (Element)locations.item(j);
                    
                    GoalObject gObj = new GoalObject();
                    gObj.name = classname;                    
                    gObj.x = Integer.parseInt(pos.getAttribute("x"));
                    gObj.y = Integer.parseInt(pos.getAttribute("y"));
                    
                    if (pos.hasAttribute("count")) {
                        gObj.count = Integer.parseInt(pos.getAttribute("count"));
                    }
                    
                    NodeList callList = pos.getElementsByTagName("call");
                    if (callList.getLength() > 0) {
                        gObj.calls = new ArrayList<String[]>();
                        
                        for (int k = 0; k < callList.getLength(); k++) {
                            Element method = ((Element)callList.item(k));
                            String[] callSignature = new String[2];
                            callSignature[0] = method.getAttribute("name");
                            callSignature[1] = method.getAttribute("value");
                            
                            gObj.calls.add(callSignature);
                        }
                    }    
                    
                    realWorld.goal.add(gObj);
                }
            }
            
            // Get a handle to the paintOrder class list
            NodeList paintOrder = ((Element)root.getElementsByTagName("paintOrder")
                                    .item(0)).getElementsByTagName("class");
            
            Class[] classes = new Class[paintOrder.getLength()];
            for (int i = 0; i < classes.length; i++) {
                String className = ((Element)paintOrder.item(i)).getAttribute("name");
                classes[i] = Class.forName(className);
            }
            
            realWorld.setPaintOrder(classes);
        }
        catch (Exception e) {
        }

        // Determine which objects appear on top of other objects
        //setPaintOrder(Actor.class, Zombie.class, ZombieDetector.class);
        
        // Populate the world
    }
    
    /**
     * Create a ZombieLand with a given size;
     */
    public ZombieLand(int width, int height, int cellSize)
    {
        super(width, height, cellSize);
    }
    
    /**
     * Check the status of the Zombies every frame
     */
    public void act()
    {
        if (!done) {
            if (checkZombies() ) {
                if (checkGoal()) {
                }
            }
        }
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
    public boolean checkZombies()
    {    
        if (!done) {
            List<Zombie> zombies = getObjects(Zombie.class);

            if (zombies.size() == 0) {
                finish("Zombie no more.", false);
                return false;
            }
            else {
                boolean allDead = true;
                for (Zombie z : zombies) {
                    if (!z.isDead()) {
                        allDead = false;
                    }
                }
                
                if (allDead) {
                    finish("Zombie dead.", false);
                    return false;
                }
            }
        }
        return true;
    }
    
    /**
     * Determine if the goal has been reached
     */
    public boolean checkGoal()
    {
        List<Actor> actors = getObjects(null);
        List<GoalObject> state = new ArrayList<GoalObject>();
        
        for (Actor a : actors) {
            GoalObject gObj = new GoalObject();
            gObj.a = a;
            gObj.name = a.getClass().getName();
            gObj.x = a.getX();
            gObj.y = a.getY();
            
            if (!gObj.name.contains("$")) {
                boolean duplicate = false;
                
                for (int i = 0; i < state.size(); i++) {
                    GoalObject o = state.get(i);
                    
                    if (o.name.equals(gObj.name) &&
                        o.x == gObj.x &&
                        o.y == gObj.y) {
                            duplicate = true;
                            o.count = o.count + 1;
                            break;
                    }
                }
                
                if (!duplicate) {
                    state.add(gObj);
                }
            }
        }
        
        if (goal != null && state.size() == goal.size()) {
            if (state.containsAll(goal)) {
                finish("Zombie do good.", true);
                return true;
            }
        }
        
        return false;
    }
    
    private class GoalObject {
        public String name;
        public int count = 1;
        public int x;
        public int y;
        public List<String[]> calls;
        public Actor a;
        
        public boolean equals(Object o) {
            if (o instanceof GoalObject) {
                GoalObject other = (GoalObject)o;
                
                boolean calls = true;
                
                if (this.calls != null) {
                    if (this.name.equals(other.name)) {
                        for (String[] methodCall : this.calls) {
                            String methodName = methodCall[0];
                            try {
                                Class c = Class.forName(this.name);
                                Method m = c.getMethod(methodName, null);
                                
                                String rval = m.invoke(other.a, null).toString();
                                
                                if (!rval.equals(methodCall[1])){
                                    calls = false;
                                }
                            }
                            catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                
                return this.name.equals(other.name) &&
                       this.x == other.x &&
                       this.y == other.y &&
                       this.count == other.count &&
                       calls == true;
            }
            return false;
        }
    }
}