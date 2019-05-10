package Pixel;

public class Pixel {
    private  int r = 0;
    private  int g = 0;
    private  int b = 0;
    private  int x = 0;
    private  int y = 0;
    private  int fitness =0;
    public Pixel(){

    }

    public Pixel(int r , int g, int b, int x, int y){
        setB(b);
        setY(y);
        setX(x);
        setG(g);
        setR(r);
    }

    public int getB() {
        return b;
    }

    public int getG() {
        return g;
    }

    public int getR() {
        return r;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getFitness() {
        return fitness;
    }

    public void setB(int b) {
        this.b = b;
    }

    public void setG(int g) {
        this.g = g;
    }

    public void setR(int r) {
        this.r = r;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setFitness(int fitness) {
        this.fitness = fitness;
    }

    public Pixel defaultPixel(){
        Pixel newPixel = new Pixel();
        newPixel.setB(-2);
        newPixel.setY(-2);
        newPixel.setX(-2);
        newPixel.setG(-2);
        newPixel.setR(-2);
        newPixel.setFitness(-2);

        return newPixel;

    }

    public Pixel setPixel(Pixel inputPixel, Pixel newPixel){
        newPixel.setB(inputPixel.getB());
        newPixel.setY(inputPixel.getY());
        newPixel.setX(inputPixel.getX());
        newPixel.setG(inputPixel.getG());
        newPixel.setR(inputPixel.getR());
        newPixel.setFitness(inputPixel.getFitness());

        return  newPixel;

    }
}
