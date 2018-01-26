package axxel.floodit;

import java.io.*;
/**
 * The class <b>DotInfo</b> is a simple helper class to store the initial color and state
 * (captured or not) at the dot position (x,y)
 *
 * @author Yassine Zahoui, University of Ottawa
 */

public class DotInfo implements Serializable, Cloneable{

    /**
     * The coordinate of this DotInfo.
     */
    private int x;
    private int y;

    /**
     * The initial color at (x,y)
     */
    private int color;
    /**
     * Is that location captured ?
     */
    private boolean captured;

    /**
     * Constructor 
     * 
     * @param x
     *            the x coordinate
     * @param y
     *            the y coordinate
     * @param color
     *            the initial color
     */
    public DotInfo(int x, int y, int color){
        this.x = x;
        this.y = y;
        this.color = color;
    }

    /**
     * Getter method for the attribute x.
     * 
     * @return the value of the attribute x
     */
    public int getX(){
        return x;
    }
    
    /**
     * Getter method for the attribute y.
     * 
     * @return the value of the attribute y
     */
    public int getY(){
        return y;
    }
    
 
    /**
     * Setter for captured
     * @param captured
     *            the new value for captured
     */
    public void setCaptured(boolean captured) {
        this.captured = captured;
    }

    /**
     * Get for captured
     *
     * @return captured
     */
    public boolean isCaptured(){
        return captured;
    }

    /**
     * Get for color
     *
     * @return color
     */
    public int getColor() {
        return color;
    }
	
	/**
     * The metod <b>clone</b> returns a deepcopy of the actual object DotInfo.
     *
     * @return An object DotInfo (the clone of the actual object DotInfo)
     */
	
	public DotInfo clone() {
		try {
			DotInfo clone = (DotInfo) super.clone();
			return clone;
		} catch (CloneNotSupportedException e) {
			throw new AssertionError();
		}
	}
}