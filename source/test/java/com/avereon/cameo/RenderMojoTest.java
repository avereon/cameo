package com.avereon.cameo;

import org.apache.maven.model.Build;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class RenderMojoTest {

	private RenderMojo mojo;

	@BeforeEach
	void setup() {
		Build build = new Build();
		build.setOutputDirectory( "target" );

		MavenProject project = new MavenProject();
		project.setBuild( build );
		project.setBasedir( new File( "." ) );

		mojo = new RenderMojo();
		mojo.setProject( project );
	}

	@Test
	void testGifImage() throws Exception {
		ImageMetadata imageMetadata = new ImageMetadata();
		imageMetadata.setClass( TestIcon.class.getName() );
		imageMetadata.setTarget( "target/images/bolt.gif" );
		imageMetadata.setSize( 256 );
		mojo.setImages( new ImageMetadata[]{ imageMetadata } );

		mojo.execute();

		assertTrue( new File( "target/images/bolt.gif" ).exists() );
	}

	@Test
	void testPngIcon() throws Exception {
		ImageMetadata imageMetadata = new ImageMetadata();
		imageMetadata.setClass( TestIcon.class.getName() );
		imageMetadata.setSize( 64 );
		IconMetadata iconMetadata = new IconMetadata();
		iconMetadata.setImages( new ImageMetadata[]{ imageMetadata } );
		iconMetadata.setTarget( "target/icons/bolt.png" );
		mojo.setIcons( new IconMetadata[]{ iconMetadata } );

		mojo.execute();
		assertTrue( new File( "target/icons/bolt.png" ).exists() );

		iconMetadata.setTarget( "target/icons/bolt-dark.png" );
		iconMetadata.setTheme( "dark" );
		mojo.execute();
		assertTrue( new File( "target/icons/bolt-dark.png" ).exists() );

		iconMetadata.setTarget( "target/icons/bolt-light.png" );
		iconMetadata.setTheme( "light" );
		mojo.execute();
		assertTrue( new File( "target/icons/bolt-light.png" ).exists() );
	}

	@Test
	void testIcoIcon() throws Exception {
		List<ImageMetadata> icons = new ArrayList<>();
		for( int exp = 8; exp >= 4; exp--) {
			ImageMetadata imageMetadata = new ImageMetadata();
			imageMetadata.setClass( TestIcon.class.getName() );
			imageMetadata.setSize( (int)Math.pow( 2, exp) );
			icons.add( imageMetadata );
		}
		IconMetadata iconMetadata = new IconMetadata();
		iconMetadata.setImages( icons.toArray( new ImageMetadata[0] ) );
		iconMetadata.setTarget( "target/icons/bolt.ico" );
		mojo.setIcons( new IconMetadata[]{ iconMetadata } );

		mojo.execute();

		assertTrue( new File( "target/icons/bolt.ico" ).exists() );
	}

	@Test
	void testExecuteWithInvalidImage() throws Exception {
		ImageMetadata im1 = new ImageMetadata();
		im1.setClass( "invalid image class name" );

		mojo.setImages( new ImageMetadata[]{ im1 } );

		try {
			mojo.execute();
			fail( "Should have thrown a MojoFailureException" );
		} catch( MojoFailureException exception ) {
			exception.printStackTrace( System.err );
			assertThat( exception.getCause(), instanceOf( NullPointerException.class ) );
			assertThat( exception.getMessage(), is( nullValue() ) );
		}
	}

	@Test
	void testExecuteWithNoImagesOrIcons() throws Exception {
		try {
			mojo.execute();
			fail( "Should have thrown a MojoExecutionException" );
		} catch( MojoExecutionException exception ) {
			assertThat( exception.getMessage(), is( "No images or icons to render" ) );
		}
	}

}
