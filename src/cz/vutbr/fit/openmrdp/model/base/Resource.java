package cz.vutbr.fit.openmrdp.model.base;

import com.sun.istack.internal.Nullable;

/**
 * @author Jiri Koudelka
 * @since 05.04.2018.
 */
public final class Resource {

    @Nullable
    private final String resourceUri;
    @Nullable
    private final String resourceLocation;

    public Resource(@Nullable String resourceUri, @Nullable String resourceLocation) {
        this.resourceUri = resourceUri;
        this.resourceLocation = resourceLocation;
    }

    public String getResourceUri() {
        return resourceUri;
    }

    public String getResourceLocation() {
        return resourceLocation;
    }
}
