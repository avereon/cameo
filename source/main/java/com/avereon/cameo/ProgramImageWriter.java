package com.avereon.cameo;

import com.avereon.util.FileUtil;
import com.avereon.util.TextUtil;
import com.avereon.xenon.ProgramImage;
import com.avereon.xenon.util.FxUtil;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import net.sf.image4j.codec.ico.ICOEncoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class ProgramImageWriter {

	private BufferedImage image;

	private Exception exception;

	void save( ProgramImage renderer, Path path ) throws Exception {
		save( List.of( renderer ), path );
	}

	void save( List<ProgramImage> renderers, Path path ) throws Exception {
		List<BufferedImage> images = renderers.stream().map( this::doCreateImage ).collect( Collectors.toList() );

		Path parent = path.getParent();
		if( !Files.exists( parent ) ) Files.createDirectories( parent );
		File absoluteFile = path.toFile().getAbsoluteFile();
		String type = FileUtil.getExtension( path );
		if( TextUtil.isEmpty( type ) ) type = "png";

		if( "ico".equals( type ) ) {
			ICOEncoder.write( images, absoluteFile );
		} else {
			ImageIO.write( images.get( 0 ), type, absoluteFile );
		}
	}

	private BufferedImage doCreateImage( ProgramImage icon ) {
		try {
			Platform.runLater( () -> doCreateImageFx( icon ) );
		} catch( IllegalStateException exception ) {
			Platform.startup( () -> doCreateImageFx( icon ) );
		}
		try {
			FxUtil.fxWait( 1000 );
		} catch( InterruptedException exception ) {
			// Intentionally ignore exception
		}
		return image;
	}

	private void doCreateImageFx( ProgramImage icon ) {
		BufferedImage buffer = new BufferedImage( (int)icon.getWidth(), (int)icon.getHeight(), BufferedImage.TYPE_INT_ARGB );
		image = SwingFXUtils.fromFXImage( icon.getImage(), buffer );
	}

}
