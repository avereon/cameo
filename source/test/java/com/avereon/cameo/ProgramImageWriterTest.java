package com.avereon.cameo;

import com.avereon.util.FileUtil;
import com.avereon.xenon.icon.WingDiscLargeIcon;
import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.Assert.assertTrue;

public class ProgramImageWriterTest {

	@Test
	public void testSave() throws Exception {
		Path tmp = FileUtil.createTempFolder( "", "" );
		try {
			Path icon = tmp.resolve( "icons/avereon.png" );
			new ProgramImageWriter().save( new WingDiscLargeIcon(), icon );
			assertTrue( Files.exists( icon ) );
		} finally {
			FileUtil.delete( tmp );
		}
	}

}
