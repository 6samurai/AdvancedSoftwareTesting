package SearchStrategies;

import APIRequest.APIRequestCommands;
import Common.CommonElements;
import Pixel.Pixel;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


class Custom_TDG {



    public static void main(String[] args) {
        Custom_TDG custom_tdg_method = new Custom_TDG();
        custom_tdg_method.customTDG_Method();
    }

    private CommonElements common = new CommonElements();
    private ArrayList<Pixel> currentPoints =  new ArrayList<Pixel>();
    private ArrayList<Pixel> originalPoints = new ArrayList<Pixel>();
    private Pixel initialPoint =null;

    private int maxIteration = 50000;

    private void selectNewPoint(ArrayList <Pixel> bugList){
        Custom_TDG custom_tdg_method = new Custom_TDG();
        currentPoints.clear();
        originalPoints.clear();
        initialPoint = null;
        currentPoints =  custom_tdg_method.defaultGridValues(common.getLength(), common.getWidth(),bugList);


        for (int i = 0; i < currentPoints.size(); i++) {
            Pixel newPixel = new Pixel();
            originalPoints.add(newPixel);
        }
       // Collections.copy(originalPoints, currentPoints);
        originalPoints = copyPixelArrayList(currentPoints,originalPoints);
        initialPoint = currentPoints.get(0);
    }


