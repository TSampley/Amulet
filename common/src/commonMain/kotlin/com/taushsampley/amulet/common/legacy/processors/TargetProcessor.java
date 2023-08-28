package acggames.amulet.processors;

import acggames.amulet.Area;
import acggames.amulet.Stack;
import acggames.amulet.display.WorldScreen;

/**
 * @author fuzzybunie
 */
public abstract class TargetProcessor extends InputProcessor {
    private boolean frontToBack;
    private Area area;
    protected Stack last;

    /**
     *
     */
    public TargetProcessor() {
        area = WorldScreen.area();
        frontToBack = false;
    }

    /*
    @Override
    public final void mouseMoved(MouseEvent e) {
        Stack s = area.getStack(e.getX(), e.getY(), frontToBack);
        if (s!=last) {
            if (last!=null)
                deselect(last);
            if (s!=null)
                select(s);
            last = s;
        }
    }
    */

    /**
     *
     * @param s
     */
    public void select(Stack s) {
        s.highlight(0xffffff00);
    }

    /**
     * 
     * @param s
     */
    public void deselect(Stack s) {
        s.highlight(0);
    }

    /*
    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_ESCAPE:
                if (last!=null)
                    last.highlight(null);
            	Area.removeProcessor();
            	break;
            case KeyEvent.VK_SHIFT:
                frontToBack = true;
                break;
            default:
                return;
        }
    }
    */

    /*
    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_SHIFT:
                frontToBack = false;
        }
    }
    */
}

