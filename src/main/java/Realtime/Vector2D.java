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
        magnitude = Math.sqrt( x*x + y*y );
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

    public void add(double fx, double fy){
        x += fx;
        y += fy;
        magnitude();
    }

    public double getSquareX(){
        return x * x;
    }

    public double getSquareY(){
        return y * y;
    }

    public void square(){
        x *= x;
        y *= y;
    }



    public double getX(){return x;}
    public double getY(){return y;}
    public double getMagnitude(){return magnitude;}

}
