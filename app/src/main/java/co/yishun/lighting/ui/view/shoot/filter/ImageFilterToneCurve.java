package co.yishun.lighting.ui.view.shoot.filter;

import android.content.Context;
import android.opengl.GLES10;

import java.io.InputStream;

import co.yishun.lighting.R;
import co.yishun.lighting.ui.view.shoot.gles.GlUtil;


public class ImageFilterToneCurve extends CameraFilterToneCurve {

    public ImageFilterToneCurve(Context context, InputStream inputStream) {
        super(context, inputStream);
    }

    @Override
    public int getTextureTarget() {
        return GLES10.GL_TEXTURE_2D;
    }

    @Override
    protected int createProgram(Context applicationContext) {
        return GlUtil.createProgram(applicationContext, R.raw.vertex_shader,
                R.raw.fragment_shader_2d_tone_curve);
    }
}
