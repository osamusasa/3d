package xyz.osamusasa.java3d;

import javax.swing.*;
import java.awt.*;

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
                g.drawLine((int)c.a.x, (int)c.a.y, (int)c.b.x, (int)c.a.y);
                g.drawLine((int)c.b.x, (int)c.b.y, (int)c.c.x, (int)c.c.y);
                g.drawLine((int)c.c.x, (int)c.c.y, (int)c.d.x, (int)c.d.y);
                g.drawLine((int)c.d.x, (int)c.d.y, (int)c.a.x, (int)c.a.y);
                g.drawLine((int)c.a.x, (int)c.a.y, (int)c.e.x, (int)c.e.y);
                g.drawLine((int)c.b.x, (int)c.b.y, (int)c.f.x, (int)c.f.y);
                g.drawLine((int)c.c.x, (int)c.c.y, (int)c.g.x, (int)c.g.y);
                g.drawLine((int)c.d.x, (int)c.d.y, (int)c.h.x, (int)c.h.y);
                g.drawLine((int)c.e.x, (int)c.e.y, (int)c.f.x, (int)c.f.y);
                g.drawLine((int)c.f.x, (int)c.f.y, (int)c.g.x, (int)c.g.y);
                g.drawLine((int)c.g.x, (int)c.g.y, (int)c.h.x, (int)c.h.y);
                g.drawLine((int)c.h.x, (int)c.h.y, (int)c.e.x, (int)c.e.y);
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

    Cube(Point3D base, float w){
        a = new Point3D(base);
        b = new Point3D(base, w, 0, 0);
        c = new Point3D(base, w, w, 0);
        d = new Point3D(base, 0, w, 0);
        e = new Point3D(base, 0, 0, w);
        f = new Point3D(base, w, 0, w);
        g = new Point3D(base, w, w, w);
        h = new Point3D(base, 0, w, w);
    }

    void rotationX(float axisY, float axisZ, double angle){
        a = new Point3D(
                a.x,
                (a.y-axisY)*Math.cos(angle)+(a.z-axisZ)*Math.sin(angle)+axisY,
                (axisY-a.y)*Math.sin(angle)+(a.z-axisZ)*Math.cos(angle)+axisZ
        );
        b = new Point3D(
                b.x,
                (b.y-axisY)*Math.cos(angle)+(b.z-axisZ)*Math.sin(angle)+axisY,
                (axisY-b.y)*Math.sin(angle)+(b.z-axisZ)*Math.cos(angle)+axisZ
        );
        c = new Point3D(
                c.x,
                (c.y-axisY)*Math.cos(angle)+(c.z-axisZ)*Math.sin(angle)+axisY,
                (axisY-c.y)*Math.sin(angle)+(c.z-axisZ)*Math.cos(angle)+axisZ
        );
        d = new Point3D(
                d.x,
                (d.y-axisY)*Math.cos(angle)+(d.z-axisZ)*Math.sin(angle)+axisY,
                (axisY-d.y)*Math.sin(angle)+(d.z-axisZ)*Math.cos(angle)+axisZ
        );
        e = new Point3D(
                e.x,
                (e.y-axisY)*Math.cos(angle)+(e.z-axisZ)*Math.sin(angle)+axisY,
                (axisY-e.y)*Math.sin(angle)+(e.z-axisZ)*Math.cos(angle)+axisZ
        );
        f = new Point3D(
                f.x,
                (f.y-axisY)*Math.cos(angle)+(f.z-axisZ)*Math.sin(angle)+axisY,
                (axisY-f.y)*Math.sin(angle)+(f.z-axisZ)*Math.cos(angle)+axisZ
        );
        g = new Point3D(
                g.x,
                (g.y-axisY)*Math.cos(angle)+(g.z-axisZ)*Math.sin(angle)+axisY,
                (axisY-g.y)*Math.sin(angle)+(g.z-axisZ)*Math.cos(angle)+axisZ
        );
        h = new Point3D(
                h.x,
                (h.y-axisY)*Math.cos(angle)+(h.z-axisZ)*Math.sin(angle)+axisY,
                (axisY-h.y)*Math.sin(angle)+(h.z-axisZ)*Math.cos(angle)+axisZ
        );
    }
    void rotationY(float axisX, float axisZ, double angle){
        a = new Point3D(
                (a.x-axisX)*Math.cos(angle)-(a.z-axisZ)*Math.sin(angle)+axisX,
                a.y,
                (a.x-axisX)*Math.sin(angle)+(a.z-axisZ)*Math.cos(angle)+axisZ
        );
        b = new Point3D(
                (b.x-axisX)*Math.cos(angle)-(b.z-axisZ)*Math.sin(angle)+axisX,
                b.y,
                (b.x-axisX)*Math.sin(angle)+(b.z-axisZ)*Math.cos(angle)+axisZ
        );
        c = new Point3D(
                (c.x-axisX)*Math.cos(angle)-(c.z-axisZ)*Math.sin(angle)+axisX,
                c.y,
                (c.x-axisX)*Math.sin(angle)+(c.z-axisZ)*Math.cos(angle)+axisZ
        );
        d = new Point3D(
                (d.x-axisX)*Math.cos(angle)-(d.z-axisZ)*Math.sin(angle)+axisX,
                d.y,
                (d.x-axisX)*Math.sin(angle)+(d.z-axisZ)*Math.cos(angle)+axisZ
        );
        e = new Point3D(
                (e.x-axisX)*Math.cos(angle)-(e.z-axisZ)*Math.sin(angle)+axisX,
                e.y,
                (e.x-axisX)*Math.sin(angle)+(e.z-axisZ)*Math.cos(angle)+axisZ
        );
        f = new Point3D(
                (f.x-axisX)*Math.cos(angle)-(f.z-axisZ)*Math.sin(angle)+axisX,
                f.y,
                (f.x-axisX)*Math.sin(angle)+(f.z-axisZ)*Math.cos(angle)+axisZ
        );
        g = new Point3D(
                (g.x-axisX)*Math.cos(angle)-(g.z-axisZ)*Math.sin(angle)+axisX,
                g.y,
                (g.x-axisX)*Math.sin(angle)+(g.z-axisZ)*Math.cos(angle)+axisZ
        );
        h = new Point3D(
                (h.x-axisX)*Math.cos(angle)-(h.z-axisZ)*Math.sin(angle)+axisX,
                h.y,
                (h.x-axisX)*Math.sin(angle)+(h.z-axisZ)*Math.cos(angle)+axisZ
        );
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
