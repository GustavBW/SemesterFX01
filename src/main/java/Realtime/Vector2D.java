package Realtime;

public class Vector2D {

    private double x;
    private double y;
    private double magnitude;
    private double magnitudeScalar = 1;

    public Vector2D(double x, double y){
        this.x = x;
        this.y = y;
        magnitude = magnitude();
    }
    public Vector2D(){this(0,0);}

    public void normalize(){
        x = x / magnitude;
        y = y / magnitude;
        magnitude();
    }
    public void ortogonalize(){
        double preX = x;
        double preY = y;
        x = preY * -1;
        y = preX;
    }
    public void invert(){
        this.multiply(-1);
    }
    public Vector2D getInverted(){
        Vector2D v2 = new Vector2D(x,y);
        v2.invert();
        return v2;
    }
    public Vector2D getOrtogonal(){
        Vector2D v2 = new Vector2D(x,y);
        v2.ortogonalize();
        return v2;
    }
    public double magnitude(){
        magnitude = Math.sqrt( (x*x) + (y*y) );
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
    public void decreaseX(double k){
        x -= k;
        magnitude();
    }
    public void decreaseY(double k){
        y -= k;
        magnitude();
    }
    public void remove(double k){
        x -= k;
        y -= k;
        magnitude();
    }
    public void subtract(Vector2D v2){
        x -= v2.getX();
        y -= v2.getY();
    }
    public void setX(double k){x = k;}
    public void setY(double k){y = k;}
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
    public static Vector2D difference(Vector2D vA, Vector2D vB){
        return new Vector2D(vB.getX() - vA.getX(), vB.getY() - vA.getY());
    }

}
