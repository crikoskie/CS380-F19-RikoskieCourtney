package kings.image;

import java.awt.Color;
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
				Color c = new Color(image.getRGB(col, row));
				int red = c.getRed();
				int green = c.getGreen();
				int blue = c.getBlue();

				int gray = (int) (0.299 * red + 0.587 * green + 0.114 * blue);

				Color newColor = new Color(gray, gray, gray);
				resultData[row * width + col] = newColor.getRGB();
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
