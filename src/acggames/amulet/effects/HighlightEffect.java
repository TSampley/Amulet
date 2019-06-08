package acggames.amulet.effects;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import acggames.amulet.Stack;
import android.view.View;

/**
*
*/
public class HighlightEffect extends Effect<Stack> {

   private int color;

   public HighlightEffect() {
       this(0xffff0000);
   }

   public HighlightEffect(int c) {
       color = c;
   }

   /**
    *
    * @param s
    */
   public void affect(Stack s) {
       s.highlight(color);
   }

   /**
    *
    * @param s
    */
   public void removeAffect(Stack s) {
       s.highlight(0);
   }

   public HighlightEffect clone() {
       return new HighlightEffect(color);
   }

   /**
    *
    * @return
    */
   public View getUI() {
       return null;
   }

   public void writeExternal(ObjectOutput out) throws IOException {
       out.writeInt(color);
   }

   public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
       color = in.readInt();
   }
}

