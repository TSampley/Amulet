package acggames.amulet;

import android.view.View;

/**
 * 
 */
public interface Alterable {
    /**
     * Returns a fully ready to use JPanel. Instantiates a JPanel for temporary
     * use with all UI components and event listeners set up that allows an
     * implementation of <code>Alterable</code> to edit it's fields.
     *
     * @return a JPanel containing all necessary UI components with any needed
     * listeners registered.
     */
    public View getUI();
    /**
     * Returns a new Alterable, using this object as a template. Implementations
     * of Alterable should not return a true clone of themselves.  The clone
     * method in Alterable is for use specifically in WorldBuilder, so not all
     * fields are un-aliased and not all fields will be instantiated.
     *
     * *note - hopefully this will be renamed at some point so that it will not
     * conflict with the java specification of Object's method clone().
     *
     * @return An implementation of Alterable
     */
    public Alterable clone();
}
