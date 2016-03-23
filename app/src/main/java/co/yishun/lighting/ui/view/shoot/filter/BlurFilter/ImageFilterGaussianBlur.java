package co.yishun.lighting.ui.view.shoot.filter.BlurFilter;

import android.content.Context;

import co.yishun.lighting.ui.view.shoot.filter.CameraFilter;
import co.yishun.lighting.ui.view.shoot.filter.FilterGroup;


public class ImageFilterGaussianBlur extends FilterGroup<CameraFilter> {

    public ImageFilterGaussianBlur(Context context, float blur) {
        super();
        addFilter(new ImageFilterGaussianSingleBlur(context, blur, false));
        addFilter(new ImageFilterGaussianSingleBlur(context, blur, true));
    }
}
