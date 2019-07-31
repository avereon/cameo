package com.avereon.cameo;

public class ImageMetadata {

	private String imageClass;

	private String target;

	private Integer width;

	private Integer height;

	public String getImageClass() {
		return imageClass;
	}

	public void setClass( String imageClass ) {
		this.imageClass = imageClass;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget( String target ) {
		this.target = target;
	}

	public Integer getSize() {
		return getWidth();
	}

	public void setSize( Integer size ) {
		this.width = size;
		this.height = size;
	}

	public Integer getWidth() {
		return width;
	}

	public void setWidth( Integer width ) {
		this.width = width;
	}

	public Integer getHeight() {
		return height;
	}

	public void setHeight( Integer height ) {
		this.height = height;
	}

}
