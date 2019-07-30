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

@Mojo( name = "render", defaultPhase = LifecyclePhase.PREPARE_PACKAGE )
public class RenderMojo extends AbstractMojo {

	@Parameter( readonly = true, defaultValue = "${project}" )
	private MavenProject project;

	@Parameter( property = "images" )
	private RenderImageMeta[] images;

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		if( images == null ) {
			getLog().info( "No images to render" );
			return;
		}

		Path output = project.getBasedir().toPath();

		// Go through the image classes and write files
		for( RenderImageMeta image : images ) {
			Path target = output.resolve( image.getTarget() );
			getLog().info( "Rendering " + image.getImageClass() + " to " + target.toAbsolutePath() );

			try {
				Class<? extends ProgramImage> imageClass = (Class<? extends ProgramImage>)Class.forName( image.getImageClass() );
				Constructor<? extends ProgramImage> constructor = imageClass.getConstructor();
				new ProgramImageWriter().save( constructor.newInstance(), target );
			} catch( Exception exception ) {
				getLog().error( "Unable to render image " + image.getImageClass() + exception );
			}
		}
	}

	public RenderImageMeta[] getImages() {
		return images;
	}

	public void setImages( RenderImageMeta[] images ) {
		this.images = images;
	}

}
