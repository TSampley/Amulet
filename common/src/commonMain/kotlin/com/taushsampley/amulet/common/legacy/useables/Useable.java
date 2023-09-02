package acggames.amulet.useables;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import acggames.amulet.Actor;
import acggames.amulet.Placeable;
import acggames.amulet.display.WorldScreen;
import acggames.amulet.effects.Effect;
import acggames.amulet.effects.EmptyEffect;
import android.graphics.Bitmap;
import android.view.View;

/**
 * @author fuzzybunie
 */
public class Useable extends Placeable implements Externalizable {
    /**
     *
     */
    private static String[] ANI_NAMES = {"active", "inactive", "progress", "digress"};

    /**
     *
     */
    public static interface ANIMATION {
        /**
         * index for the active animation
         */
        public static final int ACTIVE = 0;
        /**
         * index for the inactive animation
         */
        public static final int INACTIVE = 1;
        /**
         * index for the progress animation
         */
        public static final int PROGRESS = 2;
        /**
         * index for the digress animation
         */
        public static final int DIGRESS = 3;
    }
    /**
     *
     */
    protected Effect<Actor> effect;
    private int userTypeFlags;
    private String errorMessage;

    /**
     *
     */
    public Useable() {
        this(new EmptyEffect<Actor>(), 0, (byte)0, 0);
    }

    /**
     *
     * @param e
     * @param typeF
     * @param sf
     * @param u
     */
    public Useable(Effect<Actor> e, int typeF, byte sf, int u) {
        super(typeF, sf);
        effect = e;
        userTypeFlags = u;
        errorMessage = "";
        setSpritePath("useables/default");
    }

    // @author - Daniel and Kaler @)
    /**
     *
     * @param a
     * @return
     */
    public boolean isUser(Actor a) {
        return (a.getTypeFlags() & userTypeFlags) == userTypeFlags;
    }

    // @author - Patrick and Stephen
    /**
     *
     * @param t
     */
    public final void usedBy(Actor t) {
        if (isUser(t))
            affect(t);
        else
            displayMessage(errorMessage);
    }

    public final int getUserTypeFlags() {
        return userTypeFlags;
    }
    
    /**
     *
     * @param err
     */
    public final void displayMessage(String err) {
        // TODO display error message
    }

    /**
     *
     * @param a
     */
    public void affect(Actor a) {
        effect.affect(a);
    }

    @Override
    public View getUI() {
        return null;
    }

    public Useable clone() {
        Useable u = new Useable(effect, getTypeFlags(), getSpaceFlags(), userTypeFlags);
        u.errorMessage = errorMessage;
        return u;
    }

    /**
     *
     * @param path
     * @return
     */
    public Bitmap[][][] loadPath(String path) {
        return loadHelper(path, ANI_NAMES);
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        super.writeExternal(out);
        WorldScreen.writeExternalClass(out, effect);
        out.writeInt(userTypeFlags);
        out.writeUTF(errorMessage);
    }

    @SuppressWarnings("unchecked")
	@Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        super.readExternal(in);
        effect = (Effect<Actor>)WorldScreen.readExternalClass(in);
        userTypeFlags = in.readInt();
        errorMessage = in.readUTF();
    }
}

