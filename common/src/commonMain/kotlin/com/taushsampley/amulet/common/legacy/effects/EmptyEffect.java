package acggames.amulet.effects;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import android.view.View;

/**
*
* @param <E>
* @author fuzzybunie
*/
public class EmptyEffect<E> extends Effect<E> {
   /**
    *
    * @param a
    */
   public void affect(E a) {}
   public EmptyEffect<E> clone() {
       return new EmptyEffect<E>();
   }
   /**
    *
    * @return
    */
   public View getUI() {
       return null;
   }
   /**
    *
    * @return
    */
   public String getDescription() {
       return "Does nothing";
   }

   public void writeExternal(ObjectOutput out) throws IOException {
   }

   public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
   }
}
