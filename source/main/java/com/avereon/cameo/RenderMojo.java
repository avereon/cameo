package com.avereon.cameo;

import com.avereon.venza.image.ProgramImage;
import com.avereon.venza.image.ProgramImageWriter;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.lang.reflect.Constructor;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@SuppressWarnings( "unused" )
@Mojo( name = "render", defaultPhase = LifecyclePhase.PREPARE_PACKAGE, requiresDependencyCollection = ResolutionScope.COMPILE, requiresDependencyResolution = ResolutionScope.COMPILE )
public class RenderMojo extends AbstractMojo {

	@Parameter( readonly = true, defaultValue = "${project}" )
	private MavenProject project;

	@Parameter( property = "images" )
	private ImageMetadata[] images;

	@Parameter( property = "icons" )
	private IconMetadata[] icons;

	private ClassLoader loader;

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		if( images == null && icons == null ) throw new MojoExecutionException( "No images or icons to render" );

		// Sort the artifacts
		Set<Artifact> artifacts = project.getArtifacts();
		List<URL> urls = artifacts.stream().map( a -> {
			try {
				return a.getFile().toURI().toURL();
			} catch( MalformedURLException e ) {
				return null;
			}
		} ).filter( Objects::nonNull ).collect( Collectors.toList() );

		try {
			urls.add( 0, new File( project.getBuild().getOutputDirectory() ).toURI().toURL() );
			urls.forEach( u -> getLog().debug( "url=" + u ) );

			// Create special class loader that includes the recently created classes
			loader = new URLClassLoader( urls.toArray( new URL[ 0 ] ), getClass().getClassLoader() );

			Path targetFolder = project.getBasedir().toPath();
			renderImages( targetFolder );
			renderIcons( targetFolder );
		} catch( Throwable throwable ) {
			throw new MojoFailureException( throwable.getMessage(), throwable );
		}
	}

	private void renderIcons( Path output ) throws Exception {
		if( icons == null ) return;
		for( IconMetadata iconMetadata : icons ) {
			Path target = output.resolve( iconMetadata.getTarget() );
			getLog().info( "Render icon " + target.toAbsolutePath() );

			// Create the renderers
			List<ProgramImage> renderers = new ArrayList<>();
			for( ImageMetadata imageMetadata : iconMetadata.getImages() ) {
				renderers.add( createRenderer( imageMetadata ) );
			}

			new ProgramImageWriter().save( renderers, target );
		}
	}

	private void renderImages( Path output ) throws Exception {
		if( images == null ) return;
		for( ImageMetadata imageMetadata : images ) {
			Path target = output.resolve( imageMetadata.getTarget() );
			getLog().info( "Render image " + target.toAbsolutePath() );

			ProgramImage renderer = createRenderer( imageMetadata );
			double width = renderer.getWidth();
			double height = renderer.getHeight();
			if( imageMetadata.getImageWidth() != null ) width = imageMetadata.getImageWidth();
			if( imageMetadata.getImageHeight() != null ) height = imageMetadata.getImageHeight();
			new ProgramImageWriter().save( renderer, target, width, height );
		}
	}

	private ProgramImage createRenderer( ImageMetadata imageMetadata ) throws Exception {
		try {
			Class<? extends ProgramImage> imageClass = getImageClass( imageMetadata.getImageClass() );
			Constructor<? extends ProgramImage> constructor = imageClass.getConstructor();
			ProgramImage renderer = constructor.newInstance();

			if( imageMetadata.getSize() != null ) renderer.setSize( imageMetadata.getSize() );
			if( imageMetadata.getWidth() != null ) renderer.setWidth( imageMetadata.getWidth() );
			if( imageMetadata.getHeight() != null ) renderer.setHeight( imageMetadata.getHeight() );

			double offsetX = 0;
			double offsetY = 0;
			if( imageMetadata.getOffsetX() != null ) offsetX = imageMetadata.getOffsetX();
			if( imageMetadata.getOffsetY() != null ) offsetY = imageMetadata.getOffsetY();
			renderer.relocate( offsetX, offsetY );

			return renderer;
		} catch( ClassNotFoundException exception ) {
			getLog().error( "Unable to load renderer: " + imageMetadata.getImageClass(), exception );
			throw exception;
		}
	}

	@SuppressWarnings( "unchecked" )
	private Class<? extends ProgramImage> getImageClass( String className ) throws ClassNotFoundException {
		return (Class<? extends ProgramImage>)Class.forName( className, true, loader );
	}

	public MavenProject getProject() {
		return project;
	}

	public void setProject( MavenProject project ) {
		this.project = project;
	}

	public ImageMetadata[] getImages() {
		return images;
	}

	public void setImages( ImageMetadata[] images ) {
		this.images = images;
	}

	public IconMetadata[] getIcons() {
		return icons;
	}

	public void setIcons( IconMetadata[] icons ) {
		this.icons = icons;
	}

}
