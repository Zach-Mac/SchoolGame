package main;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;

public class Sprite {
	private int id;
	private int width, height;
	private String file;

	public static final Sprite pacMan = new Sprite("PacMan");
	public static final Sprite feelsBad = new Sprite("FeelsBad");
	
	public Sprite(String file) {
		this.file = file;
		BufferedImage image;
		try {
			image = ImageIO.read(new File("./res/sprites/" + file + ".png"));
			width = image.getWidth();
			height = image.getHeight();

			int[] pixels_raw = new int[width * height];
			pixels_raw = image.getRGB(0, 0, width, height, null, 0, width);

			ByteBuffer pixels = BufferUtils.createByteBuffer(width * height * 4);

			for (int i = 0; i < height; i++) {
				for (int j = 0; j < width; j++) {
					int pixel = pixels_raw[i * width + j];
					pixels.put((byte) ((pixel >> 16) & 0xFF)); // Red
					pixels.put((byte) ((pixel >> 8) & 0xFF)); // Green
					pixels.put((byte) ((pixel) & 0xFF)); // Blue
					pixels.put((byte) ((pixel >> 24) & 0xFF)); // Alpha
				}
			}

			pixels.flip();

			id = glGenTextures();

			glBindTexture(GL_TEXTURE_2D, id);

			glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
			glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, pixels);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Sprite(int[] pixels_raw, int size, int num) {
		
		ByteBuffer pixels = BufferUtils.createByteBuffer(size * size * 4);
			
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				int pixel = pixels_raw[i * size + j];
				pixels.put((byte) ((pixel >> 16) & 0xFF)); // Red
				pixels.put((byte) ((pixel >> 8) & 0xFF)); // Green
				pixels.put((byte) ((pixel) & 0xFF)); // Blue
				pixels.put((byte) ((pixel >> 24) & 0xFF)); // Alpha
			}
		}

		pixels.flip();
		
		id = glGenTextures();
		
		glBindTexture(GL_TEXTURE_2D, id);
	
		glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
	
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, pixels);
	}

	public String getFile(){
		return file;
	}
	
	public void bind(int sampler) {
		if (sampler >= 0 && sampler <= 31){
			glActiveTexture(GL_TEXTURE0 + sampler);
			glBindTexture(GL_TEXTURE_2D, id);
		}
	}
	
	protected void finalize() throws Throwable{
		glDeleteTextures(id);
		super.finalize();
	}
}
