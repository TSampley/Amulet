package acggames.amulet.triggers;

import acggames.amulet.Actor;
import acggames.amulet.effects.Effect;
import acggames.amulet.effects.EmptyEffect;
import android.view.View;

/**
*
* @author fuzzybunie
*/
public class LocalTrigger extends Trigger {

   /**
    *
    */
   public LocalTrigger() {
       super(new EmptyEffect<Actor>(), 0, 0, (byte)0);
   }
   /**
    *
    * @param ef
    * @param typef
    * @param tf
    */
   public LocalTrigger(Effect<Actor> ef, int typef, int tf, byte sf) {
       super(ef, typef, tf, sf);
   }
   /**
    *
    * @param a
    */
   @Override
   public void entered(Actor a)
   {
       effect.affect(a);
   }


   /**
    *
    * @param a
    */
   @Override
   public void exited(Actor a)
   {
       effect.removeEffect(a);
   }

   /**
    *
    * @return
    */
   @Override
   public View getUI() {
       return null;
   }

   //STEPHEN SANDERS 4/27/11
   @Override
   public LocalTrigger clone()
   {
       return new LocalTrigger(effect,getTypeFlags(),getTargetFlags(), getSpaceFlags());
   }
}

