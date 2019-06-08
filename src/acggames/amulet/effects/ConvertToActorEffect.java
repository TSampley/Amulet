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
public class ConvertToActorEffect extends Effect<Stack> {
   
   private Effect<Actor> effect;

   /**
    *
    */
   public ConvertToActorEffect() {
       effect = new EmptyEffect<Actor>();
   }

   /**
    *
    * @param e
    */
   public ConvertToActorEffect(Effect<Actor> e) {
       effect = e;
   }

   /**
    *
    * @param s
    */
   public void affect(Stack s) {
       for (Actor a : s.getActors())
           effect.affect(a);
   }

   /**
    *
    * @param s
    */
   @Override
   public void removeEffect(Stack s) {
       for (Actor a : s.getActors())
           effect.removeEffect(a);
   }

   /**
    *
    * @return
    */
   public View getUI() {
       return null;
   }

   public ConvertToActorEffect clone() {
       return new ConvertToActorEffect(effect.clone());
   }

   public void writeExternal(ObjectOutput out) throws IOException {
       WorldScreen.writeExternalClass(out, effect);
   }

   public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
       effect = (Effect<Actor>)WorldScreen.readExternalClass(in);
   }
}

