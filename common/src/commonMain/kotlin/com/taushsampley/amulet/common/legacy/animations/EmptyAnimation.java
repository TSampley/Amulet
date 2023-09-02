package acggames.amulet.animations;

import android.view.View;

/**
*
* @author fuzzybunie
*/
public class EmptyAnimation extends Animation {
   /**
    *
    */
   public void step() {
       hasFinished = true;
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
   public EmptyAnimation clone() {
       return null;
   }
}

