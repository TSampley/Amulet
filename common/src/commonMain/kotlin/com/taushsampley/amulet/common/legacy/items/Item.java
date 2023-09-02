package acggames.amulet.items;

import java.io.Externalizable;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import acggames.amulet.Actor;
import acggames.amulet.Alterable;
import acggames.amulet.AmuletActivity;
import acggames.amulet.Grid;
import acggames.amulet.display.WorldScreen;
import acggames.amulet.effects.Effect;
import acggames.amulet.effects.EmptyEffect;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;

/**
*
* @param <E>
* @author fuzzybunie + Daniel Griffin
*/
public abstract class Item<E> implements Alterable, Externalizable, Comparable<Item<E>> {
   
   /**
    *
    */
   public Actor owner;
   
   /**
    *
    */
   private Bitmap icon;
   /**
    *
    */
   private Bitmap image;
   /**
    *
    */
   private Matrix mat;
   /**
    *
    */
   private int rotation;
   /**
    *
    */
   private String imagePath;
   /**
    *
    */
   private int height;
   /**
    *
    */
   private int width;

   /**
    *
    */
   private boolean soulBound;
   /**
    *
    */
   private int useCost;
   /**
    *
    */
   private int uses;

   /**
    *
    */
   public Effect<E> effect;

   /**
    *
    */
   public Item() {
       setPath("items/default");
       mat = new Matrix();
       mat.setRotate(0);

       soulBound = false;
       useCost = 1;
       uses = -1;
       
       effect = new EmptyEffect<E>();
   }

   /**
    *
    * @param name
    */
   public final void setPath(String name) {
       imagePath = name;
       try {
           String ic = name+"/icon.png",
                  im = name+"/image.png";
           AssetManager assets = AmuletActivity.singleton.getAssets();
           InputStream is = assets.open(ic);
           icon = BitmapFactory.decodeStream(is);
           is.close();
           is = assets.open(im);
           image = BitmapFactory.decodeStream(is);
           is.close();
           assets.close();
           width = image.getWidth()/Grid.SQUARE_SIZE;
           height = image.getHeight()/Grid.SQUARE_SIZE;
       } catch (Exception e) {
           e.printStackTrace();
       }
   }

   /**
    *
    */
   public abstract void use();

   /**
    * @author Korey Doss
    * @param o
    */
   public void setOwner(Actor o) {
       owner = o;
   }

   /**
    *
    */
   public void rotate() {
       int temp = height;
       height = width;
       width = temp;
   }

   /**
    *
    * @param ef
    */
   public void setEffect(Effect<E> ef) {
       effect = ef;
   }

   /**
    *
    * @return
    */
   public Effect<E> getEffect() {
       return effect;
   }

   /**
    *
    * @return
    */
   public boolean reusable() {
       return uses != 0;
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
   public int getWidth() {
       return width;
   }

   @Override
   public abstract String toString();

   @Override
   public abstract Item<E> clone();

   /**
    *
    * @param page
    * @param x
    * @param y
    */
   public void drawIcon(Canvas page, int x, int y) {
       page.drawBitmap(icon, x, y, null);
   }

   /**
    *
    * @param page
    * @param x
    * @param y
    */
   public void drawItem(Canvas page, int x, int y) {
	   Matrix m = new Matrix();
	   m.setTranslate(x, y);
       page.drawBitmap(image, m, null);
   }

   public abstract int compareTo(Item<E> i);

   public void writeExternal(ObjectOutput out) throws IOException {
       out.writeInt(rotation);
       out.writeUTF(imagePath);
       out.writeInt(height);
       out.writeInt(width);
       out.writeBoolean(soulBound);
       out.writeInt(useCost);
       out.writeInt(uses);
       WorldScreen.writeExternalClass(out, effect);
   }

   @SuppressWarnings("unchecked")
   public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
       rotation = in.readInt();
       setPath(in.readUTF());
       height = in.readInt();
       width = in.readInt();
       soulBound = in.readBoolean();
       useCost = in.readInt();
       uses = in.readInt();
       effect = (Effect<E>)WorldScreen.readExternalClass(in);
   }
}
