package org.opencv.utils;

public class OpenCVUtils {
    static {
        System.loadLibrary("openCVUtils");
    }


    public static native int[] gray(int[] pix, int w, int h);
}
