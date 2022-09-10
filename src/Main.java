import org.json.*;
import java.net.*;
import java.net.http.*;
import java.util.Scanner;


public class Main {
    public static String link = "https://api.jikan.moe/v4/anime";
    public static String anime = userInputSearch();

    public static void main(String[] args) {
        requestSearch();

    }

    public static void requestSearch(){
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(link+"?q="+anime)).build();

        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenApply(Main::parseSearch)
                .thenAccept(System.out::println)
                .join();
        userInputAnime();
    }

    public static void requestAnime(int searchId){
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(link+"/"+searchId + "/full")).build();

        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenApply(Main::parseAnime)
                .thenAccept(System.out::println)
                .join();
    }

    public static String userInputSearch(){
        Scanner scan = new Scanner(System.in);

        System.out.println("What anime are you looking for?");

        String input = scan.nextLine().replaceAll("\\s+", "/");

        return input;
    }

    public static void userInputAnime(){
        Scanner scan = new Scanner(System.in);

        System.out.println("Which anime would you like to view ?(please enter the ID)");

        requestAnime(scan.nextInt());
    }

    public static String parseAnime(String responseBody) {
        int id = 0;
        String title = "";
        String type = "";
        String url = "";
        JSONArray titleArray = null;

        JSONObject response = new JSONObject(responseBody);

        JSONObject dataObject = response.getJSONObject("data");
        url = dataObject.getString("url");

        id = dataObject.getInt("mal_id");

            titleArray = dataObject.getJSONArray("titles");


            System.out.println("--------------");
            for (int j = 0; j < titleArray.length(); j++) {

                type = titleArray.getJSONObject(j).getString("type");
                System.out.println(type);
                title = titleArray.getJSONObject(j).getString("title");
                System.out.println(title);

            }

        return url+id+type+title;
    }

    public static String parseSearch(String responseBody){
        int id = 0;
        String title = "";
        String type = "";
        String url = "";
        JSONArray titleArray = null;

        JSONObject response = new JSONObject(responseBody);

        JSONArray dataArray = response.getJSONArray("data");

        for (int i = 0; i < dataArray.length(); i++) {
            url = dataArray.getJSONObject(i).getString("url");
            id = dataArray.getJSONObject(i).getInt("mal_id");

            System.out.println("--------------");
            System.out.println("url: "+url);
            System.out.println(" ");
            System.out.println("id: "+id);
            System.out.println(" ");


            titleArray = dataArray.getJSONObject(i).getJSONArray("titles");

            for (int j = 0; j < titleArray.length(); j++) {

                System.out.println(titleArray.getJSONObject(j).getString("type"));
                System.out.println(titleArray.getJSONObject(j).getString("title"));

            }

        }
        return url+id+type+title;
    }
}
