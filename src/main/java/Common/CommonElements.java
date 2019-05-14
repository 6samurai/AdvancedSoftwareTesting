package Common;

import APIRequest.APIRequestCommands;
import Pixel.Pixel;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CommonElements {

    private String url = "https://virtualscreen.azurewebsites.net/api/pixels" ;
    private String sessionID = "74993M";
    private int y_axis = 1201;//y-axis upper limit - range of y-axis is between 0 to 1200 inclusively
    private int x_axis = 1601;//x-axis upper limit - range of x-axis is between 0 to 1600 inclusively
    private int maxRGB = 256; // range of rgb colours is between 0 to 255 inclusively

    public String getUrl() {
        return url;
    }
    public String getSessionID() {
        return sessionID;
    }

    public int getLength(){
        return  y_axis;
    }

    public int getWidth(){
        return  x_axis;
    }


    public int randomColourValue(){
        Random rand = new Random();
        int n = rand.nextInt(maxRGB);

        return n;
    }

    public int randomLengthPoint(){
        Random rand = new Random();
        int n = rand.nextInt(this.y_axis);
        return n;
    }

    public int randomWidthPoint(){
        Random rand = new Random();
        int n = rand.nextInt(this.x_axis);
        return n;
    }

    public List<Pixel> controlBugs(int y_axis, int x_axis){
        List<Pixel> display = new ArrayList<Pixel>();
        HttpResponse<JsonNode> response = null;
        Pixel currentPixel = new Pixel();

        outerLoop:
        for(int i= 0; i <y_axis;i++){
            for(int j = 0; j<x_axis; j++){

                currentPixel.setX(j);
                currentPixel.setY(i);
                currentPixel = setRandomRGB(currentPixel);

                if(putPixel(currentPixel)){
                    response = getPixel(currentPixel);
                    if(!comparePixel(response,currentPixel))
                        display.add(currentPixel);

                } else{
                    display = null;
                    break outerLoop;
                }
            }
        }

        return  display;
    }

    public Pixel setRandomRGB(Pixel currentPixel){
        currentPixel.setR(randomColourValue());
        currentPixel.setG(randomColourValue());
        currentPixel.setB(randomColourValue());

        return  currentPixel;
    }

    public boolean putPixel(Pixel currentPixel ){
        APIRequestCommands apiRequestCommands = new APIRequestCommands();
        HttpResponse<JsonNode> response = null;

        response = apiRequestCommands.PUT(currentPixel.getX(),currentPixel.getY(),currentPixel.getR(),currentPixel.getG(),currentPixel.getB());

        if(response.getStatus() ==201){
            return  true;
        }
        return  false;
    }

    public HttpResponse<JsonNode> getPixel (Pixel currentPixel){
        APIRequestCommands apiRequestCommands = new APIRequestCommands();
        HttpResponse<JsonNode> response = null;

        response = apiRequestCommands.GET(currentPixel.getX(),currentPixel.getY());
        if(response.getStatus() ==200) {
                return response;
        }
        return  null;
    }

    public boolean comparePixel(HttpResponse<JsonNode> response, Pixel currentPixel){
        if ((response.getBody().getObject().get("r").equals(currentPixel.getR()) && response.getBody().getObject().get("g").equals(currentPixel.getG()) &&
                response.getBody().getObject().get("b").equals(currentPixel.getB()))) {
            return  true;
        }
        return  false;
    }

    public void printFitness(){
        APIRequestCommands apiRequestCommands = new APIRequestCommands();
        HttpResponse<JsonNode> response = null;
        for(int i =0; i <y_axis; i ++){
            for(int j=0; j<x_axis;j++){
                response = apiRequestCommands.GET(j,i);
                System.out.print(response.getBody().getObject().get("fitness") + " ");
            }
            System.out.println("\n");
        }
    }

}
