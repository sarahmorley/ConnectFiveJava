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
import java.util.UUID;
import java.util.concurrent.TimeUnit;

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
       /* Scanner scanner = new Scanner(System.in);
        System.out.println("Please choose a player ID");
        String playerId = scanner.nextLine();
        System.out.println("Please choose a colour");
        String colour = scanner.nextLine();*/
        String playerId = "22";
        String colour = "red";
        Game game = ConnectToGame(playerId, colour);
        System.out.print(game);
        Boolean playing = true;
        if(game.getGameStatus().equals("COMPLETED"))
             playing = false;
        while (playing) {
            //poll the server to see if its my turn - GET
            game = PollGame(game.getGameId(), game);
            String turn = game.getTurn();
            if (game.getTurn().equals(playerId)) {
                game = playTurn(game.getGameId(), 2, colour, game);
                DisplayBoard(game.getBoard());
                if(game.getGameStatus().equals("COMPLETED"))
                    playing = false;
            }
        }

    }
    public static Game ConnectToGame(String playerId, String colour) throws IOException, JSONException {
        Game game = new Game();
        String bodyString = "{\r\n    \"playerId\" : \"" +playerId + "\",\r\n    \"colour\" : \"" +colour + "\"\r\n}";
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
            game = objectMapper.readValue(responseData, Game.class);
            System.out.println("You have succesfully joined a game");
        }
        return game;

    }

    public static Game playTurn(UUID gameId, int turn, String colour, Game game) throws IOException {
        String bodyString = "{\r\n    \"turn\" : \"" + turn + "\",\r\n    \"colour\" : \"" +colour + "\"\r\n}";
        OkHttpClient client = new OkHttpClient().newBuilder()
                .connectTimeout(1000, TimeUnit.MINUTES)
                .writeTimeout(1000, TimeUnit.MINUTES)
                .readTimeout(1000, TimeUnit.MINUTES)
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, bodyString);
        Request request = new Request.Builder()
                .url("http://localhost:8002/game/"+ gameId.toString())
                .method("PATCH", body)
                .addHeader("Content-Type", "application/json")
                .build();
        Response response = client.newCall(request).execute();
        if(response.isSuccessful()) {
            String responseData = response.body().string();
            game = objectMapper.readValue(responseData, Game.class);
        }
        return game;
    }

    public static Game PollGame(UUID gameId, Game game) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url("http://localhost:8002/game/"+ gameId.toString())
                .method("GET", null)
                .addHeader("Content-Type", "application/json")
                .build();
        Response response = client.newCall(request).execute();
        if(response.isSuccessful()) {
            String responseData = response.body().string();
            game = objectMapper.readValue(responseData, Game.class);
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
        System.out.println("________________________________");
    }
}
