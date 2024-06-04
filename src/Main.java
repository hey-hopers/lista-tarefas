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
                    marcarTarefaComoConcluida();
                    break;
                case 3:
                    marcarTarefaComoNaoConcluida();
                    break;
                case 4:
                    alterarTarefa();
                    break;
                case 5:
                    removerTarefa();
                    break;
                case 6:
                    imprimirTarefas();
                    break;
                case 7:
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
        System.out.println("2. Marcar tarefa como concluída");
        System.out.println("3. Marcar tarefa como pendente");
        System.out.println("4. Alterar tarefa");
        System.out.println("5. Remover tarefa");
        System.out.println("6. Exibir tarefas");
        System.out.println("7. Sair");
        System.out.print("Escolha uma opção: ");
    }

    private static int getEscolhaDoUsuario() {
        return scanner.nextInt();
    }

    private static void adicionarTarefa() {
        System.out.print("Digite a descrição da tarefa: ");
        scanner.nextLine(); // Consumir a nova linha pendente após a leitura do número
        String descricao = scanner.nextLine();

        Tarefa tarefa = new Tarefa(descricao);
        if (listaDeAfazeres.adicionarTarefa(tarefa)) {
            System.out.println("\nTarefa adicionada com sucesso!");
        } else {
            System.out.println("\nErro ao adicionar a tarefa.");
        }
    }

    private static void marcarTarefaComoConcluida() {
        System.out.print("Digite o número da tarefa a ser marcada como concluída: ");
        int numeroDaTarefa = scanner.nextInt();

        if (listaDeAfazeres.marcarTarefaComoConcluida(numeroDaTarefa)) {
            System.out.println("\nTarefa marcada como concluída!");
        } else {
            System.out.println("\nErro ao marcar a tarefa como concluída.");
        }
    }

    private static void marcarTarefaComoNaoConcluida() {
        System.out.print("Digite o número da tarefa a ser marcada como pendente: ");
        int numeroDaTarefa = scanner.nextInt();

        if (listaDeAfazeres.marcarTarefaComoNaoConcluida(numeroDaTarefa)) {
            System.out.println("\nTarefa marcada como pendente!");
        } else {
            System.out.println("\nErro ao marcar a tarefa como pendente.");
        }
    }

    private static void alterarTarefa() {
        System.out.print("Digite o número da tarefa a ser alterada: ");
        int numeroDaTarefa = scanner.nextInt();

        System.out.print("Digite a nova descrição da tarefa: ");
        scanner.nextLine(); // Consumir a nova linha pendente após a leitura do número
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
