package acggames.amulet.animations;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import acggames.amulet.Alterable;
import android.graphics.Canvas;

/**
*
* @author fuzzybunie
*/
public abstract class Animation implements Alterable, Externalizable {

   /**
    *
    */
   protected boolean canSkip;
   /**
    *
    */
   protected boolean hasStarted;
   /**
    *
    */
   public boolean hasFinished;

   /**
    *
    */
   public Animation() {
       this(true);
   }

   /**
    *
    * @param s
    */
   public Animation(boolean s) {
       canSkip = s;
       hasFinished = false;
       hasStarted = false;
   }

   /**
    *
    */
   public void start() {
    hasStarted = true;
   }

   /**
    *
    * @param page
    * @param untouched
    */
   public void draw(Canvas page, Canvas untouched) {}

   /**
    *
    */
   public abstract void step();

   /**
    *
    */
   public void interupt() {}

   /**
    *
    */
   public void finish() {
       hasFinished = true;
   }

   /**
    *
    * @param b
    */
   public void setCanSkip(boolean b) {
     canSkip = b;
   }

   /**
    *
    * @return
    */
   public boolean canSkip() {
       return canSkip;
   }

   /**
    *
    * @return
    */
   public boolean hasStarted() {
       return hasStarted;
   }

   /**
    *
    * @return
    */
   public boolean hasFinished() {
       return hasFinished;
   }

   @Override
   public abstract Animation clone();

   public void writeExternal(ObjectOutput out) throws IOException {
       out.writeBoolean(canSkip);
   }

   public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
       canSkip = in.readBoolean();
   }
}

