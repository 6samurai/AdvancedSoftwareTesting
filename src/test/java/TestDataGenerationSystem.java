import APIRequest.APIRequestCommands;
import Common.CommonElements;
import Pixel.Pixel;
import SearchStrategies.Random_TDG;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;

import java.util.ArrayList;
import java.util.List;

public class TestDataGenerationSystem {
    public static void  main (String [] args){
        try{
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


            int maxIteration = 10000;

            List<Pixel> displayBugs = new ArrayList<Pixel>();

            int strategySelection = 0;
            switch (strategySelection){

                //Random Strategy
                case 0:
                    Random_TDG random_tdg = new Random_TDG();
                    displayBugs=  random_tdg.Random_TDG_Method(maxIteration);

                    break;
                //Hill Climbing - x axis traversal
                case 1:


                    break;

                //Hill Climbing - y axis traversal
                case 2:



                    break;
                //Custom Strategy
                case 3:



                    break;


            }
            //

            System.out.println("Bugs detected :" + displayBugs.size() );
        } catch (Exception e){
            System.out.println("An error has occurred during execution" );
        }
    }
}

//for an evaluation of 160*120 - 8281 bugs were detected - this was a brute force method



// DEL  https://virtualscreen.azurewebsites.net/api/pixels?session=1234M
// GET  https://virtualscreen.azurewebsites.net/api/pixels?session=74993M&x=9&y=9
// PUT  https://virtualscreen.azurewebsites.net/api/pixels?session=12345M&x=9&y=9&r=0&g=2&b=2