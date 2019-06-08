package acggames.amulet.processors;

import acggames.amulet.Alterable;
import android.graphics.Canvas;
import android.view.View;

/**
 * Extensions of InputProcessor almost serve as interchangeable game engines;
 * they receive all pertinent user-input events and are called to draw last so
 * they can display interfaces or other related graphics on the game's surface.
 *
 * @author fuzzybunie
 */
public abstract class InputProcessor implements Alterable {
    /**
     * This is the part of InputProcessor that allows information or options to
     * be displayed to the user before action is taken.
     *
     * @param page a graphics context with an origin adjusted to match the
     * current focus position of the current Area object.
     * @param untouched an unadjusted graphics context that can be used to draw
     * interfaces or other graphics to the screen.
     */
    public void draw(Canvas page, Canvas untouched) {}

    /**
     *
     */
    public void step() {}

    /**
     *
     * @return
     */
    @Override
    public View getUI() {
        return null;
    }

    @Override
    public InputProcessor clone() {
        return null;
    }
}
