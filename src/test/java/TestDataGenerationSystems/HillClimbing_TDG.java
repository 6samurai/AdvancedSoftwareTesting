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

            long startTime = System.nanoTime();

            CommonElements common = new CommonElements();
            APIRequestCommands api_Request = new APIRequestCommands();

            //number of pixel requests
            int maxIteration = 100;
            Pixel currentPixel = new Pixel();
            ArrayList<Pixel> displayBugs = new ArrayList<Pixel>();
            HttpResponse<JsonNode> response = null;

            boolean gridValuesValid = false;
            List<Pixel> currentPoints = null;

            //Initial DELETE call
            api_Request.DELETE();

            for (int i = 0; i < maxIteration; i++) {

                HillClimbing_TDG hillClimbing_tdg = new HillClimbing_TDG();
                currentPoints = hillClimbing_tdg.HillClimbing_TDG_Method(currentPoints, common.getMaxY(), common.getMaxX(), displayBugs);

                //set x and y values for current pixel
                currentPixel = currentPoints.get(0);

                //set RGB values for current pixel
                currentPixel = common.setRandomRGB(currentPixel);
                int fitness = 0;

                //PUT pixel API call
                if (common.putPixel(currentPixel)) {
                    //GET pixel API call
                    response = common.getPixel(currentPixel);
                    //compare Response to current Pixel values
                    gridValuesValid = common.comparePixel(response, currentPixel);
                    if (!gridValuesValid) {
                        displayBugs.add(currentPixel);
                        System.out.println("x " + currentPixel.getX() + " y " + currentPixel.getY());
                        fitness = (Integer) response.getBody().getObject().get("fitness");
                    } else {
                        fitness = 0;
                    }
                    //set fitness value of current pixel
                    currentPoints.get(0).setFitness(fitness);

                } else {
                    throw new Exception();
                }
            }

            System.out.println("Bugs detected :" + displayBugs.size());

            //Clean up DELETE Call
            api_Request.DELETE();

            long endTime = System.nanoTime();
            System.out.println("Elapsed time " + (endTime - startTime) / 1000000 + " ms");
           // common.saveToFile("Hill_100",displayBugs);

        } catch (Exception e) {
            System.out.println("An error has occurred during execution");
        }
    }

    //Hill Climbing method
    //a pixel request will be carried out on the pixel at index 0 of currentGrid
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

            //for initial state where no pixels are evaluated
            if (currentGrid == null) {
                currentGrid = defaultGridValues(length, width, 3, 3);
                return currentGrid;

            } else {
                //iterate through  middle and neighbouring pixels
                for (int i = 0; i < currentGrid.size(); i++) {
                    currentFitness = currentGrid.get(i).getFitness();
                    //ignore default pixel values - said pixel still needs to be evaluated for fitness
                    if (currentFitness != -2 && currentFitness != largestFitness)
                        fitnessCount += currentFitness;
                    //when pixel still needs to be evaluated
                    if (currentFitness == -2) {
                        if (i != 0)
                            Collections.swap(currentGrid, 0, i);

                        //return pixel to be evaluated for fitness
                        return currentGrid;
                    }
                    //determine pixel with highest fitness value
                    if (currentFitness > largestFitness) {
                        largestFitness = currentFitness;
                        largestFitness_ID = i;
                    }
                }
                //if current set of pixels considered produced no bugs
                if (fitnessCount == 0) {
                    //  if (fitnessCount == 0 || fitnessCount == largestFitness) {
                    currentGrid = defaultGridValues(length, width, 3, 3);
                    return currentGrid;
                }
            }

            //point with largest fitness set as first entry of list
            Collections.swap(currentGrid, 0, largestFitness_ID);

            // populate points neighbouring to new highest fitness pixel
            x = currentGrid.get(0).getX();
            y = currentGrid.get(0).getY();
            int diff_x = 0;
            int diff_y = 0;

            //check if highest fitness is in the middle of the current evaluated points
            for (int i = 0; i < currentGrid.size(); i++) {
                if (currentGrid.get(i).getY() == y && currentGrid.get(i).getX() == x) {
                    continue;
                }
                diff_x = diff_x + (currentGrid.get(i).getX()) - x;
                diff_y = diff_y + (currentGrid.get(i).getY()) - y;

            }

            //checks if hill climb algorithm reaches a local maximum
            if (diff_x == 0 && diff_y == 0 && currentGrid.size() == 9) {
                //obtain new values
                currentGrid = defaultGridValues(length, width, 3, 3);
                return currentGrid;
                //scenario when centre pixel is at the border of the screen
            } else if (currentGrid.size() == 6) {
                if (((diff_x == 0 && (diff_y == 3 || diff_y == -3)) && (y == 0 || y == 1200)
                        || (diff_y == 0 && (diff_x == 3 || diff_x == -3)) && (x == 0 || x == 1600))
                ) {
                    currentGrid = defaultGridValues(length, width, 3, 3);
                    return currentGrid;
                }
                //scenario when centre pixel is at the corner of the screen
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

            //created new grid relative to pixel with highest fitness value
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
            int x_newGrid;
            int y_newGrid;
            int swap_id = -1;
            Pixel findInBugList = new Pixel();

            for (int j = 0; j < newGrid.size(); j++) {
                x_newGrid = newGrid.get(j).getX();
                y_newGrid = newGrid.get(j).getY();


                findInBugList = common.lookupPixel(x_newGrid, y_newGrid, bugList);

                if (findInBugList != null) {
                    newGrid.get(j).setPixel(findInBugList, newGrid.get(j));
                    samePoints++;
                } else {
                    swap_id = j;
                }

            }
            //check if all of the point of the new grid correspond to the current grid
            if (samePoints == newGrid.size() || swap_id == -1) {
                //consider new random pixel and neighbouring pixels
                currentGrid = defaultGridValues(length, width, 3, 3);
                return currentGrid;
            } else {
                //swap a point to top of list to be evaluated with attributes -2
                Collections.swap(newGrid, 0, swap_id);
                return newGrid;
            }

        } catch (Exception e) {
            return null;
        }
    }

    //will generate a new random middle pixel together with all of the 8 neighbournig pixels
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