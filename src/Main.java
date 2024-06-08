import java.util.Scanner;

public class Main {

    private static Scanner scanner = new Scanner(System.in);
    private static ListaDeAfazeres listaDeAfazeres = new ListaDeAfazeres();

    public static void main(String[] args) {
        boolean sair = false;

        while (!sair) {
            imprimirMenu();
            int escolha = getEscolhaDoUsuario();

            switch (escolha) {
                case 1:
                    adicionarTarefa();
                    break;
                case 2:
                    alterarTarefa();
                    break;
                case 3:
                    removerTarefa();
                    break;
                case 4:
                    imprimirTarefas();
                    break;
                case 5:
                    sair = true;
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        }

        System.out.println("Encerrando o programa...");
    }

    private static void imprimirMenu() {
        System.out.println("\n===== Lista de Tarefas =====");
        System.out.println("1. Adicionar tarefa");
        System.out.println("2. Alterar tarefa");
        System.out.println("3. Remover tarefa");
        System.out.println("4. Consultar tarefas");
        System.out.println("5. Sair");
        System.out.print("Escolha uma opção: ");
    }

    private static int getEscolhaDoUsuario() {
        return scanner.nextInt();
    }

    private static void adicionarTarefa() {
        System.out.print("Digite a descrição da tarefa: ");
        scanner.nextLine();
        String descricao = scanner.nextLine();

        Tarefa tarefa = new Tarefa(descricao);
        if (listaDeAfazeres.adicionarTarefa(tarefa)) {
            System.out.println("\nTarefa adicionada com sucesso!");
        } else {
            System.out.println("\nErro ao adicionar a tarefa.");
        }
    }

    private static void alterarTarefa() {
        System.out.print("Digite o número da tarefa a ser alterada: ");
        int numeroDaTarefa = scanner.nextInt();

        System.out.print("Digite a nova descrição da tarefa: ");
        scanner.nextLine();
        String novaDescricao = scanner.nextLine();

        Tarefa novaTarefa = new Tarefa(novaDescricao);

        if (listaDeAfazeres.alterarTarefa(numeroDaTarefa, novaTarefa)) {
            System.out.println("\nTarefa alterada com sucesso!");
        } else {
            System.out.println("\nErro ao alterar a tarefa.");
        }
    }

    private static void removerTarefa() {
        System.out.print("Digite o número da tarefa a ser removida: ");
        int numeroDaTarefa = scanner.nextInt();

        if (listaDeAfazeres.removerTarefa(numeroDaTarefa)) {
            System.out.println("\nTarefa removida com sucesso!");
        } else {
            System.out.println("\nErro ao remover a tarefa.");
        }
    }

    private static void imprimirTarefas() {
        listaDeAfazeres.imprimirTarefas();
    }
}
