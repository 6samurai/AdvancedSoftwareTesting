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
    private int length = 1600;
    private int width = 1200;
    private int maxRGB = 256;

    public String getUrl() {
        return url;
    }
    public String getSessionID() {
        return sessionID;
    }

    public int getLength(){
        return  length;
    }

    public int getWidth(){
        return  width;
    }


    public int randomColourValue(){
        Random rand = new Random();
        int n = rand.nextInt(maxRGB);

        return n;
    }

    public int randomLengthPoint(){
        Random rand = new Random();
        int n = rand.nextInt(this.length);
        return n;
    }

    public int randomWidthhPoint(){
        Random rand = new Random();
        int n = rand.nextInt(this.width);
        return n;
    }

    public List<Pixel> controlBugs(int length, int width){
        List<Pixel> display = new ArrayList<Pixel>();
        HttpResponse<JsonNode> response = null;
        Pixel currentPixel = new Pixel();

        outerLoop:
        for(int i= 0; i <length;i++){
            for(int j = 0; j<width; j++){

                currentPixel.setX(j);
                currentPixel.setY(i);
                currentPixel = setRandomRGB(currentPixel);

                if(putPixel(i,j,currentPixel)){
                    response = getPixel(i,j,currentPixel);
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

    public boolean putPixel(int i, int j, Pixel currentPixel ){
        APIRequestCommands apiRequestCommands = new APIRequestCommands();
        HttpResponse<JsonNode> response = null;

        response = apiRequestCommands.PUT(j,i,currentPixel.getR(),currentPixel.getG(),currentPixel.getB());

        if(response.getStatus() ==201){
            return  true;
        }
        return  false;
    }

    public HttpResponse<JsonNode> getPixel (int i, int j, Pixel currentPixel){
        APIRequestCommands apiRequestCommands = new APIRequestCommands();
        HttpResponse<JsonNode> response = null;

        response = apiRequestCommands.GET(j,i);
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
        for(int i =0; i <length; i ++){
            for(int j=0; j<width;j++){
                response = apiRequestCommands.GET(j,i);
                System.out.print(response.getBody().getObject().get("fitness") + " ");
            }
            System.out.println("\n");
        }
    }

}
