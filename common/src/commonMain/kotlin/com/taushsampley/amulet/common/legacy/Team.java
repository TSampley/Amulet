package acggames.amulet;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.LinkedList;

import acggames.amulet.display.WorldScreen;
import acggames.amulet.processors.PlayerTeamProcessor;
import acggames.amulet.processors.TeamProcessor;
import android.view.View;

public final class Team implements Alterable, Externalizable {
    
    private double averageInitiative;
    private LinkedList<Actor> members;
    private Actor leader;
    private TeamProcessor teamProcessor;

    /**
     *
     */
    public Team() {
        members = new LinkedList<Actor>();
        setProcessor(new PlayerTeamProcessor());
    }

    /**
     *
     * @param tp
     */
    public Team(TeamProcessor tp) {
        members = new LinkedList<Actor>();
        setProcessor(tp);
    }
    
    /**
     *
     * @param l
     */
    public Team(Actor l) {
        this(new PlayerTeamProcessor());
        setLeader(l);
    }

    /**
     *
     * @param l
     * @param tp
     */
    public Team(Actor l, TeamProcessor tp) {
        this(tp);
        setLeader(l);
    }
    
    /**
     *
     * @param tp
     */
    public void setProcessor(TeamProcessor tp) {
        teamProcessor = new PlayerTeamProcessor();
        teamProcessor.setTeam(this);
    }

    /**
     *
     * @param a
     */
    public void setLeader(Actor a) {
        leader = a;
        add(a);
    }

    /**
     *
     * @return true if all members of this team have a health stat of 0 or less;
     * false otherwise.
     */
    public boolean isDead() {
        for(int i = 0; i < members.size(); i++)
            if(members.get(i).getStat(Actor.STAT.HEALTH) > 0)
                return false;
        return true;
    }

    /**
     *
     * @return The <code>LinkedList'<Actor>'</code> that holds all members of
     * this Team.
     */
    public LinkedList<Actor> getMembers() {
        return members;
    }

    /**
     *
     * @param a
     */
    public void add(Actor a) {
        members.add(a);
        a.setTeam(this);
        averageInitiative = ((members.size() - 1) * averageInitiative + a.getStat(Actor.STAT.SPEED)) / members.size();
    }

    /**
     *
     * @param a
     */
    public void remove(Actor a) {
        members.remove(a);
        a.setTeam(null);
        averageInitiative = ((members.size() + 1) * averageInitiative - a.getStat(Actor.STAT.SPEED)) / members.size();
    }

    /**
     *
     */
    public void updateInitiative() {
        int totalInitiative = 0;
        for(Actor a : members)
            totalInitiative += a.getStat(Actor.STAT.SPEED);
        averageInitiative = totalInitiative / members.size();
    }

    /**
     *
     * @return The Team's average initiative.
     */
    public double getInitiative() {
        // returns int averageInitiative
        return averageInitiative;
    }

    private Actor getLeader() {
        return leader;
    }

    /**
     *
     */
    public void act() {
        for(Actor a : members)
        	a.act();
    }

    /**
     *
     * @return true if this Team's TeamProcessor is an instance of
     * <code>PlayerTeamProcessor</code>;  false otherwise.
     * @see PlayerTeamProcessor
     */
    public boolean isInstance(Class c) {
        return c.isInstance(teamProcessor);
    }
    
    @Override
    public Team clone() {
        return new Team();
    }
    
    public View getUI() {
        return null;
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeDouble(averageInitiative);
        int teamSize = members.size();
        out.writeInt(teamSize);
        for (Actor a : members)
            a.writeExternal(out);
        if (leader == null)
            out.writeBoolean(false);
        else {
            out.writeBoolean(true);
            leader.writeExternal(out);
        }
        WorldScreen.writeExternalClass(out, teamProcessor);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        averageInitiative = in.readDouble();
        int teamSize = in.readInt();
        for (int i = 0; i < teamSize; i++) {
            Actor a = new Actor();
            a.readExternal(in);
            a.setTeam(this);
        }
        if (in.readBoolean()) {
            leader = new Actor();
            leader.readExternal(in);
            leader.setTeam(this);
        }
        teamProcessor = (TeamProcessor)WorldScreen.readExternalClass(in);
        teamProcessor.setTeam(this);
    }
}
