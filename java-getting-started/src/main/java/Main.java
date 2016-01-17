import java.sql.*;
import java.util.*;

import java.net.URI;
import java.net.URISyntaxException;

import static spark.Spark.*;
import spark.template.freemarker.FreeMarkerEngine;
import spark.ModelAndView;
import static spark.Spark.get;

import com.clarifai.api.*;
import java.io.*;
import java.nio.file.*;
import javax.servlet.*;
import javax.servlet.http.*;
import com.google.gson.*;

import com.heroku.sdk.jdbc.DatabaseUrl;

public class Main {

  public static void main(String[] args) {

    port(Integer.valueOf(System.getenv("PORT")));
    staticFileLocation("/public");

    get("/keywords", (request, response) -> {
      Gson gson = new Gson();
      String urlsq = request.queryParams("urls");
      String[] urls = gson.fromJson(urlsq, String[].class);
      if(urls != null && urls.length != 0){ 
        String json = gson.toJson(Tagger.tag(new RecognitionRequest(urls) ) );
        return json;
      }else{
        return "[]";
      }
    });

    get("/instafetch", (request, response) -> {
      Gson gson = new Gson();
      String tag = request.queryParams("tag");
      if(tag != null){ 
        try{
          String json = gson.toJson(Instascrape.getHashtags(tag));
          return json;
        }catch(Exception e){
          return "[]";
        }
      }else{
        return "[]";
      }
    });

    post("/tag", (request, response) -> {
        Gson gson = new Gson();
        MultipartConfigElement multipartConfigElement = new MultipartConfigElement("/tmp");
        request.raw().setAttribute("org.eclipse.multipartConfig", multipartConfigElement);
        final Part uploadedFile = request.raw().getPart("image");
        String file = "/tmp/"+Math.random();
        final Path path = Paths.get(file);
        try (final InputStream in = uploadedFile.getInputStream()) {
            Files.copy(in, path);
        }
        Tag[] tags = Tagger.tag(new RecognitionRequest(new File(file)))[0];
        ArrayList<ArrayList<String> > hashtags = Branch.branchTags(tags);
        //response.redirect("/");
        return gson.toJson(hashtags);
    });

    post("/tagurl", (request, response) -> {
        Gson gson = new Gson();
        Tag[] tags = Tagger.tag(new RecognitionRequest(request.body()))[0];
        ArrayList<ArrayList<String> > hashtags = Branch.branchTags(tags);
        return gson.toJson(hashtags);
    });

    get("/hello", (req, res) -> {
      ClarifaiClient clarifai = new ClarifaiClient("5MJ-DS69ldGpnRN0XEht3I_bshR_lHe1fIn1MeXp", 
        "DO_M_zVOM63rft3hR9PMN5t8QlxUF75hBTHgFUfr");
    List<RecognitionResult> results =
        clarifai.recognize(new RecognitionRequest(new File("cat.jpg")));

        String str = "";
        for (Tag tag : results.get(0).getTags()) {
           str += tag.getName() + ": " + tag.getProbability();
        }
        return str;
    });

    get("/", (request, response) -> {
            response.redirect("/hashbrown.html", 301);
            return "ayy lmao";
        });

    get("/db", (req, res) -> {
      Connection connection = null;
      Map<String, Object> attributes = new HashMap<>();
      try {
        connection = DatabaseUrl.extract().getConnection();

        Statement stmt = connection.createStatement();
        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS ticks (tick timestamp)");
        stmt.executeUpdate("INSERT INTO ticks VALUES (now())");
        ResultSet rs = stmt.executeQuery("SELECT tick FROM ticks");

        ArrayList<String> output = new ArrayList<String>();
        while (rs.next()) {
          output.add( "Read from DB: " + rs.getTimestamp("tick"));
        }

        attributes.put("results", output);
        return new ModelAndView(attributes, "db.ftl");
      } catch (Exception e) {
        attributes.put("message", "There was an error: " + e);
        return new ModelAndView(attributes, "error.ftl");
      } finally {
        if (connection != null) try{connection.close();} catch(SQLException e){}
      }
    }, new FreeMarkerEngine());

  }

}
