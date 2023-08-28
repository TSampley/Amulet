package acggames.amulet.items;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;

import acggames.amulet.Actor;
import acggames.amulet.Alterable;
import acggames.amulet.Area;
import acggames.amulet.clumps.CircleClump;
import acggames.amulet.clumps.Clump;
import acggames.amulet.display.WorldScreen;
import acggames.amulet.effects.Effect;
import acggames.amulet.effects.EmptyEffect;
import acggames.amulet.processors.AttackActorProcessor;
import android.view.View;

/**
 * @author fuzzybunie + Stephen Sanders
 */
public class Weapon extends ActorItem implements Alterable, Serializable {

    public static enum Type {
        SWORD, BOW, STAFF, SPEAR, MACE, KNIFE, AXE, FISTS;
    }

    private Type weaponType;
    private int eMax;
    private int eMin;
    private Clump eClump;
    private int eMag;
    private Effect<Actor> eEffect;
    private boolean currentState; //true=ranged, false=melee

    /**
     *
     */
    public Weapon() {
        weaponType = Type.SWORD;

        eMax = 6;
        eMin = 3;
        eClump = new CircleClump();
        eMag = 2;
        eEffect = new EmptyEffect<Actor>();
        
        currentState = false;
    }

    /**
     * Adds a melee processor to the current Area
     * @param state
     */
    public void attack(boolean state) {
        if(state==currentState)
            Area.addProcessor(new AttackActorProcessor(this));
        else
        {
            swapStats();
            currentState=state;
            Area.addProcessor(new AttackActorProcessor(this));
        }
    }
    
    /**
     *
     */
    public void swapStats()
    {
        Clump tempClump=super.clump;
        int tempMag=super.magnitude;
        int tempMax=super.max, tempMin=super.min;
        Effect<Actor> tempEffect=super.effect;

        super.clump=eClump;
        super.magnitude=eMag;
        super.effect=eEffect;
        super.max=eMax;
        super.min=eMin;

        eClump=tempClump;
        eMag=tempMag;
        eMax=tempMax;
        eMin=tempMin;
        eEffect=tempEffect;
    }



    /**
     * This is the standard Item method.  This is what is called if the Weapon
     * is used from the inventory
     */
    @Override
    public void use() {
        // put self in weapon slot
    }

    @Override
    public String toString() {
        return "";
    }

    @Override
    public Weapon clone() {
        return null;
    }

    /**
     *
     * @return
     */
    @Override
    public View getUI() {
        return null;
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        super.writeExternal(out);
        out.writeObject(weaponType);
        out.writeInt(eMax);
        out.writeInt(eMin);
        out.writeObject(eClump);
        out.writeInt(eMag);
        WorldScreen.writeExternalClass(out, eEffect);
        out.writeBoolean(currentState);
    }

    @SuppressWarnings("unchecked")
	@Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        super.readExternal(in);
        weaponType = (Type)in.readObject();
        eMax = in.readInt();
        eMin = in.readInt();
        eClump = (Clump)in.readObject();
        eMag = in.readInt();
        eEffect = (Effect<Actor>)WorldScreen.readExternalClass(in);
        currentState = in.readBoolean();
    }
    
    @SuppressWarnings("rawtypes") 
    @Override
    public int compareTo(Item i) {
        if (i == this)
            return 0;
        else
            return -1;
    }
}

