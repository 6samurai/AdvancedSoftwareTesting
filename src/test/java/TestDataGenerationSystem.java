import APIRequest.APIRequestCommands;
import Common.CommonElements;
import Pixel.Pixel;
import SearchStrategies.HillClimbing_TDG;
import SearchStrategies.Random_TDG;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;

import java.util.ArrayList;
import java.util.List;

public class TestDataGenerationSystem {
    public static void main(String[] args) {
        try {
            CommonElements common = new CommonElements();
            APIRequestCommands api_Request = new APIRequestCommands();
            String url = common.getUrl();

            System.out.println(api_Request.DELETE().getStatus());
            //     common.printFitness();
            //   List<Pixel> bugPixels = new ArrayList<Pixel>();
            //       bugPixels =  common.controlBugs(common.getLength(),common.getWidth());
            //        System.out.println(bugPixels.size());

       /*     System.out.println(api_Request.PUT(9,8,1,2,3).getStatus());
            System.out.println(api_Request.GET(9,8).getBody());*/


            int maxIteration = 2000;
            Pixel currentPixel = new Pixel();
            List<Pixel> displayBugs = new ArrayList<Pixel>();
            HttpResponse<JsonNode> response = null;
            int strategySelection = 1;
            boolean gridValuesValid = false;
            List<Pixel> currentPoints = null;
            for (int i = 0; i < maxIteration; i++) {


                switch (strategySelection) {
                    //Random Strategy
                    case 0:
                        Random_TDG random_tdg = new Random_TDG();
                        currentPixel = random_tdg.Random_TDG_Method(currentPixel);

                        break;
                    //Hill Climbing
                    case 1:
                        HillClimbing_TDG hillClimbing_tdg = new HillClimbing_TDG();
                        currentPoints = hillClimbing_tdg.HillClimbing_TDG_Method(currentPoints,common.getLength(), common.getWidth(),displayBugs);
                        currentPixel = currentPoints.get(0);

                        break;

                    //Custom Strategy
                    case 2:


                        break;




                }


                currentPixel = common.setRandomRGB(currentPixel);
                int fitness = 0;
                if (common.putPixel(currentPixel)) {
                    response = common.getPixel(currentPixel);
                    gridValuesValid = common.comparePixel(response, currentPixel);
                    if (!gridValuesValid){
                        displayBugs.add(currentPixel);
                        System.out.println("x " + currentPixel.getX()+ " y " + currentPixel.getY());
                        fitness = (Integer) response.getBody().getObject().get("fitness");
                    } else {
                        fitness = 0;
                    }

                    if(strategySelection==1){
                        currentPoints.get(0).setFitness(fitness);
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
}




//for an evaluation of 160*120 - 8281 bugs were detected - this was a brute force method


// DEL  https://virtualscreen.azurewebsites.net/api/pixels?session=1234M
// GET  https://virtualscreen.azurewebsites.net/api/pixels?session=74993M&x=9&y=9
// PUT  https://virtualscreen.azurewebsites.net/api/pixels?session=12345M&x=9&y=9&r=0&g=2&b=2