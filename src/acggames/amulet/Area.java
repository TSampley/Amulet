package acggames.amulet;

import java.io.BufferedWriter;
import java.io.Externalizable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.TreeSet;

import acggames.amulet.animations.Animation;
import acggames.amulet.display.AmuletView;
import acggames.amulet.display.WorldScreen;
import acggames.amulet.effects.Effect;
import acggames.amulet.effects.EmptyEffect;
import acggames.amulet.processors.InputProcessor;
import acggames.amulet.processors.WanderingProcessor;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.PointF;
import android.util.Log;

public class Area implements Externalizable {


    /**
     *
     */
    public static InputProcessor command;
    /**
     *
     */
    protected static java.util.Stack<InputProcessor> commandStack;
    /**
     *
     */
    public static double rigidity = .2;
    private static Point center = new Point(AmuletView.dim.x/2, AmuletView.dim.y/2);
    private final LinkedList<Animation> animations, blockingAnimations;
    private LinkedList<Team> teams;
    private Stack[][] terrain;
    private Point focus;
    private PointF cameraFocus;
    private Canvas untouched;

    private Effect<Stack> effect;
    private Location effectLocation;
    private String name;

    private Area() {
        focus = new Point();
        cameraFocus = new PointF();
        untouched = new Canvas();
        untouched.setBitmap(AmuletView.getBitmap());
        animations = new LinkedList<Animation>();
        blockingAnimations = new LinkedList<Animation>();
        teams = new LinkedList<Team>();
        effect = new EmptyEffect<Stack>();
        effectLocation = new Location(0,0);
        name = "default Area Name. this should not be here";
    }

