package acggames.amulet;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.Region;

public class Tile {
	private static final int TOP = 0, LEFT = 1, RIGHT = 2, FRONT = 3;
	private static final Bitmap[][][] images;
	
	private static int _tlLeftOffset, _tTopOffset, _rlTopOffset, _rLeftOffset, _fLeftOffset, _fTopOffset;
	private static int frame, count = 10, interval = 10;
	
	/**
	 *
	 */
	public static final Region HITBOX;
	public static final Path PATH;
	/**
	 *
	 */
	public static final String[] ID_NAMES = {"wireframe", "dirt", "grass",
	        "woodland", "water", "brown sand", "yellow sand", "stone", "lava", "snow", "wood"};
	/**
	 *
	 */
	/**
	 *
	 */
	/**
	 *
	 */
	/**
	 *
	 */
	/**
	 *
	 */
	/**
	 *
	 */
	/**
	 *
	 */
	/**
	 *
	 */
	/**
	 *
	 */
	/**
	 *
	 */
	/**
	 *
	 */
	public static final int WIREFRAME = 0, DIRT = 1, GRASS = 2, WOODLAND = 3,
	        WATER = 4, BROWN_SAND = 5, YELLOW_SAND = 6, STONE = 7, LAVA = 8,
	        SNOW = 9, WOOD = 10;
	/**
	 *
	 */
	/**
	 *
	 */
	/**
	 *
	 */
	/**
	 *
	 */
	public static int _stackStep, _sideStep, _diagXStep, _diagYStep;
	
	private transient boolean leftVisible, rightVisible, frontVisible, topVisible;
	private int id;
	
	/**
	 *
	 */
	public Tile() {
	    id = 0;
	    leftVisible = rightVisible = frontVisible = topVisible = true;
	}
	
	/**
	 *
	 * @param id
	 */
	public Tile(int id) {
	    this.id = id;
	    leftVisible = rightVisible = frontVisible = topVisible = true;
	}
	
	/**
	 *
	 * @param page
	 */
	public void draw(Canvas page) {
	    int i = frame%images[id][TOP].length;
	    if (leftVisible)
	        page.drawBitmap(images[id][LEFT][i], _tlLeftOffset, _rlTopOffset, null);
	    if (rightVisible)
	        page.drawBitmap(images[id][RIGHT][i], _rLeftOffset, _rlTopOffset, null);
	    if (frontVisible)
	        page.drawBitmap(images[id][FRONT][i], _fLeftOffset, _fTopOffset, null);
	    if (topVisible)
	        page.drawBitmap(images[id][TOP][i], _tlLeftOffset, _tTopOffset, null);
	    //page.fill(HITBOX);
	}
	
	/**
	 * @param b the value to which to set leftVisible
	 * Korey Doss and Steven Cao
	 */
	public void setLeftVisible(boolean b) {
	    leftVisible = b;
	}
	
	/**
	 * @param b the value to which to set rightVisible
	 * Korey Doss and Steven Cao
	 */
	public void setRightVisible(boolean b) {
	    rightVisible = b;
	}
	
	/**
	 * @param b the value to which to set topVisible
	 * Korey Doss and Steven Cao
	 */
	public void setTopVisible(boolean b) {
	    topVisible = b;
	}
	
	/**
	 * @param b the value to which to set frontVisible
	 * Korey Doss and Steven Cao
	 */
	public void setFrontVisible(boolean b) {
	    frontVisible = b;
	}
	
	/**
	 * checks the visibility of the front, left, right, and top and returns
	 * true if all four are not visible.
	 *
	 * @return whether or not the entire tile is covered
	 * Korey Doss and Steven Cao
	 */
	public boolean isCovered() {
	    // Fields: leftVisible, rightVisible, topVisible, frontVisible
	    return !(leftVisible || rightVisible || topVisible || frontVisible);
	}
	
	/**
	 *
	 * @return true if the Tile is one of the non-opaque Tile types.
	 */
	public boolean isClear() {
	    return id == WATER || id == WIREFRAME;
	}
	
	/**
	 * @return the type id of this Tile
	 *Mac3/1/11
	 */
	
	public int getId() {
	    return id;
	}
	
