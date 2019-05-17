package com.jaden.opencvimgproc.transforms;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.jaden.opencvimgproc.PageFragment;
import com.jaden.opencvimgproc.R;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

public class ScharrFragment extends PageFragment {

    private ImageView mSrcIV;
    private ImageView mDstIV;
    private Button mSwitchBtn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.common_fragment, container, false);
        initUI(view, R.drawable.logo);
        return view;
    }

    private void initUI(View view, final int res){
        mSrcIV = view.findViewById(R.id.src_img);
        mDstIV = view.findViewById(R.id.dst_img);
        mSwitchBtn = view.findViewById(R.id.switch_btn);
        final Bitmap src = BitmapFactory.decodeResource(getResources(), res);
        mSrcIV.setImageBitmap(src);
        mSwitchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDstIV.setImageBitmap(option(src));
            }
        });
    }

    public Bitmap option(Bitmap src){
        Mat srcMat = new Mat();
        Mat dstMatX = new Mat();
        Mat dstMatXAbs = new Mat();
        Mat dstMatY = new Mat();
        Mat dstMatYAbs = new Mat();
        Mat dstMat = new Mat();
        Utils.bitmapToMat(src, srcMat);
        //x方向
        Imgproc.Scharr(srcMat, dstMatX, -1, 1, 0);
        Core.convertScaleAbs(dstMatX, dstMatXAbs);
        //y方向
        Imgproc.Scharr(srcMat, dstMatY, -1, 0, 1);
        Core.convertScaleAbs(dstMatY, dstMatYAbs);
        //合并梯度
        Core.addWeighted(dstMatXAbs, 0.5, dstMatYAbs, 0.5, 0, dstMat);

        Bitmap dst = Bitmap.createBitmap(dstMat.width(), dstMat.height(), Bitmap.Config.RGB_565);
        Utils.matToBitmap(dstMat, dst); //convert mat to bitmap
        return dst;
    }
}
