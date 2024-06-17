import javax.swing.*;

public class Main {
    private static ListaDeAfazeres listaDeAfazeres = new ListaDeAfazeres();

    public static void main(String[] args) {
        JFrame frame = new JFrame("Lista de Tarefas");
        ImageIcon icon = new ImageIcon("img/favicon.ico");
        
        // Definindo o ícone para o JFrame
        frame.setIconImage(icon.getImage());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

        // Crie um JTabbedPane
        JTabbedPane tabbedPane = new JTabbedPane();

        // Adicione o JTabbedPane ao JFrame
        frame.add(tabbedPane);

        // Adicione as abas
        adicionarMenu(tabbedPane, listaDeAfazeres);

        frame.setVisible(true);
    }

    private static void adicionarMenu(JTabbedPane tabbedPane, ListaDeAfazeres listaDeAfazeres) {
        // Painel de menu principal
        JPanel menuPanel = new MenuPanel(tabbedPane, listaDeAfazeres);
        tabbedPane.addTab("Menu Principal", menuPanel);
        
    }
}
