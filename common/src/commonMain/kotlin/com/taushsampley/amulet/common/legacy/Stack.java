package acggames.amulet;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.TreeSet;

import acggames.amulet.display.WorldScreen;
import acggames.amulet.triggers.Trigger;
import acggames.amulet.useables.Useable;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

public class Stack {

    private static final int HEIGHTLIMIT = 10;
    /**
     *
     */
    public static final int ACTOR = 0;
    /**
     *
     */
    public static final int TRIGGER = 1;
    /**
     *
     */
    public static final int USEABLE = 2;
    
    private boolean enabled, fog;
    private int startLine, shearLine;
    private Paint highlight;
    private Location loc;
    private Stack[] neighbors;
    private ArrayList<Tile> tiles;
    @SuppressWarnings("rawtypes")
	private TreeSet[] placeables;
    private TreeSet<Placeable> sprites;

    /**
     *
     * @param loc
     */
    public Stack(Location loc) {
        neighbors = new Stack[6];
        tiles = new ArrayList<Tile>(HEIGHTLIMIT);
        placeables = new TreeSet[3];
        placeables[0] = new TreeSet<Actor>();
        placeables[1] = new TreeSet<Trigger>();
        placeables[2] = new TreeSet<Useable>();
        sprites = new TreeSet<Placeable>();
        startLine = 0;
        shearLine = 0;
        enabled = true;
        fog = false;
        this.loc = loc;
    }

    /**
     *
     */
    public void updateVisibility() {
        for (Tile t : tiles) {
            t.setFrontVisible(true);
            t.setLeftVisible(true);
            t.setRightVisible(true);
            t.setTopVisible(true);
        }
        int s = opaqueHeight(this);
        int h = getHeight();
        for (int i = 0; i < s-1; i++)
            getTile(i).setTopVisible(false);
        Stack n = neighbors[Location.I_210];
        if (n != null)
            for (int i = 0, e = opaqueHeight(n); i < e && i < h; i++)
                getTile(i).setLeftVisible(false);
        n = neighbors[Location.I_270];
        if (n != null)
            for (int i = 0, e = opaqueHeight(n); i < e && i < h; i++)
                getTile(i).setFrontVisible(false);
        n = neighbors[Location.I_330];
        if (n != null)
            for (int i = 0, e = opaqueHeight(n); i < e && i < h; i++)
                getTile(i).setRightVisible(false);
        startLine = 0;
        for (Tile t : tiles)
            if (t.isCovered())
                startLine++;
            else
                break;
    }

    /**
     *
     * @param s
     * @return
     */
    public static int opaqueHeight(Stack s) {
        if (s.getHeight()>0 && s.getTop().isClear()) {
            for (int i = s.getHeight()-1; i >= 0; i--)
                if (!s.getTile(i).isClear())
                    return i+1;
            return 0;
        } else
            return s.getHeight();
    }

    // @author Korey
    /**
     *
     * @param v
     */
    public void setEnabled(boolean v) {
        enabled = v;
    }

    // @author Korey
    /**
     *
     * @return
     */
    public boolean enabled() {
        return enabled;
    }

    /**
     *
     * @param f
     */
    public void setFog(boolean f) {
        fog = f;
    }

    /**
     *
     * @return
     */
    public boolean fog() {
        return fog;
    }

    /**
     *
     * @param l
     */
    public void setLocation(Location l) {
        loc = l;
    }

    /**
     *
     * @return
     */
    public Location getLocation() {
        return loc;
    }

    /**
     *
     * @return
     */
    public int getHeight() {
        return tiles.size();
    }

    /**
     *
     * @return
     */
    @SuppressWarnings("unchecked")
	public TreeSet<Actor> getActors() {
        return (TreeSet<Actor>)placeables[ACTOR];
    }
    /**
     *
     * @return
     */
    @SuppressWarnings("unchecked")
	public TreeSet<Useable> getUseables() {
        return (TreeSet<Useable>)placeables[USEABLE];
    }
    /**
     *
     * @return
     */
    @SuppressWarnings("unchecked")
	public TreeSet<Trigger> getTriggers() {
        return (TreeSet<Trigger>)placeables[TRIGGER];
    }
    
    /**
     * @param i The index of the tile to be retreived
     * @return The tile at index i or null if there is no tile there.
     *  Author: Mac 2/22/11
     */
    public Tile getTile(int i) {
        if( i < tiles.size())
            return tiles.get(i);
        else
            return null;
    }

    /**
     * Returns the tile at the top of the stack
     * @return The tile at the top of the stack
     * Korey Doss and Steven Cao
     */
    public Tile getTop() {
        if (getHeight()!=0)
            return tiles.get(getHeight()-1);
        else
            return null;
    }

    /**
     *
     * @param dir
     * @return
     */
    public Stack getAdjacent(int dir) {
        return neighbors[dir];
    }

    /**
     *
     * @param d
     * @param s
     */
    public void setNeighbor(int d, Stack s) {
        neighbors[d] = s;
    }

    /**
     *
     * @return
     */
    public Point getPosition() {
        return new Point(loc.getX()*Tile._sideStep+(loc.getY()%2==0?0:Tile._diagXStep),
                         loc.getY()*Tile._diagYStep - getHeight()*Tile._stackStep);
    }

    /**
     *
     * @param sl
     */
    public void setShearLine(int sl) {
        shearLine = sl;
    }

    /**
     *
     * @param sl
     */
    public void setStartLine(int sl) {
        startLine = sl;
    }

    /**
     * Adds the Tile t to the end of the list.
     *
     * @param t the Tile to be added to the end of the list.
     */
    public void addTile(Tile t) {
        if (getHeight() < HEIGHTLIMIT) {
            tiles.add(t);
            shearLine = tiles.size();
        }
    }

