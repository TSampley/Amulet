package acggames.amulet.clumps;

import java.util.LinkedList;
import java.util.Queue;
import java.util.TreeSet;

import acggames.amulet.Stack;

/**
*
* @author Taush Sampley
*/
public class MovementClump extends Clump {

   /**
    *
    * @param start
    * @param dir
    * @param mag
    * @return
    */
   public TreeSet<Stack> get(Stack start, int dir, int mag) {
       TreeSet<Stack> stacks = new TreeSet<Stack>();
       Queue<Stack> queue = new LinkedList<Stack>();
       Queue<Integer> mags = new LinkedList<Integer>();

       stacks.add(start);
       queue.add(start);
       mags.add(mag);
       while (!queue.isEmpty()) {
           Stack stack = queue.remove();
           int m = mags.remove();
           for (int i = 0; i < 6; i++) {
               Stack a = stack.getAdjacent(i);
               if (!(a == null || stacks.contains(a)) && a.enabled()) {
                   int newM = m;
                   switch (stack.getHeight() - a.getHeight()) {
                       case -1:
                       case 0:
                       case 1:
                           newM--;
                       default:
                           newM-=2;
                   }
                   if (newM>=0) {
                       stacks.add(a);
                       queue.add(a);
                       mags.add(newM);
                   }
               }
           }
       }

       return stacks;
   }
}

