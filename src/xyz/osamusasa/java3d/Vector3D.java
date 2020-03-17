package xyz.osamusasa.java3d;

public class Vector3D {
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
