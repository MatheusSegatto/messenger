package Client;

import java.io.IOException;
import java.util.Scanner;

import Util.commandPrompt;

public class MenuInteracao {
    public static void main(String[] args) throws IOException, InterruptedException {
        Scanner scanner = new Scanner(System.in);
        int opt = -1;
        

        while (opt != 5) {
            System.out.print("============================================\n");
            System.out.print("                 MENU INICIAL               \n");
            System.out.print("============================================\n");
            System.out.print("[1] - Enviar Mensagem Para um Usuário;\n");
            System.out.print("[2] - Enviar Mensagem para Todos os Usuários;\n");
            System.out.print("[3] - Gerenciar Conta;\n");
            System.out.print("[4] - Verificar Usuários Online;\n");
            System.out.print("[5] - Ver Histórico de Mensagem;\n");
            System.out.print("[6] - Sair;\n");
            System.out.print("Digite a opção desejada:\n");

            opt = scanner.nextInt();
            

            switch (opt) {
                case 1:
                    commandPrompt.clearPrompt();
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

    
    public void Login(){
        System.out.println("================ LOGIN ================");
        System.out.println("Username:");
        System.out.println("Senha:");
    }

}



//     void menu_aluno(alunos **vet_alunos, int *cad_alunos, int *tam_vet_alunos){
//         int opt;
//         for(;;){
//             system("clear");
//             puts("============================================");
//             puts("                 SECAO ALUNO                ");
//             puts("============================================");
//             puts("[1] - Cadastrar Aluno;");
//             puts("[2] - Remover Aluno;");
//             puts("[3] - Listar Alunos;");
//             puts("[4] - Buscar Aluno;");
//             puts("[5] - Voltar ao Menu Inicial;");
//             puts("Digite a opção desejada:");
//             scanf("%d", &opt);
    
//             switch (opt) {
//                 case 1:
//                     system("clear");
//                     cadastrar_aluno(vet_alunos, cad_alunos, tam_vet_alunos);
//                     puts("Pressione ENTER para continuar...");
//                     __fpurge(stdin);
//                     getchar();
//                     __fpurge(stdin);
//                     break;
//                 case 2:
//                     system("clear");
//                     remover_aluno(vet_alunos, cad_alunos);
//                     puts("Pressione ENTER para continuar...");
//                     __fpurge(stdin);
//                     getchar();
//                     __fpurge(stdin);
//                     break;
//                 case 3:
//                     system("clear");
//                     listar_alunos(vet_alunos, cad_alunos);
//                     puts("Pressione ENTER para continuar...");
//                     __fpurge(stdin);
//                     getchar();
//                     __fpurge(stdin);
//                     break;
//                 case 4:
//                     system("clear");
//                     buscar_aluno(vet_alunos, cad_alunos);
//                     puts("Pressione ENTER para continuar...");
//                     __fpurge(stdin);
//                     getchar();
//                     __fpurge(stdin);
//                     break;
//                 case 5:
//                     return;
//             }
//         }
//     }

// }
