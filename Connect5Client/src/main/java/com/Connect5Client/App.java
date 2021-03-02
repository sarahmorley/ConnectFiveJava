package com.Connect5Client;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.json.JSONException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class App
{
    public static final int Rows = 6;
    public static final int Cols = 9;
    private static ObjectMapper objectMapper = new ObjectMapper();
    private static URL url;
    static {
        try {
            url = new URL("http://localhost:8080/game");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public static void main( String[] args ) throws IOException, JSONException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please choose a player ID");
        String playerId = scanner.nextLine();
        System.out.println("Please choose a letter to represent your tile colour");
        String colour = scanner.nextLine();
        Game game = ConnectToGame(playerId, colour);
        Boolean playing = true;
        while (playing) {
            game = PollGame(game.getGameId(), game);
            if(game.getGameStatus().equals("CREATEDWITHONEPLAYER"))
                System.out.println("Waiting for another player to join.");
            else if(game.getGameStatus().equals("COMPLETED")){
                System.out.println("The game is over. The winner is " + game.getWinner());
                break;
            }
            if (game.getTurn() != null && game.getTurn().equals(colour)) {
                DisplayBoard(game.getBoard());
                System.out.println("Play your turn");
                int turn = Integer.parseInt(scanner.nextLine());
                while (turn < 0 || turn > Cols){
                    System.out.println("You must play a turn between 1 and 9: Try Again");
                    turn = Integer.parseInt(scanner.nextLine());
                }
                game = playTurn(game.getGameId(), turn, colour, game);
                if(game.getGameStatus().equals("FULLCOL"))
                    System.out.println("That column is already full. You lose a turn");
                DisplayBoard(game.getBoard());
                if(game.getGameStatus().equals("COMPLETED")){
                    System.out.println("The game is over. The winner is " + game.getWinner());
                    playing = false;
                }
            }
        }
    }

    public static Game ConnectToGame(String playerId, String colour) throws IOException, JSONException {
        Game game = new Game();
        String bodyString = "{\r\n    \"playerId\" : \"" + playerId + "\",\r\n    \"colour\" : \"" +colour + "\"\r\n}";
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
                .url("http://localhost:8080/game/"+ gameId.toString())
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
                .url("http://localhost:8080/game/" + gameId.toString())
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
