package kings.image;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferInt;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.IOException;

/**
 * Algorithms for processing images.
 * 
 * @author Courtney Rikoskie
 * @version 09-03-19
 */
public class ImageProcessor {
	public static final int SEPIA_DEPTH = 20;
	public static final int SEPIA_INTENSITY = 30;
	
	/** The writer of the time spent on the algorithm. */
	private TimeWriter writer;

	/**
	 * Allows for images to be edited.
	 */
	public ImageProcessor() {
		writer = new TimeWriter();
	}

	/**
	 * Takes an image and turns it into a grayscaled version of itself.
	 * 
	 * @param image
	 *            The image to edit.
	 * @return The grayscaled image.
	 * @throws IOException
	 *             Thrown if ReadMe file cannot be written to.
	 */
	public BufferedImage grayscale(BufferedImage image) throws IOException {
		long currentTime = System.nanoTime();

		BufferedImage result = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
		int width = image.getWidth();
		int height = image.getHeight();

		int[] inputData = getPixelData(image);

		int[] resultData = new int[inputData.length];

		for (int row = 0; row < height; row += 1) {
			for (int col = 0; col < width; col += 1) {
			    int index = row * width + col; 
			    int pixel = inputData[index]; 
			   
			    Pixel pixelData = new Pixel(pixel);
			    
			    int red = pixelData.getRed();
			    int green = pixelData.getGreen();
			    int blue = pixelData.getBlue();
			    
			    int gray = (int) (red * 0.299 + green * 0.587 + blue * 0.114);
			    
			    pixelData.setRed(gray);
			    pixelData.setGreen(gray);
			    pixelData.setBlue(gray);
			    
			    resultData[index] = pixelData.getData(); 
			}
		}

		result = convertPixelDataToImage(resultData, image, result);

		long finishTime = System.nanoTime();
		long timeSpent = (finishTime - currentTime) / 1000000;
		writer.writeToReadMe("grayscale", timeSpent);

		return result;
	}
	
	public BufferedImage sepia(BufferedImage image) throws IOException {
		long currentTime = System.nanoTime();

		BufferedImage result = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
		int width = image.getWidth();
		int height = image.getHeight();

		int[] inputData = getPixelData(image);

		int[] resultData = new int[inputData.length];

		for (int row = 0; row < height; row += 1) {
			for (int col = 0; col < width; col += 1) {
			    int index = row * width + col; 
			    int pixel = inputData[index]; 
			   
			    Pixel pixelData = new Pixel(pixel);
			    
			    int red = pixelData.getRed();
			    int green = pixelData.getGreen();
			    int blue = pixelData.getBlue();
			    
			    int average = (int) ((red + blue + green) / 3);
			    
			    red = average + (SEPIA_DEPTH * 2);
			    blue = average - SEPIA_INTENSITY;
			    green = average + SEPIA_DEPTH;
			    
			    if (red > 255) {
			    	red = 255;
			    }
			    
			    if (green > 255) {
			    	green = 255;
			    }
			    
			    if (blue < 0) {
			    	blue = 0;
			    }
			    
			    pixelData.setRed(red);
			    pixelData.setGreen(green);
			    pixelData.setBlue(blue);
			    
			    resultData[index] = pixelData.getData(); 
			}
		}

		result = convertPixelDataToImage(resultData, image, result);

		long finishTime = System.nanoTime();
		long timeSpent = (finishTime - currentTime) / 1000000;
		writer.writeToReadMe("sepia", timeSpent);

		return result;
	}
	
	public int[] getPixelData(BufferedImage image) {
		WritableRaster inputRaster = image.getRaster();
		DataBuffer idb = inputRaster.getDataBuffer();
		DataBufferInt inputBytes = (DataBufferInt) idb;
		
		return inputBytes.getData();
	}
	
	public BufferedImage convertPixelDataToImage(int[] resultData, BufferedImage image, BufferedImage result) {
		DataBufferInt rdb = new DataBufferInt(resultData, resultData.length);
		Raster resultRaster = Raster.createRaster(image.getSampleModel(), rdb, new Point(0, 0));
		result.setData(resultRaster);
		
		return result;
	}
}
