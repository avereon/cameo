package com.avereon.cameo;

public class ImageMetadata {

	private String imageClass;

	private String target;

	private Integer width;

	private Integer height;

	private Integer imageWidth;

	private Integer imageHeight;

	private Double offsetX;

	private Double offsetY;

	public String getImageClass() {
		return imageClass;
	}

	/* This method is intentionally named setClass() instead of setImageClass()
	* for user convenience specifying the image class from the pom file. */
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

	public Integer getImageWidth() {
		return imageWidth;
	}

	public void setImageWidth( Integer imageWidth ) {
		this.imageWidth = imageWidth;
	}

	public Integer getImageHeight() {
		return imageHeight;
	}

	public void setImageHeight( Integer imageHeight ) {
		this.imageHeight = imageHeight;
	}

	public Double getOffsetX() {
		return offsetX;
	}

	public void setOffsetX( Double offsetX ) {
		this.offsetX = offsetX;
	}

	public Double getOffsetY() {
		return offsetY;
	}

	public void setOffsetY( Double offsetY ) {
		this.offsetY = offsetY;
	}

}
