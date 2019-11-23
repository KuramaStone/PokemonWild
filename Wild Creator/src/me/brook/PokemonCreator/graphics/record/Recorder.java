package me.brook.PokemonCreator.graphics.record;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;

public class Recorder {

	private static final File parent = new File("C:\\Users\\Stone\\PokemonWild\\recording");

	private List<BufferedImage> images;

	public Recorder() {
		images = new ArrayList<>();
	}

	public void saveImages() throws FileNotFoundException, IOException {
		ImageOutputStream outputStream = new FileImageOutputStream(new File(parent, System.currentTimeMillis() + ".gif"));
		GifSequenceWriter writer = new GifSequenceWriter(outputStream, BufferedImage.TYPE_INT_RGB, 1000 / 30,
				true);

		for(BufferedImage image : new ArrayList<>(images)) {
			writer.writeToSequence(image);
		}
		
		writer.close();

		images.clear();
	}

	public void addImage(BufferedImage buffer) {
		images.add(buffer);
	}

}
