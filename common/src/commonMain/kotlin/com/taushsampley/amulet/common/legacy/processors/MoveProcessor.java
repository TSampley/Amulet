package acggames.amulet.processors;

import java.util.TreeSet;

import acggames.amulet.Actor;
import acggames.amulet.Stack;
import acggames.amulet.animations.MoveAnimation;
import acggames.amulet.display.WorldScreen;
import android.database.Cursor;
import android.graphics.Color;

/**
*
* @author fuzzybunie
*/
public class MoveProcessor extends InputProcessor {
	
   /**
    *
    */
   public static final int COST = 5; // this is the action points cost
   private TreeSet<Stack> possMoves;
   private Actor thatGuy;
   private boolean[] dirs = new boolean[]{false, false, false, false, false, false};
   private MoveAnimation ani;

   /**
    *
    * @param poss
    * @param whoIsMoving
    */
   public MoveProcessor(TreeSet<Stack> poss, Actor whoIsMoving) {
       possMoves=poss;
       thatGuy=whoIsMoving;
       for(Stack thisOne : possMoves) {
           thisOne.highlight(Color.MAGENTA);
       }
   }

   /*
  @Override
   public void keyPressed(KeyEvent e) {
       int d = 0;
       switch (e.getKeyCode()) {
           case KeyEvent.VK_E:
               d = 0;
               break;
           case KeyEvent.VK_W:
               d = 1;
               break;
           case KeyEvent.VK_Q:
               d = 2;
               break;
           case KeyEvent.VK_A:
               d = 3;
               break;
           case KeyEvent.VK_S:
               d = 4;
               break;
           case KeyEvent.VK_D:
               d = 5;
               break;
           case KeyEvent.VK_ENTER:
           {//ACTION POINTS DECREMENT!
               thatGuy.setStat(Actor.STAT.ACTION_POINTS, thatGuy.getStat(Actor.STAT.ACTION_POINTS)-5);
               Area.removeProcessor();
                for(Stack thatOne: possMoves)
                    thatOne.highlight(null);
               }
               break;
           case KeyEvent.VK_ESCAPE:
               {
                   thatGuy.getStack().remove(thatGuy);
                   whereHeBeStartin.put(thatGuy);
                   Point pee=whereHeBeStartin.getPosition();
                   thatGuy.setPosition(pee.x,pee.y);
                   World.area().setFocus(pee.x, pee.y);
                Area.removeProcessor();
                for(Stack thatOne: possMoves)
                    thatOne.highlight(null);
               }
           default:
               return;
       }
       dirs[d] = true;
   }

   @Override
   public void keyReleased(KeyEvent e) {
       int d = 0;
       switch (e.getKeyCode()) {
           case KeyEvent.VK_E:
               d = 0;
               break;
           case KeyEvent.VK_W:
               d = 1;
               break;
           case KeyEvent.VK_Q:
               d = 2;
               break;
           case KeyEvent.VK_A:
               d = 3;
               break;
           case KeyEvent.VK_S:
               d = 4;
               break;
           case KeyEvent.VK_D:
               d = 5;
               break;
           default:
               return;
       }
       dirs[d] = false;
   }
   */
   public Cursor getCursor() {
       return null;
   }
   
	/**
	 *
	 */
   	@Override
   	public void step() {
   		if (ani != null) {
   			if (ani.hasFinished())
   				getNewAnimation();
   		} else
   			getNewAnimation();
   	}

   /**
*
*/
   	public void getNewAnimation() {
   		for (int i = 0; i < 6; i++)
   			if (dirs[i]) {
   				if(thatGuy.getStack().getAdjacent(i)!=null&&possMoves.contains(thatGuy.getStack().getAdjacent(i)))
   					ani = thatGuy.attemptToMove(i);
   				if (ani != null) {
   					ani.focus = true;
   					WorldScreen.area().addAnimation(ani, false);
   				}

   				return;
   			}
   	}
}

