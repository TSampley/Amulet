package acggames.amulet.effects;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Collection;
import java.util.LinkedList;
import java.util.TreeSet;

import acggames.amulet.Actor;
import acggames.amulet.Area;
import acggames.amulet.Location;
import acggames.amulet.Stack;
import acggames.amulet.Team;
import acggames.amulet.clumps.CircleClump;
import acggames.amulet.clumps.Clump;
import acggames.amulet.display.WorldScreen;
import acggames.amulet.processors.BattleProcessor;
import android.view.View;

/**
*
* @param <E>
* @author fuzzybunie
*/
public class BattleStartEffect<E> extends Effect<E> {

   private Location loc;
   private Clump c;
   private int dir, mag;
   private LinkedList<Team> teams;

   /**
    *
    */
   public BattleStartEffect() {
       // for deserialization only
   }

   /**
    *
    * @param stack
    * @param incoming
    */
   public BattleStartEffect(Location l, Collection<Team> incoming) {
       loc = l;
       c = new CircleClump();
       dir = 0;
       mag = 3;
       teams = new LinkedList<Team>();
       teams.addAll(incoming);
   }

   /**
    *
    * @param target
    */
   public void affect(E target) {
       String inBattle = WorldScreen.getVariable("inBattle");
       if (inBattle == null || inBattle.equals("false"))
           WorldScreen.setVariable("inBattle", "true");
       else
           return;
       WorldScreen.area().interuptAnimations();
       Stack s = WorldScreen.area().getStack(loc);
       TreeSet<Stack> field = c.get(s, dir, mag);
       for (Team t : teams)
           for (Actor a : t.getMembers())
               field.addAll(s.getPath(a.getStack()));
           
       TreeSet<Stack> enclosure = WorldScreen.area().enclose(field);
       for (Stack st : enclosure)
           st.setEnabled(false);
       for (Stack st : field)
           st.updateVisibility();
       Area.addProcessor(new BattleProcessor(enclosure, field, teams));
   }

   /**
    *
    * @return
    */
   public String getDescription() {
       return "Starts a battle";
   }

   public BattleStartEffect<E> clone() {
       return new BattleStartEffect<E>();
   }

   /**
    *
    * @return
    */
   public View getUI() {
       return null;
   }
   
   @Override
   public void writeExternal(ObjectOutput out) throws IOException {
       out.writeInt(loc.getX());
       out.writeInt(loc.getY());
       WorldScreen.writeExternalClass(out, c);
       out.writeInt(dir);
       out.writeInt(mag);
       LinkedList<Team> areaTeams = WorldScreen.area().getTeams();
       int size = teams.size();
       out.writeInt(size);
       for (Team t : teams)
           out.writeInt(areaTeams.indexOf(t));
   }

   @Override
   public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
       loc = new Location(in.readInt(), in.readInt());
       c = (Clump)WorldScreen.readExternalClass(in);
       dir = in.readInt();
       mag = in.readInt();
       LinkedList<Team> areaTeams = WorldScreen.area().getTeams();
       for (int i = 0, e = in.readInt(); i < e; i++)
           teams.add(teams.get(in.readInt()));
       // TODO finish
   }
}