    public void customTDG_Method(){

        try {
            APIRequestCommands api_Request = new APIRequestCommands();
            //  List<Pixel> currentPoints = null;
            // List<Pixel> originalPoints = null;
            //   CommonElements common = new CommonElements();
           // Custom_TDG custom_tdg_method = new Custom_TDG();

            ArrayList<Pixel> displayBugs = new ArrayList<Pixel>();
            HttpResponse<JsonNode> response = null;
            boolean gridValuesValid = false;

            boolean  findInitialPoints = true;

            int currentFitness = -2;
            int totalFitness = -2;
            int identifiedInitialPoints = 0;
            int direction_x = 0;
            int direction_y = 0;
            int prev_x = 0;
            int prev_y = 0;
            int max_direction =0;
            int quadrant = 0;
            int new_x = 0;
            int new_y = 0;
            boolean changeDirection = false;
            Pixel bugPixel = new Pixel();

            //delete any previous entries
            System.out.println(api_Request.DELETE().getStatus());

            for (int i = 0; i < maxIteration; i++) {

                //initial random selection of point
                if (currentPoints == null || currentPoints.size()==0) {
                    selectNewPoint(displayBugs);
                    findInitialPoints = true;

                } else{

                    //selecting initial points to start evaluation of quadrants
                    if(findInitialPoints){

                        identifiedInitialPoints = 0;
                        totalFitness = 0;
                        for(int j = 0; j <currentPoints.size();j++){
                            currentFitness = currentPoints.get(j).getFitness();

                            bugPixel = common.lookupPixel(currentPoints.get(j).getX(),currentPoints.get(j).getY(),displayBugs);
                            //if chosen points have already been processed
                            if(bugPixel != null &&  !currentPoints.get(0).equals(initialPoint) && currentPoints.get(j).getFitness()==-2){
                                //if initial point is already recorded
                                if(j ==0 ) {
                                    currentPoints = defaultGridValues(common.getLength(), common.getWidth(), displayBugs);
                                    initialPoint = currentPoints.get(0);
                                    findInitialPoints = true;
                                    break;
                                }

                                currentPoints.remove(j);
                                bugPixel = null;
                                break;


                            } else if(currentFitness!=-2 ){

                                //if initial point has a fitness value of zero - select new point
                                if(j ==0 && currentFitness==0 && currentPoints.get(0).equals(initialPoint) ){
                                    currentPoints = defaultGridValues(common.getLength(), common.getWidth(),displayBugs);
                                    initialPoint = currentPoints.get(0);
                                    findInitialPoints = true;
                                    break;

                                } else if (j == 0 && currentPoints.get(0).equals(initialPoint)) {
                                    //to exclude original point for direction selection
                                    currentPoints.remove(0);
                                    break;

                                }else
                                    //verify that the fitness present for selected points is greater than zero
                                    totalFitness = totalFitness + currentFitness;
                            } else {
                                // to swap point to first in list - to evaluate fitness value
                                if(j!=0)
                                    Collections.swap(currentPoints, 0, j);
                                break;
                            }

                            identifiedInitialPoints ++;

                        }

                        if(identifiedInitialPoints == currentPoints.size()){

                            if(totalFitness>0){
                                findInitialPoints = false;
                                //sort current points selected by fitness value
                                Comparator<Pixel> compareByFitness = (Pixel o1, Pixel o2) -> ((Integer) o1.getFitness()).compareTo( (Integer) o2.getFitness() );
                                Collections.sort(currentPoints, compareByFitness.reversed());
                                originalPoints = copyPixelArrayList(currentPoints,originalPoints);
                                continue;
                              /*  currentPoints.remove(0);
                                originalPoints.remove(0);
                                currentPoints.remove(0);
                                originalPoints.remove(0);
                                currentPoints.remove(0);
                                originalPoints.remove(0);*/

                            } else{
                                //reset current point selection
                                currentPoints = defaultGridValues(common.getLength(), common.getWidth(),displayBugs);
                                // originalPoints = currentPoints;
                                initialPoint = currentPoints.get(0);
                            }
                        }
                    //handle the traversal
                    } else{

                        //when all possible bugs present in the specified quadrant have been identified, remove that specific entry
                        if (changeDirection && currentPoints.get(0).getFitness()==0  ){
                            currentPoints.remove(0);
                            originalPoints.remove(0);
                            changeDirection = false;
                            quadrant = 0;

                            if(currentPoints.size() ==0 ){
                                selectNewPoint(displayBugs);
                                findInitialPoints = true;
                            }
                        } else if  ((!changeDirection && currentPoints.get(0).getFitness()==0)
                                ||  currentPoints.get(0).getX()==common.getWidth()-1 ||currentPoints.get(0).getY()==common.getLength()-1
                                || currentPoints.get(0).getX()==0 ||currentPoints.get(0).getY()==0 ){

                            if(direction_x ==0 && direction_y<0){

                               // direction_x = -1;

                                max_direction = currentPoints.get(0).getY();
                                quadrant = 1;

                            } else if(direction_x ==0 && direction_y>0){
                             //  direction_x = 1;
                                max_direction = currentPoints.get(0).getY();
                                quadrant = 2;

                            }else if(direction_x <0 && direction_y==0){

                             //   direction_y = 1;
                                max_direction = currentPoints.get(0).getX();
                                quadrant = 3;

                            }else if(direction_x >0 && direction_y==0){
                            //    direction_y = -1;
                                max_direction = currentPoints.get(0).getX();
                                quadrant = 4;
                            }

                            //to include 1199 and 1599 respectively
                            if( max_direction==common.getWidth()-1 ||currentPoints.get(0).getY()==common.getLength()-1 )  {
                                max_direction++;

                            }
                            //to include point zero
                            else if (max_direction==0 ){
                                max_direction = -1;
                            }

                            changeDirection = true;
                        }

                        if( currentPoints.get(0).getFitness()!= -2 ){
                     //   if(currentPoints.get(0).getFitness()!=0 && currentPoints.get(0).getFitness()!= -2 ){
                            //determine direction to follow based on largest fitness value
                            if(!changeDirection){
                                direction_x = originalPoints.get(0).getX() - initialPoint.getX();
                                direction_y = originalPoints.get(0).getY() - initialPoint.getY();


                            }else{
/*
                                bugPixel = common.lookupPixel(currentPoints.get(0).getX(),currentPoints.get(0).getY(),displayBugs);
                                //if chosen points have already been processed
                                if(bugPixel != null){


                                }*/

                                switch (quadrant){
                                    case 1:
                                        if(currentPoints.get(0).getY() > max_direction +1){
                                            direction_x = 0;
                                            direction_y = -1;

                                       /*     currentPoints.get(0).setY(originalPoints.get(0).getY());
                                            currentPoints.get(0).setX(currentPoints.get(0).getX()-1 );*/
                                        } else if(currentPoints.get(0).getY() == max_direction || currentPoints.get(0).getY() == max_direction +1  ){
                                            direction_x = 0;
                                            direction_y = 0;
                                           currentPoints.get(0).setX(currentPoints.get(0).getX()-1);
                                           currentPoints.get(0).setY(originalPoints.get(0).getY());
                                        }
                                        break;

                                    case 2:

                                        if(currentPoints.get(0).getY() < max_direction-1){
                                            direction_x = 0;
                                            direction_y = 1;
                                          /*  currentPoints.get(0).setY(originalPoints.get(0).getY());
                                            currentPoints.get(0).setX(  currentPoints.get(0).getX()+1);*/
                                        }else if(currentPoints.get(0).getY() == max_direction  || currentPoints.get(0).getY() == max_direction-1){
                                            direction_x = 0;
                                            direction_y = 0;
                                            currentPoints.get(0).setX(currentPoints.get(0).getX()+1);
                                            currentPoints.get(0).setY(originalPoints.get(0).getY());
                                        }
                                        break;

                                    case 3:
                                        if(currentPoints.get(0).getX() > max_direction+1){
                                            direction_x = -1;
                                            direction_y = 0;
                                         /*   currentPoints.get(0).setX(originalPoints.get(0).getX());
                                            currentPoints.get(0).setY(  currentPoints.get(0).getY()+1);*/
                                        }else if(currentPoints.get(0).getX() == max_direction || currentPoints.get(0).getX() == max_direction +1){
                                            direction_x = 0;
                                            direction_y = 0;
                                            currentPoints.get(0).setX(originalPoints.get(0).getX());
                                            currentPoints.get(0).setY(currentPoints.get(0).getY()+1);
                                        }
                                        break;
                                    case  4:
                                        if(currentPoints.get(0).getX() < max_direction -1 ){
                                            direction_x = 1;
                                            direction_y = 0;
                                          /*  currentPoints.get(0).setX(originalPoints.get(0).getX());
                                            currentPoints.get(0).setY(  currentPoints.get(0).getY()-1);*/
                                        }else if(currentPoints.get(0).getX() == max_direction || currentPoints.get(0).getX() == max_direction -1 ){
                                            direction_x = 0;
                                            direction_y = 0;
                                            currentPoints.get(0).setX(originalPoints.get(0).getX());
                                            currentPoints.get(0).setY(currentPoints.get(0).getY()-1);
                                        }
                                        break;
                                }
                            }

                            prev_x =  currentPoints.get(0).getX();
                            prev_y =  currentPoints.get(0).getY();

                            new_x = prev_x + direction_x;
                            new_y = prev_y + direction_y;

                            if((new_x>0 && new_x<common.getWidth() && new_y>0 && new_y<common.getLength())){
                                currentPoints.get(0).setY(new_y);
                                currentPoints.get(0).setX(new_x);

                            } else{
                                selectNewPoint(displayBugs);
                                findInitialPoints = true;

                            }
                        }
                    }
                }

                // currentPixel = common.setRandomRGB(currentPixel);
                int fitness;
                if (common.putPixel(currentPoints.get(0))) {
                    response = common.getPixel(currentPoints.get(0));
                    gridValuesValid = common.comparePixel(response, currentPoints.get(0));

                    //checks if the selected point was already serviced
                    bugPixel = common.lookupPixel(currentPoints.get(0).getX(),currentPoints.get(0).getY(),displayBugs);

                    if (!gridValuesValid &&  bugPixel == null) {
                        addPixelToList(displayBugs,currentPoints.get(0));
                       // displayBugs.add(currentPoints.get(0));
                        System.out.println("x" + currentPoints.get(0).getX() + " y " + currentPoints.get(0).getY());
                        fitness = (Integer) response.getBody().getObject().get("fitness");

                    }
                    else{

                        System.out.println("bug pixel " + bugPixel );
                        fitness = 0;
                    }
                    currentPoints.get(0).setFitness(fitness);

                } else {
                    throw new Exception("Put pixel operation failed");
                }
            }

            System.out.println("Bugs detected :" + displayBugs.size());
            System.out.println(api_Request.DELETE().getStatus());
        } catch (Exception e) {
            System.out.println("An error has occurred: " +e.getMessage() );
        }
    }

