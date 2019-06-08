package acggames.amulet.display;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.TreeMap;

import acggames.amulet.Actor;
import acggames.amulet.Area;
import acggames.amulet.Location;
import acggames.amulet.Placeable.SPACE;
import acggames.amulet.Placeable.TYPE;
import acggames.amulet.Stack;
import acggames.amulet.Team;
import acggames.amulet.Tile;
import acggames.amulet.behaviors.EmptyBehavior;
import acggames.amulet.behaviors.RandomBehavior;
import acggames.amulet.effects.BattleStartEffect;
import acggames.amulet.effects.ConvertToStackEffect;
import acggames.amulet.effects.HighlightEffect;
import acggames.amulet.processors.WanderingProcessor;
import acggames.amulet.triggers.LocalTrigger;
import acggames.amulet.useables.Useable;
import android.graphics.Canvas;

public class WorldScreen extends Screen {
    private static String gameName;
    private static String saveName;
    private static int sky = 0x00000000;

    private static Area area;
    private static TreeMap<String, String> variables;

    /**
     *
     */
    public WorldScreen() {
        this(1, 4);
    }

    /**
     *
     * @param w
     * @param h
     */
    public WorldScreen(int w, int h) {
        area = new Area(w, h);
        area.setCenter(AmuletView.dim.x/2, AmuletView.dim.y/2);
        int x = Tile._diagXStep*(area.getWidth()+1)/2, y = Tile._diagYStep*(area.getHeight())/2;
        area.setFocus(x, y);
        variables = new TreeMap();
    }

    /**
     *
     * @param startName
     */
    public WorldScreen(String startName) {
        area = Area.load(startName);
        area.setCenter(AmuletView.dim.x/2, AmuletView.dim.y/2);
        area.setFocus(Tile._diagXStep*(area.getWidth()+1)/2, Tile._diagYStep*(area.getHeight())/2);
        variables = new TreeMap();
        
        Actor a = new Actor(TYPE.ACTOR, SPACE.GROUND);
        area.getStack(new Location(1, 3)).put(a);
        a.getStack().compile();
        ((WanderingProcessor)Area.command).setup(a);
        setupScenario(a);
    }

    // TODO remove when done with testing
    /**
     *
     * @param leader
     */
    public final void setupScenario(Actor leader) {
        ArrayList<Team> teams = new ArrayList<Team>();
        Team t = new Team(leader);
        teams.add(t);
        leader.setBehavior(new EmptyBehavior(leader));

        Actor a = new Actor(TYPE.ACTOR, SPACE.GROUND);
        a.setBehavior(new RandomBehavior(a));
        area.getStack(new Location(3, 14)).put(a);
        Team enemies = new Team(a);
        teams.add(enemies);
        a = new Actor(TYPE.ACTOR, SPACE.GROUND);
        a.setBehavior(new RandomBehavior(a));
        area.getStack(new Location(2, 13)).put(a);
        enemies.add(a);
        a = new Actor(TYPE.ACTOR, SPACE.GROUND);
        a.setBehavior(new RandomBehavior(a));
        area.getStack(new Location(3, 12)).put(a);
        enemies.add(a);

        area.getTeams().add(t);
        area.getTeams().add(enemies);

        Stack s = area.getStack(new Location(1, 5));
        s.put(new LocalTrigger(new ConvertToStackEffect(new BattleStartEffect<Stack>(new Location(0, 0), teams)), 0, TYPE.ACTOR, SPACE.NOTHING));

        Useable u = new Useable(new ConvertToStackEffect(new HighlightEffect()), 0, SPACE.GROUND, TYPE.ACTOR);
        area.getStack(new Location(1, 6)).put(u);

        area.compileTerrain();
    }

    public void drawCurrentFrame(Canvas page, Canvas untouched) {
        area.draw(untouched);
        page.drawColor(sky);
    }

    /**
     *
     */
    public void step() {
        area.step();
        Tile.step();
    }

    public void setup() {
    }

    public void cleanup() {
    }
    
    /**
     *
     * @param a
     */
    public static void setArea(Area a) {
        area = a;
    }

    /**
     *
     * @return The Area object that is the current focus of World.
     */
    public static Area area() {
        return area;
    }

    /**
     *
     * @param c
     * @param f
     */
    public static void setSkyColor(int c) {
        sky = c;
    }

	//@author Daniel
    /**
     *
     * @param name
     * @param val
     */
    public static void setVariable(String name, String val) {
        variables.put(name, val);
    }
    
    /**
     *
     * @param name
     * @return The String value of the variable specified by the key
     * <code>name</code> or null if the key mapping does not exist.
     */
    //@author Daniel
    public static String getVariable(String name) {
    	return variables.get(name);
    }

    /**
     *
     * @param g
     */
    public static void setGame(String g) {
        gameName = g;
    }

    /**
     *
     * @return The name of the folder in the "Games" folder that was last loaded
     * or null if a game has not yet been loaded.
     */
    public static String getGame() {
        return gameName;
    }

    public static void saveGame() {
        // TODO saving:
        // save current Area to Saves folder
        // save gameName, vars, hero's team, new WarpPoint for their location
    }

    public static void loadGame() {
        
    }

    public static void writeExternalClass(ObjectOutput out, Externalizable e) throws IOException {
        out.writeObject(e.getClass());
        e.writeExternal(out);
    }

    @SuppressWarnings("rawtypes")
	public static Externalizable readExternalClass(ObjectInput in) throws IOException, ClassNotFoundException {
        Class c = (Class) in.readObject();
        Constructor[] cons = c.getConstructors();
        try {
            for (Constructor con : cons)
                if (con.getParameterTypes().length == 0) {
                    Externalizable it = (Externalizable) con.newInstance();
                    it.readExternal(in);
                    return it;
                }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(0);
        }
        return null;
    }

    static {
    	sky = 0x00000000;
        gameName = "";
        saveName = null;
    }
}
