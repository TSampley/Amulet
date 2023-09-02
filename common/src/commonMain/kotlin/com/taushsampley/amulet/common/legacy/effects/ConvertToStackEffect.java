package acggames.amulet.effects;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import acggames.amulet.Actor;
import acggames.amulet.Stack;
import acggames.amulet.display.WorldScreen;
import android.view.View;

/**
*
* @author fuzzybunie
*/
public class ConvertToStackEffect extends Effect<Actor> {
   
   private Effect<Stack> effect;

   /**
    *
    */
   public ConvertToStackEffect() {
       effect = new EmptyEffect<Stack>();
   }

   /**
    *
    * @param e
    */
   public ConvertToStackEffect(Effect<Stack> e) {
       effect = e;
   }

   /**
    *
    * @param s
    */
   public void affect(Actor s) {
       effect.affect(s.getStack());
   }

   /**
    *
    * @param s
    */
   @Override
   public void removeEffect(Actor s) {
       effect.removeEffect(s.getStack());
   }
   
   /**
    *
    * @return
    */
   public View getUI() {
       return null;
   }

   public ConvertToStackEffect clone() {
       return new ConvertToStackEffect(effect.clone());
   }

   public void writeExternal(ObjectOutput out) throws IOException {
       WorldScreen.writeExternalClass(out, effect);
   }

   public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
       effect = (Effect<Stack>)WorldScreen.readExternalClass(in);
   }
}
