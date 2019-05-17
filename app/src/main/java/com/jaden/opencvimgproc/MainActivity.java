package com.jaden.opencvimgproc;

import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.jaden.opencvimgproc.processing.AdaptiveThresholdFragment;
import com.jaden.opencvimgproc.processing.BilateralFilterFragment;
import com.jaden.opencvimgproc.processing.BlackHatFragment;
import com.jaden.opencvimgproc.processing.BlurFragment;
import com.jaden.opencvimgproc.processing.BoxFilterFragment;
import com.jaden.opencvimgproc.processing.ClosingOperationFragment;
import com.jaden.opencvimgproc.processing.DilateFragment;
import com.jaden.opencvimgproc.processing.ErodeFragment;
import com.jaden.opencvimgproc.processing.FloodFillFragment;
import com.jaden.opencvimgproc.processing.GaussianBlurFragment;
import com.jaden.opencvimgproc.processing.MedianBlurFragment;
import com.jaden.opencvimgproc.processing.MorphologicalGradientFragment;
import com.jaden.opencvimgproc.processing.OpeningOperationFragment;
import com.jaden.opencvimgproc.processing.PyrDownFragment;
import com.jaden.opencvimgproc.processing.PyrUpFragment;
import com.jaden.opencvimgproc.processing.ThresholdFragment;
import com.jaden.opencvimgproc.processing.TopHatFragment;
import com.jaden.opencvimgproc.transforms.CannyFragment;
import com.jaden.opencvimgproc.transforms.LaplacianFragment;
import com.jaden.opencvimgproc.transforms.ScharrFragment;
import com.jaden.opencvimgproc.transforms.SobelFragment;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "OpenCV_Test";

    ViewPager pager;
    TabLayout tabLayout;
    List<PageModel> pageModels = new ArrayList<>();

    {
        //图像处理
        {
            //图像平滑处理
            {
                pageModels.add(new PageModel(new BoxFilterFragment(), "方框滤波"));
                pageModels.add(new PageModel(new BlurFragment(), "均值滤波"));
                pageModels.add(new PageModel(new GaussianBlurFragment(), "高斯滤波"));
                pageModels.add(new PageModel(new MedianBlurFragment(), "中值滤波"));
                pageModels.add(new PageModel(new BilateralFilterFragment(), "双边滤波"));
            }
            //腐蚀与膨胀
            {
                pageModels.add(new PageModel(new ErodeFragment(), "腐蚀"));
                pageModels.add(new PageModel(new DilateFragment(), "膨胀"));
            }
            //更多形态学变换
            {
                pageModels.add(new PageModel(new OpeningOperationFragment(), "开运算"));
                pageModels.add(new PageModel(new ClosingOperationFragment(), "闭运算"));
                pageModels.add(new PageModel(new MorphologicalGradientFragment(), "形态学梯度"));
                pageModels.add(new PageModel(new TopHatFragment(), "顶帽"));
                pageModels.add(new PageModel(new BlackHatFragment(), "黑帽"));
            }
            //漫水填充
            {
                pageModels.add(new PageModel(new FloodFillFragment(), "漫水填充"));
            }
            //图像金字塔
            {
                pageModels.add(new PageModel(new PyrUpFragment(), "金字塔向上-放大"));
                pageModels.add(new PageModel(new PyrDownFragment(), "金字塔向下-缩小"));
            }
            //阈值处理
            {
                pageModels.add(new PageModel(new ThresholdFragment(), "阈值化"));
                pageModels.add(new PageModel(new AdaptiveThresholdFragment(), "自适应阈值"));
            }
        }
        //图像变换
        {
            //边缘检测
            {
                pageModels.add(new PageModel(new CannyFragment(), "Canny算子"));
                pageModels.add(new PageModel(new SobelFragment(), "Sobel算子"));
                pageModels.add(new PageModel(new LaplacianFragment(), "Laplacian算子"));
                pageModels.add(new PageModel(new ScharrFragment(), "Scharr滤波器"));
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pager = findViewById(R.id.pager);
        PagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), pageModels);
        pager.setAdapter(adapter);
        tabLayout =  findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(pager);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadOpenCV();
    }

    //load OpenCV
    private void loadOpenCV(){
        if (!OpenCVLoader.initDebug()) {
            Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION, this, mLoaderCallback);
        } else {
            Log.d(TAG, "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    //OpenCV库加载并初始化成功后的回调函数
    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status){
                case BaseLoaderCallback.SUCCESS:
                    break;
                default:
                    super.onManagerConnected(status);
                    break;
            }

        }
    };

    private class PageModel{
        PageFragment pageFragment;
        String title;

        PageModel(PageFragment pageFragment, String title){
            this.pageFragment = pageFragment;
            this.title = title;
        }
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {
        private List<PageModel> list;

        public ViewPagerAdapter(FragmentManager fm, List<PageModel> list) {
            super(fm);
            this.list = list;
        }
        @Override
        public Fragment getItem(int position) {
            return list.get(position).pageFragment;
        }
        @Override
        public int getCount() {
            return list.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return list.get(position).title;
        }
    }


}
