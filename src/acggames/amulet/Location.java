package acggames.amulet;

public class Location {

    /**
     *
     */
    /**
     *
     */
    /**
     *
     */
    /**
     *
     */
    /**
     *
     */
    /**
     *
     */
    /**
     *
     */
    public static final int R_0 = 0,  R_60 = 1, R_120 = 2, R_180 = 3, R_240 = 4, R_300 = 5, R_360 = 6;
    /**
     *
     */
    /**
     *
     */
    /**
     *
     */
    /**
     *
     */
    /**
     *
     */
    /**
     *
     */
    public static final int I_30 = 0, I_90 = 1, I_150 = 2, I_210 = 3, I_270 = 4, I_330 = 5;

    private int x, y;
    /**
     *
     * @param x
     * @param y
     */
    public Location(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Finds and returns a Location object adjacent from this Location in the
     * given direction.
     *
     * @param dir the direction in which to "travel"
     * @return the new Location object in the direction of dir
     */
    public Location getAdjacentLocation(int dir) {
        /*
         * Here's an example for you guys:
         * assume the matrix where each stack is represented by a number
         * x    0  1  2  3  4  5
         *
         * 0   10 11 12  0  0  0
         * 1    9  4  5  0  0  0
         * 2    8  1  2  0  0  0
         * 3    6  3  7  0  0  0
         * 4    0  0  0  0  0  0
         * 5    0  0  0  0  0  0
         *
         * Location loc = new Location(1, 2)
         * Area.getStack(loc) returns the stack 1.
         * Area.getStack(loc.getAdjacentLocation(_60)) returns the stack 4.
         * Area.getStack(loc.getAdjacentLocation(_240)) returns the stack 7.
         *
         * loc = new Location(1, 1)
         * Area.getStack(loc) returns the stack 4.
         * Area.getStack(loc.getAdjacentLocation(_60)) returns the stack 12.
         * Area.getStack(loc.getAdjacentLocation(_240)) returns the stack 1.
         * Use stack.highlight() to test individual stacks
         */
         if(getY() % 2 == 0) { //y is even
            switch (dir){
            	case R_0:
            		return new Location(getX(), getY() - 1);
         		case R_60:
         			return new Location(getX(), getY() - 2);
         		case R_120:
         			return new Location(getX() - 1, getY() - 1);
         		case R_180:
         			return new Location(getX() - 1, getY() + 1);
         		case R_240:
         			return new Location(getX(), getY() + 2);
         		case R_300:
         			return new Location(getX(), getY() + 1);
            }
         }
         else { //y is odd
         	switch (dir){
            	case R_0:
            		return new Location(getX() + 1, getY() - 1);
         		case R_60:
         			return new Location(getX(), getY() - 2);
         		case R_120:
         			return new Location(getX(), getY() - 1);
         		case R_180:
         			return new Location(getX(), getY() + 1);
         		case R_240:
         			return new Location(getX(), getY() + 2);
         		case R_300:
         			return new Location(getX() + 1, getY() + 1);
            }
         }
			return null;
    }

    /**
     * Returns the x coordinate of this location
     * @return the x coordinate of this location
     */
    public int getX() {
        return x;
    }

    /**
     * Returns the y coordinate of this location
     * @return the y coordinate of this location
     */
    public int getY() {
        return y;
    }

    /**
     * Takes a direction and returns the new direction after applying the
     * specified rotation.  All "degrees" are represented by indexes.  The
     * following examples evaluate to true.
     *
     * getDirection(_0, _120) == _120;
     * getDirection(_60, -_120) == _300;
     *
     * @param dir The initial direction.
     * @param rot The degree (index) by which to rotate.
     * @return The rotated direction.
     */
    public static int getDirection(int dir, int rot) {
        if(rot >= 0)
            return (dir + rot) % R_360;
        return (dir + rot + R_360) % R_360;
    }
    
    /**
     *
     * @param loc
     * @return
     */
    public int getDirectionToward(Location loc) {

    	int yDif = y-loc.y;
    	int xDif;

    	if(y%2 == 0)
    	{
    		if(loc.y %2 == 0)
    			xDif = 2*(x-loc.x);
    		else if(x==loc.x)
    			xDif = -1;
    		else xDif = 2*(x-loc.x)+1;
    	}
    	else
    		if(loc.y %2 == 0)
    			xDif = 2*(x-loc.x)+1;
    		else xDif = 2*(x-loc.x);

		if(yDif>0 && xDif>0)
		{
			if(yDif/xDif <4)
				return 2;
			else return 1;
		}
		if(yDif>0 && xDif<0)
		{
			if(yDif/xDif >-4)
				return 0;
			else return 1;
		}
		if(yDif<0 && xDif>0)
		{
			if(yDif/xDif >-4)
				return 3;
			else return 4;
		}
		if(yDif<0 && xDif<0)
		{
			if(yDif/xDif <4)
				return 5;
			else return 4;
		}
		if(yDif == 0)
		{
			if(xDif < 0)
				if(getAdjacentLocation(0).getY() < 0)
					return 5;
				else return 0;
			if(xDif > 0 && getAdjacentLocation(2).getY() > 0)
				return 2;
			return 3;
		}
		if(xDif == 0)
		{
			if(yDif < 0)
				return 4;
			else return 1;
		}
    	return 0;
   }

    /**
     *
     * @param loc
     * @return
     */
    public int distanceTo(Location loc) {
        Location here = this;
        int pathSize = 0;

        while(!here.equals(loc)) {
            here = here.getAdjacentLocation(here.getDirectionToward(loc));
            pathSize++;
        }
        return pathSize;
   }

    @Override
   public boolean equals(Object o) {
   	if(o instanceof Location)
   		return toString().equals(((Location)o).toString());
   	return false;
   }

    @Override
    public String toString() {
        return "["+x+","+y+"]";
    }
}
