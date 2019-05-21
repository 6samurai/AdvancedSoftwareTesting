package TestDataGenerationSystems;

import APIRequest.APIRequestCommands;
import Common.CommonElements;
import Pixel.Pixel;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;

import java.util.ArrayList;
import java.util.List;


public class Random_TDG {


    public static void main(String[] args) {
        try {
            CommonElements common = new CommonElements();
            APIRequestCommands api_Request = new APIRequestCommands();
            String url = common.getUrl();

            System.out.println(api_Request.DELETE().getStatus());

            int maxIteration = 5000;
            Pixel currentPixel = new Pixel();
            List<Pixel> displayBugs = new ArrayList<Pixel>();
            HttpResponse<JsonNode> response = null;
            int strategySelection = 1;
            boolean gridValuesValid = false;
            List<Pixel> currentPoints = null;
            for (int i = 0; i < maxIteration; i++) {

                //Random Strategy
                Random_TDG random_tdg = new Random_TDG();
                currentPixel = random_tdg.Random_TDG_Method(currentPixel);

                currentPixel = common.setRandomRGB(currentPixel);
                int fitness = 0;
                if (common.putPixel(currentPixel)) {
                    response = common.getPixel(currentPixel);
                    gridValuesValid = common.comparePixel(response, currentPixel);
                    if (!gridValuesValid) {
                        displayBugs.add(currentPixel);
                        System.out.println("x " + currentPixel.getX() + " y " + currentPixel.getY());
                        fitness = (Integer) response.getBody().getObject().get("fitness");
                    }

                } else {
                    throw new Exception();
                }
            }


            System.out.println("Bugs detected :" + displayBugs.size());
            System.out.println(api_Request.DELETE().getStatus());
        } catch (Exception e) {
            System.out.println("An error has occurred during execution");
        }
    }

    public Pixel Random_TDG_Method(Pixel currentPixel) {
        CommonElements common = new CommonElements();
        int x = common.randomWidthPoint();
        int y = common.randomLengthPoint();
        currentPixel.setX(x);
        currentPixel.setY(y);


        return currentPixel;

    }
}