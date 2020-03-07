package xyz.osamusasa.java3d;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class Main {
    public static void main(String args[]) {
        JFrame frame = new JFrame("draw graph");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //from https://nat.hatenadiary.com/entry/20050513/p1
        //--
        java.awt.GraphicsEnvironment env = java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment();
        // 変数desktopBoundsにデスクトップ領域を表すRectangleが代入される
        java.awt.Rectangle desktopBounds = env.getMaximumWindowBounds();
        //--
        //windowサイズを画面の最大サイズに設定
        frame.setBounds(desktopBounds);

        Cube c = new Cube(new Point3D(100, 100, 100), 100);
        c.rotationX(150, 150, Math.PI/4);
        c.rotationY(150, 150, Math.PI/4);

        Canvas canvas = new Canvas() {
            @Override
            public void paint(Graphics g) {
                c.draw(g);
            }
        };

        frame.getContentPane().add(canvas);
        canvas.repaint();
        frame.setVisible(true);
    }
}
class Cube{
    /**
     * connect
     *  a-b, b-c, c-d, d-a,
     *  a-e, b-f, c-g, d-h,
     *  e-f, f-g, g-h, h-e
     */
    Point3D a,b,c,d,e,f,g,h;

    /**
     * face[0] a-b-c-d
     * face[1] b-f-g-c
     * face[2] f-e-h-g
     * face[3] e-a-d-h
     * face[4] a-e-f-b
     * face[5] d-h-g-c
     */
    Polygon3D[] face;

    Cube(Point3D base, float w){
        a = new Point3D(base);
        b = new Point3D(base, w, 0, 0);
        c = new Point3D(base, w, w, 0);
        d = new Point3D(base, 0, w, 0);
        e = new Point3D(base, 0, 0, w);
        f = new Point3D(base, w, 0, w);
        g = new Point3D(base, w, w, w);
        h = new Point3D(base, 0, w, w);

        face = new Polygon3D[6];
        face[0] = new Polygon3D(a,b,c,d);
        face[1] = new Polygon3D(b,f,g,c);
        face[2] = new Polygon3D(f,e,h,g);
        face[3] = new Polygon3D(e,a,d,h);
        face[4] = new Polygon3D(a,e,f,b);
        face[5] = new Polygon3D(d,h,g,c);
    }

    void rotationX(float axisY, float axisZ, double angle){
        Arrays.asList(face).forEach(p->p.rotationX(axisY, axisZ, angle));
    }
    void rotationY(float axisX, float axisZ, double angle){
        Arrays.asList(face).forEach(p->p.rotationY(axisX, axisZ, angle));
    }

    void draw(Graphics g){
        for(Polygon3D p:face){
            g.drawPolygon(p.getDrawablePolygon(null));
        }
    }
}

class Point3D{
    double x,y,z;

    public Point3D(Point3D p){
        this.x = p.x;
        this.y = p.y;
        this.z = p.z;
    }
    public Point3D(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    public Point3D(Point3D p, double dx, double dy, double dz){
        this.x = p.x+dx;
        this.y = p.y+dy;
        this.z = p.z+dz;
    }
}

class Polygon3D{
    int len;
    double[] xpoints;
    double[] ypoints;
    double[] zpoints;

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
     * @param camera 使ってない
     * @return 平面に投影したポリゴン
     */
    Polygon getDrawablePolygon(Camera camera){
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
}

class Vector3D{
    double x,y,z;

    Vector3D(){
        x=0.0;
        y=0.0;
        z=0.0;
    }
    Vector3D(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
}

class Camera{
    Vector3D posission;
    Vector3D direction;

    Camera(Vector3D p, Vector3D d){
        posission = p;
        direction = d;
    }
}
