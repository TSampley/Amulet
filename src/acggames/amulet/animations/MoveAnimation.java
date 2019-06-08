package acggames.amulet.animations;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import acggames.amulet.Actor;
import acggames.amulet.Location;
import acggames.amulet.Stack;
import acggames.amulet.Tile;
import acggames.amulet.display.AmuletView;
import acggames.amulet.display.WorldScreen;
import android.graphics.Point;
import android.view.View;

/**
*
* @author fuzzybunie
*/
public class MoveAnimation extends Animation {

   private int frames, halfFrames;
   private double x, y, sx, sy;
   private Point target;
   private Location previous, next;
   private Actor actor;
   private int count;
   /**
    *
    */
   public boolean focus;

   public MoveAnimation() {
       
   }
   /**
    *
    * @param act
    * @param s
    * @param f
    */
   public MoveAnimation(Actor act, Location l, boolean f) {
       x = act.getX();
       y = act.getY();
       actor = act;
       next = l;
       target = WorldScreen.area().getStack(l).getPosition();
       focus = f;
       frames = AmuletView.getFrameRate();
       halfFrames = frames/2;
   }

   /**
    *
    */
   @Override
   public void start() {
       super.start();
       previous = actor.getStack().getLocation();
       Point p = WorldScreen.area().getStack(previous).getPosition();
       double tx = (p.x+target.x)/2, ty;
       
       int lDif = next.getY()-previous.getY();
       if (lDif>0) {
           actor.getStack().remove(actor);
           WorldScreen.area().getStack(next).put(actor);
           if (WorldScreen.area().getStack(next).getHeight() >
           WorldScreen.area().getStack(previous).getHeight()) {
               ty = target.y;
               if (lDif == 1)
                   ty-=Tile._diagYStep/2;
               else
                   ty-=Tile._diagYStep;
           } else {
               ty = p.y;
               if (lDif == 1)
                   ty+=Tile._diagYStep/2;
               else
                   ty+=Tile._diagYStep;
           }
       } else if (WorldScreen.area().getStack(next).getHeight() >
       		  WorldScreen.area().getStack(previous).getHeight()) {
           ty = target.y;
           if (lDif == -1)
               ty+=Tile._diagYStep/2;
           else
               ty+=Tile._diagYStep;
       } else {
           ty = p.y;
           if (lDif == -1)
               ty-=Tile._diagYStep/2;
           else
               ty-=Tile._diagYStep;
       }
       
       sx = (tx - x)/halfFrames;
       sy = (ty - y)/halfFrames;
       count = frames;
       if (WorldScreen.area().getStack(previous).getTop().getId() != Tile.WATER)
           actor.setAnimation(Actor.ANIMATION.MOVE);
   }

   /**
    *
    */
   public void step() {
       if (count==halfFrames){
           sx = (target.x - x)/halfFrames;
           sy = (target.y - y)/halfFrames;
           if (WorldScreen.area().getStack(next).getTop().getId() == Tile.WATER)
               actor.setAnimation(Actor.ANIMATION.SWIM);
           else
               actor.setAnimation(Actor.ANIMATION.MOVE);
       }
       x+=sx;
       y+=sy;
       actor.setPosition((int)x, (int)y);
       if (focus)
    	   WorldScreen.area().setFocus((int)x, (int)y);
       hasFinished = count-- <= 0;
   }

   /**
    *
    */
   @Override
   public void interupt() {
       if (hasFinished)
           return;
       super.finish();
       if (next.getY()>previous.getY()) {
    	   WorldScreen.area().getStack(next).remove(actor);
    	   WorldScreen.area().getStack(previous).put(actor);
       }
       if (WorldScreen.area().getStack(previous).getTop().getId() != Tile.WATER)
           actor.setAnimation(Actor.ANIMATION.STAND);
       target = WorldScreen.area().getStack(previous).getPosition();
       actor.setPosition(target.x, target.y);
   }

   /**
    *
    */
   @SuppressWarnings("unused")
   @Override
   public void finish() {
       super.finish();
       Stack nex = WorldScreen.area().getStack(next), prev = WorldScreen.area().getStack(previous);
       if (next.getY()<previous.getY()) {
           actor.getStack().remove(actor);
           WorldScreen.area().getStack(next).put(actor);
       }
       if (WorldScreen.area().getStack(next).getTop().getId() != Tile.WATER)
           actor.setAnimation(Actor.ANIMATION.STAND);
       actor.setPosition(target.x, target.y);
       WorldScreen.area().getStack(previous).exited(actor);
       WorldScreen.area().getStack(next).entered(actor);
   }

   /**
    *
    * @return
    */
   public View getUI() {
       return null;
   }

   @Override
   public MoveAnimation clone() {
       return null;
   }

   @Override
   public void writeExternal(ObjectOutput out) throws IOException {
       super.writeExternal(out);
       out.writeInt(frames);
//       private Actor act;
//       private int count;
//       public boolean focus;
   }

   @Override
   public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
       super.readExternal(in);
//       private final int FRAMES, HALF_FRAMES;
//       private double x, y, sx, sy;
//       private Point target;
//       private Stack previous, stack;
//       private Actor act;
//       private int count;
//       public boolean focus;
   }
}
