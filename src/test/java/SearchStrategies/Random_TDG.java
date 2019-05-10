package SearchStrategies;

import APIRequest.APIRequestCommands;
import Common.CommonElements;
import Pixel.Pixel;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;

import java.util.ArrayList;
import java.util.List;

public class Random_TDG {


    public Pixel Random_TDG_Method (Pixel currentPixel ){
        CommonElements common = new CommonElements();
        int x = common.randomWidthPoint();
        int y = common.randomLengthPoint();
        currentPixel.setX(x);
        currentPixel.setY(y);


        return  currentPixel;

    }


    /*
    public List<Pixel> Random_TDG_Method (int maxIteration ){
        try{
            CommonElements common = new CommonElements();

            Pixel currentPixel = new Pixel();

            HttpResponse<JsonNode> response = null;
            List<Pixel> displayBugs = new ArrayList<Pixel>();


            for(int i =0; i <maxIteration;i++){

                int x = common.randomWidthPoint();
                int y = common.randomLengthPoint();
                currentPixel.setX(x);
                currentPixel.setY(y);
                currentPixel = common.setRandomRGB(currentPixel);

                    if(common.putPixel(x,y,currentPixel)){
                        response = common.getPixel(x,y,currentPixel);
                        if(!common.comparePixel(response,currentPixel))
                            displayBugs.add(currentPixel);

                    } else{
                        throw new Exception() ;
                    }
                }

           return displayBugs;
        } catch (Exception e){
            System.out.println("An error has occurred during execution" );
            return  null;
        }
    }*/
}

//for an evaluation of 160*120 - 8281 bugs were detected - this was a brute force method



// DEL  https://virtualscreen.azurewebsites.net/api/pixels?session=1234M
// GET  https://virtualscreen.azurewebsites.net/api/pixels?session=74993M&x=9&y=9
// PUT  https://virtualscreen.azurewebsites.net/api/pixels?session=12345M&x=9&y=9&r=0&g=2&b=2