    private Area(String f) {
        this();
        try {
            Scanner scan = new Scanner(new File(f));
            terrain = new Stack[scan.nextInt()][scan.nextInt()];
            scan.nextLine();
            for (int i = 0; i < terrain[0].length; i++) {
                for (int j = 0; j < terrain.length; j++) {
                    Scanner line = new Scanner(scan.nextLine());
                    Location loc = new Location(j, i);
                    Stack stack = new Stack(loc);
                    while (line.hasNext())
                        stack.addTile(new Tile(line.nextInt()));
                    terrain[j][i] = stack;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        compileTerrain();
    }

    /**
     *
     * @param x
     * @param y
     */
    public Area(int x, int y) {
        this();
        terrain = new Stack[x][y];
        for (int i = 0; i < x; i++)
            for (int j = 0; j < y; j++) {
                terrain[i][j] = new Stack(new Location(i, j));
                terrain[i][j].addTile(new Tile(Tile.GRASS));
                terrain[i][j].addTile(new Tile((int)(Math.random()*11)));
            }
    }

    /**
     *
     * @param page
     */
    public void draw(Canvas page) {
        // SMOOTH TRANSITION of focus
        double dx = focus.x-cameraFocus.x, dy = focus.y-cameraFocus.y;
        cameraFocus.x+=dx*rigidity;
        cameraFocus.y+=dy*rigidity;
        int x = (int)(cameraFocus.x/Tile._sideStep), y = (int)(cameraFocus.y/Tile._diagYStep);
        int lx = x-4, rx = x+5, ty = y-12, by = y+20;
        if (lx < 0)
            lx = 0;
        if (rx > terrain.length)
            rx = terrain.length;
        if (ty < 0)
            ty = 0;
        if (by > terrain[0].length)
            by = terrain[0].length;
        page.save();
        // move the Graphics context to center the image on the screen and look
        //     at the focus area of the image.
        page.translate(center.x-cameraFocus.x, center.y-cameraFocus.y);
        // adjust the starting point based on the first row drawn
        page.translate(lx*Tile._sideStep+(ty%2==0?0:Tile._diagXStep), ty*Tile._diagYStep);
        page.save();
        // start at the back and go row by row
        for (int i = ty; i < by; i++) {
            // create a duplicate of page to prevent permanent changes
        	page.save();
            // start at the left and draw to the right
            for (int j = lx; j < rx; j++) {
                // terrain should be initialized and filled entirely with non-null elements
                if (terrain[j][i].enabled())
                    terrain[j][i].draw(page, untouched);
                // translate over to the position of the next Tile in the row.
                page.translate(Tile._sideStep, 0);
            }
            // release the context. Because it's good to...
            page.restore();

            // translate down and left or right, depending on the row.
            page.translate(i%2==0? Tile._diagXStep : -Tile._diagXStep,
                            Tile._diagYStep);
        }
        page.restore();
        
        for (Animation a : animations)
            a.draw(page, untouched);
        for (Animation a : blockingAnimations)
            a.draw(page, untouched);
        command.draw(page, untouched);
        page.restore();
    }

    /**
     *
     */
    public void step() {
        stepHelper(animations);
        stepHelper(blockingAnimations);
        command.step();
    }

    private void stepHelper(final LinkedList<Animation> anis) {
        synchronized (anis) {
            Iterator<Animation> it = anis.iterator();
            Animation an;
            while (it.hasNext()) {
                an = it.next();
                an.step();
                if (an.hasFinished()) {
                    an.finish();
                    it.remove();
                }
            }
        }
    }

    /**
     *
     * @param an
     * @param block
     */
    public void addAnimation(Animation an, boolean block) {
        if (block)
            synchronized (blockingAnimations) {
                blockingAnimations.add(an);
                an.start();
            }
        else
            synchronized (animations) {
                animations.add(an);
                an.start();
            }
    }

    /**
     *
     * @return
     */
    public boolean isNotBlocking() {
        return blockingAnimations.isEmpty();
    }

    /**
     *
     */
    public void tryToSkipAnimations() {
        for (Animation a : blockingAnimations)
            if (a.canSkip())
                a.finish();
    }

    /**
     * Updates Point focus to the x and y coordinates.
     * @param x the x-coordinate of the center of the focus area
     * @param y the y-coordinate of the center of the focus area
     */
    public void setFocus(int x, int y) {
        focus.x = x;
        focus.y = y;
    }

    /**
     *
     * @param x
     * @param y
     */
    public void setCameraFocus(int x, int y) {
        focus.x = (int)(cameraFocus.x = x);
        focus.y = (int)(cameraFocus.y = y);
    }

    /**
     *
     * @return
     */
    public int getFocusX() {
        return focus.x;
    }

    /**
     *
     * @return
     */
    public int getFocusY() {
        return focus.y;
    }

    /**
     * Updates Point center to the x and y coordinates.
     * @param x the x-coordinate of the center of the drawing area
     * @param y the y-coordinate of the center of the drawing area
     */
    public void setCenter(int x, int y) {
        center.x = x;
        center.y = y;
    }

    /**
     * Checks the x and y coordinates of the Location against the dimensions of
     * the matrix. Returns true if the Location is valid.
     *
     * @param loc the location to check against the matrix
     * @return whether or not the location is valid
     * @author Andy and Kevin
     */
    public boolean isValid(Location loc) {
    	int x = loc.getX(),
            y = loc.getY();
    	if(x < terrain.length && x >= 0)
    		if(y < terrain[0].length && y >= 0)
    			return true;
    	return false;
    }

    /**
     *
     * @return
     */
    public int getWidth() {
        return terrain.length;
    }

    /**
     *
     * @return
     */
    public int getHeight() {
        return terrain[0].length;
    }

    /**
     * Searches through the Stack matrix and compares the x and y coordinates
     * against the Stack objects.  Returns the Stack whose top contains the
     * point (x, y) or returns null if no Stack was found.
     *
     * @param x the x coordinate of the mouse
     * @param y the y coordinate of the mouse
     * @param backToFront true means the stacks should be searched from back to
     * front. false means they should be searched from front to back.
     * @return the Stack whose top-most tile contained the x and y coordinates.
     */
         //@AUTHORS EQUALZ STEPHEN AND PATRICK! :D :D :D :D Poopie, tehe!
    public Stack getStack(int x, int y, boolean backToFront) {

        if (backToFront)
        {
            x-= center.x;
            x+= cameraFocus.x;
            y-= center.y;
            y+= cameraFocus.y;

            int testY = y;
            int testX = x;
            int rowFigurer=y;

            for (int j = 0; j < terrain[0].length; j++)
            {
                for(int i = 0; i < terrain.length; i++)
                {
                    testY=rowFigurer;
                    if(Tile.HITBOX.contains(testX,testY))
                    return terrain[i][j];
                    testY-=terrain[i][j].getHeight() * Tile._stackStep;
                    testX-= Tile._sideStep;
                }

                if(j % 2 == 0)
                {
                    rowFigurer-=Tile._diagYStep;
                    testX=x-Tile._diagXStep;
                }
                else
                {
                    rowFigurer-=Tile._diagYStep;
                    testX=x;
                }
            }
        }
        else
        {
            // determine whether you search from back-to-front or front-to-back.
            x-= center.x;
            x+= cameraFocus.x;
            y-= center.y;
            y+= cameraFocus.y;

            // adjust x and y by Point center and Point focus.

            for (int l = 0; l < terrain[0].length; l++)
            {
                y-=Tile._diagYStep;
            }
            x+=Tile._diagXStep;

            int testY=y;
            int testX = x;
            int rowFigurer=y;

            for (int j = terrain[0].length-1; j>=0 ; j--)
            {
                rowFigurer+=Tile._diagYStep;
                if(j % 2 == 1)
                {
                    testX=x-Tile._sideStep;
                }
                else
                {
                    testX=x-Tile._diagXStep;
                }

                for(int i = 0; i < terrain.length; i++)
                {
                    testY=rowFigurer-Tile._stackStep;
                    testY+=terrain[i][j].getHeight() * Tile._stackStep;
                    if(Tile.HITBOX.contains(testX,testY))
                    return terrain[i][j];

                    testX-= Tile._sideStep;
                }
            }
        }

        return null;
    }

    /**
     *
     * @param loc
     * @return
     */
    public Stack getStack(Location loc) {
    	return terrain[loc.getX()][loc.getY()];
    }

    /**
     *
     * @return
     */
    public LinkedList<Team> getTeams() {
        return teams;
    }

    public Team[] teamsAsArray() {
        return (Team[])teams.toArray();
    }

    /**
     *
     * @param field
     * @return
     */
    public TreeSet<Stack> enclose(TreeSet<Stack> field) {
    	TreeSet<Stack> enclosure = new TreeSet<Stack>();
    	for(Stack[] s: terrain)
            enclosure.addAll(Arrays.asList(s));
        enclosure.removeAll(field);
    	return enclosure;
    }

    /**
     *
     */
    public void interuptAnimations() {
        interuptAnimationsHelper(animations);
        interuptAnimationsHelper(blockingAnimations);
    }

    /**
     * 
     * @param anis
     */
    private void interuptAnimationsHelper(final LinkedList<Animation> anis) {
        synchronized (anis) {
            for (Animation a : anis)
                a.interupt();
            anis.clear();
        }
    }
    
    /**
     *
     */
    public void addLeft() {
        Stack[][] temp = new Stack[terrain.length+1][terrain[0].length];

        for (int i = 0; i < terrain[0].length; i++)
            temp[0][i] = new Stack(new Location(0, i));

        for (int i = 1; i < temp.length; i++)
            temp[i] = terrain[i-1];

        terrain = temp;
        cameraFocus.x+=Tile._sideStep;
        focus.x+=Tile._sideStep;
        compileTerrain();
    }

    /**
     *
     */
    public void addRight() {
        Stack[][] temp = new Stack[terrain.length+1][terrain[0].length];

        for (int i = 0; i < terrain[0].length; i++)
            temp[terrain.length][i] = new Stack(new Location(terrain.length, i));
        System.arraycopy(terrain, 0, temp, 0, terrain.length);

        terrain = temp;
        compileTerrain();
    }

    /**
     *
     */
    public void addTop() {
        Stack[][] temp = new Stack[terrain.length][terrain[0].length+2];

        for (int i = 0; i < terrain.length; i++) {
            for (int j = 0; j < 2; j++)
                temp[i][j] = new Stack(new Location(0, 0));
            for (int j = 2; j < temp[0].length; j++)
                temp[i][j] = terrain[i][j-2];
        }

        terrain = temp;
        cameraFocus.y+=Tile._diagYStep*2;
        focus.y+=Tile._diagYStep*2;
        compileTerrain();
    }

    /**
     *
     */
    public void addBottom() {
        Stack[][] temp = new Stack[terrain.length][terrain[0].length+2];

        for (int i = 0; i < terrain.length; i++) {
            System.arraycopy(terrain[i], 0, temp[i], 0, terrain[0].length);
            for (int j = terrain[0].length; j < temp[0].length; j++)
                temp[i][j] = new Stack(new Location(0, 0));
        }

        terrain = temp;
        compileTerrain();
    }

    /**
     *
     */
    public void trim() {
        int sx, ex, sy, ey;
        sx = ex = sy = ey = 0;
        loop1:
        for (int i = 0; i < terrain.length; i++)
            for (int j = 0; j < terrain[0].length; j++)
                if (terrain[i][j].getHeight() != 0) {
                    sx = i;
                    break loop1;
                }
        loop2:
        for (int i = terrain.length-1; i >= 0; i--)
            for (int j = 0; j < terrain[0].length; j++)
                if (terrain[i][j].getHeight() != 0) {
                    ex = i+1;
                    break loop2;
                }
        loop3:
        for (int i = 0; i < terrain[0].length; i++)
            for (int j = 0; j < terrain.length; j++)
                if (terrain[j][i].getHeight() != 0) {
                    sy = i;
                    break loop3;
                }
        sy=(sy/2)*2;
        loop4:
        for (int i = terrain[0].length-1; i >= 0; i--)
            for (int j = 0; j < terrain.length; j++)
                if (terrain[j][i].getHeight() != 0) {
                    ey = i+1;
                    break loop4;
                }
        Stack[][] grid = new Stack[ex-sx][ey-sy];
        for (int i  = sx; i < ex; i++)
            for (int j = sy; j < ey; j++)
                grid[i-sx][j-sy] = terrain[i][j];
        terrain = grid;
        compileTerrain();
    }

    /**
     *
     */
    public final void compileTerrain() {
        for (int x = 0; x < terrain.length; x++)
            for (int y = 0; y < terrain[0].length; y++) {
                terrain[x][y].setLocation(new Location(x, y));
                for (int i = 0; i < Location.R_360; i++) {
                    Location l = terrain[x][y].getLocation().getAdjacentLocation(i);
                    if (isValid(l))
                        terrain[x][y].setNeighbor(i, getStack(l));
                }
                terrain[x][y].compile();
            }
    }

    /**
     *
     * @param n
     */
    public void setName(String n) {
        name = n;
    }

    /**
     *
     * @param ef
     */
    public void setEffect(Effect<Stack> ef) {
        effect = ef;
    }

    /**
     *
     * @param loc
     */
    public void setEffectLocation(Location loc) {
        effectLocation = loc;
    }

    /**
     *
     */
    public void runStartEffect() {
        if (effect != null)
            if (effectLocation == null)
                effect.affect(terrain[0][0]);
            else
                effect.affect(getStack(effectLocation));
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        int w = terrain.length,
            h = terrain[0].length;
        out.writeInt(w);
        out.writeInt(h);
        for (int i = 0; i < w; i++)
            for (int j = 0; j < h; j++) {
                terrain[i][j].writeExternal(out);
                System.out.println(terrain[i][j]);
            }
        out.writeInt(teams.size());
        for (Team t : teams)
            t.writeExternal(out);
        WorldScreen.writeExternalClass(out, effect);
        out.writeInt(effectLocation.getX());
        out.writeInt(effectLocation.getY());
        out.writeUTF(name);
        System.out.println("Area Saved");
    }

    @SuppressWarnings("unchecked")
	@Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        terrain = new Stack[in.readInt()][in.readInt()];
        for (int i = 0, w = terrain.length, h = terrain[0].length; i < w; i++)
            for (int j = 0; j < h; j++) {
                terrain[i][j] = new Stack(new Location(i, j));
                terrain[i][j].readExternal(in);
                System.out.println(terrain[i][j]);
            }
        int teamSize = in.readInt();
        teams = new LinkedList<Team>();
        for (int i = 0; i < teamSize; i++) {
            Team t = new Team();
            t.readExternal(in);
            teams.add(t);
        }
        effect = (Effect<Stack>)WorldScreen.readExternalClass(in);
        effectLocation = new Location(in.readInt(), in.readInt());
        name = in.readUTF();
    }
    
    /**
     *
     * @param cp
     */
    public static void addProcessor(InputProcessor cp) {
        commandStack.push(command);
        command = cp;
        System.out.println("addProcessor(); active: " + cp);
    }

    /**
     *
     */
    public static void removeProcessor() {
        command = commandStack.pop();
        System.out.println("removeProcessor(); active:" + command);
    }

    /**
     *
     */
    public static void printProcessorStack() {
        System.out.println(command);
        Iterator<InputProcessor> it = commandStack.iterator();
        while (it.hasNext())
        	Log.v(AmuletActivity.D_OTHER, "\t"+it.next());
    }

    /**
     *
     * @param fileName
     * @param a
     * @return
     */
    public static boolean save(String fileName, Area a) {
        try {
            if (fileName.endsWith(".old")) {
                FileWriter fw = new FileWriter(fileName);
                BufferedWriter bw = new BufferedWriter(fw);
                bw.write(a.terrain.length + " " + a.terrain[0].length + "\n");
                for (int y = 0; y < a.terrain[0].length; y++)
                    for (int x = 0; x < a.terrain.length; x++) {
                        Stack s = a.terrain[x][y];
                        for (int i = 0, e = a.terrain[x][y].getHeight(); i < e; i++)
                            bw.write(s.getTile(i).getId()+" ");
                        bw.write("\n");
                    }
                bw.close();
                fw.close();
            } else {
                FileOutputStream fos = new FileOutputStream(fileName);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                a.writeExternal(oos);
                oos.close();
                fos.close();
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     *
     * @param fileName
     * @return
     */
    public static Area load(String fileName) {
        try {
            if (fileName.endsWith(".old")) {
                return new Area(fileName);
            } else {
                FileInputStream fis = new FileInputStream(fileName);
                ObjectInputStream ois = new ObjectInputStream(fis);
                Area a = new Area();
                a.readExternal(ois);
                ois.close();
                fis.close();
                return a;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    static {
        commandStack = new java.util.Stack<InputProcessor>();
        command = new WanderingProcessor();
    }
}
