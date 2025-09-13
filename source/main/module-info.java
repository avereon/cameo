module cameo {
	requires static com.avereon.zenna;
	requires static maven.plugin.api;
	requires static maven.plugin.annotations;
	requires static maven.project;

	requires transitive javafx.controls;
	requires transitive javafx.graphics;

	requires com.avereon.zerra;

	exports com.avereon.cameo;
}