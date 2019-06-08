package acggames.amulet;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.TreeSet;

import acggames.amulet.animations.MoveAnimation;
import acggames.amulet.behaviors.Behavior;
import acggames.amulet.behaviors.EmptyBehavior;
import acggames.amulet.conditions.Condition;
import acggames.amulet.conditions.Condition.Type;
import acggames.amulet.display.WorldScreen;
import acggames.amulet.items.Item;
import acggames.amulet.items.Weapon;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;

public final class Actor extends Placeable {

    private static final String[] ANI_NAMES = {"stand", "move", "swim", "primary", "secondary", "use", "recoil", "dead"};
    private static final String[] MOODS = {"default", "angry", "sad", "happy", "confused", "shocked"};

    /**
     * A collection of indices for the <code>images</code> 3-d BufferedImage
     * array in <code>Placeable</code>.
     *
     * These indices correspond to elements in Actor's <code>private static
     * final String[] ANI_NAMES</code>.
     * @see Placeable
     */
    public static interface ANIMATION {
        /**
         * index for stand animation
         */
        public static final int STAND = 0;
        /**
         * index for move animation
         */
        public static final int MOVE = 1;
        /**
         * index for swim animation
         */
        public static final int SWIM = 2;
        /**
         * index for primary action animation
         */
        public static final int PRIMARY = 3;
        /**
         * index for secondary action animation
         */
        public static final int SECONDARY = 4;
        /**
         * index for use animation
         */
        public static final int USE = 5;
        /**
         * index for recoil animation
         */
        public static final int RECOIL = 6;
        /**
         * index for dead animation
         */
        public static final int DEAD = 7;
    }
    /**
     * A collection of indices for the currentStats and normalStats arrays.
     */
    public static interface STAT {
        /**
         * index of health stat
         */
        public static final int HEALTH = 0;
        /**
         * index of max health stat
         */
        public static final int MAX_HEALTH = 1;
        /**
         * index of mana stat
         */
        public static final int MANA = 2;
        /**
         * index of max mana stat
         */
        public static final int MAX_MANA = 3;
        /**
         * index of attack stat
         */
        public static final int ATTACK = 4;
        /**
         * index of defense stat
         */
        public static final int DEFENSE = 5;
        /**
         * index of accuracy stat
         */
        public static final int ACCURACY = 6;
        /**
         * index of vision stat
         */
        public static final int VISION = 7;
        /**
         * index of stamina stat
         */
        public static final int STAMINA = 8;
        /**
         * index of movement stat
         */
        public static final int MOVEMENT = 9;
        /**
         * index of speed stat
         */
        public static final int SPEED = 10;
        /**
         * index of action points stat
         */
        public static final int ACTION_POINTS = 11;
    }
    /**
     * A collection of indices for the damageResistances array.
     */
    public static interface DAMAGE {
        /**
         * index for fire damage resistance
         */
        public static final int FIRE = 0;
        /**
         * index for ice damage resistance
         */
        public static final int ICE = 1;
        /**
         * index for water damage resistance
         */
        public static final int WATER = 2;
        /**
         * index for earth damage resistance
         */
        public static final int EARTH = 3;
        /**
         * index for wind damage resistance
         */
        public static final int WIND = 4;
        /**
         * index for poison damage resistance
         */
        public static final int POISON = 5;
        /**
         * index for melee damage resistance
         */
        public static final int MELEE = 6;
        /**
         * index for ranged damage resistance
         */
        public static final int RANGED = 7;
        /**
         * index for magic damage resistance
         */
        public static final int MAGIC = 8;
        /**
         * index for pierce damage resistance
         */
        public static final int PIERCE = 9;
        /**
         * index for slash damage resistance
         */
        public static final int SLASH = 10;
        /**
         * index for blunt damage resistance
         */
        public static final int BLUNT = 11;
    }
    /**
     * A collection of indices for the currentStatuses and normalStatuses arrays.
     */
    public static interface STATUS {
        /**
         *
         */
        public static final int INVULNERABLE = 0;
        /**
         *
         */
        public static final int EXPLODE_ON_DEATH = 1;
        /**
         *
         */
        public static final int CONFUSION = 2;
        /**
         *
         */
        public static final int ZOMBIE = 3;
    }
    /**
     * The bottom line for health.  A character whose health is below this can
     * not be healed by a normal healing spell - they must be resurrected by a
     * resurrectionEffect.
     */
    public static final int OVER_KILL = -35; // 35?... change?...

    private transient Bitmap[] portraits;
    private transient Bitmap icon;
    private int[] currentStats;
    private int[] normalStats;
    private boolean[] currentStatuses;
    private boolean[] normalStatuses;
    private int[] resistances;
    private Team team;
    private Behavior behavior;
    private LinkedList<Condition> conditions;
    private TreeSet<Item> items;
    private Grid itemGrid;
    private Weapon equipped;

