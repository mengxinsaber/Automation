package com.lhx.image.util;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @Name com.lhx.automation.util.ImageUtils
 * @Description
 * @Author lhx
 * @Version 2019/11/21 19:16
 */
public class ImageUtils {
    private static Logger logger = LoggerFactory.getLogger(ImageUtils.class);

    public static void test() {

        Mat src = Imgcodecs.imread("E:\\dome.png", 1);
        Mat temp = Imgcodecs.imread("E:\\temp.png", 1);
        Mat result;  // 用来存放结果
        if (src == null || temp == null) {
            System.out.print("打开图片失败");
            return;
        }
        int srcW, srcH, tempW, tempH, resultH, resultW;
        srcW = src.width();
        srcH = src.height();
        tempW = temp.width();
        tempH = temp.height();
        if (srcW < tempW || srcH < tempH) {
            System.out.print("模板不能比原图小");
            return;
        }
        resultW = srcW - tempW + 1;
        resultH = srcH - tempH + 1;
        result = new Mat(resultW, resultH, CvType.CV_32FC1);//  匹配方法计算的结果最小值为float
        int mothed = Imgproc.TM_CCOEFF_NORMED;
        // 归一化平方差匹配法
//        int mothed = Imgproc.TM_CCOEFF;// 相关系数匹配法：1表示完美的匹配；-1表示最差的匹配。
//        int mothed = Imgproc.TM_CCORR;// 相关匹配法
//        int mothed = Imgproc.TM_SQDIFF;// 平方差匹配法：该方法采用平方差来进行匹配；最好的匹配值为0；匹配越差，匹配值越大。
//        int mothed = Imgproc.TM_CCORR_NORMED;// 归一化平方差匹配法
        Imgproc.matchTemplate(src, temp, result, mothed);
        Core.normalize(result, result, 0, 1, Core.NORM_MINMAX, -1);


        //4.遍历resultImg，给定筛选条件，筛选出前几个匹配位置
        int tempX = 0;
        int tempY = 0;
        for (int i = 0; i < result.rows(); i++) {
            for (int j = 0; j < result.cols(); j++) {
                //4.result(j,x)位置的匹配值matchValue
                double[] matchValues = result.get(i, j);
                double matchValue = matchValues[0];
                //4.3给定筛选条件
                //条件1:概率值大于0.9
                //条件2:任何选中的点在x方向和y方向上都要比上一个点大5(避免画边框重影的情况)
                if (matchValue > 0.8 && Math.abs(i - tempY) > 5 && Math.abs(j - tempX) > 5) {
                    //5.给筛选出的点画出边框和文字
                    Imgproc.rectangle(src, new Point(j, i), new Point(j + tempW, i + tempH), new Scalar(0, 255, 0), 1, 8);
                    tempX = j;
                    tempY = i;
                }
            }
        }


//        double minValue, maxValue;
//        Point minLoc, maxLoc;
//        Core.MinMaxLocResult mmlr = Core.minMaxLoc(result);
//        maxLoc = mmlr.maxLoc;
//        minLoc = mmlr.minLoc;
//        minValue = mmlr.minVal;
//        maxValue = mmlr.maxVal;
//
//        logger.info("最大坐标值：{}，最小坐标值 ：{}，最大值：{}，最小值：{}",maxLoc,minLoc,maxValue,minValue);
//
//
//
//
//        Imgproc.rectangle(src, minLoc,
//                new Point(minLoc.x + tempW, minLoc.y + tempH),
//                new Scalar(0, 0, 0, 0));
//
//
//        // 计算下一个最小值
//        minLoc = getNextMinLoc(result, minLoc, maxValue, tempW, tempH, src);
//        System.out.println(minLoc);
//        Imgproc.rectangle(src, minLoc,
//                new Point(minLoc.x + tempW, minLoc.y + tempH),
//                new Scalar(0, 0, 0, 0));
//
//
//        // 再下一个
//        minLoc = getNextMinLoc(result, minLoc, maxValue, tempW, tempH, src);
//        System.out.println(minLoc);
//
//        Imgproc.rectangle(src, minLoc,
//                new Point(minLoc.x + tempW, minLoc.y + tempH),
//                new Scalar(0, 0, 0, 0));

        Imgcodecs.imwrite("E:\\match.png", src);
    }

    static Point getNextMinLoc(Mat result, Point minLoc, double maxVaule, int tempW, int tempH, Mat src) {

        // 先将第一个最小值点附近两倍模板宽度和高度的都设置为最大值防止产生干扰
        int startX = (int) minLoc.x - tempW / 2;
        int startY = (int) minLoc.y - tempH / 2;
        double endX = minLoc.x + tempW * 1.5;
        double endY = minLoc.y + tempH * 1.5;
        Imgproc.rectangle(src, new Point(startX, startY),
                new Point(endX, endY),
                new Scalar(120, 0, 0, 0));
        if (startX < 0 || startY < 0) {
            startX = 0;
            startY = 0;
        }
        if (endX > result.width() - 1 || endY > result.height() - 1) {
            endX = result.width() - 1;
            endY = result.height() - 1;
        }
        int y, x;
        for (y = startY; y < endY; y++) {
            for (x = startX; x < endX; x++) {
                result.put(x, y, maxVaule);
            }
        }
        return Core.minMaxLoc(result).minLoc;

    }


}
