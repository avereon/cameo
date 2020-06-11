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
		ImageMetadata imageMetadata = getSingleImageMetadata();
		imageMetadata.setTarget( "target/images/bolt.gif" );
		imageMetadata.setSize( 256 );
		mojo.setImages( new ImageMetadata[]{ imageMetadata } );

		mojo.execute();

		assertTrue( new File( "target/images/bolt.gif" ).exists() );
	}

	@Test
	void testJpgImage() throws Exception {
		ImageMetadata imageMetadata = getSingleImageMetadata();
		imageMetadata.setTarget( "target/images/bolt.jpg" );
		imageMetadata.setFill( "#206080");
		imageMetadata.setSize( 256 );
		mojo.setImages( new ImageMetadata[]{ imageMetadata } );

		mojo.execute();

		assertTrue( new File( "target/images/bolt.jpg" ).exists() );
	}

	@Test
	void testPngImage() throws Exception {
		ImageMetadata imageMetadata = getSingleImageMetadata();
		imageMetadata.setTarget( "target/images/bolt.png" );
		imageMetadata.setSize( 256 );
		mojo.setImages( new ImageMetadata[]{ imageMetadata } );

		mojo.execute();

		assertTrue( new File( "target/images/bolt.png" ).exists() );
	}

	@Test
	void testPngImageDark() throws Exception {
		ImageMetadata imageMetadata = getSingleImageMetadata();
		imageMetadata.setTarget( "target/images/bolt-dark.png" );
		imageMetadata.setSize( 256 );
		imageMetadata.setTheme( "dark" );
		mojo.setImages( new ImageMetadata[]{ imageMetadata } );

		mojo.execute();

		assertTrue( new File( "target/images/bolt-dark.png" ).exists() );
	}

	@Test
	void testPngImageLight() throws Exception {
		ImageMetadata imageMetadata = getSingleImageMetadata();
		imageMetadata.setTarget( "target/images/bolt-light.png" );
		imageMetadata.setSize( 256 );
		imageMetadata.setTheme( "light" );
		mojo.setImages( new ImageMetadata[]{ imageMetadata } );

		mojo.execute();

		assertTrue( new File( "target/images/bolt-light.png" ).exists() );
	}

	@Test
	void testPngImageFill() throws Exception {
		ImageMetadata imageMetadata = getSingleImageMetadata();
		imageMetadata.setTarget( "target/images/bolt-fill.png" );
		imageMetadata.setTheme( "light" );
		imageMetadata.setFill( "#E0E0E0");
		imageMetadata.setSize( 256 );
		mojo.setImages( new ImageMetadata[]{ imageMetadata } );

		mojo.execute();

		assertTrue( new File( "target/images/bolt-fill.png" ).exists() );
	}

	@Test
	void testPngIcon() throws Exception {
		IconMetadata iconMetadata = getSingleImageIconMetadata();
		iconMetadata.setTarget( "target/icons/bolt.png" );
		mojo.setIcons( new IconMetadata[]{ iconMetadata } );

		mojo.execute();
		assertTrue( new File( "target/icons/bolt.png" ).exists() );
	}

	@Test
	void testPngIconDark() throws Exception {
		IconMetadata iconMetadata = getSingleImageIconMetadata();
		iconMetadata.setTarget( "target/icons/bolt-dark.png" );
		iconMetadata.setTheme( "dark" );
		mojo.setIcons( new IconMetadata[]{ iconMetadata } );

		mojo.execute();

		assertTrue( new File( "target/icons/bolt-dark.png" ).exists() );
	}

	@Test
	void testPngIconLight() throws Exception {
		IconMetadata iconMetadata = getSingleImageIconMetadata();
		iconMetadata.setTarget( "target/icons/bolt-light.png" );
		iconMetadata.setTheme( "light" );
		mojo.setIcons( new IconMetadata[]{ iconMetadata } );

		mojo.execute();

		assertTrue( new File( "target/icons/bolt-light.png" ).exists() );
	}

	@Test
	void testPngIconFill() throws Exception {
		IconMetadata iconMetadata = getSingleImageIconMetadata();
		iconMetadata.setTarget( "target/icons/bolt-fill.png" );
		iconMetadata.setFill( "#E0E0E0" );
		iconMetadata.setTheme( "light" );
		mojo.setIcons( new IconMetadata[]{ iconMetadata } );

		mojo.execute();

		assertTrue( new File( "target/icons/bolt-fill.png" ).exists() );
	}

	@Test
	void testIcoIcon() throws Exception {
		IconMetadata iconMetadata = getMultiImageIconMetadata();
		iconMetadata.setTarget( "target/icons/bolt.ico" );
		mojo.setIcons( new IconMetadata[]{ iconMetadata } );

		mojo.execute();

		assertTrue( new File( "target/icons/bolt.ico" ).exists() );
	}

	@Test
	void testIcoIconDark() throws Exception {
		IconMetadata iconMetadata = getMultiImageIconMetadata();
		iconMetadata.setTarget( "target/icons/bolt-dark.ico" );
		iconMetadata.setTheme( "dark" );
		mojo.setIcons( new IconMetadata[]{ iconMetadata } );

		mojo.execute();

		assertTrue( new File( "target/icons/bolt-dark.ico" ).exists() );
	}

	@Test
	void testIcoIconLight() throws Exception {
		IconMetadata iconMetadata = getMultiImageIconMetadata();
		iconMetadata.setTarget( "target/icons/bolt-light.ico" );
		iconMetadata.setTheme( "light" );
		mojo.setIcons( new IconMetadata[]{ iconMetadata } );

		mojo.execute();

		assertTrue( new File( "target/icons/bolt-light.ico" ).exists() );
	}

	@Test
	void testIcoIconFill() throws Exception {
		IconMetadata iconMetadata = getMultiImageIconMetadata();
		iconMetadata.setTarget( "target/icons/bolt-fill.ico" );
		iconMetadata.setFill( "#E0E0E0" );
		iconMetadata.setTheme( "light" );
		mojo.setIcons( new IconMetadata[]{ iconMetadata } );

		mojo.execute();

		assertTrue( new File( "target/icons/bolt-fill.ico" ).exists() );
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
			//exception.printStackTrace( System.err );
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

	private IconMetadata getSingleImageIconMetadata() {
		ImageMetadata imageMetadata = getSingleImageMetadata();
		imageMetadata.setSize( 64 );

		IconMetadata iconMetadata = new IconMetadata();
		iconMetadata.setImages( new ImageMetadata[]{ imageMetadata } );
		return iconMetadata;
	}

	private IconMetadata getMultiImageIconMetadata() {
		List<ImageMetadata> icons = new ArrayList<>();
		for( int exp = 8; exp >= 4; exp-- ) {
			ImageMetadata imageMetadata = getSingleImageMetadata();
			imageMetadata.setSize( (int)Math.pow( 2, exp ) );
			icons.add( imageMetadata );
		}

		IconMetadata iconMetadata = new IconMetadata();
		iconMetadata.setImages( icons.toArray( new ImageMetadata[ 0 ] ) );
		return iconMetadata;
	}

	private ImageMetadata getSingleImageMetadata() {
		ImageMetadata imageMetadata = new ImageMetadata();
		imageMetadata.setClass( TestIcon.class.getName() );
		return imageMetadata;
	}

}
