package com.avereon.cameo;

import com.avereon.venza.icon.RenderedIcon;

public class TestIcon extends RenderedIcon {

	@Override
	protected void render() {
		// Bolt
		startPath( g( 16 ), g( 5 ) );
		lineTo( g( 11 ), g( 17 ) );
		lineTo( g( 15 ), g( 17 ) );
		lineTo( g( 9 ), g( 29 ) );
		lineTo( g( 21 ), g( 13 ) );
		lineTo( g( 17 ), g( 13 ) );
		lineTo( g( 21 ), g( 5 ) );
		closePath();
		fill( getPrimaryPaint() );
	}

	public static void main( String[] args ) {
		proof( new TestIcon() );
	}

}
