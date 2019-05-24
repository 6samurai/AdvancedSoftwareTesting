package Common;

import APIRequest.APIRequestCommands;
import Pixel.Pixel;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.sun.glass.ui.Pixels;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CommonElements {

    private String url = "https://virtualscreen.azurewebsites.net/api/pixels"; //url of Virtual Screen
    private String sessionID = "74993M"; // Session ID of Virtual Screen
    private int y_axis = 1201;//y-axis upper limit - range of y-axis is between 0 to 1200 inclusively
    private int x_axis = 1601;//x-axis upper limit - range of x-axis is between 0 to 1600 inclusively
    private int maxRGB = 256; // range of rgb colours is between 0 to 255 inclusively

    public String getUrl() {
        return url;
    }

    public String getSessionID() {
        return sessionID;
    }

    public int getMaxY() {
        return y_axis;
    } //returns maximum y-axis attribute

    public int getMaxX() {
        return x_axis;
    }//returns maximum x-axis attribute

    //Generates random value within RGB constrains
    public int randomColourValue() {
        Random rand = new Random();
        int n = rand.nextInt(maxRGB);

        return n;
    }

    //Generates random value within Y-axis Virtual Screen constrains
    public int randomLengthPoint() {
        Random rand = new Random();
        int n = rand.nextInt(this.y_axis);
        return n;
    }

    //Generates random value within X-axis Virtual Screen constrains
    public int randomWidthPoint() {
        Random rand = new Random();
        int n = rand.nextInt(this.x_axis);
        return n;
    }

    //set random RFG values for a specific Pixel
    public Pixel setRandomRGB(Pixel currentPixel) {
        currentPixel.setR(randomColourValue());
        currentPixel.setG(randomColourValue());
        currentPixel.setB(randomColourValue());

        return currentPixel;
    }

    //Carry out a PUT API request with Response verification
    public boolean putPixel(Pixel currentPixel) {
        APIRequestCommands apiRequestCommands = new APIRequestCommands();
        HttpResponse<JsonNode> response = null;

        response = apiRequestCommands.PUT(currentPixel.getX(), currentPixel.getY(), currentPixel.getR(), currentPixel.getG(), currentPixel.getB());

        if (response.getStatus() == 201) {
            return true;
        }
        return false;
    }

    //Carry out a GET API request with Response verification
    public HttpResponse<JsonNode> getPixel(Pixel currentPixel) {
        APIRequestCommands apiRequestCommands = new APIRequestCommands();
        HttpResponse<JsonNode> response = null;

        response = apiRequestCommands.GET(currentPixel.getX(), currentPixel.getY());
        if (response.getStatus() == 200) {
            return response;
        }
        return null;
    }

    //Compare Response RGB attributes to Pixel Attributes
    public boolean comparePixel(HttpResponse<JsonNode> response, Pixel currentPixel) {
        if ((response.getBody().getObject().get("r").equals(currentPixel.getR()) && response.getBody().getObject().get("g").equals(currentPixel.getG()) &&
                response.getBody().getObject().get("b").equals(currentPixel.getB()))) {
            return true;
        }
        return false;
    }

    //Search pixelList for Pixel classes with similar x and y values
    public Pixel lookupPixel(int x_axis, int y_axis, List<Pixel> pixelList) {
        for (final Pixel currentPixel : pixelList) {
            // Access properties of person, usage of getter methods would be good
            if (currentPixel.getX() == x_axis && currentPixel.getY() == y_axis) {
                // Found matching person
                return currentPixel;
            }
        }
        return null;
    }

    //Brute force method to view all bugged pixels - this is not used in any of the developed TDG methods
    public void printFitness() {
        APIRequestCommands apiRequestCommands = new APIRequestCommands();
        HttpResponse<JsonNode> response = null;
        for (int i = 0; i < y_axis; i++) {
            for (int j = 0; j < x_axis; j++) {
                response = apiRequestCommands.GET(j, i);
                System.out.print(response.getBody().getObject().get("fitness") + " ");
            }
            System.out.println("\n");
        }
    }

    public void saveToFile(String fileName, ArrayList<Pixel> pixelList) throws FileNotFoundException {
        PrintWriter pw;
        pw = new PrintWriter(new FileOutputStream(fileName));
        String toSave;
        for (int i = 0; i < pixelList.size(); i++) {
            toSave = "x " + pixelList.get(i).getX() + " y " + pixelList.get(i).getY();
            pw.println(toSave);
        }
        pw.close();
    }
}
