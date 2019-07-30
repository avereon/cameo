package com.avereon.cameo;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;

@Mojo( name = "render", defaultPhase = LifecyclePhase.PREPARE_PACKAGE )
public class RenderMojo extends AbstractMojo {

	private RenderImageMeta[] images;

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		// Go through the image classes and write files
	}

	public RenderImageMeta[] getImages() {
		return images;
	}

	public void setImages( RenderImageMeta[] images ) {
		this.images = images;
	}

}