	/**
	 *
	 */
	public static void step() {
	    if (--count <= 0) {
	        count = interval;
	        frame++;
	    }
	}
	
	/**
	 *
	 * @param i
	 */
	public static void setInterval(int i) {
	    interval = i;
	}
	
	@Override
	public Tile clone() {
	    return new Tile(id);
	}
	
	@Override
	public String toString() {
	    return ID_NAMES[id];
	}
	
	static {
	    images = new Bitmap[ID_NAMES.length][4][];
	    try {
	    	AssetManager assets = AmuletActivity.singleton.getAssets();
	        for (int i = 0; i < ID_NAMES.length; i++) {
	            loadHelperImages("front", i, FRONT, assets);
	            loadHelperImages("left", i, LEFT, assets);
	            loadHelperImages("right", i, RIGHT, assets);
	            loadHelperImages("top", i, TOP, assets);
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    _tlLeftOffset = -images[0][TOP][0].getWidth()/2;
	    _rLeftOffset = images[0][TOP][0].getWidth()/2-images[0][LEFT][0].getWidth();
	    _fLeftOffset = -images[0][FRONT][0].getWidth()/2;
	
	    _tTopOffset = -images[0][TOP][0].getHeight()/2 - images[0][FRONT][0].getHeight();
	    _fTopOffset = images[0][TOP][0].getHeight()/2-images[0][FRONT][0].getHeight();
	    _rlTopOffset = images[0][TOP][0].getHeight()-images[0][FRONT][0].getHeight()*2-images[0][LEFT][0].getHeight();
	
	    _diagXStep = images[0][TOP][0].getWidth() - images[0][LEFT][0].getWidth();
	    _diagYStep = images[0][TOP][0].getHeight()/2;
	    _sideStep = _diagXStep*2;
	    _stackStep = images[0][FRONT][0].getHeight();
	
	    int midTop, midLeft, midRight, midBottom;
	    midTop = midLeft = midRight = midBottom = 0;
	    for (int i = 0; i < images[0][TOP][0].getHeight(); i++)
	        if ((images[0][TOP][0].getPixel(0, i) & 0xff000000) != 0) {
	            midTop = i;
	            break;
	        }
	    for (int i = images[0][TOP][0].getHeight()-1; i > 0; i--)
	        if ((images[0][TOP][0].getPixel(0, i) & 0xff000000) != 0) {
	            midBottom = i;
	            break;
	        }
	    for (int i = 0; i < images[0][TOP][0].getWidth(); i++)
	        if ((images[0][TOP][0].getPixel(i, 0) & 0xff000000) != 0) {
	            midLeft = i;
	            break;
	        }
	    for (int i = images[0][TOP][0].getWidth()-1; i > 0; i--)
	        if ((images[0][TOP][0].getPixel(i, 0) & 0xff000000) != 0) {
	            midRight = i;
	            break;
	        }
	    HITBOX = new Region();
	    PATH = new Path();
	    PATH.moveTo(0, midTop);
	    PATH.lineTo(midLeft, 0);
	    PATH.lineTo(midRight, 0);
	    PATH.lineTo(images[0][TOP][0].getWidth(), midTop);
	    PATH.lineTo(images[0][TOP][0].getWidth(), midBottom);
	    PATH.lineTo(midRight, images[0][TOP][0].getHeight());
	    PATH.lineTo(midLeft, images[0][TOP][0].getHeight());
	    PATH.lineTo(0, midBottom);
	    PATH.close();
	    Matrix m = new Matrix();
	    m.setTranslate(_tlLeftOffset, _tTopOffset);
	    PATH.transform(m);
	    HITBOX.setPath(PATH, new Region(-5000, -5000, 5000, 5000));
	}
	
	private static void loadHelperImages(String name, int id, int side, AssetManager assets) throws IOException {
	    String[] pictures = new File("tiles/"+ID_NAMES[id]+"/"+name).list();
	    images[id][side] = new Bitmap[pictures.length];
	    for (int j = 0; j < pictures.length; j++) {
	    	InputStream is = assets.open(pictures[j]);
	        images[id][side][j] = BitmapFactory.decodeStream(is);
	    	is.close();
	    }
	}
}
