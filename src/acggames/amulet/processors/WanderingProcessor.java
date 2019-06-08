package acggames.amulet.processors;

import acggames.amulet.Actor;
import acggames.amulet.Team;
import acggames.amulet.animations.MoveAnimation;
import acggames.amulet.display.WorldScreen;
import android.database.Cursor;

public class WanderingProcessor extends InputProcessor {
    private MoveAnimation ani;
    private Actor user;
    private boolean[] dirs;

    public WanderingProcessor() {
        dirs = new boolean[]{false, false, false, false, false, false};
    }

    public Cursor getCursor() {
        return null;
    }
    /**
     *
     * @param u
     */
    public void setup(Actor u) {
        user = u;
    }
    
    /**
     *
     */
    @Override
    public void step() {
        for (Team t : WorldScreen.area().getTeams()) {
            t.act();
        }
        if (ani == null)
            getNewAnimation();
        else if(ani.hasFinished())
            getNewAnimation();
    }
    
    /**
     *
     */
    public void getNewAnimation() {
        for (int i = 0; i < 6; i++)
            if (dirs[i]) {
                ani = user.attemptToMove(i);
                if (ani != null) {
                    ani.focus = true;
                    WorldScreen.area().addAnimation(ani, false);
                }
                return;
            }
    }
}
