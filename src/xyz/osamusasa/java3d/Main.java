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