package com.avereon.cameo;

import com.avereon.venza.icon.BrokenIcon;
import org.apache.maven.model.Build;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

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
	void testExecuteWithImage() throws Exception {
		ImageMetadata imageMetadata = new ImageMetadata();
		imageMetadata.setClass( BrokenIcon.class.getName() );
		imageMetadata.setTarget( "target/images/broken.png" );
		mojo.setImages( new ImageMetadata[]{ imageMetadata } );

		mojo.execute();

		assertTrue( new File( "target/images/broken.png" ).exists() );
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
