package Server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import Model.Mensagem;
import Model.User;
import Util.dataTools;

public class HandleRequests {

    private static HashMap<String, Long> activeClients = new HashMap<>();

    static public class SetClientOnServer implements HttpHandler {
        public void handle(HttpExchange exchange) throws IOException {
            if ("POST".equals(exchange.getRequestMethod())) {

                InputStream requestBodyStream = exchange.getRequestBody();
                String userNameConected = new String(requestBodyStream.readAllBytes(), StandardCharsets.UTF_8);

                String response = "[SERVER] Client Conected Successfully!";
                if (activeClients.containsKey(userNameConected)) {
                    exchange.sendResponseHeaders(401, response.getBytes().length);
                } else {
                    exchange.sendResponseHeaders(200, response.getBytes().length);
                }

                Long timeStamp = System.currentTimeMillis();
                activeClients.put(userNameConected, timeStamp);

                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }
        }
    }

    static public class AuthenticateUser implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("POST".equals(exchange.getRequestMethod())) {
                InputStream requestBodyStream = exchange.getRequestBody();
                String body = new String(requestBodyStream.readAllBytes(), StandardCharsets.UTF_8);

                // username e password separados por uma vírgula
                String[] credentials = body.split(",");
                if (credentials.length != 2) {
                    String response = "Invalid request format";
                    exchange.sendResponseHeaders(400, response.getBytes().length);
                    OutputStream os = exchange.getResponseBody();
                    os.write(response.getBytes());
                    os.close();
                    return;
                }

                String username = credentials[0];
                String password = credentials[1];

                System.out.println("Username: " + username + " Password: " + password);

                ControllerUser userManager = ControllerUser.getInstance();
                User userAuthenticated = userManager.authenticate(username, password);
                String response;

