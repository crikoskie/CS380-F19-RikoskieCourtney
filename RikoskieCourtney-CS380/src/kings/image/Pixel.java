package kings.image;

/**
 * Represents a pixel in an image.
 * 
 * @author Courtney Rikoskie
 * @version 09-04-19
 */
public class Pixel {
	/** Allows for the isolation of the alpha data. */
	public static final int ALPHA_MASK = 0xff000000; 
	/** The alpha offset in the pixel data. */
	public static final int ALPHA_OFFSET = 24; 
	/** Allows for the isolation of the red data. */
	public static final int RED_MASK = 0x00ff0000; 
	/** The red offset in the pixel data. */
	public static final int RED_OFFSET = 16; 
	/** Allows for the isolation of the green data. */
	public static final int GREEN_MASK = 0x0000ff00; 
	/** The green offset in the pixel data. */
	public static final int GREEN_OFFSET = 8; 
	/** Allows for the isolation of the blue data. */
	public static final int BLUE_MASK = 0x000000ff; 

	/** The pixel color information. */
	private int data;
	
	/**
	 * Constructs a new Pixel.
	 * 
	 * @param data The pixel color information.
	 */
	public Pixel(int data) {
		this.data = data;
	}
	
	/**
	 * Gets the portion of the pixel data that contains the red value.
	 * 
	 * @return The red value.
	 */
	public int getRed() {
		int red = 0;
		
		red = data & RED_MASK;
		red = red >> RED_OFFSET;
		
		return red;
	}
	
	/**
	 * Gets the portion of the pixel data that contains the green value.
	 * 
	 * @return The green value.
	 */
	public int getGreen() {
		int green = 0;
		
		green = data & GREEN_MASK;
		green = green >> GREEN_OFFSET;
		
		return green;
	}
	
	/**
	 * Gets the portion of the pixel data that contains the blue value.
	 * 
	 * @return The blue value.
	 */
	public int getBlue() {
		int blue = 0;
		
		blue = data & BLUE_MASK;
		
		return blue;
	}
	
	/**
	 * Sets the red value of the pixel to the new one.
	 * 
	 * @param newRed The new red value.
	 */
	public void setRed(int newRed) {
		int red = data & 0xff00ffff;
		newRed = newRed << RED_OFFSET;
		
		data = red | newRed;
	}
	
	/**
	 * Sets the green value of the pixel to the new one.
	 * 
	 * @param newGreen The new green value.
	 */
	public void setGreen(int newGreen) {
		int green = data & 0xffff00ff;
		newGreen = newGreen << GREEN_OFFSET;
		
		data = green | newGreen;
	}
	
	/**
	 * Sets the blue value of the pixel to the new one.
	 * 
	 * @param newBlue The new blue value.
	 */
	public void setBlue(int newBlue) {
		int blue = data & 0xffffff00;
		
		data = blue | newBlue;
	}
	
	/**
	 * Gets the pixel color information.
	 * 
	 * @return The pixel color information.
	 */
	public int getData() {
		return data;
	}
}
