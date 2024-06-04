package Client;

import java.io.IOException;
import java.util.Scanner;

import Util.commandPrompt;

public class MenuInteracao {
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {
        int opt = -1;

        while (opt != 3) {
            commandPrompt.clearPrompt();
            menuInicial();
            opt = scanner.nextInt();
            scanner.nextLine(); // Consumir a nova linha

            switch (opt) {
                case 1:
                    menuLogin();
                    break;
                case 2:
                    menuCreateAccount();
                    break;
                case 3:
                    System.out.println("Encerrando o programa.");
                    System.exit(0);
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }

        scanner.close();
    }

    public static void menuLogin() throws ClassNotFoundException, InterruptedException {
        commandPrompt.clearPrompt();
        System.out.print("============================================\n");
        System.out.print("                     LOGIN                  \n");
        System.out.print("============================================\n");
        System.out.println("- USERNAME: ");
        String userName = scanner.nextLine();
        System.out.println("- PASSWORD:");
        String passWord = scanner.nextLine();

        try {
            authenticateUser(userName, passWord);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void authenticateUser(String userName, String passWord)
            throws IOException, ClassNotFoundException, InterruptedException {
        while (!ClientHandler.authenticate(userName, passWord)) {
            commandPrompt.clearPrompt();
            System.out.println("Invalid credentials. Please try again.");
            System.out.println("- USERNAME: ");
            userName = scanner.nextLine();
            System.out.println("- PASSWORD:");
            passWord = scanner.nextLine();
        }
        commandPrompt.clearPrompt();
        System.out.println("Login successful.");
        commandPrompt.WaitForInteraction(scanner);
        menuClient();
    }

    public static void menuInicial() {
        commandPrompt.clearPrompt();
        System.out.print("============================================\n");
        System.out.print("                  MAIN MENU                 \n");
        System.out.print("============================================\n");
        System.out.print("[1] - Fazer Login;\n");
        System.out.print("[2] - Criar Conta;\n");
        System.out.print("[3] - Desligar Cliente;\n");
        System.out.print("Digite a opção desejada:\n");
    }

    public static void menuCreateAccount() {
        commandPrompt.clearPrompt();
        System.out.print("============================================\n");
        System.out.print("              CREATE AN ACCOUNT             \n");
        System.out.print("============================================\n");

        System.out.println("- USERNAME: ");
        String userNameCreate = scanner.nextLine();
        System.out.println("- PASSWORD:");
        String passWordCreate = scanner.nextLine();
        System.out.println("- CONFIRM PASSWORD:");
        String confirmPassWordCreate = scanner.nextLine();

        createAccount(userNameCreate, passWordCreate, confirmPassWordCreate);
    }

    private static void createAccount(String userNameCreate, String passWordCreate, String confirmPassWordCreate) {
        while (!passWordCreate.equals(confirmPassWordCreate)) {
            commandPrompt.clearPrompt();
            System.out.println("\n\n[ATTENTION]: The passwords are not the same!\n\n");
            commandPrompt.WaitForInteraction(scanner);
            System.out.println("- PASSWORD:");
            passWordCreate = scanner.nextLine();
            System.out.println("- CONFIRM PASSWORD:");
            confirmPassWordCreate = scanner.nextLine();
        }

        while (!ClientHandler.createAccount(userNameCreate, passWordCreate)) {
            System.out.println("\n\n[ATTENTION]: User already exists!\n\n");
            commandPrompt.WaitForInteraction(scanner);
            System.out.println("- USERNAME: ");
            userNameCreate = scanner.nextLine();
            System.out.println("- PASSWORD:");
            passWordCreate = scanner.nextLine();
            System.out.println("- CONFIRM PASSWORD:");
            confirmPassWordCreate = scanner.nextLine();
        }

        commandPrompt.WaitForInteraction(scanner);
    }

    private static void checkOnlineUsers()
            throws IOException, ClassNotFoundException, InterruptedException {
        System.out.println(ClientHandler.getListOfUsersConected());
        commandPrompt.WaitForInteraction(scanner);
    }

    private static void viewMessageHistory()
            throws IOException, ClassNotFoundException, InterruptedException {
        System.out.print("============================================\n");
        System.out.print(" HISTORICO DE MENSAGENS \n");
        System.out.print("============================================\n");
        commandPrompt.clearPrompt();
        ControllerArquive.readFile();
        commandPrompt.WaitForInteraction(scanner);
    }

    public static void menuClient() throws IOException, ClassNotFoundException, InterruptedException {
        commandPrompt.clearPrompt();

        ClientHandler.stablishConection();
        int opt = -1;

        while (opt != 6) {
            if (!ClientHandler.isConected()) {
                return;
            }
            System.out.print("============================================\n");
            System.out.print(" CLIENT MENU \n");
            System.out.print("============================================\n");
            System.out.print("[1] - Enviar Mensagem Para um Usuário;\n");
            System.out.print("[2] - Enviar Mensagem para Todos os Usuários;\n");
            System.out.print("[3] - Verificar Usuários Online;\n");
            System.out.print("[4] - Gerenciar Conta;\n");
            System.out.print("[5] - Ver Histórico de Mensagem;\n");
            System.out.print("[6] - Sair;\n");
            System.out.print("Digite a opção desejada:\n");

            opt = scanner.nextInt();
            scanner.nextLine(); // Consumir a nova linha

            switch (opt) {
                case 1:
                    sendMessageToUser();
                    break;
                case 2:
                    sendMessageToAll();
                    break;
                case 3:
                    checkOnlineUsers();
                    break;
                case 4:
                    menuManageAccount();
                    break;
                case 5:
                    viewMessageHistory();
                    break;
                case 6:
                    commandPrompt.clearPrompt();
                    System.out.println("Exiting...");
                    ClientHandler.logOutUser();
                    commandPrompt.WaitForInteraction(scanner);
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }

    }

    public static void menuManageAccount() throws IOException, ClassNotFoundException, InterruptedException {
        commandPrompt.clearPrompt();

        int opt = -1;

        while (opt != 3) {
            if (!ClientHandler.isConected()) {
                return;
            }
            System.out.print("============================================\n");
            System.out.print(" GERENCIAR CONTA \n");
            System.out.print("============================================\n");
            System.out.print("[1] - Alterar Senha;\n");
            System.out.print("[2] - Excluir Conta;\n");
            System.out.print("[3] - Sair;\n");
            System.out.print("Digite a opção desejada:\n");

            opt = scanner.nextInt();
            scanner.nextLine(); // Consumir a nova linha

            switch (opt) {
                case 1:
                    menuChangePassword();
                    break;
                case 2:
                    menuDeleteAccount();
                    break;
                case 3:
                    System.out.println("Exiting...");
                    commandPrompt.WaitForInteraction(scanner);
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    public static void menuDeleteAccount() throws IOException, ClassNotFoundException, InterruptedException {
        commandPrompt.clearPrompt();
        System.out.println("============================================");
        System.out.println("DELETE ACCOUNT");
        System.out.println("============================================");
        System.out.println("Are you sure you want to delete your account? (Y/N)");
        String confirmation = scanner.nextLine();

        if (confirmation.equalsIgnoreCase("N")) {
            System.out.println("Account deletion canceled.");
            commandPrompt.WaitForInteraction(scanner);
            return;
        }

        if (ClientHandler.deleteAccount()) {
            System.out.println("Account deleted successfully.");

        } else {
            System.out.println("Failed to delete account.");
        }

        commandPrompt.WaitForInteraction(scanner);
    }

    public static void sendMessageToUser() throws ClassNotFoundException, IOException, InterruptedException {
        commandPrompt.clearPrompt();
        String listOnline = ClientHandler.getListOfUsersConected();
        if (listOnline.equals("There is no Online Client!")) {
            System.out.println("No users online.");
            commandPrompt.WaitForInteraction(scanner);
            return;
        }

        System.out.println(ClientHandler.getListOfUsersConected());
        System.out.println("Write the username of the person that you want to chat:");
        String destinatario = scanner.nextLine();
        MessageHandler.chatMessage(destinatario, 1);
    }

    public static void sendMessageToAll() throws ClassNotFoundException, IOException, InterruptedException {
        commandPrompt.clearPrompt();

        MessageHandler.chatMessage("", 2);
    }

    public static void menuChangePassword() {
        System.out.println("============================================");
        System.out.println("CHANGE PASSWORD");
        System.out.println("============================================");
        System.out.println("Enter your current password:");
        String currentPassword = scanner.nextLine();
        System.out.println("Enter your new password:");
        String newPassword = scanner.nextLine();
        System.out.println("Confirm your new password:");
        String confirmNewPassword = scanner.nextLine();

        changePassword(currentPassword, newPassword, confirmNewPassword);
    }

    private static boolean changePassword(String currentPassword, String newPassword, String confirmNewPassword) {
        while (!newPassword.equals(confirmNewPassword)) {
            System.out.println("\n\n[ATTENTION]: The passwords are not the same!\n\n");
            System.out.println("Enter your new password:");
            newPassword = scanner.nextLine();
            System.out.println("Confirm your new password:");
            confirmNewPassword = scanner.nextLine();
        }

        if (ClientHandler.changePassword(currentPassword, newPassword)) {
            System.out.println("Password changed successfully.");
            return true;
        } else {
            System.out.println("Failed to change password.");
            return false;
        }

    }

}
