
package acggames.amulet.behaviors;

import acggames.amulet.Actor;
import android.view.View;

/**
*
*/
public class EmptyBehavior extends Behavior {
   /**
    *
    */
   public EmptyBehavior() {
       super(null);
   }

   /**
    *
    * @param a
    */
   public EmptyBehavior(Actor a) {
       super(a);
   }

   /**
    *
    */
   public void act() {}
   
   /**
    *
    * @return
    */
   public View getUI() {
       return null;
   }

   public EmptyBehavior clone() {
       return new EmptyBehavior();
   }
}
