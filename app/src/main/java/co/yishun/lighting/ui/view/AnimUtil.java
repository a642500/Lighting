package co.yishun.lighting.ui.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import co.yishun.lighting.R;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

/**
 * Created by Carlos on 2016/4/11.
 */
public class AnimUtil {
    private static void alpha(final View view, boolean show) {
        view.setAlpha(!show ? 1 : 0);
        view.setVisibility(VISIBLE);
        view.animate().alpha(show ? 1 : 0).setDuration(view.getResources()
                .getInteger(android.R.integer.config_shortAnimTime))
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        view.setVisibility(show ? VISIBLE : INVISIBLE);
                    }
                }).start();
    }

    public static void alphaShow(final View view) {
        alpha(view, true);
    }

    public static void alphaHide(final View view) {
        alpha(view, false);
    }

    public static FragmentTransaction alpha(FragmentTransaction transaction) {
        return transaction.setCustomAnimations(R.anim.fragment_fade_in, R.anim.fragment_fade_out);
    }

    public static FragmentTransaction alpha(FragmentManager fragmentManager) {
        return alpha(fragmentManager.beginTransaction());
    }

}