    public ArrayList<Pixel> copyPixelArrayList(ArrayList<Pixel> sourceList, ArrayList<Pixel> copyToList){
        for(int i = 0; i <sourceList.size();i++){
            copyToList.get(i).setX(sourceList.get(i).getX());
            copyToList.get(i).setY(sourceList.get(i).getY());
            copyToList.get(i).setR(sourceList.get(i).getR());
            copyToList.get(i).setG(sourceList.get(i).getG());
            copyToList.get(i).setB(sourceList.get(i).getB());
            copyToList.get(i).setFitness(sourceList.get(i).getFitness());

        }

        return  copyToList;
    }

    public ArrayList<Pixel> addPixelToList(ArrayList<Pixel> originalList, Pixel addPixel){
        Pixel newPixel = new Pixel();
        newPixel.setX(addPixel.getX());
        newPixel.setY(addPixel.getY());
        newPixel.setR(addPixel.getR());
        newPixel.setG(addPixel.getG());
        newPixel.setB(addPixel.getB());
        newPixel.setFitness(addPixel.getFitness());

        originalList.add(newPixel);
        return  originalList;
    }

    public ArrayList<Pixel> defaultGridValues(int length, int width, ArrayList<Pixel> bugList) {

        CommonElements commonElements = new CommonElements();
        int x = commonElements.randomWidthPoint();
        int y = commonElements.randomLengthPoint();
        Pixel bugPixel =  common.lookupPixel(x,y,bugList);
     //   int x = 1114;
     //   int y = 1129;


        ArrayList<Pixel> newGrid = new ArrayList<Pixel>();
        int counter  =0;
        do{
            if(bugList!=null){
                x = commonElements.randomWidthPoint();
                y = commonElements.randomLengthPoint();
                bugPixel = common.lookupPixel(x,y,bugList);

            }
            counter++;

        }while (bugPixel!=null || counter <10);

        int new_x = x;
        int new_y = y;

        Pixel currentPixel = new Pixel();
        currentPixel.setY(new_y);
        currentPixel.setX(new_x);
        currentPixel.setFitness(-2);
        currentPixel =  commonElements.setRandomRGB(currentPixel);
        newGrid.add(currentPixel); //initial point of (x,y)

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

