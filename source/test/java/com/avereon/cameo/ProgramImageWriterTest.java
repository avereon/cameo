package com.avereon.cameo;

import com.avereon.xenon.ProgramIcon;
import com.avereon.xenon.icon.BrokenIcon;
import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ProgramImageWriterTest {

	private Path targetFolder = Paths.get( "target", "images" );

	@Test
	public void testSavePng() throws Exception {
		runTest( "testicon.png", ( path ) -> new ProgramImageWriter().save( new BrokenIcon(), path ) );
	}

	@Test
	public void testSaveIco() throws Exception {
		runTest( "testicon.ico", ( path ) -> new ProgramImageWriter().save( new BrokenIcon(), path ) );
	}

	@Test
	public void testSavePngWithOffset() throws Exception {
		runTest( "testiconoffset.png", ( path ) -> {
			ProgramIcon icon = new BrokenIcon();
			double size = icon.getSize();
			double pad = 0.2 * size;
			icon.relocate( pad, pad );
			new ProgramImageWriter().save( icon, path, size + 2 * pad, size + 2 * pad );
		} );
	}

	private void runTest( String name, Generator generator ) throws Exception {
		Path path = targetFolder.resolve( name );
		Files.deleteIfExists( path );
		assertFalse( Files.exists( path ) );
		generator.run( path );
		assertTrue( Files.exists( path ) );
	}

	private interface Generator {

		void run( Path path ) throws Exception;

	}

}
