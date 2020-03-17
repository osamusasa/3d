package xyz.osamusasa.java3d;

import java.awt.*;
import java.util.Arrays;

public class Polygon3D {
    private int len;
    private double[] xpoints;
    private double[] ypoints;
    private double[] zpoints;

    public static final int MIN_LENGTH = 4;

    private Polygon3D(int size){
        len = size;
        xpoints = new double[len];
        ypoints = new double[len];
        zpoints = new double[len];
    }
    Polygon3D(){
        this(MIN_LENGTH);
        Arrays.fill(xpoints, 0.0);
        Arrays.fill(ypoints, 0.0);
        Arrays.fill(zpoints, 0.0);
    }
    Polygon3D(Point3D... points){
        this(points.length);
        for(int i=0;i<points.length;i++){
            xpoints[i] = points[i].x;
            ypoints[i] = points[i].y;
            zpoints[i] = points[i].z;
        }
    }

    /**
     * ３次元上のポリゴンを平面に投影する
     *
     * 現在は、z軸のデータを消して２次元のポリゴンにしている
     *
     * @return 平面に投影したポリゴン
     */
    Polygon getDrawablePolygon(){
        int len = Math.min(xpoints.length, ypoints.length);
        int[] x = new int[len];
        int[] y = new int[len];
        for(int i=0;i<len;i++){
            x[i] = (int)xpoints[i];
            y[i] = (int)ypoints[i];
        }
        return new Polygon(x,y,len);
    }

    /**
     * 指定された量だけ移動させる
     *
     * @param dx x軸の移動量
     * @param dy y軸の移動量
     * @param dz z軸の移動量
     */
    void move(float dx, float dy, float dz){
        for(int i=0;i<len;i++){
            xpoints[i] += dx;
            ypoints[i] += dy;
            zpoints[i] += dz;
        }
    }

    /**
     * x軸と平行な軸を中心に回転
     *
     * @param axisY 回転軸のy座標
     * @param axisZ 回転軸のz座標
     * @param angle 回転角度
     */
    void rotationX(float axisY, float axisZ, double angle){
        double y,z;
        for(int i=0;i<len;i++){
            y = (ypoints[i]-axisY)*Math.cos(angle)+(zpoints[i]-axisZ)*Math.sin(angle)+axisY;
            z = (axisY-ypoints[i])*Math.sin(angle)+(zpoints[i]-axisZ)*Math.cos(angle)+axisZ;
            ypoints[i] = y;
            zpoints[i] = z;
        }
    }
    /**
     * y軸と平行な軸を中心に回転
     *
     * @param axisX 回転軸のx座標
     * @param axisZ 回転軸のz座標
     * @param angle 回転角度
     */
    void rotationY(float axisX, float axisZ, double angle){
        double x,z;
        for(int i=0;i<len;i++){
            x = (xpoints[i]-axisX)*Math.cos(angle)-(zpoints[i]-axisZ)*Math.sin(angle)+axisX;
            z = (xpoints[i]-axisX)*Math.sin(angle)+(zpoints[i]-axisZ)*Math.cos(angle)+axisZ;
            xpoints[i] = x;
            zpoints[i] = z;
        }
    }

    Point3D getPoint(int n){
        if(n<0||n>=len){
            throw new IndexOutOfBoundsException();
        }
        return new Point3D(xpoints[n], ypoints[n], zpoints[n]);
    }

    /**
     * 法線ベクトルを計算して返す
     *
     * @return ポリゴンの法線ベクトル
     */
    Vector3D getNormalVect(){
        Vector3D normal = Vector3D.crossprod(
                new Vector3D(
                        new Point3D(getPoint(0)),
                        new Point3D(getPoint(1))
                ),
                new Vector3D(
                        new Point3D(getPoint(1)),
                        new Point3D(getPoint(2))
                )
        );
        return normal;
    }

    @Override
    public String toString() {
        return "Polygon3D{" +
                "xpoints=" + Arrays.toString(xpoints) +
                ", ypoints=" + Arrays.toString(ypoints) +
                ", zpoints=" + Arrays.toString(zpoints) +
                '}';
    }
}
