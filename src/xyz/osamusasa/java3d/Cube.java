package xyz.osamusasa.java3d;

import java.awt.*;
import java.util.Arrays;

public class Cube {
    /**
     * connect
     *  a-b, b-c, c-d, d-a,
     *  a-e, b-f, c-g, d-h,
     *  e-f, f-g, g-h, h-e
     */
    private Point3D a,b,c,d,e,f,g,h;

    /**
     * face[0] a-b-c-d
     * face[1] b-f-g-c
     * face[2] f-e-h-g
     * face[3] e-a-d-h
     * face[4] a-e-f-b
     * face[5] d-c-g-h
     */
    private Polygon3D[] face;

    //視点
    private Point3D see;

    /**
     * 見えない部分を描画するか
     * trueなら見えない部分は点線で描画する
     */
    private boolean isDrawingInvisible;

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
        BasicStroke solidLine = (BasicStroke) g2.getStroke();
        BasicStroke dottedLine = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 1.0f, new float[] {6}, 0);
        for(Polygon3D p:face){
            look = new Vector3D(p.getPoint(0), see);
            norm = p.getNormalVect();

            if(Vector3D.dotprod(look, norm) < 0){
                if(isDrawingInvisible){
                    g2.setStroke(dottedLine);
                    g.drawPolygon(p.getDrawablePolygon());
                }
            }else{
                g2.setStroke(solidLine);
                g.drawPolygon(p.getDrawablePolygon());
            }

        }

        g2.setStroke(solidLine);
    }
}
