package xyz.osamusasa.java3d;

public class Point3D {
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
