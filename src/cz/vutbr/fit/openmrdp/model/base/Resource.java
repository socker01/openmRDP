package cz.vutbr.fit.openmrdp.model.base;

import com.sun.istack.internal.Nullable;

/**
 * Object that represents resource. It contains resource URI and resource location.
 *
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

    @Nullable
    public String getResourceUri() {
        return resourceUri;
    }

    @Nullable
    public String getResourceLocation() {
        return resourceLocation;
    }
}
