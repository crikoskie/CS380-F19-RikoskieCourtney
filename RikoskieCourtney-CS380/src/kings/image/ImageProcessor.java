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

		WritableRaster inputRaster = image.getRaster();
		DataBuffer idb = inputRaster.getDataBuffer();
		DataBufferInt inputBytes = (DataBufferInt) idb;
		int[] inputData = inputBytes.getData();

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

		DataBufferInt rdb = new DataBufferInt(resultData, resultData.length);
		Raster resultRaster = Raster.createRaster(image.getSampleModel(), rdb, new Point(0, 0));
		result.setData(resultRaster);

		long finishTime = System.nanoTime();
		long timeSpent = (finishTime - currentTime) / 1000000;
		writer.writeToReadMe("grayscale", timeSpent);

		return result;
	}
}