    private transient String className;
    
    public Actor() {
        this(0, Placeable.SPACE.GROUND);
    }

    /**
     *
     * @param typef
     * @param sf
     */
    public Actor(int typef, byte sf) {
        this(0, 0, 0, typef | Placeable.TYPE.ACTOR, sf);
    }
    
    /**
     *
     * @param an
     * @param d
     * @param f
     * @param tf
     * @param sf
     */
    public Actor(int an, int d, int f, int tf, byte sf) {
        super(an, d, f, tf, sf);
        setSpritePath("actors/default");
        currentStats = new int[12];
        normalStats = new int[12];
        currentStatuses = new boolean[4];
        normalStatuses = new boolean[4];
        resistances = new int[12];
        behavior = new EmptyBehavior(this);
        conditions = new LinkedList<Condition>();
        items = new TreeSet<Item>();
        itemGrid = new Grid(5, 2);
        equipped = new Weapon();
        equipped.setOwner(this);
        setStat(STAT.HEALTH, 10);
        setStat(STAT.ACTION_POINTS, 10);
        setStat(STAT.MOVEMENT, 5);
    }

    /**
     *
     */
    public void act() {
        behavior.act();
    }

	 // edit - Korey Doss
    /**
     *
     */
    public void reset() {
        System.arraycopy(normalStats, 0, currentStats, 0, normalStats.length);
        System.arraycopy(normalStatuses, 0, currentStatuses, 0, normalStatuses.length);
    }

	 // author - Korey Doss
    /**
     *
     */
    public void updateConditions() {
        ListIterator<Condition> list = conditions.listIterator();
        while(list.hasNext()) {
            Condition cond = list.next();
            cond.alter(this);
            if (cond.step())
                list.remove();
        }
    }

	 // author - Korey Doss
    /**
     *
     * @param i
     */
    public void giveItem(Item i) {
        items.add(i);
        i.setOwner(this);
    }

	 // author - Korey Doss
    /**
     *
     * @param i
     */
    public void takeItem(Item i) {
    	items.remove(i);
    	i.setOwner(null);
    }

    /**
     *
     * @param i
     * @return
     */
    public boolean hasItem(Item i) {
        return items.contains(i);
    }

    /**
     *
     * @param w
     */
    public void equip(Weapon w) {
        equipped = w;
    }

    /**
     *
     * @return
     */
    public Weapon getWeapon() {
        return equipped;
    }

    /**
     *
     * @return
     */
    public Grid getItemGrid() {
        return itemGrid;
    }

	 // author - Korey Doss
    /**
     *
     * @param id
     * @param value
     */
    public void setStat(int id, int value) {
    	currentStats[id] = value;
    }

	 // author - Korey Doss
    /**
     *
     * @param id
     * @return
     */
    public int getStat(int id) {
    	return currentStats[id];
    }

    // author - Korey Doss
    /**
     *
     * @param id
     * @param existance
     */
    public void setStatus(int id, boolean existance) {
    	currentStatuses[id] = existance;
    }

    // author - Korey Doss
    /**
     *
     * @param id
     * @return
     */
    public boolean getStatus(int id) {
    	return currentStatuses[id];
    }

    public void setNormStatus(int statusId, boolean val) {
        normalStatuses[statusId] = val;
    }

    public boolean getNormStatus(int statusId) {
        return normalStatuses[statusId];
    }

    /**
     *
     * @param id
     * @param value
     */
    public void setNormStat(int id, int value)
    {
    	normalStats[id] = value;
    }

    /**
     *
     * @param id
     * @return
     */
    public int getNormStat(int id)
    {
    	return normalStats[id];
    }

    /**
     *
     * @param s
     */
    public void addCondition(Condition s) {
        if (s.getType() == Condition.Type.BURN)
            removeCondition(Condition.Type.FREEZE);
        else if (s.getType() == Condition.Type.FREEZE)
            removeCondition(Condition.Type.BURN);
        else
            conditions.add(s);
    }

    /**
     *
     * @param t
     */
    public void removeCondition(Type t)
    {
    	Iterator<Condition> iter = conditions.iterator();
    	while(iter.hasNext())
    	{
            if(iter.next().getType() == t)
                iter.remove();
    	}
    }

    /**
     *
     * @param dmg
     * @param id
     * @param damageTypes
     */
    public void damage(int dmg, int id, int[] damageTypes) {
        // r = resistance to a given damage type
        // weakness = r < 0
        // resistance = r > 0
        // boon = r > 100
        for (int dt : damageTypes) {
            dmg = dmg*(100-resistances[dt])/100;
        }
        currentStats[id] -= dmg;
    }

