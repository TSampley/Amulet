package acggames.amulet;

import java.io.Externalizable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import acggames.amulet.items.Weapon;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.util.Log;

public abstract class Placeable implements Alterable, Comparable<Placeable>, Externalizable {

    /**
     * A collection of flags to be stored in <code>typeFlags</code>.
     * These are the flag values for the typeFlags variable
     */
    public static interface TYPE {
        /**
         * flag value for nothing. This is just here for the sake of
         * consistency.  There is really no reason for this other than that..
         */
        public static final int NOTHING = 0x0;
        /**
         * flag value for Actors
         */
        public static final int ACTOR = 0x1;
        /**
         * flag value for Triggers
         */
        public static final int TRIGGER = 0x2;
        /**
         * flag value for Useables
         */
        public static final int USEABLE = 0x4;
        /**
         * flag value for an Actor which can only use the SWORD weapon type.
         * @see Weapon
         */
        public static final int SWORDSMAN = 0x8;
        public static final int ARCHER = 0x10;
        public static final int MAGE = 0x20;
        public static final int SPEARMAN = 0x40;
        public static final int MACEMAN = 0x80;
        public static final int THIEF = 0x100;
        public static final int AXEMAN = 0x200;
        public static final int MONK = 0x400;
        public static final int F = 0x800;
        public static final int G = 0x1000;
        public static final int H = 0x2000;
        public static final int I = 0x4000;
        public static final int J = 0x8000;
        public static final int K = 0x10000;
        public static final int L = 0x20000;
        public static final int M = 0x40000;
        public static final int N = 0x80000;
    }
    /**
     * These are the flag values for the spaceFlags variable
     */
    public static interface SPACE {
        public static final byte NOTHING = 0x0;
        public static final byte GROUND = 0x1;
        public static final byte AIR = 0x2;
        public static final byte GHOST = 0x4;
        public static final byte OVERRIDE = 0x8;
    }

    private static int interval = 5;

    private int count;
    private Bitmap[][][] images;
    private String spritePath;
    private Stack myStack;
    private int animation;
    private int direction;
    private int frame;
    private int typeFlags;
    private byte spaceFlags;
    private Point offset;
    private Point origin;
    
    /**
     *
     */
    public Placeable() {
        this(0, (byte)0);
    }

    /**
     *
     * @param tf
     */
    public Placeable(int tf, byte sf) {
        this(0, 0, 0, tf, sf);
    }

    /**
     *
     * @param an
     * @param d
     * @param f
     * @param tf
     */
    public Placeable(int an, int d, int f, int tf, byte sf) {
        myStack = null;
        animation = an;
        direction = d;
        frame = f;
        typeFlags = tf;
        spaceFlags = sf;
        count = interval;
        origin = new Point();
        offset = new Point();
    }

// mac 3/1/11
    /**
     *
     * @return
     */
    public int getTypeFlags() {

        return typeFlags;
    }

    /**
     *
     * @param tf
     */
    public void setTypeFlags(int tf) {
        typeFlags = tf;
    }

    /**
     *
     * @param t
     */
    public void setType(int t) {
        typeFlags = t;
    }

    /**
     *
     * @param path
     */
    public final void setSpritePath(String path) {
        spritePath = path;
        images = loadPath(path);
        updateOffset();
    }

    /**
     *
     * @param path
     * @return
     */
    public abstract Bitmap[][][] loadPath(String path);

    /**
     *
     * @param a
     */
    public void setAnimation(int a) {
        frame = 0;
        animation = a;
    }

    /**
     *
     * @param d
     */
    public void setDirection(int d) {
        frame = 0;
        direction = d%Location.R_360;
    }
    
    /**
     *
     * @param f
     */
    public void setFrame(int f) {
        frame = f;
    }

    /**
     *
     * @return
     */
    public int getDirection() {
        return direction;
    }

    /**
     *
     * @return
     */
    public int getX() {
        return origin.x;
    }

    /**
     *
     * @return
     */
    public int getY() {
        return origin.y;
    }

    /**
     *
     * @return
     */
    public final Point getOffset() {
        return offset;
    }

    /**
     *
     */
    public final void updateOffset() {
        offset = new Point(-images[direction][animation][0].getWidth()/2, -images[direction][animation][0].getHeight()/2);
    }

    /**
     *
     * @param x
     * @param y
     */
    public void setPosition(int x, int y) {
        origin.x = x;
        origin.y = y;
    }

