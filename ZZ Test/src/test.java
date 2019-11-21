import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class test {

	public static void main(String[] args) throws IOException {

		BufferedImage image = ImageIO.read(new File("C:\\Users\\Stone\\PokemonWild\\empty.png"));
		System.out.println(isEmpty(image));
	}

	private static boolean isEmpty(BufferedImage image) {
		for(int x = 0; x < image.getWidth(); x++) {
			for(int y = 0; y < image.getHeight(); y++) {
				if(image.getRGB(x, y) != 0) { // 0 is the rgb value if the pixel is transparent
					return false;
				}
			}
		}
		return true;
	}

}