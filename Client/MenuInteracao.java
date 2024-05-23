package Client;

import java.io.IOException;
import java.util.Scanner;

import Util.commandPrompt;
import Util.dataTools;

import User.UserManager;

public class MenuInteracao {
    private static UserManager userManager = new UserManager();

    public static void main(String[] args) throws IOException, InterruptedException {
        Scanner scanner = new Scanner(System.in);
        int opt = -1;

        while (opt != 5) {

            menuInicial();
            opt = scanner.nextInt();

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
        Scanner scanner = new Scanner(System.in);

        commandPrompt.clearPrompt();
        System.out.print("============================================\n");
        System.out.print("                     LOGIN                  \n");
        System.out.print("============================================\n");
        System.out.println("- USERNAME: ");
        String userName = scanner.nextLine();
        System.out.println("- PASSWORD:");
        String passWord = scanner.nextLine();

        scanner.close();

        if (!userManager.authenticate(userName, passWord)) {
            System.out.println("\n\n[ATTENTION]: User or password incorrect!\n\n");
            commandPrompt.WaitForInteraction();
            menuLogin();
        }
    }

    public static void menuInicial() {
        // Scanner scanner = new Scanner(System.in);
        commandPrompt.clearPrompt();
        System.out.print("============================================\n");
        System.out.print("                  MAIN MENU                 \n");
        System.out.print("============================================\n");
        System.out.print("[1] - Fazer Login;\n");
        System.out.print("[2] - Criar Conta;\n");
        System.out.print("[3] - Desligar Cliente;\n");
        System.out.print("Digite a opção desejada:\n");

        // scanner.close();
    }

    // public static void menuClient(){
    // Scanner scanner = new Scanner(System.in);
    // commandPrompt.clearPrompt();
    // System.out.print("============================================\n");
    // System.out.print(" CLIENT MENU \n");
    // System.out.print("============================================\n");
    // System.out.print("[1] - Enviar Mensagem Para um Usuário;\n");
    // System.out.print("[2] - Enviar Mensagem para Todos os Usuários;\n");
    // System.out.print("[3] - Gerenciar Conta;\n");
    // System.out.print("[4] - Verificar Usuários Online;\n");
    // System.out.print("[5] - Ver Histórico de Mensagem;\n");
    // System.out.print("[6] - Sair;\n");
    // System.out.print("Digite a opção desejada:\n");

    // scanner.close();
    // }

    public static void menuCreateAccount() {
        Scanner scanner = new Scanner(System.in);

        commandPrompt.clearPrompt();

        System.out.print("============================================\n");
        System.out.print("              CREATE AN ACCOUNT             \n");
        System.out.print("============================================\n");

        String userNameCreate = "";
        String passWordCreate = "";
        String confirmPassWordCreate = "";

        System.out.println("- USERNAME: ");

        userNameCreate = scanner.nextLine();

        System.out.println("- PASSWORD:");
        passWordCreate = scanner.nextLine();
        System.out.println("- CONFIRM PASSWORD:");
        confirmPassWordCreate = scanner.nextLine();
        if (!(passWordCreate.equals(confirmPassWordCreate))) {
            commandPrompt.clearPrompt();
            System.out.println("\n\n[ATTENTION]: The passwords are not the same!\n\n");

            commandPrompt.WaitForInteraction();
            menuCreateAccount();
        }


        if (!userManager.addUser(userNameCreate, passWordCreate)) {
            System.out.println("\n\n[ATTENTION]: User already exists!\n\n");
            commandPrompt.WaitForInteraction();
            menuCreateAccount();
        }

    }

}
