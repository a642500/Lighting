package co.yishun.lighting.api.model;

import co.yishun.lighting.api.APIFactory;

/**
 * Created by carlos on 4/9/16.
 */
public abstract class JsonBean {
    @Override
    public String toString() {
        return APIFactory.getGson().toJson(this);
    }
}