    /**
     *
     * @param t
     */
    public void setTeam(Team t) {
        team = t;
    }

    /**
     *
     * @param b
     */
    public void setBehavior(Behavior b) {
        behavior = b;
    }

    /**
     *
     * @param dir
     * @return
     */
    public MoveAnimation attemptToMove(int dir) {
        setDirection(dir);
        Stack adj = getStack().getAdjacent(dir);
        if (adj != null && adj.getHeight() > 0 && adj.enabled() && adj.canTake(this)) {
            if(getSpaceFlags() == SPACE.AIR)
                return new MoveAnimation(this, adj.getLocation(), false);
            else if(Math.abs(getStack().getHeight() - adj.getHeight()) < 3)
                return new MoveAnimation(this, adj.getLocation(), false);
        }
        return null;
    }

    public Actor clone() {
        return new Actor();
    }

    /**
     *
     * @return
     */
    @Override
    public View getUI() {
        return null;
    }

    /**
     *
     * @return
     */
    public Team getTeam() {
        return team;
    }

    /**
     *
     * @return
     */
    public Bitmap[] getPortraits() {
        return portraits;
    }

    /**
     *
     * @return
     */
    public Bitmap getIcon() {
        return icon;
    }

    /**
     *
     * @param path
     * @return
     */
    public Bitmap[][][] loadPath(String path) {
        loadPortrait(path);
        return loadHelper(path, ANI_NAMES);
    }

    /**
     *
     * @param path
     */
    public void loadPortrait(String path) {
    	AssetManager assets = AmuletActivity.singleton.getAssets();
    	
        try {
        	InputStream is = assets.open(path+"/small.png");
            icon = BitmapFactory.decodeStream(is);
            is.close();
            portraits = new Bitmap[MOODS.length];
            for (int i = 0; i < MOODS.length; i++) {
            	is = assets.open(path+"/"+MOODS[i]+".png");
                portraits[i] = BitmapFactory.decodeStream(is);
                is.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        assets.close();
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        super.writeExternal(out);
// <editor-fold defaultstate="collapsed" desc="currentStats, normalStats, currentStatuses, normalStatuses, resistances">
        out.writeInt(currentStats.length);
        for (int i = 0; i < currentStats.length; i++)
            out.writeInt(currentStats[i]);
        out.writeInt(normalStats.length);
        for (int i = 0; i < normalStats.length; i++)
            out.writeInt(normalStats[i]);
        out.writeInt(currentStatuses.length);
        for (int i = 0; i < currentStatuses.length; i++)
            out.writeBoolean(currentStatuses[i]);
        out.writeInt(normalStatuses.length);
        for (int i = 0; i < normalStatuses.length; i++)
            out.writeBoolean(normalStatuses[i]);
        out.writeInt(resistances.length);
        for (int i = 0; i < resistances.length; i++)
            out.writeInt(resistances[i]);// </editor-fold>
        WorldScreen.writeExternalClass(out, behavior);
        out.writeInt(conditions.size());
        for (Condition c : conditions)
        	WorldScreen.writeExternalClass(out, c);
        out.writeInt(items.size());
        for (Item i : items)
        	WorldScreen.writeExternalClass(out, i);
        itemGrid.writeExternal(out);

        out.writeUTF(className);
        if (getStack() == null)
            out.writeBoolean(false);
        else {
            out.writeBoolean(true);
            Location loc = getStack().getLocation();
            out.writeInt(loc.getX());
            out.writeInt(loc.getY());
        }
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        super.readExternal(in);
        for (int i = 0, e = in.readInt(); i < e; i++)
            currentStats[i] = in.readInt();
        for (int i = 0, e = in.readInt(); i < e; i++)
            normalStats[i] = in.readInt();
        for (int i = 0, e = in.readInt(); i < e; i++)
            currentStatuses[i] = in.readBoolean();
        for (int i = 0, e = in.readInt(); i < e; i++)
            normalStatuses[i] = in.readBoolean();
        for (int i = 0, e = in.readInt(); i < e; i++)
            resistances[i] = in.readInt();
        behavior = (Behavior)WorldScreen.readExternalClass(in);
        for (int i = 0, e = in.readInt(); i < e; i++)
            conditions.add((Condition)WorldScreen.readExternalClass(in));
        for (int i = 0, e = in.readInt(); i < e; i++)
            items.add((Item)WorldScreen.readExternalClass(in));
        itemGrid.readExternal(in);
        itemGrid.readExternalAreaHelper(this);

        className = in.readUTF();
        if (in.readBoolean())
            setStack(new Stack(new Location(in.readInt(), in.readInt())));
        else
            setStack(new Stack(new Location(0, 0)));
    }
}