    /**
     *
     * @param s
     */
    public void setStack(Stack s) {
        myStack = s;
    }

    /**
     *
     * @return
     */
    public Stack getStack() {
        return myStack;
    }

    /**
     * increments the frame counter.
     *
     * Author: Daniel and Kaler
     */
    public void step() {
        if (--count<=0) {
    	  	if (++frame>=images[direction][animation].length)
    	  		frame = 0;
    	  	count = interval;
    	  }
    }
    
    /**
     * returns true if it has been set to the last frame of animation
     * 
     * @return true if the last frame of animation will be drawn when
     * draw(Graphics2D) is called
     * 
     * Author: Daniel and Kaler
     */
    public boolean animationDone() {
    	  return frame == images[direction][animation].length - 1;
    }

    /**
     *
     * @return
     */
    public byte getSpaceFlags() {
        return spaceFlags;
    }

    /**
     *
     * @param sf
     */
    public void setSpaceFlags(byte sf) {
        spaceFlags = sf;
    }
    /**
     *
     * @param a
     * @param b
     * @return
     */
    //Authors: Stephen and Patrick 4/6/11 - Edited: Kaler and Daniel
    // further edited by fuzzybunie
    public static boolean canMerge(Placeable a, Placeable b) {
    //a is being moved onto b
        if ((a.getSpaceFlags()& SPACE.OVERRIDE) !=0)
            return true;
   	if (((a.getSpaceFlags()&SPACE.GHOST) == (b.getSpaceFlags()&SPACE.GHOST))) {
   		if((a.getSpaceFlags() & SPACE.GROUND) == SPACE.GROUND && (b.getSpaceFlags() | SPACE.GROUND) == b.getSpaceFlags())
                    return false;
                else if((a.getSpaceFlags() & SPACE.AIR) == SPACE.AIR && (b.getSpaceFlags() | SPACE.AIR) == b.getSpaceFlags())
                    return false;
                else
                    return true;
   	} else
            return true;
    }

    //written by Kaler and Daniel
    public int compareTo (Placeable p) {
    	if(spaceFlags - p.spaceFlags == 0 && this != p)
    		return 1;
    	return spaceFlags - p.spaceFlags;
    	//if the result is negative, the first object should be drawn first
    	//otherwise the second placeable will be drawn first;
    }

    /**
     *
     * @param ref
     */
    public void draw(Canvas ref) {
        step();
        ref.drawBitmap(images[direction][animation][frame], origin.x+offset.x, origin.y+offset.y, null);
    }

    @Override
    public abstract Placeable clone();

    /**
     *
     * @param i
     */
    public static void setInterval(int i) {
        interval = i;
    }

    /**
     *
     * @param path
     * @param aniNames
     * @return
     */
    protected static Bitmap[][][] loadHelper(String path, String[] aniNames) {
        Bitmap[][][] temp = new Bitmap[6][aniNames.length][];

    	AssetManager assets = AmuletActivity.singleton.getAssets();
        for (int d = 0; d < 6; d++)
            for (int an = 0; an < aniNames.length; an++) {
                String[] frames = new File(path+"/"+d+"/"+aniNames[an]).list();
                if (frames == null) {
                	Log.v(AmuletActivity.D_OTHER, path+"/"+d+"/"+aniNames[an] + "  does not exist");
                    continue;
                }
                Bitmap[] frameTemp = new Bitmap[frames.length];
                for (int f = 0; f < frames.length; f++)
                    try {
                    	InputStream is = assets.open(frames[f]);
                        frameTemp[f] = BitmapFactory.decodeStream(is);
                        is.close();
                    } catch (Exception e) {
                        System.out.println(frames[f]);
                        e.printStackTrace();
                    }
                temp[d][an] = frameTemp;
            }

        return temp;
    }

    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeUTF(spritePath);
        out.writeInt(animation);
        out.writeInt(direction);
        out.writeInt(frame);
        out.writeInt(typeFlags);
        out.writeByte(spaceFlags);
        out.writeInt(origin.x);
        out.writeInt(origin.y);
    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        setSpritePath(in.readUTF());
        animation = in.readInt();
        direction = in.readInt();
        frame = in.readInt();
        typeFlags = in.readInt();
        spaceFlags = in.readByte();
        origin.x = in.readInt();
        origin.y = in.readInt();
    }
}
