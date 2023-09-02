package acggames.amulet.clumps;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.TreeSet;

import acggames.amulet.Stack;

/**
*
* @author fuzzybunie
*/
public abstract class Clump implements Externalizable {
   /**
    *
    * @param start
    * @param dir
    * @param mag
    * @return
    */
   public abstract TreeSet<Stack> get(Stack start, int dir, int mag);
   public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {}
   public void writeExternal(ObjectOutput out) throws IOException {}
}

