package com.avereon.cameo;

import com.avereon.zarra.image.SvgIcon;

public class TestAidIcon extends SvgIcon {

	public TestAidIcon() {
		super( 24, 24, "M19 3H5c-1.1 0-1.99.9-1.99 2L3 19c0 1.1.9 2 2 2h14c1.1 0 2-.9 2-2V5c0-1.1-.9-2-2-2zm-1 11h-4v4h-4v-4H6v-4h4V6h4v4h4v4z" );
	}

	public static void main( String[] args ) {
		proof( new TestAidIcon() );
	}

}
