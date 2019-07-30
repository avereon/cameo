package com.avereon.cameo;

import java.nio.file.Path;

public class RenderImageMeta {

	private String image;

	private Path target;

	private String name;

	public String getImage() {
		return image;
	}

	public void setImage( String image ) {
		this.image = image;
	}

	public Path getTarget() {
		return target;
	}

	public void setTarget( Path target ) {
		this.target = target;
	}

	public String getName() {
		return name;
	}

	public void setName( String name ) {
		this.name = name;
	}
}
