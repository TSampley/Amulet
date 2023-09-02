package acggames.amulet.processors;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import acggames.amulet.Actor;
import android.database.Cursor;

/**
*
* @author fuzzybunie
*/
public class BerserkProcessor extends TeamProcessor {
   /**
    *
    */
   public static final int MELEE = 0;
   /**
    *
    */
   public static final int RANGED = 1;
   /**
    *
    */
   public static final int SPELLS = 2;

   private Actor actor;
   private int mode;

   public BerserkProcessor() {
       super(null);
   }

   /**
    *
    * @param a
    * @param m
    */
   public BerserkProcessor(Actor a, int m) {
       super(null);
       actor = a;
       mode = m;
   }

   public Cursor getCursor() {
       return null;
   }
   
   @Override
   public void writeExternal(ObjectOutput out) throws IOException {
       out.writeInt(mode);
   }

   @Override
   public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
       mode = in.readInt();
   }
}

