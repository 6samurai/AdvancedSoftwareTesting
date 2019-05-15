package SearchStrategies;

import APIRequest.APIRequestCommands;
import Common.CommonElements;
import Pixel.Pixel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HillClimbing_TDG {

    public List<Pixel> HillClimbing_TDG_Method (List<Pixel> currentGrid, int length, int width  ,  List<Pixel> bugList) {
        try {

            Pixel currentPixel = new Pixel();
            CommonElements common = new CommonElements();
            int x = 0;
            int y =0;
            int fitnessCount = 0;
            int currentFitness = 0;
            int largestFitness = 0;
            int largestFitness_ID = 0;

            List<Pixel> newGrid = new ArrayList<Pixel>();

            //for initial case where no points are evaluated
            if(currentGrid ==null){
                currentGrid = defaultGridValues(length, width,3,3);
                return  currentGrid;

            } else {
                for(int i = 0; i <currentGrid.size();i++){
                    currentFitness = currentGrid.get(i).getFitness();
                    //ignore default values
                    if(currentFitness!=-2 && currentFitness!=largestFitness)
                        fitnessCount+= currentFitness;

                    if(currentFitness==-2){
                        if(i!=0)
                            Collections.swap(currentGrid, 0, i);
                        //return point to be evaluated for fitness
                        return  currentGrid;
                    }

                    if(currentFitness>largestFitness){
                        largestFitness = currentFitness;
                        largestFitness_ID = i;
                    }
                }
                //if current set of points considered produced no bugs
                if(fitnessCount==0){
                    currentGrid = defaultGridValues(length, width,3,3);
                    return  currentGrid;
                }
            }

            //point with largest fitness set as first entry of list
            Collections.swap(currentGrid, 0, largestFitness_ID);

            // populate points neighbouring new max point
            x = currentGrid.get(0).getX();
            y = currentGrid.get(0).getY();
            int new_x = x;
            int new_y = y;

            //created new grid relative to point with highest fitness value
            for(int i =0;i<3;i++){
                for(int j =0; j<3;j++){

                    if(i !=0)
                         new_x =  x + (int) Math.pow(-1, i);

                    if( j !=0)
                         new_y =  y + (int) Math.pow(-1, j);

                    if(new_x<width && new_y<length){
                        currentPixel = new Pixel();
                        currentPixel = currentPixel.defaultPixel();
                        currentPixel.setX(new_x);
                        currentPixel.setY(new_y);
                        newGrid.add(currentPixel);
                        new_x=x;
                        new_y=y;
                    }
                }
            }
            //new grid generated is compared to current grid - any points similar to each are set with the same attributes
            int samePoints = 0;
            int x_currentGrid ;
            int y_currentGrid;
            int x_newGrid;
            int y_newGrid;
            int swap_id = -1;
            Pixel findInBugList = null;
            for(int i =0; i<currentGrid.size();i++){
                x_currentGrid = currentGrid.get(i).getX();
                y_currentGrid = currentGrid.get(i).getY();
                for(int j = 0; j<newGrid.size();j++){
                    x_newGrid = newGrid.get(j).getX();
                    y_newGrid = newGrid.get(j).getY();

                    findInBugList = common.lookupPixel(x_newGrid, y_newGrid,bugList);

                    if(findInBugList != null){
                        newGrid.get(j).setPixel(findInBugList,  newGrid.get(j));

                    } else if(x_currentGrid==x_newGrid && y_currentGrid==y_newGrid){
                        newGrid.get(j).setPixel(currentGrid.get(i),  newGrid.get(j));

                        samePoints++;
                        continue;
                    } else{
                        swap_id = j;
                    }

                }

            }
            //if all of the point of the new grid correspond to the
            if(samePoints == currentGrid.size() || swap_id==-1){
                currentGrid =   defaultGridValues(length, width,3,3);
                return  currentGrid;
            } else{
                //swap a point to top of list to be evaluated
                Collections.swap(newGrid, 0, swap_id);
                return newGrid ;
            }

        } catch (Exception e) {
            return  null;
        }
    }


    public List<Pixel> defaultGridValues(int length, int width, int max_i, int max_j){

        CommonElements commonElements = new CommonElements();
        int x =  commonElements.randomWidthPoint();
        int y = commonElements.randomLengthPoint();

        int new_x=x;
        int new_y=y;
        List<Pixel> newGrid = new ArrayList<Pixel>();

        for(int i =0;i<max_i;i++){
            for(int j =0; j<max_j;j++){

                if(i !=0)
                    new_x =  x + (int) Math.pow(-1, i);

                if( j !=0)
                    new_y =  y + (int) Math.pow(-1, j);

                if(new_x<width && new_y<length){
                    Pixel currentPixel = new Pixel();
                    currentPixel = currentPixel.defaultPixel();
                    currentPixel.setY(new_y);
                    currentPixel.setX(new_x);
                    newGrid.add(currentPixel);
                    new_x=x;
                    new_y=y;
                }
            }
        }
        return  newGrid;
    }
}