package com.avereon.cameo;

import com.avereon.util.FileUtil;
import com.avereon.util.TextUtil;
import com.avereon.xenon.ProgramImage;
import com.avereon.xenon.util.FxUtil;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class ProgramImageWriter {

	public static void save( ProgramImage icon, Path folder, String file ) throws InterruptedException {
		save( icon, folder.resolve( file ) );
	}

	public static void save( ProgramImage icon, Path path ) throws InterruptedException {
		try {
			Platform.runLater( () -> saveImage( icon, path ) );
		} catch( IllegalStateException exception ) {
			Platform.startup( () -> saveImage( icon, path ) );
		}
		FxUtil.fxWait( 5000 );
	}

	private static void saveImage( ProgramImage icon, Path path ) {
		File absoluteFile = path.toFile().getAbsoluteFile();
		String type = FileUtil.getExtension( path );
		if( TextUtil.isEmpty( type ) ) type = "png";
		try {
			BufferedImage buffer = new BufferedImage( (int)icon.getWidth(), (int)icon.getHeight(), BufferedImage.TYPE_INT_ARGB );
			BufferedImage image = SwingFXUtils.fromFXImage( icon.getImage(), buffer );
			ImageIO.write( image, type, absoluteFile );
			System.out.println( "Image saved to: " + absoluteFile );
		} catch( IOException exception ) {
			System.err.println( "Unable to write image file: " + absoluteFile );
			exception.printStackTrace( System.err );
		}
	}

}
