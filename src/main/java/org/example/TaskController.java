package org.example;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;
import org.example.model.Task;
import org.example.service.TaskService;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class TaskController {

    private static final String API_KEY = "12345";
    private static final TaskService service = new TaskService();

    public static void main(String[] args) throws IOException {

        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        server.createContext("/api/tasks", (exchange) -> {

            String method = exchange.getRequestMethod();
            String apiKey = exchange.getRequestHeaders().getFirst("API-KEY");

            // Проверка API ключа
            if (!API_KEY.equals(apiKey)) {
                sendResponse(exchange, 403, "Forbidden");
                return;
            }

            if ("GET".equals(method)) {
                sendResponse(exchange, 200, service.getAll().toString());
            }

            else if ("POST".equals(method)) {
                Task task = new Task();
                task.setTitle("Новая задача");
                task.setDescription("Создано через API");
                task.setCompleted(false);

                service.create(task);
                sendResponse(exchange, 201, "Task created");
            }

            else if ("DELETE".equals(method)) {
                service.delete(1L);
                sendResponse(exchange, 200, "Task deleted");
            }
        });

        server.start();
        System.out.println("Server started on http://localhost:8080");
    }

    private static void sendResponse(HttpExchange exchange, int status, String response) throws IOException {
        exchange.sendResponseHeaders(status, response.length());
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}