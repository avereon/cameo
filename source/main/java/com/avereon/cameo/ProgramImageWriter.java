package com.avereon.cameo;

import com.avereon.xenon.ProgramImage;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ProgramImageWriter implements Runnable {

	private ProgramImage icon;

	private File file;

	private ProgramImageWriter( ProgramImage icon, File file ) {
		this.icon = icon;
		this.file = file;
	}

	public static void save( ProgramImage icon, File file ) {
		try {
			Platform.runLater( new ProgramImageWriter( icon, file ) );
		} catch( IllegalStateException exception ) {
			Platform.startup( new ProgramImageWriter( icon, file ) );
		}
	}

	public static void done() {
		Platform.exit();
	}

	public void run() {
		saveImage( icon, file );
	}

	private void saveImage( ProgramImage icon, File file ) {
		File absoluteFile = file.getAbsoluteFile();
		try {
			BufferedImage image = SwingFXUtils.fromFXImage( icon.getImage(), new BufferedImage( (int)icon.getWidth(), (int)icon.getHeight(), BufferedImage.TYPE_INT_ARGB ) );
			ImageIO.write( image, "png", absoluteFile );
			System.out.println( "Image saved to: " + absoluteFile );
		} catch( IOException exception ) {
			System.err.println( "Unable to write image file: " + absoluteFile );
			exception.printStackTrace( System.err );
		}
	}

}
