package TestDataGenerationSystems;

import APIRequest.APIRequestCommands;
import Common.CommonElements;
import Pixel.Pixel;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HillClimbing_TDG {

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

                HillClimbing_TDG hillClimbing_tdg = new HillClimbing_TDG();
                currentPoints = hillClimbing_tdg.HillClimbing_TDG_Method(currentPoints, common.getLength(), common.getWidth(), displayBugs);
                currentPixel = currentPoints.get(0);

                currentPixel = common.setRandomRGB(currentPixel);
                int fitness = 0;
                if (common.putPixel(currentPixel)) {
                    response = common.getPixel(currentPixel);
                    gridValuesValid = common.comparePixel(response, currentPixel);
                    if (!gridValuesValid) {
                        displayBugs.add(currentPixel);
                        System.out.println("x " + currentPixel.getX() + " y " + currentPixel.getY());
                        fitness = (Integer) response.getBody().getObject().get("fitness");
                    } else {
                        fitness = 0;
                    }

                    currentPoints.get(0).setFitness(fitness);


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

    public List<Pixel> HillClimbing_TDG_Method(List<Pixel> currentGrid, int length, int width, List<Pixel> bugList) {
        try {

            Pixel currentPixel = new Pixel();
            CommonElements common = new CommonElements();
            int x = 0;
            int y = 0;
            int fitnessCount = 0;
            int currentFitness = 0;
            int largestFitness = 0;
            int largestFitness_ID = 0;

            List<Pixel> newGrid = new ArrayList<Pixel>();

            //for initial case where no points are evaluated
            if (currentGrid == null) {
                currentGrid = defaultGridValues(length, width, 3, 3);
                return currentGrid;

            } else {
                for (int i = 0; i < currentGrid.size(); i++) {
                    currentFitness = currentGrid.get(i).getFitness();
                    //ignore default values
                    if (currentFitness != -2 && currentFitness != largestFitness)
                        fitnessCount += currentFitness;

                    if (currentFitness == -2) {
                        if (i != 0)
                            Collections.swap(currentGrid, 0, i);
                        //return point to be evaluated for fitness
                        return currentGrid;
                    }

                    if (currentFitness > largestFitness) {
                        largestFitness = currentFitness;
                        largestFitness_ID = i;
                    }
                }
                //if current set of points considered produced no bugs
                if (fitnessCount == 0 || fitnessCount == largestFitness) {
                    currentGrid = defaultGridValues(length, width, 3, 3);
                    return currentGrid;
                }
            }

            //point with largest fitness set as first entry of list
            Collections.swap(currentGrid, 0, largestFitness_ID);

            // populate points neighbouring new max point
            x = currentGrid.get(0).getX();
            y = currentGrid.get(0).getY();
            int diff_x = 0;
            int diff_y = 0;

            //to check if highest fitness is in the middle of the current evaluated points
            for (int i = 0; i < currentGrid.size(); i++) {
                if (currentGrid.get(i).getY() == y && currentGrid.get(i).getX() == x) {
                    continue;
                }
                diff_x = diff_x + (currentGrid.get(i).getX()) - x;
                diff_y = diff_y + (currentGrid.get(i).getY()) - y;

            }
            //checks when hill climb algorithm reaches a local maximum
            if (diff_x == 0 && diff_y == 0 && currentGrid.size() == 9) {
                //when hill climbing point is its current highest - reset
                currentGrid = defaultGridValues(length, width, 3, 3);
                return currentGrid;
            } else if (currentGrid.size() == 6) {
                if (((diff_x == 0 && (diff_y == 3 || diff_y == -3)) && (y == 0 || y == 1200)
                        || (diff_y == 0 && (diff_x == 3 || diff_x == -3)) && (x == 0 || x == 1600))
                ) {
                    currentGrid = defaultGridValues(length, width, 3, 3);
                    return currentGrid;
                }

            } else if (currentGrid.size() == 4) {
                if ((x == 0 && y == 0 && diff_x == 2 && diff_y == 2) ||
                        (x == 0 && y == 1200 && diff_x == 2 && diff_y == -2) ||
                        (x == 1600 && y == 0 && diff_x == 2 && diff_y == 2) ||
                        (x == 1600 && y == 1600 && diff_x == 2 && diff_y == -2)
                ) {
                    currentGrid = defaultGridValues(length, width, 3, 3);
                    return currentGrid;
                }


            }

            //created new grid relative to point with highest fitness value
            int new_x = x;
            int new_y = y;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {

                    if (i != 0)
                        new_x = x + (int) Math.pow(-1, i);

                    if (j != 0)
                        new_y = y + (int) Math.pow(-1, j);

                    if (new_x < width && new_y < length & new_x >= 0 && new_y >= 0) {
                        currentPixel = new Pixel();
                        currentPixel = currentPixel.defaultPixel();
                        currentPixel.setX(new_x);
                        currentPixel.setY(new_y);
                        newGrid.add(currentPixel);
                        new_x = x;
                        new_y = y;
                    }
                }
            }
            //new grid generated is compared to current grid - any points similar to each are set with the same attributes
            int samePoints = 0;
            int x_currentGrid;
            int y_currentGrid;
            int x_newGrid;
            int y_newGrid;
            int swap_id = -1;
            Pixel findInBugList = new Pixel();
            //     for(int i =0; i<currentGrid.size();i++){
            //        x_currentGrid = currentGrid.get(i).getX();
            //          y_currentGrid = currentGrid.get(i).getY();
            for (int j = 0; j < newGrid.size(); j++) {
                x_newGrid = newGrid.get(j).getX();
                y_newGrid = newGrid.get(j).getY();

                 /*   for(int i =0; i<currentGrid.size();i++){
                       x_currentGrid = currentGrid.get(i).getX();
                       y_currentGrid = currentGrid.get(i).getY();


                    }*/
                findInBugList = common.lookupPixel(x_newGrid, y_newGrid, bugList);

                if (findInBugList != null) {
                    newGrid.get(j).setPixel(findInBugList, newGrid.get(j));
                    samePoints++;
                } else {
                    swap_id = j;
                }

            }
            //if all of the point of the new grid correspond to the
            if (samePoints == newGrid.size() || swap_id == -1) {
                currentGrid = defaultGridValues(length, width, 3, 3);
                return currentGrid;
            } else {
                //swap a point to top of list to be evaluated
                Collections.swap(newGrid, 0, swap_id);
                return newGrid;
            }

        } catch (Exception e) {
            return null;
        }
    }

    public List<Pixel> defaultGridValues(int length, int width, int max_i, int max_j) {

        CommonElements commonElements = new CommonElements();
        int x = commonElements.randomWidthPoint();
        int y = commonElements.randomLengthPoint();

        int new_x = x;
        int new_y = y;
        List<Pixel> newGrid = new ArrayList<Pixel>();

        for (int i = 0; i < max_i; i++) {
            for (int j = 0; j < max_j; j++) {

                if (i != 0)
                    new_x = x + (int) Math.pow(-1, i);

                if (j != 0)
                    new_y = y + (int) Math.pow(-1, j);

                if (new_x < width && new_y < length && new_x >= 0 && new_y >= 0) {
                    Pixel currentPixel = new Pixel();
                    currentPixel = currentPixel.defaultPixel();
                    currentPixel.setY(new_y);
                    currentPixel.setX(new_x);
                    newGrid.add(currentPixel);
                    new_x = x;
                    new_y = y;
                }
            }
        }
        return newGrid;
    }
}