package acggames.amulet.conditions;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import acggames.amulet.Actor;
import acggames.amulet.Alterable;

/**
*
*/
public abstract class Condition implements Alterable, Externalizable {
   /**
    *
    */
   public static enum Type {
       /**
        *
        */
       BERSERK,
       /**
        *
        */
       BURN,
       /**
        *
        */
       FREEZE,
       /**
        *
        */
       IMMOBILIZE,
       /**
        *
        */
       MANALEAK,
       /**
        *
        */
       POISON,
       /**
        *
        */
       SLOW;
   }
   /**
    *
    */
   public final Type type;
   /**
    *
    */
   protected int base;
   /**
    *
    */
   protected int rand;
   /**
    *
    */
   protected int count;

   /**
    *
    * @param b
    * @param r
    * @param t
    */
   public Condition(int b, int r, Type t) {
       base = b;
       rand = r;
       type = t;
   }
   
   /**
    * This is the method through which Condition objects take effect. This is
    * called on every Condition object with an Actor at the beginning of their
    * turn
    *
    * @param a the Actor object whose attributes should be altered
    */
   public abstract void alter(Actor a);

   /**
    *
    * @param a
    */
   public void start(Actor a) {
       count = base + (int)(rand*(Math.random()+1));
   }

   // @author Steven Cao
   /**
    * 
    * @return
    */
   public boolean step() {
       if(count > 0)
       {
           count--;
           return false;
       }
       return true;
   }

   /**
    *
    * @return
    */
   public Type getType() {
       return type;
   }

   @Override
   public abstract Condition clone();

   public void writeExternal(ObjectOutput out) throws IOException {
       out.writeInt(base);
       out.writeInt(rand);
       out.writeInt(count);
   }

   public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
       base = in.readInt();
       rand = in.readInt();
       count = in.readInt();
   }
}
