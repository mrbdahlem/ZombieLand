import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

import java.util.List;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.w3c.dom.Attr;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.OutputKeys;
import java.io.File;
import java.lang.reflect.Method;

/**
 * Write a description of class WorldBuilder here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class WorldBuilder extends World
{
    int sizex;
    int sizey;
    int worldIndex;
    List<WorldBuilder> worlds;

    /**
     * Constructor for objects of class WorldBuilder.
     * 
     */
    public WorldBuilder()
    {    
        this(5, 5, 0, new ArrayList<WorldBuilder>(), true);
    }

    public WorldBuilder(int horizsize, int vertsize, int worldIndex, List<WorldBuilder> worlds, boolean add)
    {
        // Create a new world builder
        super(horizsize, vertsize * 2 + 1, 64); 

        sizex = horizsize;
        sizey = vertsize;
        this.worldIndex = worldIndex;
        this.worlds = worlds;

        // If the world is being added to the list
        if (add) {
            // insert it into the list
            this.worlds.add(worldIndex, this);
        }
        else {
            // otherwise, replace the world at the index in the list
            this.worlds.set(worldIndex, this);
        }

        int cellSize = this.getCellSize();

        GreenfootImage img = this.getBackground();
        img.setColor(Color.GRAY);
        img.fillRect(0, sizey * cellSize, sizex * cellSize, cellSize);

    }

    public void saveWorldXML()
    {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = dbf.newDocumentBuilder();
            Document doc = builder.newDocument();

            // create the root element node
            Element scenario = doc.createElement("scenario");
            doc.appendChild(scenario); 
            for (WorldBuilder w : worlds) {
                scenario.appendChild(w.buildWorldTree(doc));
            }

            prettyPrint(doc);
        }
        catch (Exception e) {
        }
    }

    private Element buildWorldTree(Document doc)
    {
        Element world = doc.createElement("world");

        world.setAttribute("width", "" + sizex);
        world.setAttribute("height", "" + sizey);

        Element initial = doc.createElement("initial");
        world.appendChild(initial);

        Element objective = doc.createElement("objective");
        world.appendChild(objective);

        List<Actor> actors = getObjects(null);

        for (Actor a : actors) {
            if (a.getY() < sizey) {
                Element obj = buildObject(doc, a);
                mergeObjectInto(obj, initial);
            }
            else if (a.getY() > sizey) {
                Element obj = buildGoalObject(doc, a);
                mergeObjectInto(obj, objective);
            }
        }

        return world;
    }

    private Element buildObject(Document doc, Actor a)
    {
        Element obj = doc.createElement("object");

        obj.setAttribute("classname", a.getClass().getName());

        Element location = doc.createElement("location");
        location.setAttribute("x", "" + a.getX());
        location.setAttribute("y", "" + a.getY());
        location.setAttribute("dir", "" + a.getRotation());

        obj.appendChild(location);

        return obj;
    }

    private Element buildGoalObject(Document doc, Actor a)
    {
        Element obj = doc.createElement("object");

        obj.setAttribute("classname", a.getClass().getName());

        Element location = doc.createElement("location");
        location.setAttribute("x", "" + a.getX());
        location.setAttribute("y", "" + (a.getY() - (sizey + 1)));
        location.setAttribute("dir", "" + a.getRotation());

        obj.appendChild(location);

        return obj;
    }

    private void mergeObjectInto(Element obj, Element parent)
    {
        NodeList others = parent.getElementsByTagName("object");
        Element newLocation = (Element)obj.getElementsByTagName("location").item(0);
        String newx = newLocation.getAttribute("x");
        String newy = newLocation.getAttribute("y");

        String classname = obj.getAttribute("classname");

        for (int i = 0; i < others.getLength(); i++) {
            Element el = (Element)others.item(i);
            if (el.getAttribute("classname") == classname) {
                NodeList locations = ((Element)others.item(i)).getElementsByTagName("location");

                for (int j = 0; j < locations.getLength(); j++) {
                    Element location = (Element)locations.item(j);

                    if (location.getAttribute("x").equals(newx) &&
                    location.getAttribute("y").equals(newy)) {

                        int count = 2;
                        if (location.hasAttribute("count")) {
                            count = Integer.parseInt(location.getAttribute("count")) + 1;
                        }

                        location.setAttribute("count", "" + count);

                        return;
                    }
                }

                el.appendChild(newLocation);
                return;
            }
        }

        parent.appendChild(obj);
    }

    private static final void prettyPrint(Document xml) throws Exception {
        Transformer tf = TransformerFactory.newInstance().newTransformer();
        tf.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        tf.setOutputProperty(OutputKeys.INDENT, "yes");
        tf.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "3");
        File out = new File("ZombieLand.xml");
        tf.transform(new DOMSource(xml), new StreamResult(out));
    }

    private void loadWorldXML()
    {

    }

    public void resize(int xsize, int ysize)
    {
        // Create a new world with the specified size
        WorldBuilder w = new WorldBuilder(xsize, ysize, worldIndex, worlds, false);

        // move all actors from this world to the new world
        for (Actor a : this.getObjects(Actor.class))
        {
            // Find out the location of the actor
            int x = a.getX();
            int y = a.getY();

            // move the actor to fit in the world
            x = (x < xsize) ? x : xsize - 1;

            // Check if the actor is a part of the problem or a part of the solution
            if (y < sizey)
            {
                // Part of the problem: make sure that actor fits in the world
                y = (y < ysize) ? y : ysize - 1;
            }
            else if (y > sizey)
            {
                // Part of the solution: make sure the actor fits in the solution
                y = y - (sizey + 1);
                y = (y < ysize) ? y : ysize - 1;
                y = y + ysize + 1;
            }
            // move the actor
            w.addObject(a, x, y);            
        }

        // Replace this world with the new world
        Greenfoot.setWorld(w);
    }

    public void nextWorld()
    {
        int nextIndex = worldIndex + 1;
        if (nextIndex >= worlds.size()) {
            WorldBuilder w = new WorldBuilder(sizex, sizey, worlds.size(), worlds, true);
            Greenfoot.setWorld(w);
        }
        else {
            WorldBuilder w = worlds.get(nextIndex);
            w.worldIndex = nextIndex;
            Greenfoot.setWorld(w);
        }
    }

    public void prevWorld()
    {
        int prevIndex = worldIndex - 1;

        if (prevIndex == -1)
        {
            WorldBuilder w = new WorldBuilder(sizex, sizey, 0, worlds, true);
            Greenfoot.setWorld(w);
        }
        else {
            WorldBuilder w = worlds.get(prevIndex);
            w.worldIndex = prevIndex;
            Greenfoot.setWorld(w);
        }
    }
}
