package acggames.amulet.behaviors;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import acggames.amulet.Actor;
import acggames.amulet.Alterable;
import android.view.View;

/**
*
* @author fuzzybunie
*/
public abstract class Behavior implements Alterable, Externalizable {
   /**
    *
    */
   protected Actor subject;
   /**
    *
    * @param a
    */
   public Behavior(Actor a) {
       subject = a;
   }
   /**
    *
    */
   public abstract void act();

   public void setSubject(Actor a) {
       subject = a;
   }
   /**
    *
    * @return
    */
   public abstract View getUI();
   @Override
   public abstract Behavior clone();

   public void writeExternal(ObjectOutput out) throws IOException {}

   public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {}
}
