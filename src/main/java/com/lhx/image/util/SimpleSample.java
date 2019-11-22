package com.lhx.image.util;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;

/**
 * @Name com.lhx.automation.util.SimpleSample
 * @Description * @Author lhx
 * @Version 2019/11/21 19:19
 */
public class SimpleSample {
    //静态代码块定义，会在程序开始运行时先被调用初始化
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME); //得保证先执行该语句，用于加载库，才能调用其他操作库的语句，
    }                         //否则会报 java.lang.UnsatisfiedLinkError: org.opencv.core.Mat.n_Mat(IIIDDDD)J 错误

    public static void main(String[] args) {
//        System.out.println("Welcome to OpenCV" + Core.VERSION);
//        Mat m = new Mat(5,10,CvType.CV_8UC1,new Scalar(0));
//        System.out.println("OpenCV Mat: "+m);
//        Mat mr1 = m.row(1);
//        mr1.setTo(new Scalar(1));
//        Mat mc5 = m.col(5);
//        mc5.setTo(new Scalar(5));
//        System.out.println("OpenCV Mat data:\n"+m.dump());

        ImageUtils.test();
    }
}
