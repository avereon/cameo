package com.avereon.cameo;

import com.avereon.venza.icon.BrokenIcon;
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
	void testPngImage() throws Exception {
		ImageMetadata imageMetadata = new ImageMetadata();
		imageMetadata.setClass( BrokenIcon.class.getName() );
		imageMetadata.setSize( 256 );
		imageMetadata.setTarget( "target/images/broken.png" );
		mojo.setImages( new ImageMetadata[]{ imageMetadata } );

		mojo.execute();

		assertTrue( new File( "target/images/broken.png" ).exists() );
	}

	@Test
	void testPngIcon() throws Exception {
		ImageMetadata imageMetadata = new ImageMetadata();
		imageMetadata.setClass( BrokenIcon.class.getName() );
		imageMetadata.setSize( 64 );
		IconMetadata iconMetadata = new IconMetadata();
		iconMetadata.setImages( new ImageMetadata[]{ imageMetadata } );
		iconMetadata.setTarget( "target/icons/broken.png" );
		mojo.setIcons( new IconMetadata[]{ iconMetadata } );

		mojo.execute();

		assertTrue( new File( "target/icons/broken.png" ).exists() );
	}

	@Test
	void testIcoIcon() throws Exception {
		List<ImageMetadata> icons = new ArrayList<>();
		for( int exp = 8; exp >= 4; exp--) {
			ImageMetadata imageMetadata = new ImageMetadata();
			imageMetadata.setClass( BrokenIcon.class.getName() );
			imageMetadata.setSize( (int)Math.pow( 2, exp) );
			icons.add( imageMetadata );
		}
		IconMetadata iconMetadata = new IconMetadata();
		iconMetadata.setImages( icons.toArray( new ImageMetadata[0] ) );
		iconMetadata.setTarget( "target/icons/broken.ico" );
		mojo.setIcons( new IconMetadata[]{ iconMetadata } );

		mojo.execute();

		assertTrue( new File( "target/icons/broken.ico" ).exists() );
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
