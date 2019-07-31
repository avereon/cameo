package com.avereon.cameo;

import com.avereon.xenon.ProgramImage;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.lang.reflect.Constructor;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Mojo( name = "render", defaultPhase = LifecyclePhase.PREPARE_PACKAGE )
public class RenderMojo extends AbstractMojo {

	@Parameter( readonly = true, defaultValue = "${project}" )
	private MavenProject project;

	@Parameter( property = "images" )
	private ImageMetadata[] images;

	@Parameter( property = "icons" )
	private IconMetadata[] icons;

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		if( images == null ) {
			getLog().info( "No images to render" );
			return;
		}

		Path output = project.getBasedir().toPath();

		// Go through the images and write files
		for( ImageMetadata imageMetadata : images ) {
			Path target = output.resolve( imageMetadata.getTarget() );
			getLog().info( "Rendering image " + target.toAbsolutePath() );

			try {
				new ProgramImageWriter().save( createRenderer( imageMetadata ), target );
			} catch( Exception exception ) {
				getLog().error( "Unable to render image " + imageMetadata.getImageClass(), exception );
			}
		}

		for( IconMetadata iconMetadata : icons ) {
			Path target = output.resolve( iconMetadata.getTarget() );
			getLog().info( "Rendering icon " + target.toAbsolutePath() );

			// Create the renderers
			List<ProgramImage> renderers = new ArrayList<>();
			for( ImageMetadata imageMetadata : iconMetadata.getImages() ) {
				try {
					renderers.add( createRenderer( imageMetadata ));
				} catch( Exception exception ) {
					// Intentionally skip image
				}
			}


			try {
				new ProgramImageWriter().save( renderers, target );
			} catch( Exception exception ) {
				getLog().error( "Unable to render icon ", exception );
			}
		}
	}

	private ProgramImage createRenderer( ImageMetadata imageMetadata ) throws Exception {
		Class<? extends ProgramImage> imageClass = (Class<? extends ProgramImage>)Class.forName( imageMetadata.getImageClass() );
		Constructor<? extends ProgramImage> constructor = imageClass.getConstructor();
		ProgramImage renderer = constructor.newInstance();

		if( imageMetadata.getSize() != null ) renderer.setSize(  imageMetadata.getSize() );
		if( imageMetadata.getWidth() != null ) renderer.setWidth(  imageMetadata.getWidth() );
		if( imageMetadata.getHeight() != null ) renderer.setHeight( imageMetadata.getHeight() );

		return renderer;
	}

	public ImageMetadata[] getImages() {
		return images;
	}

	public void setImages( ImageMetadata[] images ) {
		this.images = images;
	}

}
