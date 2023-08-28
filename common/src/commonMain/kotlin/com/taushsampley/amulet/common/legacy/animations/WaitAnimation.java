package acggames.amulet.animations;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import android.view.View;

/**
*
*/
public class WaitAnimation extends Animation {
   
   private int duration;

   public WaitAnimation() {
       
   }

   /**
    *
    * @param d
    */
   public WaitAnimation(int d) {
       duration = d;
   }

   /**
    *
    */
   public void step() {
       hasFinished = (--duration <= 0);
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
   public WaitAnimation clone() {
       return null;
   }

   @Override
   public void writeExternal(ObjectOutput out) throws IOException {
       super.writeExternal(out);
       out.writeInt(duration);
   }

   @Override
   public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
       super.readExternal(in);
       duration = in.readInt();
   }
}

