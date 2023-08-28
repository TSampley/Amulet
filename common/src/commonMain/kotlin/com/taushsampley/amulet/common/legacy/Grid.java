package acggames.amulet;

import java.io.Externalizable;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import acggames.amulet.display.WorldScreen;
import acggames.amulet.items.Item;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;

/**
*
* @author Daniel Griffin @)
*/
@SuppressWarnings("rawtypes")
public class Grid implements Externalizable {

   /**
    *
    */
   public static final Bitmap SQUARE;
   /**
    *
    */
   public static final int SQUARE_SIZE;
   private Item [][] grid;
   private int width;
   private int height;
   private Point origin;
   private int itemCount;

   /**
    *
    */
   public Grid() {
       this(10, 10);
   }

   /**
    *
    * @param w
    * @param h
    */
   public Grid(int w, int h) {
       grid = new Item[w][h];
       width = w;
       height = h;
       origin = new Point();
       itemCount = 0;
   }

   /**
    *
    * @return
    */
   public int getWidth() {
       return width;
   }

   /**
    *
    * @return
    */
   public int getHeight() {
       return height;
   }

   /**
    *
    * @return
    */
   public Point getOrigin() {
       return origin;
   }

   public Item getItem(Point p) {
       if(p.x < origin.x || p.y < origin.y)
           return null;
       if (p.x > origin.x + width * Grid.SQUARE_SIZE)
           return null;
       if(p.y > origin.x + height * Grid.SQUARE_SIZE)
           return null;
       int col = (p.x - origin.x) / Grid.SQUARE_SIZE;
       int row = (p.y - origin.y) / Grid.SQUARE_SIZE;
       return grid[col][row];
   }

   /**
    *
    * @param h
    * @param w
    * @param p
    * @return
    */
   public boolean canAdd(int h, int w, Point p) {
       //checks for in grid and not other objects already there
       if(h + p.y > grid[0].length)
           return false;
       if(w + p.x > grid.length)
           return false;
       for(int x = p.x; x <= p.x + w; x++)
           for(int y = p.y; y <= p.y + h; y++)
               if(grid[x][y] != null)
                   return false;
       return true;
   }

   /**
    *
    * @param i
    * @param p
    */
   public void add(Item i, Point p) {
       for(int x = p.x; x <= p.x + i.getWidth(); x++)
           for(int y = p.y; y <= p.y + i.getHeight(); y++)
               grid[x][y] = i;
       itemCount++;
   }
   
   public void remove(Item it) {
       for (int y = 0; y < height; y++)
           for (int x = 0; x < width; x++)
               remove(it, x, y);
   }

   public void remove(Item it, int x, int y) {
       if (grid[x][y]==it) {
           int ie = x+it.getWidth(),
               je = y+it.getHeight();
           for (int i = x; i < ie; i++)
               for (int j = y; j < je; j++)
                   grid[i][j] = null;
           itemCount--;
       }
   }

   public void readExternalAreaHelper(Actor a) {
       for (Item[] ar : grid)
           for (Item i : ar)
               if (i!=null)
                   i.setOwner(a);
   }

   public void writeExternal(ObjectOutput out) throws IOException {
       out.writeInt(width);
       out.writeInt(height);
       out.writeInt(itemCount);
       for (int y = 0; y < height; y++)
           for (int x = 0; x < width; x++)
               if (grid[x][y]!=null) {
                   out.writeInt(x);
                   out.writeInt(y);
                   WorldScreen.writeExternalClass(out, grid[x][y]);
                   remove(grid[x][y], x, y);
               }
   }

   public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
       grid = new Item[width = in.readInt()][height = in.readInt()];
       for (int i = 0, e = in.readInt(); i < e; i++)
           add((Item)WorldScreen.readExternalClass(in), new Point(in.readInt(), in.readInt()));
   }

   static {
       Bitmap bi;
       try {
           AssetManager assets = AmuletActivity.singleton.getAssets();
           InputStream is = assets.open("misc/gridBox.pnj");
    	   bi = BitmapFactory.decodeStream(is);
    	   is.close();
    	   assets.close();
       } catch (Exception e) {
           bi = Bitmap.createBitmap(20, 20, Bitmap.Config.ARGB_8888);
       }
       SQUARE = bi;
       SQUARE_SIZE = SQUARE.getWidth();
   }
}
