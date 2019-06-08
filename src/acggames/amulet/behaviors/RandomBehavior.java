package acggames.amulet.behaviors;

import java.io.Serializable;

import acggames.amulet.Actor;
import acggames.amulet.Location;
import acggames.amulet.animations.Animation;
import acggames.amulet.animations.EmptyAnimation;
import acggames.amulet.animations.WaitAnimation;
import acggames.amulet.display.WorldScreen;
import android.view.View;

/**
*
*/
public class RandomBehavior extends Behavior implements Serializable {

   private Animation ani;
   private boolean wait;

   /**
    *
    */
   public RandomBehavior() {
       super(null);
   }

   /**
    *
    * @param a
    */
   public RandomBehavior(Actor a) {
       super(a);
       ani = new EmptyAnimation();
       ani.hasFinished = true;
       wait = false;
   }
   
   /**
    *
    */
   public void act() {
       if (ani.hasFinished)
           if (wait) {
               ani = new WaitAnimation((int)(30+Math.random()*30));
               WorldScreen.area().addAnimation(ani, false);
               wait = false;
           } else {
               Animation a = subject.attemptToMove(Location.getDirection(subject.getDirection(),(int)(Math.random()*3-1)));
               if (a != null) {
                   ani = a;
                   WorldScreen.area().addAnimation(ani, false);
               }
               wait = Math.random()<.25;
           }
   }

   /**
    *
    * @return
    */
   public View getUI() {
       return null;
   }

   public RandomBehavior clone() {
       return null;
   }
}

