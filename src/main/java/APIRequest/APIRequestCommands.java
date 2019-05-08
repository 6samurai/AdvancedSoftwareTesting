package APIRequest;

import Common.CommonElements;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;

public class APIRequestCommands {


    CommonElements commonElements = new CommonElements();

    String url = commonElements.getUrl();

    public HttpResponse<JsonNode> PUT(int x, int y, int r, int g, int b ){
        try{
            HttpResponse<JsonNode> response = Unirest.put(url)
                    .header("Content-Type", "application/json")
                    .header("Accept", "*/*")
                    .header("Cache-Control", "no-cache")
                    .header("Connection", "keep-alive")
                    .body("{\"session\":\""+commonElements.getSessionID()+"\", \"x\":\""+x+"\", \"y\" :\""+y+"\", \"r\":\""+r+"\", \"g\":\""+g+"\", \"b\":\""+b+"\"}")
                    .asJson();

         //   System.out.println("statusCode = " + response.getStatus());

            return  response;

        } catch (Exception e){
            return null;
        }

    }

 //   HttpResponse<String> response = Unirest.put("https://virtualscreen.azurewebsites.net/api/pixels")
 //           .header("Content-Type", "application/json")
  //          .header("User-Agent", "PostmanRuntime/7.11.0")
 //           .header("Accept", "*/*")
 //           .header("Cache-Control", "no-cache")
 //           .header("Postman-Token", "341afbff-bf12-4004-82b8-3721c6aa0eb8,489e6ccd-486e-4f3d-92a8-49d0cf5113d2")
 //           .header("Host", "virtualscreen.azurewebsites.net")
 //           .header("cookie", "ARRAffinity=0a7c27f9a984dc5f41af72a8d83a879416bc10a87f4e2fac4a80123dd531cb20")
 //           .header("accept-encoding", "gzip, deflate")
 //           .header("content-length", "81")
 //           .header("Connection", "keep-alive")
 //           .header("cache-control", "no-cache")
 //           .body("{\n  \"session\" : \"12345M\",\n  \"x\" : 9,\n  \"y\" : 9,\n  \"r\" : 1,\n  \"g\" : 2,\n  \"b\" : 3\n}")
 //           .asString();


    public HttpResponse<JsonNode> DELETE(){
        try{

            final HttpResponse<JsonNode> response = Unirest.delete(url+"?session="+commonElements.getSessionID())
                    .header("cache-control", "no-cache")
                    .asJson();
            //     System.out.println(response.getBody().getObject().get("r"));
            return response;

        } catch (Exception e){
            return  null;
        }
    }

 //   HttpResponse<String> response = Unirest.delete("https://virtualscreen.azurewebsites.net/api/pixels?session=12345M")
 //           .header("cache-control", "no-cache")
 //           .header("Postman-Token", "78a2c9d5-9692-4e7c-9747-90c961caafbf")
 //           .asString();


    public HttpResponse<JsonNode> GET(int x, int y){

        try{
            final HttpResponse<JsonNode> response = Unirest.get(url+"?session="+commonElements.getSessionID()+"&x="+x+"&y="+y)
                    .header("content", "application/json")
                    .header("Accept", "*/*")
                    .header("Cache-Control", "no-cache")
                    .header("Host", "virtualscreen.azurewebsites.net")
                    .header("Connection", "keep-alive")
                    //  .header("cache-control", "no-cache")
                    .asJson();
            //     System.out.println(response.getBody().getObject().get("r"));
            return response;

        }catch (Exception e){
            return  null;
        }
    }


//    HttpResponse<String> response = Unirest.get("https://virtualscreen.azurewebsites.net/api/pixels?session=12345M&x=9&y=9")
 //           .header("content", "application/json")
  //          .header("User-Agent", "PostmanRuntime/7.11.0")
   //         .header("Accept", "*/*")
    //        .header("Cache-Control", "no-cache")
     //       .header("Postman-Token", "8507df17-be53-435f-8f37-1678783fd70e,c4b1f572-fcfc-4138-ab8e-f26ce76d11db")
      //      .header("Host", "virtualscreen.azurewebsites.net")
       //     .header("cookie", "ARRAffinity=83b427bb19fd60d52f296056b135ba40fb873b99f41c49b33c92e70ef79b5d7b")
        //    .header("accept-encoding", "gzip, deflate")
         //   .header("Connection", "keep-alive")
          //  .header("cache-control", "no-cache")
           // .asString();
}
