package com.jaden.opencvimgproc.processing;


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
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

//漫水填充
public class FloodFillFragment extends PageFragment {

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
    //漫水填充
    public Bitmap option(Bitmap src){
        Mat srcMat = new Mat();
        Mat dstMat = new Mat();
        Utils.bitmapToMat(src, srcMat);

        //这一步是将CV_8UC4转成CV8UC3
        Mat tmpMat = new Mat();
        Imgproc.cvtColor(srcMat,tmpMat,Imgproc.COLOR_RGBA2RGB);
        Bitmap tmp = Bitmap.createBitmap(tmpMat.width(), tmpMat.height(), Bitmap.Config.RGB_565);
        Utils.matToBitmap(tmpMat, tmp);
        mSrcIV.setImageBitmap(tmp);

        //使Point正好选择红色模块
        Imgproc.floodFill(tmpMat, dstMat, new Point(tmpMat.width()/2, 20), new Scalar(50,50,50));
        Bitmap dst = Bitmap.createBitmap(tmpMat.width(), tmpMat.height(), Bitmap.Config.RGB_565);
        Utils.matToBitmap(tmpMat, dst); //convert mat to bitmap
        return dst;
    }
}
