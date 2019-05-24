package TestDataGenerationSystems;

import APIRequest.APIRequestCommands;
import Common.CommonElements;
import Pixel.Pixel;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Random_TDG {


    public static void main(String[] args) {
        try {

                long startTime = System.nanoTime();
                CommonElements common = new CommonElements();
                APIRequestCommands api_Request = new APIRequestCommands();
                String url = common.getUrl();

                //number of pixel requests
                int maxIteration = 100;
                Pixel currentPixel = new Pixel();
                //list of bugged pixels
                ArrayList<Pixel> displayBugs = new ArrayList<Pixel>();
                HttpResponse<JsonNode> response = null;
                boolean gridValuesValid = false;

                //Initial DELETE call
                api_Request.DELETE();

                for (int i = 0; i < maxIteration; i++) {
                    currentPixel = new Pixel();
                    //Random Strategy
                    Random_TDG random_tdg = new Random_TDG();

                    //set x and y values for current pixel
                    currentPixel = random_tdg.Random_TDG_Method(currentPixel);
                    //set RGB values for current pixel
                    currentPixel = common.setRandomRGB(currentPixel);


                    //PUT pixel API call
                    if (common.putPixel(currentPixel)) {
                        //GET pixel API call
                        response = common.getPixel(currentPixel);
                        //compare Response to current Pixel values
                        gridValuesValid = common.comparePixel(response, currentPixel);
                        if (!gridValuesValid) {
                            //display bugged pixel
                            displayBugs.add(currentPixel);
                            System.out.println("x " + currentPixel.getX() + " y " + currentPixel.getY());
                        }

                    } else {
                        throw new Exception();
                    }
                }

               // common.saveToFile("Random_100",displayBugs);
                System.out.println("Bugs detected :" + displayBugs.size());
                //Clean up DELETE Call
                api_Request.DELETE();

                long endTime = System.nanoTime();
                System.out.println("Elapsed time " + (endTime-startTime)/1000000 + " ms");


        } catch (Exception e) {
            System.out.println("An error has occurred during execution");
        }
    }

    //Random TDG method
    public Pixel Random_TDG_Method(Pixel currentPixel) {
        CommonElements common = new CommonElements();

        int x = common.randomWidthPoint();
        int y = common.randomLengthPoint();

        currentPixel.setX(x);
        currentPixel.setY(y);

        return currentPixel;

    }
}