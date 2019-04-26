package com.xeomar.cameo;

import com.xeomar.xenon.icon.WingDiscLargeIcon;
import com.xeomar.xenon.icon.XRingLargeIcon;

import java.io.File;

public class Main {

	public static void main( String[] commands ) {
		try {
			ProgramImageWriter.save( new WingDiscLargeIcon(), new File( "xeomar.png" ) );
			ProgramImageWriter.save( new XRingLargeIcon(), new File( "xenon.png" ) );
			ProgramImageWriter.done();
		} catch( Throwable throwable ) {
			throwable.printStackTrace( System.err );
			System.exit( -1 );
		}
	}

}
