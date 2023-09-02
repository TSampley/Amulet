package acggames.amulet.processors;

import java.util.TreeSet;

import acggames.amulet.Stack;
import acggames.amulet.items.ExternalUseItem;

/**
*
* @param <E>
* @author Daniel @)
* @author fuzzybunie - implementing Generics/restructuring
*/
public abstract class ExternalUseProcessor<E> extends TargetProcessor {
   ExternalUseItem<E> item;
   int dir;
   TreeSet<Stack> stacks;

   /**
    *
    * @param i
    */
   public ExternalUseProcessor(ExternalUseItem<E> i) {
       super();
       item = i;
       dir = item.owner.getDirection();
   }

   /*
   @Override
   public void mousePressed(MouseEvent e) {
		if(stacks != null) {
	           for (Stack s : stacks)
	               s.highlight(null);
	           affect(stacks);
	           Area.removeProcessor();
		}
   }
   */
   
   /**
    *
    * @param s
    */
   @Override
   public final void select(Stack s) {
       if(validStart(s)) {
           int d = item.owner.getStack().getLocation().getDirectionToward(s.getLocation());
           stacks = item.clump.get(s, d, item.magnitude);
           for (Stack st : stacks)
               st.highlight(0xffffff00);
       } else {
           s.highlight(0xffff0000);
       }
   }
   
   /**
    *
    * @param s
    */
   @Override
   public final void deselect(Stack s) {
       s.highlight(0);
       if (stacks != null) {
           for (Stack stack : stacks)
               stack.highlight(0);
           stacks = null;
       }
   }

   /**
    *
    * @param stacks
    */
   public abstract void affect(TreeSet<Stack> stacks);

   private boolean validStart(Stack st) {
       int distance = item.owner.getStack().getLocation().distanceTo(st.getLocation());
       return distance <= item.max && distance >= item.min;
   }
}

