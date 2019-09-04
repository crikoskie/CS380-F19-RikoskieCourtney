package kings.image;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * The image processor GUI.
 * 
 * @author Courtney Rikoskie
 * @version 09-03-19
 */
public class ImageGUI extends JFrame implements ActionListener {

	/** Generated unique serial ID. */
	private static final long serialVersionUID = -83467130412987566L;

	/** The image processor. */
	private ImageProcessor processor;

	/** The save menu item. */
	private JMenuItem saveItem;
	/** The exit menu item. */
	private JMenuItem exitItem;
	/** The open menu item. */
	private JMenuItem openItem;
	/** The clear menu item. */
	private JMenuItem clearItem;
	/** The button to grayscale image. */
	private JButton gray;
	/** The image input panel. */
	private JPanel input;
	/** The image output panel. */
	private JPanel output;
	/** The panel that contains the rest of the components. */
	private JPanel mainPanel;
	/** The original image. */
	private BufferedImage inputImage;
	/** The edited image. */
	private BufferedImage outputImage;
	/** The panel containing the images. */
	private JPanel imagesPanel;
	/** The panel containing the buttons. */
	private JPanel buttonPanel;

	/**
	 * Creates the image processor GUI.
	 */
	public ImageGUI() {
		processor = new ImageProcessor();

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new FlowLayout());

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu fileMenu = new JMenu("File");
		menuBar.add(fileMenu);

		openItem = new JMenuItem("Open");
		openItem.addActionListener(this);
		fileMenu.add(openItem);

		saveItem = new JMenuItem("Save");
		saveItem.addActionListener(this);
		fileMenu.add(saveItem);

		clearItem = new JMenuItem("Clear");
		clearItem.addActionListener(this);
		fileMenu.add(clearItem);

		exitItem = new JMenuItem("Exit");
		exitItem.addActionListener(this);
		fileMenu.add(exitItem);

		inputImage = null;
		outputImage = null;

		input = new JPanel(new FlowLayout()) {
			private static final long serialVersionUID = 8512292549566217461L;

			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(inputImage, 0, 0, null);
			}
		};

		output = new JPanel(new FlowLayout()) {
			private static final long serialVersionUID = 8310832477514305462L;

			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(outputImage, 0, 0, null);
			}
		};

		buttonPanel = new JPanel();

		gray = new JButton("Grayscale");
		gray.addActionListener(this);

		buttonPanel.add(gray);

		imagesPanel = new JPanel();

		GroupLayout imagesLayout = new GroupLayout(imagesPanel);
		imagesPanel.setLayout(imagesLayout);

		mainPanel = new JPanel();

		GroupLayout mainLayout = new GroupLayout(mainPanel);
		mainPanel.setLayout(mainLayout);

		add(mainPanel);

		imagesLayout.setAutoCreateGaps(true);
		imagesLayout.setAutoCreateContainerGaps(true);

		mainLayout.setAutoCreateGaps(true);
		mainLayout.setAutoCreateContainerGaps(true);

		imagesLayout.setHorizontalGroup(imagesLayout.createSequentialGroup().addComponent(input).addComponent(output));
		imagesLayout.setVerticalGroup(imagesLayout.createSequentialGroup().addGroup(imagesLayout
				.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(input).addComponent(output)));

		mainLayout.setHorizontalGroup(mainLayout.createSequentialGroup()
				.addGroup(mainLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(imagesPanel)
						.addComponent(buttonPanel)));
		mainLayout.setVerticalGroup(
				mainLayout.createSequentialGroup().addComponent(imagesPanel).addComponent(buttonPanel));

		pack();
		setVisible(true);
	}

	/**
	 * Default action listener.
	 * 
	 * @param event
	 *            The action event.
	 */
	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == saveItem) {
			JFileChooser chooser = new JFileChooser();
			chooser.showSaveDialog(null);

			File saveFile = chooser.getSelectedFile();

			try {
				ImageIO.write(outputImage, "png", saveFile);
			} catch (IOException e) {
				JOptionPane.showMessageDialog(this, "Could not save image.", "Error", JOptionPane.ERROR_MESSAGE);
			}
		} else if (event.getSource() == exitItem) {
			System.exit(0);
		} else if (event.getSource() == clearItem) {
			if (inputImage != null) {
				inputImage = new BufferedImage(inputImage.getWidth(), inputImage.getHeight(),
						BufferedImage.TYPE_INT_ARGB);
				Graphics g = inputImage.getGraphics();
				g.drawImage(inputImage, 0, 0, null);
			}

			if (outputImage != null) {
				outputImage = new BufferedImage(outputImage.getWidth(), outputImage.getHeight(),
						BufferedImage.TYPE_INT_ARGB);
				Graphics g = outputImage.getGraphics();
				g.drawImage(outputImage, 0, 0, null);
			}

			input.repaint();
			output.repaint();
			
			inputImage = null;
			outputImage = null;
		} else if (event.getSource() == openItem) {
			JFileChooser chooser = new JFileChooser();
			chooser.showOpenDialog(null);

			File imageFile = chooser.getSelectedFile();

			BufferedImage bi = null;

			try {
				bi = ImageIO.read(imageFile);

				if (bi != null) {
					inputImage = new BufferedImage(bi.getWidth(), bi.getHeight(), BufferedImage.TYPE_INT_ARGB);
					Graphics g = inputImage.getGraphics();

					int width = inputImage.getWidth();
					int height = inputImage.getHeight();

					input.setPreferredSize(new Dimension(width + 50, height + 50));
					output.setPreferredSize(new Dimension(width + 50, height + 50));
					imagesPanel.setPreferredSize(new Dimension(width * 2 + 50, height + 50));
					mainPanel.setPreferredSize(new Dimension(width * 2 + 50, height + gray.getHeight() + 75));
					pack();

					g.drawImage(bi, 0, 0, null);
					input.repaint();

					if (outputImage != null) {
						outputImage = new BufferedImage(outputImage.getWidth(), outputImage.getHeight(),
								BufferedImage.TYPE_INT_ARGB);
						g = outputImage.getGraphics();
						g.drawImage(outputImage, 0, 0, null);
					}

					output.repaint();
				} else {
					JOptionPane.showMessageDialog(this, "The file you chose was not an image.", "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			} catch (IOException e) {
				JOptionPane.showMessageDialog(this, "Could not open image.", "Error", JOptionPane.ERROR_MESSAGE);
			} catch (IllegalArgumentException e) {
				// do nothing
			}
		} else if (event.getSource() == gray) {
			if (inputImage != null) {
				try {
					outputImage = processor.grayscale(inputImage);
				} catch (IOException e) {
					JOptionPane.showMessageDialog(this, "Could not write time spent to ReadMe file.", "Error",
							JOptionPane.ERROR_MESSAGE);
				}

				output.repaint();
			}
		}
	}
}
