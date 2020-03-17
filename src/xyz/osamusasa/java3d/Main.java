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

        Cube c2 = new Cube(new Point3D(400, 100, 100), 100);
        c2.rotationX(150, 150, Math.PI/4);
        c2.rotationY(150, 150, -Math.PI/4);

        Canvas canvas = new Canvas() {
            @Override
            public void paint(Graphics g) {
                c.draw(g);
                c2.draw(g);
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

    //視点
    Point3D see;

    /**
     * 見えない部分を描画するか
     * trueなら見えない部分は点線で描画する
     */
    boolean isDrawingInvisible;

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
        face[5] = new Polygon3D(d,c,g,h);

        see = new Point3D(base.x+w/2, base.y+w/2, base.z+2*w);
        isDrawingInvisible = false;
    }

    void rotationX(float axisY, float axisZ, double angle){
        Arrays.asList(face).forEach(p->p.rotationX(axisY, axisZ, angle));
    }
    void rotationY(float axisX, float axisZ, double angle){
        Arrays.asList(face).forEach(p->p.rotationY(axisX, axisZ, angle));
    }

    void draw(Graphics g){
        Vector3D look,norm;
        Graphics2D g2 = (Graphics2D)g;
        BasicStroke bs1 = (BasicStroke) g2.getStroke();
        BasicStroke bs2 = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 1.0f, new float[] {6}, 0);
        for(Polygon3D p:face){
            look = new Vector3D(p.getPoint(0), see);
            norm = p.getNormalVect();

            if(Vector3D.dotprod(look, norm) < 0){
                if(isDrawingInvisible){
                    g2.setStroke(bs2);
                    g.drawPolygon(p.getDrawablePolygon());
                }
            }else{
                g2.setStroke(bs1);
                g.drawPolygon(p.getDrawablePolygon());
            }

        }

        g2.setStroke(bs1);
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
                        new Point3D(xpoints[1],ypoints[1],zpoints[1]),
                        new Point3D(xpoints[0],ypoints[0],zpoints[0])
                ),
                new Vector3D(
                        new Point3D(xpoints[2],ypoints[2],zpoints[2]),
                        new Point3D(xpoints[1],ypoints[1],zpoints[1])
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
    Vector3D(Point3D base, Point3D direction){
        this(
                direction.x-base.x,
                direction.y-base.y,
                direction.z-base.z
        );
    }

    /**
     * 2つのベクトルの内積を計算する
     *
     * @param v1 ベクトル
     * @param v2 ベクトル
     * @return ２つのベクトルの内積を表す新しいベクトルオブジェクト
     */
    static double dotprod(Vector3D v1, Vector3D v2){
        return v1.x*v2.x + v1.y*v2.y + v1.z*v2.z;
    }

    /**
     * 2つのベクトルの外積を計算する
     *
     * @param v1 ベクトル
     * @param v2 ベクトル
     * @return ２つのベクトルの外積を表す新しいベクトルオブジェクト
     */
    static Vector3D crossprod(Vector3D v1, Vector3D v2){
        return new Vector3D(
                v1.y*v2.z - v1.z*v2.y,
                v1.z*v2.x - v1.x*v2.z,
                v1.x*v2.y - v1.y*v2.x
        );
    }

    @Override
    public String toString() {
        return "Vector3D{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }
}