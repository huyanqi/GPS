package com.codoon.clubgps.util;

import android.net.Uri;

import com.codoon.clubgps.application.GPSApplication;
import com.codoon.clubgps.widget.CImageView;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;

/**
 * Created by Frankie on 2017/1/6.
 */

public class ImageLoadUtil {

    public static void loadCommonImage(String url, CImageView imageView){
        GenericDraweeHierarchy dh = imageView.getHierarchy();
        if(dh == null){
            GenericDraweeHierarchyBuilder builder = new GenericDraweeHierarchyBuilder(GPSApplication.getContext().getResources());
            dh = builder
                    .setFadeDuration(300)
                    .setActualImageScaleType(ScalingUtils.ScaleType.CENTER_CROP)
                    .build();
            imageView.setHierarchy(dh);
        }

        imageView.setImageURI(Uri.parse(url));
    }

}
