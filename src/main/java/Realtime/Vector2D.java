package Realtime;

public class Vector2D {

    private double x;
    private double y;
    private double magnitude;

    public Vector2D(double x, double y){
        this.x = x;
        this.y = y;
        magnitude = magnitude();
    }

    public void normalize(){
        x = x / magnitude;
        y = y / magnitude;
        magnitude();
    }
    public double magnitude(){
        magnitude = Math.sqrt( Math.abs((x*x) + (y*y)) );
        return magnitude;
    }
    public void multiply(double k){
        x *= k;
        y *= k;
        magnitude();
    }
    public void multiply(double fx, double fy){
        x *= fx;
        y *= fy;
        magnitude();
    }
    public void multiply(Vector2D v2){
        x *= v2.getX();
        y *= v2.getY();
        magnitude();
    }
    public void add(double fx, double fy){
        x += fx;
        y += fy;
        magnitude();
    }
    public void add(Vector2D v2){
        x += v2.getX();
        x += v2.getY();
        magnitude();
    }
    public void addX(double k){
        x += k;
        magnitude();
    }
    public void addY(double k){
        y += k;
        magnitude();
    }
    public void clear(){
        x = 0;
        y = 0;
        magnitude();
    }


    public Vector2D square(){
        x *= x;
        y *= y;
        magnitude();
        return this;
    }
    public Vector2D getNormalized(){
        return new Vector2D(x / magnitude, y / magnitude);
    }

    public double getSquareX(){
        return x * x;
    }
    public double getSquareY(){
        return y * y;
    }
    public double getX(){return x;}
    public double getY(){return y;}
    public double getMagnitude(){return magnitude;}

    public static Vector2D normalize(double x1, double y1){
        double magnitude = Math.sqrt( Math.abs(( x1 * x1 ) + ( y1 * y1 )) );
        return new Vector2D(x1 / magnitude , y1 / magnitude);
    }

}
