import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Comparator;
import java.util.List;

public class Method {
    private static final HttpClient CLIENT = HttpClient.newHttpClient();
    private static final Gson GSON = new Gson();

    public static User createUser(URI uri, User user) throws IOException, InterruptedException {
        final String requestBody = GSON.toJson(user);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .header("Content-type", "application/json")
                .build();
        HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("Status code createUser "+response.statusCode());
        return GSON.fromJson(response.body(), User.class);
    }

    public static User updateUser(URI uri, User user) throws IOException, InterruptedException {
        final String requestBody = GSON.toJson(user);
        URI newUri = URI.create(String.format("%s/%d", uri.toString(), user.getId()));
        HttpRequest request = HttpRequest.newBuilder()
                .uri(newUri)
                .PUT(HttpRequest.BodyPublishers.ofString(requestBody))
                .header("Content-type", "application/json")
                .build();
        HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("Status code updateUser "+response.statusCode());
        return GSON.fromJson(response.body(), User.class);
    }

    public static int deleteUser(URI uri, int userId) throws IOException, InterruptedException {
        URI newUri = URI.create(String.format("%s/%d", uri.toString(), userId));
        HttpRequest request = HttpRequest.newBuilder()
                .uri(newUri)
                .DELETE()
                .header("Content-type", "application/json")
                .build();
        HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        return response.statusCode();
    }

    public static List<User> getUsers(URI uri) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .header("Content-type", "application/json")
                .build();
        HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        List<User> users = GSON.fromJson(response.body(), new TypeToken<List<User>>() {
        }.getType());
        System.out.println("Status code getUsers "+response.statusCode());
        return users;
    }

    public static User getUserById(URI uri, int userId) throws IOException, InterruptedException {
        URI newUri = URI.create(String.format("%s/%d", uri.toString(), userId));
        HttpRequest request = HttpRequest.newBuilder()
                .uri(newUri)
                .GET()
                .header("Content-type", "application/json")
                .build();
        HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        User user = GSON.fromJson(response.body(), User.class);
        return user;
    }

    public static User getUserByUsername(URI uri, String username) throws IOException, InterruptedException {
        URI newUri = URI.create(String.format("%s?username=%s", uri.toString(), username));
        HttpRequest request = HttpRequest.newBuilder()
                .uri(newUri)
                .GET()
                .header("Content-type", "application/json")
                .build();
        HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        List<User> users = GSON.fromJson(response.body(), new TypeToken<List<User>>() {
        }.getType());
        System.out.println("Status code getUserByUsername "+response.statusCode());
        return users.get(0);
    }

    public static List<Comments> getUserLastPostComments (URI uri, int postId) throws IOException, InterruptedException {
        URI newUri = URI.create(String.format("%s/%d/comments", uri.toString(), postId));
        HttpRequest request = HttpRequest.newBuilder()
                .uri(newUri)
                .GET()
                .header("Content-type", "application/json")
                .build();
        HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        List<Comments> comments = GSON.fromJson(response.body(), new TypeToken<List<Comments>>() {
        }.getType());
        return comments;
    }

    public static List<Post> getUserPosts(URI uri, int userId) throws IOException, InterruptedException {
        URI newUri = URI.create(String.format("%s/%d/posts", uri.toString(), userId));
        HttpRequest request = HttpRequest.newBuilder()
                .uri(newUri)
                .GET()
                .header("Content-type", "application/json")
                .build();
        HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        List<Post> posts = GSON.fromJson(response.body(), new TypeToken<List<Post>>() {
        }.getType());
        return posts;
    }

    public static int getUserLastPostId(List<Post> posts) {
        int maxPostId = posts.stream()
                .max(Comparator.comparingInt(Post::getId))
                .get().getId();
        return maxPostId;
    }

    public static void fileWriter (int userId, int postId, String string) {
        File file = new File(String.format("src/main/resources/user-%d-post-%d-comments.json", userId, postId));
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            try {
                file.createNewFile();
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file))) {
            bufferedWriter.write(string);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    public static List<Task> getOpenTasks(URI uri, int userId) throws IOException, InterruptedException {
        URI newUri = URI.create(String.format("%s/%d/todos?completed=false", uri.toString(), userId));
        System.out.println(newUri);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(newUri)
                .GET()
                .header("Content-type", "application/json")
                .build();
        HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        List<Task> tasks = GSON.fromJson(response.body(), new TypeToken<List<Task>>() {
        }.getType());
        return tasks;
    }
}
