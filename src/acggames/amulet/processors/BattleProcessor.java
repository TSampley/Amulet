package acggames.amulet.processors;

import java.util.LinkedList;
import java.util.Queue;
import java.util.TreeSet;

import acggames.amulet.Actor;
import acggames.amulet.Area;
import acggames.amulet.Stack;
import acggames.amulet.Team;
import acggames.amulet.conditions.Condition.Type;
import acggames.amulet.display.WorldScreen;
import android.database.Cursor;

/**
 * Battle ends if all members of the main team faint.
 * the Battle is won if all members of all other teams faint.
 */
public class BattleProcessor extends InputProcessor {
    private TreeSet<Stack> enclosure, field;
    private Queue<Team> teams;

    /**
     *
     * @param enc
     * @param fie
     * @param teams
     */
    public BattleProcessor(TreeSet<Stack> enc, TreeSet<Stack> fie, LinkedList<Team> teams) {
        enclosure = enc;
        field = fie;
        // put the teams into the queu in order of the Team's total initiative

        Team chickenCowboy;
        this.teams = new LinkedList<Team>();
        if(teams.size()>1)
        {
            while(!teams.isEmpty())
            {   chickenCowboy=teams.getFirst();
                for(Team weShallSee: teams)
                {
                    if(weShallSee.getInitiative()>=chickenCowboy.getInitiative())
                        chickenCowboy=weShallSee;
                }
               this.teams.add(chickenCowboy);
               teams.remove(chickenCowboy);
            }
            System.out.println(this.teams);
        }
    }

    public Cursor getCursor() {
        return null;
    }

    /**
     *
     */
    @Override
    public void step() {
        // draw interface
        if(teams.element().isDead())
        {
            if(teams.element().isInstance(PlayerTeamProcessor.class))
            {
                exit();
                //Extra code for lose
                System.out.println("You lost");
            }
            else
                teams.remove();
        }
        if(teams.size() < 2)
        {
            exit();
            //Extra code for win
            System.out.println("You won");
        }
        Team temp = teams.element();
        teams.add(teams.remove());
        Area.addProcessor(new PlayerTeamProcessor(temp));
    }

    /**
     *
     */
    public void exit() {
        for (Team t : teams)
            if (t.isInstance(BerserkProcessor.class)) {
                for (Actor a : t.getMembers())
                    a.removeCondition(Type.BERSERK);
                WorldScreen.area().getTeams().remove(t);
            } else if (t.isDead())
                WorldScreen.area().getTeams().remove(t);
        for(Stack s : enclosure)
            s.setEnabled(true);
        for(Stack s : field)
            s.updateVisibility();
        Area.removeProcessor();
        WorldScreen.setVariable("inBattle", "false");
    }
}

