package Client;

import java.io.IOException;
import java.util.Scanner;

import Server.UserManager;
import Util.commandPrompt;

public class MenuInteracao {
    private static UserManager userManager = new UserManager();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws IOException, InterruptedException {
        int opt = -1;

        while (opt != 3) {
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
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }

        scanner.close();
    }

    public static void menuLogin() {
        // commandPrompt.clearPrompt();
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

    private static void authenticateUser(String userName, String passWord) throws IOException {
        while (!ClientHandler.authenticate(userName, passWord)) {
            System.out.println("Invalid credentials. Please try again.");
            System.out.println("- USERNAME: ");
            userName = scanner.nextLine();
            System.out.println("- PASSWORD:");
            passWord = scanner.nextLine();
        }
        System.out.println("Login successful.");
        menuClient();
    }

    public static void menuInicial() {
        // commandPrompt.clearPrompt();
        System.out.print("============================================\n");
        System.out.print("                  MAIN MENU                 \n");
        System.out.print("============================================\n");
        System.out.print("[1] - Fazer Login;\n");
        System.out.print("[2] - Criar Conta;\n");
        System.out.print("[3] - Desligar Cliente;\n");
        System.out.print("Digite a opção desejada:\n");
    }

    public static void menuCreateAccount() {
        // commandPrompt.clearPrompt();
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
            // commandPrompt.clearPrompt();
            System.out.println("\n\n[ATTENTION]: The passwords are not the same!\n\n");
            commandPrompt.WaitForInteraction();
            System.out.println("- PASSWORD:");
            passWordCreate = scanner.nextLine();
            System.out.println("- CONFIRM PASSWORD:");
            confirmPassWordCreate = scanner.nextLine();
        }

        while (!userManager.addUser(userNameCreate, passWordCreate)) {
            System.out.println("\n\n[ATTENTION]: User already exists!\n\n");
            commandPrompt.WaitForInteraction();
            System.out.println("- USERNAME: ");
            userNameCreate = scanner.nextLine();
            System.out.println("- PASSWORD:");
            passWordCreate = scanner.nextLine();
            System.out.println("- CONFIRM PASSWORD:");
            confirmPassWordCreate = scanner.nextLine();
        }

        System.out.println("Account created successfully.");
    }

    public static void menuClient() {
        // commandPrompt.clearPrompt();
        System.out.print("============================================\n");
        System.out.print(" CLIENT MENU \n");
        System.out.print("============================================\n");
        System.out.print("[1] - Enviar Mensagem Para um Usuário;\n");
        System.out.print("[2] - Enviar Mensagem para Todos os Usuários;\n");
        System.out.print("[3] - Verificar Usuários Online;\n");
        System.out.print("[4] - Alterar Senha da Conta;\n");
        System.out.print("[5] - Ver Histórico de Mensagem;\n");
        System.out.print("[6] - Sair;\n");
        System.out.print("Digite a opção desejada:\n");

        int opt = scanner.nextInt();
        scanner.nextLine(); // Consumir a nova linha

        switch (opt) {
            // case 1:
            // sendMessageToUser();
            // break;
            // case 2:
            // sendMessageToAll();
            // break;
            // case 3:
            // checkOnlineUsers();
            // break;
            case 4:
                menuChangePassword();
                break;
            // case 5:
            // viewMessageHistory();
            // break;
            case 6:
                System.out.println("Exiting...");
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }

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
}
