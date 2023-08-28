package acggames.amulet.processors;

import java.util.TreeSet;

import acggames.amulet.Actor;
import acggames.amulet.Stack;
import acggames.amulet.items.ExternalUseItem;
import android.database.Cursor;

/**
*
* @author fuzzybunie
*/
public class AttackActorProcessor extends ExternalUseProcessor<Actor> {
   /**
    *
    * @param it
    */
   public AttackActorProcessor(ExternalUseItem<Actor> it) {
       super(it);
   }

   public Cursor getCursor() {
       return null;
   }

   /**
    *
    * @param stacks
    */
   @Override
   public void affect(TreeSet<Stack> stacks) {
       for (Stack s : stacks)
           for (Actor a : s.getActors())
               item.getEffect().affect(a);
   }
}