    /**
     * Removes the Tile at the end of the list.
     * Korey Doss and Steven Cao - edit Taush Sampley
     */
    public void removeTile() {
        if (tiles.remove(getTop()))
            shearLine--;
    }

    /**
     * @param c
     * @return
     * @author Stephen and Patrick
     */
    public boolean canTake(Placeable c) {
        for (TreeSet<? extends Placeable> ts : placeables)
            for (Placeable p : ts)
                if (!Placeable.canMerge(c, p))
                    return false;
    	return true;
    }

    /**
     *
     * @param a
     */
    @SuppressWarnings("unchecked")
	public void usedBy(Actor a) {
      	for(Useable canIUse : (TreeSet<Useable>)placeables[USEABLE])
            canIUse.usedBy(a);
    }

    /**
     * 
     * @param a the Actor for the Triggers to target
     */
    @SuppressWarnings("unchecked")
	public void entered(Actor a) {
        for (Trigger t : (TreeSet<Trigger>)placeables[TRIGGER])
            if (t.targets(a))
                t.entered(a);
    }

    /**
     *
     * @param a
     */
    @SuppressWarnings("unchecked")
	public void exited(Actor a) {
        for (Trigger t : (TreeSet<Trigger>)placeables[TRIGGER])
            if (t.targets(a))
                t.exited(a);
    }

    /**
     *
     * @param p
     */
    @SuppressWarnings("unchecked")
	public void put(Placeable p) {
        if (p instanceof Actor)
            placeables[ACTOR].add(p);
        else if (p instanceof Useable)
            placeables[USEABLE].add(p);
        else
            placeables[TRIGGER].add(p);
        sprites.add(p);
        p.setStack(this);
    }

    /**
     * Removes the Actor from the Stack
     *
     * @param p the Actor to remove from the Stack
     * @author Daniel and Kaler @)
     */
    public void remove(Placeable p) {
        if (p instanceof Actor)
            placeables[ACTOR].remove(p);
        else if (p instanceof Useable)
            placeables[USEABLE].remove(p);
        else
            placeables[TRIGGER].remove(p);
        sprites.remove(p);
        p.setStack(this);
    }

    /**
     * Centers all Placeables on this Stack to the center of the Stack and
     * updates the visibility of the Stack.
     */
    public void compile() {
        Point p = getPosition();
        for (Placeable placeable : sprites)
            placeable.setPosition(p.x, p.y);
        updateVisibility();
        tiles.trimToSize();
    }

    /**
     *  Highlights the top of the stack with the desired color. Passing null
     *  removes highlighting from the Stack.
     * @param color the highlight color
     */
    public void highlight(int color) {
    	if (color == 0)
    		highlight = null;
    	else
    		highlight = new Paint(color);
    }

    /**
     *
     * @param page
     * @param ref
     */
    public void draw(Canvas page, Canvas ref) {
        page.translate(0, -Tile._stackStep*startLine);
        for (int i = startLine, e = tiles.size(); i < e; i++) {
            tiles.get(i).draw(page);
            page.translate(0, -Tile._stackStep);
        }
        if (highlight!=null) {
            page.translate(0, Tile._stackStep);
            page.drawPath(Tile.PATH, highlight);
        }
        for (Placeable p : sprites)
            if (!(fog && p instanceof Actor))
                p.draw(ref);
    }

    public int compareTo(Stack temp) {
            if(loc.getX() == temp.loc.getX())
                    return loc.getY() - temp.loc.getY();
            return loc.getX() - temp.loc.getX();
     }

    /**
     *
     * @param end
     * @return
     */
    // @author Daniel and Kaler
    public ArrayList<Stack> getPath(Stack end) {

        Location here = getLocation();
        Location there = end.getLocation();
        ArrayList<Stack> path = new ArrayList<Stack>();

        Area area = WorldScreen.area();
        while(!here.equals(there)) {
                here = here.getAdjacentLocation(here.getDirectionToward(there));
                path.add(area.getStack(here));
        }

        return path;
     }

    @SuppressWarnings("rawtypes")
	@Override
    public String toString() {
        String r = "";
        for (Tile t : tiles)
            r+=t.getId()+" ";
        for (TreeSet set : placeables)
            for (Object o : set)
                r+=o+" ";
        return r;
    }

    @SuppressWarnings("unchecked")
	public void writeExternal(ObjectOutput out) throws IOException {
        out.writeInt(tiles.size());
        for (Tile t : tiles)
            out.writeInt(t.getId());
        out.writeInt(placeables[TRIGGER].size());
        for (Trigger p : (TreeSet<Trigger>)placeables[TRIGGER])
            WorldScreen.writeExternalClass(out, p);
        out.writeInt(placeables[USEABLE].size());
        for (Useable p : (TreeSet<Useable>)placeables[USEABLE])
        	WorldScreen.writeExternalClass(out, p);
    }

    @SuppressWarnings("unchecked")
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        shearLine = in.readInt();
        for (int i = 0; i < shearLine; i++)
            tiles.add(new Tile(in.readInt()));
        for (int i = 0, e = in.readInt(); i < e; i++) {
            Trigger t = (Trigger)WorldScreen.readExternalClass(in);
            t.setStack(this);
            placeables[TRIGGER].add(t);
            sprites.add(t);
        }
        for (int i = 0, e = in.readInt(); i < e; i++) {
            Useable u = (Useable)WorldScreen.readExternalClass(in);
            u.setStack(this);
            placeables[USEABLE].add(u);
            sprites.add(u);
        }
    }
}
