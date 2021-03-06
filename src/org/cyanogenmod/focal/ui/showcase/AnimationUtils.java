/**
 * Copyright (C) 2013 The CyanogenMod Project
 * Copyright (C) 2012 Alex Curran
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package org.cyanogenmod.focal.ui.showcase;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Handler;
import android.view.View;


public class AnimationUtils {

    private static final int DEFAULT_DURATION = 300;
    private static final String ALPHA = "alpha";
    private static final float INVISIBLE = 0f;
    private static final float VISIBLE = 1f;
    private static final String COORD_X = "x";
    private static final String COORD_Y = "y";
    private static final int INSTANT = 0;

    public interface AnimationStartListener {
        void onAnimationStart();
    }

    public interface AnimationEndListener {
        void onAnimationEnd();
    }

    public static float getX(View view) {
        return view.getX();
    }

    public static float getY(View view) {
        return view.getY();
    }

    public static void hide(View view) {
        view.setAlpha(INVISIBLE);
    }

    public static ObjectAnimator createFadeInAnimation(Object target, final AnimationStartListener listener) {
        return createFadeInAnimation(target, DEFAULT_DURATION, listener);
    }

    public static ObjectAnimator createFadeInAnimation(Object target, int duration, final AnimationStartListener listener) {
        ObjectAnimator oa = ObjectAnimator.ofFloat(target, ALPHA, VISIBLE);
        oa.setDuration(duration).addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                listener.onAnimationStart();
            }

            @Override
            public void onAnimationEnd(Animator animator) {
            }

            @Override
            public void onAnimationCancel(Animator animator) {
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        });
        return oa;
    }

    public static ObjectAnimator createFadeOutAnimation(Object target, final AnimationEndListener listener) {
        return createFadeOutAnimation(target, DEFAULT_DURATION, listener);
    }

    public static ObjectAnimator createFadeOutAnimation(Object target, int duration, final AnimationEndListener listener) {
        ObjectAnimator oa = ObjectAnimator.ofFloat(target, ALPHA, INVISIBLE);
        oa.setDuration(duration).addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                listener.onAnimationEnd();
            }

            @Override
            public void onAnimationCancel(Animator animator) {
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        });
        return oa;
    }

    public static AnimatorSet createMovementAnimation(View view, float canvasX, float canvasY,
                                                      float offsetStartX, float offsetStartY,
                                                      float offsetEndX, float offsetEndY,
                                                      final AnimationEndListener listener) {
        hide(view);

        ObjectAnimator alphaIn = ObjectAnimator.ofFloat(view, ALPHA, INVISIBLE, VISIBLE).setDuration(500);

        ObjectAnimator setUpX = ObjectAnimator.ofFloat(view, COORD_X, canvasX + offsetStartX).setDuration(INSTANT);
        ObjectAnimator setUpY = ObjectAnimator.ofFloat(view, COORD_Y, canvasY + offsetStartY).setDuration(INSTANT);

        ObjectAnimator moveX = ObjectAnimator.ofFloat(view, COORD_X, canvasX + offsetEndX).setDuration(1000);
        ObjectAnimator moveY = ObjectAnimator.ofFloat(view, COORD_Y, canvasY + offsetEndY).setDuration(1000);
        moveX.setStartDelay(1000);
        moveY.setStartDelay(1000);

        ObjectAnimator alphaOut = ObjectAnimator.ofFloat(view, ALPHA, INVISIBLE).setDuration(500);
        alphaOut.setStartDelay(2500);

        AnimatorSet as = new AnimatorSet();
        as.play(setUpX).with(setUpY).before(alphaIn).before(moveX).with(moveY).before(alphaOut);

        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                listener.onAnimationEnd();
            }
        };
        handler.postDelayed(runnable, 3000);

        return as;
    }

    public static AnimatorSet createMovementAnimation(View view, float x, float y) {
        ObjectAnimator alphaIn = ObjectAnimator.ofFloat(view, ALPHA, INVISIBLE, VISIBLE).setDuration(500);

        ObjectAnimator setUpX = ObjectAnimator.ofFloat(view, COORD_X, x).setDuration(INSTANT);
        ObjectAnimator setUpY = ObjectAnimator.ofFloat(view, COORD_Y, y).setDuration(INSTANT);

        AnimatorSet as = new AnimatorSet();
        as.play(setUpX).with(setUpY).before(alphaIn);
        return as;
    }
}