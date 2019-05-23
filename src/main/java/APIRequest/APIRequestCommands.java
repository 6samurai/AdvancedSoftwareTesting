package APIRequest;

import Common.CommonElements;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;

public class APIRequestCommands {

    CommonElements commonElements = new CommonElements();
    String url = commonElements.getUrl();


    //PUT API Call
    public HttpResponse<JsonNode> PUT(int x, int y, int r, int g, int b) {
        try {
            HttpResponse<JsonNode> response = Unirest.put(url)
                    .header("Content-Type", "application/json")
                    .header("Accept", "*/*")
                    .header("Cache-Control", "no-cache")
                    .header("Connection", "keep-alive")
                    .body("{\"session\":\"" + commonElements.getSessionID() + "\", \"x\":\"" + x + "\", \"y\" :\"" + y + "\", \"r\":\"" + r + "\", \"g\":\"" + g + "\", \"b\":\"" + b + "\"}")
                    .asJson();

            //   System.out.println("statusCode = " + response.getStatus());

            return response;

        } catch (Exception e) {
            return null;
        }

    }

    //DELETE API Call
    public HttpResponse<JsonNode> DELETE() {
        try {

            final HttpResponse<JsonNode> response = Unirest.delete(url + "?session=" + commonElements.getSessionID())
                    .header("cache-control", "no-cache")
                    .asJson();
            //     System.out.println(response.getBody().getObject().get("r"));
            return response;

        } catch (Exception e) {
            return null;
        }
    }

    //GET API Call
    public HttpResponse<JsonNode> GET(int x, int y) {

        try {
            final HttpResponse<JsonNode> response = Unirest.get(url + "?session=" + commonElements.getSessionID() + "&x=" + x + "&y=" + y)
                    .header("content", "application/json")
                    .header("Accept", "*/*")
                    .header("Cache-Control", "no-cache")
                    .header("Host", "virtualscreen.azurewebsites.net")
                    .header("Connection", "keep-alive")
                    //  .header("cache-control", "no-cache")
                    .asJson();
            //     System.out.println(response.getBody().getObject().get("r"));
            return response;

        } catch (Exception e) {
            return null;
        }
    }
}
