package com.avereon.cameo;

public class IconMetadata {

	private ImageMetadata[] images;

	private String target;

	private String fill;

	private String theme;

	public ImageMetadata[] getImages() {
		return images;
	}

	public void setImages( ImageMetadata[] images ) {
		this.images = images;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget( String target ) {
		this.target = target;
	}

	public String getFill() {
		return fill;
	}

	public void setFill( String fill ) {
		this.fill = fill;
	}

	public String getTheme() {
		return theme;
	}

	public void setTheme( String theme ) {
		this.theme = theme;
	}

}
