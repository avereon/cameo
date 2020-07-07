package com.avereon.cameo;

import com.avereon.venza.image.VectorImage;
import com.avereon.venza.image.VectorImageWriter;
import com.avereon.venza.style.Theme;
import javafx.scene.paint.Color;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings( "unused" )
@Mojo( name = "render", defaultPhase = LifecyclePhase.PREPARE_PACKAGE )
public class RenderMojo extends AbstractMojo {

	@Parameter( readonly = true, defaultValue = "${project}" )
	private MavenProject project;

	@Parameter( property = "images" )
	private ImageMetadata[] images;

	@Parameter( property = "icons" )
	private IconMetadata[] icons;

	ClassLoader loader;

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		if( images == null && icons == null ) throw new MojoExecutionException( "No images or icons to render" );

		try {
			List<URL> urls = List.of( new File( project.getBuild().getOutputDirectory() ).toURI().toURL() );
			//urls.forEach( u -> getLog().info( "url=" + u ) );

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

			Color fill = null;
			if( iconMetadata.getFill() != null ) fill = Color.web( iconMetadata.getFill() );

			// Create the renderers
			List<VectorImage> renderers = new ArrayList<>();
			for( ImageMetadata imageMetadata : iconMetadata.getImages() ) {
				if( iconMetadata.getTheme() != null && imageMetadata.getTheme() == null ) imageMetadata.setTheme( iconMetadata.getTheme() );
				renderers.add( createRenderer( imageMetadata ) );
			}

			new VectorImageWriter().save( renderers, target, fill );
		}
	}

	private void renderImages( Path output ) throws Exception {
		if( images == null ) return;
		for( ImageMetadata imageMetadata : images ) {
			Path target = output.resolve( imageMetadata.getTarget() );
			getLog().info( "Render image " + target.toAbsolutePath() );

			VectorImage renderer = createRenderer( imageMetadata );
			double width = renderer.getWidth();
			double height = renderer.getHeight();
			Color fill = null;
			if( imageMetadata.getImageWidth() != null ) width = imageMetadata.getImageWidth();
			if( imageMetadata.getImageHeight() != null ) height = imageMetadata.getImageHeight();
			if( imageMetadata.getFill() != null ) fill = Color.web( imageMetadata.getFill() );
			new VectorImageWriter().save( renderer, target, width, height, fill );
		}
	}

	private VectorImage createRenderer( ImageMetadata imageMetadata ) throws Exception {
		try {
			Class<? extends VectorImage> imageClass = getImageClass( imageMetadata.getImageClass() );
			Constructor<? extends VectorImage> constructor = imageClass.getConstructor();
			VectorImage renderer = constructor.newInstance();

			if( imageMetadata.getSize() != null ) renderer.resize( imageMetadata.getSize() );
			if( imageMetadata.getWidth() != null ) renderer.setWidth( imageMetadata.getWidth() );
			if( imageMetadata.getHeight() != null ) renderer.setHeight( imageMetadata.getHeight() );

			double offsetX = 0;
			double offsetY = 0;
			if( imageMetadata.getOffsetX() != null ) offsetX = imageMetadata.getOffsetX();
			if( imageMetadata.getOffsetY() != null ) offsetY = imageMetadata.getOffsetY();
			renderer.relocate( offsetX, offsetY );

			String theme = imageMetadata.getTheme();
			if( theme == null ) theme = Theme.DARK.name();
			renderer.setTheme( Theme.valueOf( theme.toUpperCase() ) );

			return renderer;
		} catch( ClassNotFoundException exception ) {
			getLog().error( "Unable to load renderer: " + imageMetadata.getImageClass(), exception );
			throw exception;
		}
	}

	@SuppressWarnings( "unchecked" )
	private Class<? extends VectorImage> getImageClass( String className ) throws ClassNotFoundException {
		return (Class<? extends VectorImage>)Class.forName( className, true, loader );
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
