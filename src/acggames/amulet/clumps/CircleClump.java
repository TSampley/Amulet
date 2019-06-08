package acggames.amulet.clumps;

import java.util.LinkedList;
import java.util.Queue;
import java.util.TreeSet;

import acggames.amulet.Location;
import acggames.amulet.Stack;

/**
*
* @author Taush Sampley
*/
public class CircleClump extends Clump {

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
                   int newM = m - 1;
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

   /**
    *
    * @param stacks
    * @param stack
    * @param mag
    * @param dir
    */
   public void circleHelper(TreeSet<Stack> stacks, Stack stack, int mag, int dir) {
       if(mag >= 0)
       {
           stacks.add(stack);
           for(int i = 0, r=(dir+4)%6; i < Location.R_360; i++, r=(r+1)%6)
           {
               Stack temp = stack.getAdjacent(r);
               if(!(temp == null || stacks.contains(temp)))
                   circleHelper(stacks, temp, mag - 1, r);
           }
       }
   }
}

