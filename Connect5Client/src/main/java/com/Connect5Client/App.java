package com.Connect5Client;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import okhttp3.*;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;
public class App
{
    public static int Rows = 6;
    public static int Cols = 9;
    private static ObjectMapper objectMapper = new ObjectMapper();
    private static URL url;
    static {
        try {
            url = new URL("http://localhost:8002/game");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }


    public static void main( String[] args ) throws IOException, JSONException {
        //Scanner scanner = new Scanner(System.in);
       // System.out.println("Please choose a player ID");
        //String playerId = scanner.nextLine();
        Game game = ConnectToGame("12");
        System.out.print(game);

    }

    public static Game ConnectToGame(String playerId) throws IOException, JSONException {
        Game game = new Game();
        String bodyString = "{\r\n    \"playerId\": \"" + playerId+ "\"\r\n}";
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, bodyString);
        Request request = new Request.Builder()
                .url(url)
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .build();
        Response response = client.newCall(request).execute();
        if(response.isSuccessful()) {
            String responseData = response.body().string();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            game = objectMapper.readValue(responseData, Game.class);
            System.out.println("You have succesfully joined a game");
        }
        return game;

    }
    public static void DisplayBoard(String[][] board) {
        for (int i = 0; i < Rows; i++)
        {
            System.out.print("|");
            for (int z = 0; z < Cols; z++)
            {
                if (board[i][z] == null)
                board[i][z] = "[ ]";
                System.out.print(board[i][z]);
            }
            System.out.print("| \n");
        }
    }
}
