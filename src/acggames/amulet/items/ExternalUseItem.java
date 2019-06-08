package acggames.amulet.items;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import acggames.amulet.clumps.CircleClump;
import acggames.amulet.clumps.Clump;
import android.view.View;

public abstract class ExternalUseItem<E> extends Item<E> {
    /**
     *
     */
    public int max;
    /**
     *
     */
    public int min;
    /**
     *
     */
    public Clump clump;
    /**
     *
     */
    public int magnitude;

    /**
     *
     */
    public ExternalUseItem() {
        max = 1;
        min = 1;
        clump = new CircleClump();
        magnitude = 2;
    }

    public String toString() {
        return getClass().getCanonicalName();
    }

    /**
     * 
     * @return
     */
    public View getUI() {
        return null;
    }

    public ExternalUseItem<E> clone() {
        return null;
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        super.writeExternal(out);
        out.writeInt(max);
        out.writeInt(min);
        out.writeInt(magnitude);
        out.writeObject(clump);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        super.readExternal(in);
        max = in.readInt();
        min = in.readInt();
        magnitude = in.readInt();
        clump = (Clump)in.readObject();
    }
}

