package net.sf.okapi.acorn;

import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;

public class Application extends ResourceConfig {
    public Application() {
        packages("org.glassfish.jersey.examples.multipart");
        register(MultiPartFeature.class);
    }
}