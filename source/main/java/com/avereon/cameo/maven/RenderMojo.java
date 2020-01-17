package com.avereon.cameo.maven;

import com.avereon.cameo.ProgramImageWriter;
import com.avereon.venza.image.ProgramImage;
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

	private ClassLoader loader;

	//	java.lang.NullPointerException
	//	at java.util.Objects.requireNonNull (Objects.java:222)
	//	at java.util.ImmutableCollections$List12.<init> (ImmutableCollections.java:392)
	//	at java.util.List.of (List.java:808)
	//	at com.avereon.cameo.ProgramImageWriter.save (ProgramImageWriter.java:41)
	//	at com.avereon.cameo.maven.RenderMojo.renderImages (RenderMojo.java:81)
	//	at com.avereon.cameo.maven.RenderMojo.execute (RenderMojo.java:44)
	//	at org.apache.maven.plugin.DefaultBuildPluginManager.executeMojo (DefaultBuildPluginManager.java:137)
	//	at org.apache.maven.lifecycle.internal.MojoExecutor.execute (MojoExecutor.java:210)
	//	at org.apache.maven.lifecycle.internal.MojoExecutor.execute (MojoExecutor.java:156)
	//	at org.apache.maven.lifecycle.internal.MojoExecutor.execute (MojoExecutor.java:148)
	//	at org.apache.maven.lifecycle.internal.LifecycleModuleBuilder.buildProject (LifecycleModuleBuilder.java:117)
	//	at org.apache.maven.lifecycle.internal.LifecycleModuleBuilder.buildProject (LifecycleModuleBuilder.java:81)
	//	at org.apache.maven.lifecycle.internal.builder.singlethreaded.SingleThreadedBuilder.build (SingleThreadedBuilder.java:56)
	//	at org.apache.maven.lifecycle.internal.LifecycleStarter.execute (LifecycleStarter.java:128)
	//	at org.apache.maven.DefaultMaven.doExecute (DefaultMaven.java:305)
	//	at org.apache.maven.DefaultMaven.doExecute (DefaultMaven.java:192)

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		if( images == null && icons == null ) throw new MojoExecutionException( "No images or icons to render" );

		try {
			// Create special class loader that includes the recently created classes
			loader = new URLClassLoader( new URL[]{ new File( project.getBuild().getOutputDirectory() ).toURI().toURL() }, getClass().getClassLoader() );

			Path targetFolder = project.getBasedir().toPath();
			renderImages( targetFolder );
			renderIcons( targetFolder );
		} catch( Throwable throwable ) {
			throw new MojoFailureException( throwable.getMessage(), throwable );
		}
	}

	private void renderIcons( Path output ) {
		for( IconMetadata iconMetadata : icons ) {
			Path target = output.resolve( iconMetadata.getTarget() );
			getLog().info( "Render icon " + target.toAbsolutePath() );

			try {
				// Create the renderers
				List<ProgramImage> renderers = new ArrayList<>();
				for( ImageMetadata imageMetadata : iconMetadata.getImages() ) {
					renderers.add( createRenderer( imageMetadata ) );
				}

				new ProgramImageWriter().save( renderers, target );
			} catch( Throwable throwable ) {
				getLog().error( "Unable to render icon: " + iconMetadata.getTarget(), throwable );
			}
		}
	}

	private void renderImages( Path output ) {
		for( ImageMetadata imageMetadata : images ) {
			Path target = output.resolve( imageMetadata.getTarget() );
			getLog().info( "Render image " + target.toAbsolutePath() );

			try {
				ProgramImage renderer = createRenderer( imageMetadata );
				double width = renderer.getWidth();
				double height = renderer.getHeight();
				if( imageMetadata.getImageWidth() != null ) width = imageMetadata.getImageWidth();
				if( imageMetadata.getImageHeight() != null ) height = imageMetadata.getImageHeight();
				new ProgramImageWriter().save( renderer, target, width, height );
			} catch( Throwable throwable ) {
				getLog().error( "Unable to render image: " + imageMetadata.getTarget(), throwable );
			}
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
