package acggames.amulet.items;

import acggames.amulet.Actor;
import acggames.amulet.Area;
import acggames.amulet.processors.AttackActorProcessor;
import android.view.View;

public class ActorItem extends ExternalUseItem<Actor> {

    /**
     *
     */
    @Override
    public void use() {
        Area.addProcessor(new AttackActorProcessor(this));
    }

    @Override
    public String toString() {
        return getClass().getCanonicalName();
    }

    @Override
    public ActorItem clone() {
        return null;
    }

    /**
     *
     * @return
     */
    @Override
    public View getUI() {
        return null;
    }

    public int compareTo(Item i) {
        if (i instanceof Weapon)
            return 1;
        else if (i == this)
            return 0;
        else
            return -1;
    }
}

