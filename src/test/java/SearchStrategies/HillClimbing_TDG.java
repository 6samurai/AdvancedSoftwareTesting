package SearchStrategies;

import Common.CommonElements;
import Pixel.Pixel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HillClimbing_TDG {

    public List<Pixel> HillClimbing_TDG_Method (List<Pixel> currentGrid, int length, int width ) {
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
            if(currentGrid ==null){
                currentGrid = defaultGridValues(length, width,3,3);
                return  currentGrid;

            } else {
                for(int i = 0; i <currentGrid.size();i++){
                    currentFitness = currentGrid.get(i).getFitness();
                    //ignore default values
                    if(currentFitness!=-2)
                        fitnessCount+= currentFitness;

                    if(currentFitness==-2){
                        if(i!=0)
                            Collections.swap(currentGrid, 0, i);

                        return  currentGrid;
                    }

                    if(currentFitness>largestFitness){
                        largestFitness = currentFitness;
                        largestFitness_ID = i;
                    }
                }

                if(fitnessCount==0){
                    currentGrid = defaultGridValues(length, width,3,3);
                    return  currentGrid;
                }
            }

            //point with largest fitness set as first entry of list
            Collections.swap(currentGrid, 0, largestFitness_ID);

            //move grid to consider points neighbouring new max point
            x = currentGrid.get(0).getX();
            y = currentGrid.get(0).getY();
            int new_x = x;
            int new_y = y;

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

            int samePoints = 0;
            int x_currentGrid ;
            int y_currentGrid;
            int x_newGrid;
            int y_newGrid;
            int swap_id = -1;
            for(int i =0; i<currentGrid.size();i++){
                x_currentGrid = currentGrid.get(i).getX();
                y_currentGrid = currentGrid.get(i).getY();
                for(int j = 0; j<newGrid.size();j++){
                    x_newGrid = newGrid.get(j).getX();
                    y_newGrid = newGrid.get(j).getY();

                    if(x_currentGrid==x_newGrid && y_currentGrid==y_newGrid){
                        newGrid.get(j).setPixel(currentGrid.get(i),  newGrid.get(j));

                        samePoints++;
                        continue;
                    } else{
                        swap_id = j;
                    }

                }

            }

            if(samePoints == currentGrid.size()){
            currentGrid =   defaultGridValues(length, width,3,3);
                return  currentGrid;
            } else{

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

    /*    for(int i =0;i<3;i++){
            for(int j =0; j<3;j++){
                new_x = x + i;
                new_y = y + j;
                if(new_x<width && new_y<length){
                    Pixel currentPixel = new Pixel();
                    currentPixel = currentPixel.defaultPixel();
                    currentPixel.setY(new_y);
                    currentPixel.setX(new_x);
                    newGrid.add(currentPixel);
                }
            }
        }*/


    }
}