                if (userAuthenticated != null) {
                    String userAuthenticatedString = dataTools.objToString(userAuthenticated);
                    response = userAuthenticatedString;

                    exchange.sendResponseHeaders(200, response.getBytes().length);
                } else {
                    response = "Authentication failed";
                    exchange.sendResponseHeaders(401, response.getBytes().length);
                }

                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            } else {
                exchange.sendResponseHeaders(405, -1); // 405 Method Not Allowed
            }
        }
    }

    static public class ChangePassword implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("POST".equals(exchange.getRequestMethod())) {
                InputStream requestBodyStream = exchange.getRequestBody();
                String body = new String(requestBodyStream.readAllBytes(), StandardCharsets.UTF_8);

                // username, oldPassword e newPassword separados por uma vírgula
                String[] credentials = body.split(",");
                if (credentials.length != 3) {
                    String response = "Invalid request format";
                    exchange.sendResponseHeaders(400, response.getBytes().length);
                    OutputStream os = exchange.getResponseBody();
                    os.write(response.getBytes());
                    os.close();
                    return;
                }

                String username = credentials[0];
                String oldPassword = credentials[1];
                String newPassword = credentials[2];

                System.out.println("Username: " + username + " Old Password: " + oldPassword + " New Password: "
                        + newPassword);

                ControllerUser userManager = ControllerUser.getInstance();
                boolean passwordChanged = userManager.changePassword(username, oldPassword, newPassword);

                // print de debug
                System.out.println("Password changed: " + passwordChanged);
                String response;

                if (passwordChanged) {
                    response = "Password changed successfully";
                    exchange.sendResponseHeaders(200, response.getBytes().length);
                } else {
                    response = "Password change failed";
                    exchange.sendResponseHeaders(401, response.getBytes().length);
                }

                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            } else {
                exchange.sendResponseHeaders(405, -1); // 405 Method Not Allowed
            }
        }
    }

    static public class CreateAccount implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("POST".equals(exchange.getRequestMethod())) {
                InputStream requestBodyStream = exchange.getRequestBody();
                String body = new String(requestBodyStream.readAllBytes(), StandardCharsets.UTF_8);

                // username, password e email separados por uma vírgula
                String[] credentials = body.split(",");
                if (credentials.length != 2) {
                    String response = "Invalid request format";
                    exchange.sendResponseHeaders(400, response.getBytes().length);
                    OutputStream os = exchange.getResponseBody();
                    os.write(response.getBytes());
                    os.close();
                    return;
                }

                String username = credentials[0];
                String password = credentials[1];

                System.out.println("Username: " + username + " Password: " + password);

                ControllerUser userManager = ControllerUser.getInstance();
                boolean accountCreated = userManager.addUser(username, password);

                // print de debug
                System.out.println("Account created: " + accountCreated);
                String response;

                if (accountCreated) {
                    response = "Account created successfully";
                    exchange.sendResponseHeaders(200, response.getBytes().length);
                } else {
                    response = "Account creation failed";
                    exchange.sendResponseHeaders(401, response.getBytes().length);
                }

                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            } else {
                exchange.sendResponseHeaders(405, -1); // 405 Method Not Allowed
            }
        }
    }

    static public class DeleteAccount implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("POST".equals(exchange.getRequestMethod())) {
                InputStream requestBodyStream = exchange.getRequestBody();
                String body = new String(requestBodyStream.readAllBytes(), StandardCharsets.UTF_8);

                // username e password separados por uma vírgula
                String username = body;

                ControllerUser userManager = ControllerUser.getInstance();
                boolean accountDeleted = userManager.removeUser(username);

                String response;

                if (accountDeleted) {
                    response = "Account deleted successfully";
                    exchange.sendResponseHeaders(200, response.getBytes().length);
                } else {
                    response = "Account deletion failed";
                    exchange.sendResponseHeaders(401, response.getBytes().length);
                }

                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            } else {
                exchange.sendResponseHeaders(405, -1); // 405 Method Not Allowed
            }
        }
    }

    static public class PingHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {

            // Mandar a lista de onlines para o client
            // verificar se tem alguma mensagem pendente
            Long timeStamp = System.currentTimeMillis();
            // Extrai o sessionID do cabeçalho da solicitação
            String userNameConected = exchange.getRequestHeaders().getFirst("userName");

            activeClients.put(userNameConected, timeStamp);

            String response;

            // Verificar se tem alguma mensagem
            if (ControllerMessage.checkIfThereIsMessage(userNameConected)) {
                TreeMap<Long, Mensagem> messagesReceved = ControllerMessage.getRecentMessages(userNameConected);
                response = dataTools.serializeTreeMapToString(messagesReceved);
                exchange.sendResponseHeaders(200, response.getBytes().length);

            } else {
                // Envia uma resposta de sucesso para o cliente
                response = "Nenhuma Mensagem Recebida!";
                exchange.sendResponseHeaders(204, response.getBytes().length);
            }

            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();

        }
    }

    static public class MessageReceiver implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("POST".equals(exchange.getRequestMethod())) {

                // Lê o corpo da requisição
                InputStream requestBodyStream = exchange.getRequestBody();
                String requestBody = new String(requestBodyStream.readAllBytes(), StandardCharsets.UTF_8);

                try {
                    Mensagem newMessage = (Mensagem) dataTools.stringToObj(requestBody);
                    System.out.println(activeClients);
                    if (!activeClients.containsKey(newMessage.getDestinatario())) {
                        String response = "[SERVER DENIED]";
                        exchange.sendResponseHeaders(401, response.getBytes().length);
                        OutputStream os = exchange.getResponseBody();
                        os.write(response.getBytes());
                        os.close();
                        return;
                    }
                    System.out.println("newMessage: " + newMessage.getContent() + " " + newMessage.getDestinatario()
                            + " " + newMessage.getRemetente());
                    ControllerMessage.addNewMessage(newMessage);

                } catch (ClassNotFoundException | IOException e) {
                    System.out.println("[SERVER ERROR]: Message Error!");
                }

                // Envia uma resposta de sucesso para o cliente
                String response = "[SERVER SUCCESS]: Message Receved!";
                exchange.sendResponseHeaders(200, response.getBytes().length);
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();

            } else {
                // Método não suportado
                exchange.sendResponseHeaders(405, -1); // 405 Method Not Allowed
            }

        }

    }

    static public class MessageReceiverToALL implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("POST".equals(exchange.getRequestMethod())) {

                // Lê o corpo da requisição
                InputStream requestBodyStream = exchange.getRequestBody();
                String requestBody = new String(requestBodyStream.readAllBytes(), StandardCharsets.UTF_8);

                try {
                    Mensagem newMessage = (Mensagem) dataTools.stringToObj(requestBody);

                    for (Map.Entry<String, Long> entry : activeClients.entrySet()) {
                        String key = entry.getKey();
                        if (key.equals(newMessage.getRemetente())) {
                            continue;
                        }
                        newMessage.setDestinatario(key);
                        ControllerMessage.addNewMessage(newMessage);

                        System.out.println("newMessage: " + newMessage.getContent() + " " + newMessage.getDestinatario()
                                + " " + newMessage.getRemetente());
                    }

                } catch (ClassNotFoundException | IOException e) {
                    System.out.println("[SERVER ERROR]: Message Error!");
                }

                // Envia uma resposta de sucesso para o cliente
                String response = "[SERVER SUCCESS]: Message send to All online users!";
                exchange.sendResponseHeaders(200, response.getBytes().length);
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();

            } else {
                // Método não suportado
                exchange.sendResponseHeaders(405, -1); // 405 Method Not Allowed
            }

        }

    }

    static public class GetOnlineClients implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {

            System.err.println("entrou aqui");
            System.out.println(activeClients);

            String response;
            // Verificar se tem alguma mensagem
            if (!activeClients.isEmpty()) {
                String onlineClients = dataTools.objToString(activeClients);
                response = onlineClients;
                exchange.sendResponseHeaders(200, response.getBytes().length);

            } else {
                // Envia uma resposta de sucesso para o cliente
                response = "Nenhum Cliente Online!";
                exchange.sendResponseHeaders(401, response.getBytes().length);
            }

            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();

        }
    }

    public static void checkClientActivity() {
        new Thread(() -> {
            while (true) {
                long currentTime = System.currentTimeMillis();
                System.out.println(activeClients);

                List<String> tempRemoveArray = new ArrayList<>();
                try {
                    for (Map.Entry<String, Long> entry : activeClients.entrySet()) {
                        String clientId = entry.getKey();
                        Long lastActiveTime = entry.getValue();
                        if (currentTime - lastActiveTime > 10000) { // Tempo limite de inatividade de 10 segundos
                            System.out.println("Client " + clientId + " disconnected due to inactivity.");
                            tempRemoveArray.add(clientId);
                        }
                    }
                    for (String item : tempRemoveArray) {
                        activeClients.remove(item);
                    }
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    System.out.println("Deu Ruim na Thread de [checkClientActivity]");
                    // e.printStackTrace();
                }
            }
        }).start();
    }

}
