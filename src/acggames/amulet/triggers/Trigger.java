package acggames.amulet.triggers;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import acggames.amulet.Actor;
import acggames.amulet.Placeable;
import acggames.amulet.display.WorldScreen;
import acggames.amulet.effects.Effect;
import android.graphics.Bitmap;
import android.view.View;

/**
*
*/
public abstract class Trigger extends Placeable {
   /**
    *
    */
   private static final String[] ANI_NAMES = {"active", "inactive", "progress", "digress"};

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
    * The effect to use when an actor activates this trigger.
    */
   protected Effect<Actor> effect;
   
   private int targetFlags;

   /**
    * 
    */
   public Trigger() {
       super(0, (byte)0);
   }

   /**
    *
    * @param ef
    * @param typef
    * @param tf
    */
   public Trigger(Effect<Actor> ef, int typef, int tf, byte sf) {
       super(typef | Placeable.TYPE.TRIGGER, sf);
       effect = ef;
       targetFlags = tf;
       setSpritePath("triggers/default");
   }

   @Override
   public View getUI() {
       return null;
   }
   //STEPHEN SANDERS 4/27/11
   /**
    *
    * @return
    */
   public int getTargetFlags()
   {
   	return targetFlags;
   }

   /**
    *
    * @param a
    */
   public void entered(Actor a) {}

   /**
    *
    * @param a
    */
   public void exited(Actor a) {}

	// Patrick and Stephen 4/6/11
   /**
    *
    * @param a
    * @return
    */
   public boolean targets(Actor a) {
       return 0 != (a.getTypeFlags()&targetFlags);
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
       out.writeInt(targetFlags);
       WorldScreen.writeExternalClass(out, effect);
   }

   @SuppressWarnings("unchecked")
   @Override
   public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
       super.readExternal(in);
       targetFlags = in.readInt();
       effect = (Effect<Actor>)WorldScreen.readExternalClass(in);
   }
}

