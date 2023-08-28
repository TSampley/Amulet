package acggames.amulet.processors;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.LinkedList;

import acggames.amulet.Actor;
import acggames.amulet.Area;
import acggames.amulet.Button;
import acggames.amulet.Team;
import acggames.amulet.clumps.MovementClump;
import acggames.amulet.display.AmuletView;
import acggames.amulet.display.WorldScreen;
import android.database.Cursor;
import android.graphics.Canvas;

/**
 * @author fuzzybunie
 */
public class PlayerTeamProcessor extends TeamProcessor {
    private LinkedList<Actor> members;
    private int currentActor;
    
    private Button[] butts;

    /**
     *
     */
    public PlayerTeamProcessor() {
        this(null);
    }
    
    /**
     *
     * @param t
     */
    public PlayerTeamProcessor(Team t) {
        super(t);
        butts = new Button[]{
            new Button("melee", AmuletView.dim.x/2, 160)
            {
                public void act()
                {
                    melee();
                }
            },
            new Button("ranged",AmuletView.dim.x/2,250)
            {
                public void act()
                {
                    ranged();
                }
            },
            new Button("move", AmuletView.dim.x/2, 300)
            {
                public void act()
                {
                    move();
                }
            },
            new Button("items", AmuletView.dim.x/2, 350)
            {
                public void act()
                {
                    items();
                }
            },
            new Button("finish", AmuletView.dim.x/2, 400)
            {
                public void act()
                {
                    finish();
                }
            }
        };
        Actor a = t.getMembers().getFirst();
        WorldScreen.area().setCameraFocus(a.getX(), a.getY());
    }

    public Cursor getCursor() {
        return null;
    }

    /**
     *
     * @param t
     */
    @Override
    public final void setTeam(Team t) {
        super.team = t;
        members = t.getMembers();
        currentActor = 0;
    }
    
    @Override
    public void draw(Canvas page, Canvas untouched)
    {
        for(int i = 0; i < 5; i++)
            butts[i].draw(untouched);
    }

    /**
     *
     */
    public void melee() {
        members.get(currentActor).getWeapon().attack(false);
    }

    /**
     *
     */
    public void ranged() {
        members.get(currentActor).getWeapon().attack(true);
    }

    /**
     *
     */
    public void move() {
        if(members.get(currentActor).getStat(Actor.STAT.ACTION_POINTS)>=5)
            Area.addProcessor(new MoveProcessor(new MovementClump().get(members.get(currentActor).getStack(),0,members.get(currentActor).getStat(Actor.STAT.MOVEMENT)),members.get(currentActor)));
        else
            System.out.println("You be outta Action points foo, you best try sometin else.");
    }

    /**
     *
     */
    public void items() {
        Area.addProcessor(new ItemProcessor(members.get(currentActor).getItemGrid()));
    }

    /**
     *
     */
    public void finish() {
        for(Actor regularVariableName: members)
        {
            regularVariableName.setStat(Actor.STAT.ACTION_POINTS, regularVariableName.getNormStat(Actor.STAT.ACTION_POINTS));
        }
        Area.removeProcessor();
    }
    
    /*@Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                if(currentActor > 0)
                {
                    currentActor--;
                    WorldScreen.area().setFocus(members.get
                            (currentActor).getX(), members.get(currentActor).getY());
                }
                else
                {
                    currentActor += members.size()-1;
                    WorldScreen.area().setFocus(members.get
                            (currentActor).getX(), members.get(currentActor).getY());
                }
                break;
            case KeyEvent.VK_RIGHT:
                if(currentActor < members.size()-1)
                {
                    currentActor++;
                    WorldScreen.area().setFocus(members.get
                            (currentActor).getX(), members.get(currentActor).getY());
                }
                else
                {
                    currentActor = 0;
                    WorldScreen.area().setFocus(members.get
                            (currentActor).getX(), members.get(currentActor).getY());
                }
                break;
            case KeyEvent.VK_A:
                melee();
                break;
            case KeyEvent.VK_R:
                ranged();
                break;
            case KeyEvent.VK_M:
                move();
                break;
            case KeyEvent.VK_I:
                items();
                break;
            case KeyEvent.VK_ESCAPE:
                finish();
                break;
            default:
                return;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                break;
            case KeyEvent.VK_RIGHT:
                break;
            default:
                return;
        }
    }

    @Override
    public void mousePressed(MouseEvent e)
    {
        for(int constipated=0;constipated<=4;constipated++)
        {
            if(butts[constipated].contains(e.getX(), e.getY()))
                butts[constipated].act();
        }
    }
    */

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
    }
}
