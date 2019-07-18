package com.avereon.cameo;

import com.avereon.xenon.icon.WingDiscLargeIcon;
import com.avereon.xenon.icon.XRingLargeIcon;

import java.io.File;

public class Main {

	public static void main( String[] commands ) {
		try {
			ProgramImageWriter.save( new WingDiscLargeIcon(), new File( "avereon.png" ) );
			ProgramImageWriter.save( new XRingLargeIcon(), new File( "xenon.png" ) );
			ProgramImageWriter.done();
		} catch( Throwable throwable ) {
			throwable.printStackTrace( System.err );
			System.exit( -1 );
		}
	}

}
