package com.avereon.cameo;

import com.avereon.xenon.icon.WingDiscLargeIcon;
import com.avereon.xenon.icon.XRingLargeIcon;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {

	public static void main( String[] commands ) {
		Path downloads = Paths.get( System.getProperty( "user.home" ), "Downloads" );
		try {
			ProgramImageWriter.save( new WingDiscLargeIcon(), downloads.resolve( "avereon.png" ).toFile() );
			ProgramImageWriter.save( new XRingLargeIcon(), downloads.resolve( "xenon.png" ).toFile() );
			ProgramImageWriter.done();
		} catch( Throwable throwable ) {
			throwable.printStackTrace( System.err );
			System.exit( -1 );
		}
	}

}
