package SearchStrategies;

import Common.CommonElements;
import Pixel.Pixel;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Custom_TDG {


    public static void main(String[] args) {
        try {
            Custom_TDG_Method custom_tdg_method = new Custom_TDG_Method();
            int maxIteration = 2000;
            Pixel currentPixel = new Pixel();
            List<Pixel> displayBugs = new ArrayList<Pixel>();
            HttpResponse<JsonNode> response = null;
            boolean gridValuesValid = false;
            List<Pixel> currentPoints = null;
            List<Pixel> originalPoints = null;
            CommonElements common = new CommonElements();
            int currentFitness = -2;
            int maxFitness = -2;
            int max_ID=0;
            boolean  findInitialPoints = true;
            int identifiedInitialPoints = 0;
            Pixel initialPoint = null;


            int direction_x = 0;
            int direction_y = 0;
            List<Pixel> rectangleArea = new ArrayList<Pixel>();

            for (int i = 0; i < maxIteration; i++) {

                if (currentPoints == null) {
                    currentPoints = custom_tdg_method.defaultGridValues(common.getLength(), common.getWidth());
                    originalPoints = currentPoints;
                    initialPoint = currentPoints.get(0);
                    findInitialPoints = true;
                } else{
                    if(findInitialPoints){

                        identifiedInitialPoints = 0;
                        for(int j = 0; j <currentPoints.size();j++){
                            currentFitness = currentPoints.get(j).getFitness();
                            //ignore default values
                            if(currentFitness!=-2 && currentFitness>maxFitness){

                                //if initial point has a fitness value of zero - select new point
                                if(j ==0 && currentFitness==0){
                                    currentPoints = custom_tdg_method.defaultGridValues(common.getLength(), common.getWidth());
                                    originalPoints = currentPoints;
                                    initialPoint = currentPoints.get(0);
                                    findInitialPoints = true;
                                    break;
                                }

                                maxFitness = currentFitness;

                            }

                            if(currentFitness==-2){

                                if(j!=0)
                                    Collections.swap(currentPoints, 0, j);

                                break;
                            } else{
                                identifiedInitialPoints ++;
                            }
                        }

                        if(identifiedInitialPoints == currentPoints.size()){

                            if(maxFitness>0){
                                findInitialPoints = false;

                                Comparator<Pixel> compareByFitness = (Pixel o1, Pixel o2) -> ((Integer) o1.getFitness()).compareTo( (Integer) o2.getFitness() );
                                Collections.sort(currentPoints, compareByFitness);
                            } else{
                                currentPoints = custom_tdg_method.defaultGridValues(common.getLength(), common.getWidth());
                                originalPoints = currentPoints;
                                initialPoint = currentPoints.get(0);
                            }

                        }

                    } else{



                        if(rectangleArea == null){
                            rectangleArea.add(currentPoints.get(0));
                        }

                        //determine direction to follow based on largest fitness value
                        direction_x = currentPoints.get(0).getX() - initialPoint.getX();
                        direction_y = currentPoints.get(0).getY() - initialPoint.getY();





                    }

                }


               // currentPixel = common.setRandomRGB(currentPixel);

                if (common.putPixel(currentPoints.get(0))) {
                    response = common.getPixel(currentPoints.get(0));
                    gridValuesValid = common.comparePixel(response, currentPoints.get(0));
                    if (!gridValuesValid) {
                        displayBugs.add(currentPoints.get(0));
                        System.out.println("x" + currentPoints.get(0).getX() + "y " + currentPoints.get(0).getY());
                    }


                    int fitness = (Integer) response.getBody().getObject().get("fitness");
                    currentPoints.get(0).setFitness(fitness);


                } else {
                    throw new Exception("Put pixel operation failed");
                }
            }

        } catch (Exception e) {
            System.out.println("An error has occurred: " +e.getMessage() );
        }
    }

}


class Custom_TDG_Method {

  /*  public List<Pixel> sortByFitness(List<Pixel> inputList){
     //   List<Pixel> newList = new ArrayList<Pixel>();
        int maxFitness = 0;

        for(int i = 0; i<inputList.size();i++){
            if(inputList.get(i).getFitness()>maxFitness){
                maxFitness;
            }
        }

        return newList;
    }*/


    public List<Pixel> defaultGridValues(int length, int width) {

        CommonElements commonElements = new CommonElements();
        int x = commonElements.randomWidthPoint();
        int y = commonElements.randomLengthPoint();

        int new_x = x;
        int new_y = y;
        List<Pixel> newGrid = new ArrayList<Pixel>();

        Pixel currentPixel = new Pixel();
        currentPixel.setY(new_y);
        currentPixel.setX(new_x);
        currentPixel.setFitness(-2);
        currentPixel =  commonElements.setRandomRGB(currentPixel);
        newGrid.add(currentPixel); //initial point of (x,y)

       /* for (int i = 0; i < max_i; i++) {
            for (int j = 0; j < max_j; j++) {

                if(i ==0)
                    new_x = x + (int) Math.pow(-1, i);
               else
                    new_y = y + (int) Math.pow(-1, j);

                if (new_x < width && new_y < length) {
                    currentPixel = new Pixel();
                    currentPixel.setY(new_y);
                    currentPixel.setX(new_x);
                    currentPixel.setFitness(-2);
                    currentPixel =  commonElements.setRandomRGB(currentPixel);
                    newGrid.add(currentPixel);

                }
            }
        }*/

       for(int i= 0; i<4;i++){
           new_x = x;
           new_y = y;
           if(i<2){
               new_x = x + (int) Math.pow(-1, i);

           } else{
               new_y = y + (int) Math.pow(-1, i);
           }


           if (new_x < width && new_y < length) {
               currentPixel = new Pixel();
               currentPixel.setY(new_y);
               currentPixel.setX(new_x);
               currentPixel.setFitness(-2);
               currentPixel =  commonElements.setRandomRGB(currentPixel);
               newGrid.add(currentPixel);

           }
       }
        return newGrid;


    }


}

