package acggames.amulet.processors;

import acggames.amulet.Grid;
import android.database.Cursor;
import android.graphics.Canvas;

/**
*
* @author fuzzybunie
*/
public class ItemProcessor extends InputProcessor {
   private Grid grid;

   /**
    *
    * @param g
    */
   public ItemProcessor(Grid g) {
       grid = g;
   }

   /*
   @Override
   public void mousePressed(MouseEvent e) {
       Item i = grid.getItem(e.getPoint());
       if(i != null)
           i.use();// corrected by - Taush Sampley a.k.a. fuzzybunie
   }

   /**
    *
    *@author Ryan Jones
    
   @Override
   public void keyPressed(KeyEvent e) {
       switch (e.getKeyCode()) {
           case KeyEvent.VK_ESCAPE:
           	Area.removeProcessor();
           	break;
           default:
               return;
       }
   }
   */

   public Cursor getCursor() {
       return null;
   }

   @Override
   public void draw(Canvas page, Canvas untouched) {
       // TODO draw interface
   }
}

