package com.messenger.Client;

import java.io.IOException;
import java.util.Scanner;

public class Controller {
    public static void main(String[] args) throws IOException, InterruptedException {
        Scanner scanner = new Scanner(System.in);
        int opt = -1;

        while (opt != 5) {
            System.out.print("============================================\n");
            System.out.print("                 MENU INICIAL               \n");
            System.out.print("============================================\n");
            System.out.print("[1] - Enviar Mensagem;\n");
            System.out.print("[2] - Trocar UserName;\n");
            System.out.print("[3] - Trocar Senha;\n");
            System.out.print("[4] - Verificar Usuários Online;\n");
            System.out.print("[5] - Sair;\n");
            System.out.print("Digite a opção desejada:\n");

            opt = scanner.nextInt();

            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            }

            switch (opt) {
                case 1:
                    System.out.println("teste");
                    break;
                case 2:
                    // system("clear");
                    // remover_aluno(vet_alunos, cad_alunos);
                    // puts("Pressione ENTER para continuar...");
                    // __fpurge(stdin);
                    // getchar();
                    // __fpurge(stdin);
                    break;
                case 3:
                    // system("clear");
                    // listar_alunos(vet_alunos, cad_alunos);
                    // puts("Pressione ENTER para continuar...");
                    // __fpurge(stdin);
                    // getchar();
                    // __fpurge(stdin);
                    break;
                case 4:
                    // system("clear");
                    // buscar_aluno(vet_alunos, cad_alunos);
                    // puts("Pressione ENTER para continuar...");
                    // __fpurge(stdin);
                    // getchar();
                    // __fpurge(stdin);
                    break;
                case 5:
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");

            }

        }
        scanner.close();

    }

}

// void menu_aluno(alunos **vet_alunos, int *cad_alunos, int *tam_vet_alunos){
// int opt;
// for(;;){
// system("clear");
// puts("============================================");
// puts(" SECAO ALUNO ");
// puts("============================================");
// puts("[1] - Cadastrar Aluno;");
// puts("[2] - Remover Aluno;");
// puts("[3] - Listar Alunos;");
// puts("[4] - Buscar Aluno;");
// puts("[5] - Voltar ao Menu Inicial;");
// puts("Digite a opção desejada:");
// scanf("%d", &opt);

// switch (opt) {
// case 1:
// system("clear");
// cadastrar_aluno(vet_alunos, cad_alunos, tam_vet_alunos);
// puts("Pressione ENTER para continuar...");
// __fpurge(stdin);
// getchar();
// __fpurge(stdin);
// break;
// case 2:
// system("clear");
// remover_aluno(vet_alunos, cad_alunos);
// puts("Pressione ENTER para continuar...");
// __fpurge(stdin);
// getchar();
// __fpurge(stdin);
// break;
// case 3:
// system("clear");
// listar_alunos(vet_alunos, cad_alunos);
// puts("Pressione ENTER para continuar...");
// __fpurge(stdin);
// getchar();
// __fpurge(stdin);
// break;
// case 4:
// system("clear");
// buscar_aluno(vet_alunos, cad_alunos);
// puts("Pressione ENTER para continuar...");
// __fpurge(stdin);
// getchar();
// __fpurge(stdin);
// break;
// case 5:
// return;
// }
// }
// }

// }
