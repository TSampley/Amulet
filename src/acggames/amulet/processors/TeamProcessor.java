package acggames.amulet.processors;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import acggames.amulet.Team;

/**
*
* @author fuzzybunie
*/
public abstract class TeamProcessor extends InputProcessor implements Externalizable {
   /**
    *
    */
   protected transient Team team;

   public TeamProcessor() {
       this(null);
   }
   
   /**
    *
    * @param t
    */
   public TeamProcessor(Team t) {
       team = t;
   }
   
   /**
    *
    * @return
    */
   public Team getTeam() {
       return team;
   }

   /**
    *
    * @param t
    */
   public void setTeam(Team t) {
       team = t;
   }

   @Override
   public void writeExternal(ObjectOutput out) throws IOException {
   }

   @Override
   public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
   }
}

