package net.sf.okapi.acorn;

import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;

public class MyApplication extends ResourceConfig {
    public MyApplication() {
        packages("org.glassfish.jersey.examples.multipart");
        register(MultiPartFeature.class);
    }
}