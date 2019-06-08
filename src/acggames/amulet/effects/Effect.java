package acggames.amulet.effects;

import java.io.Externalizable;

import acggames.amulet.Alterable;

/**
*
* @param <E>
* @author fuzzybunie
*/
public abstract class Effect<E> implements Alterable, Externalizable {
   /**
    *
    * @param e
    */
   public abstract void affect(E e);
   /**
    *
    * @param e
    */
   public void removeEffect(E e) {}
   @Override
   public abstract Effect<E> clone();
   @Override
   public String toString() {
       return getClass().getSimpleName();
   }
}
