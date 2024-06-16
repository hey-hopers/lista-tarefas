import javax.swing.*;
import java.awt.*;

public class MenuPanel extends JPanel {
    private JTabbedPane tabbedPane;
    private ListaDeAfazeres listaDeAfazeres;

    public MenuPanel(JTabbedPane tabbedPane, ListaDeAfazeres listaDeAfazeres) {
        this.tabbedPane = tabbedPane;
        this.listaDeAfazeres = listaDeAfazeres;
        initComponents();
    }

    private void initComponents() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.VERTICAL;

        // Adicionando imagem
        ImageIcon imagem_menu = new ImageIcon("img/banner.jpg");
        JLabel Jimagem = new JLabel(imagem_menu);
        add(Jimagem, gbc);

        // Painel para os botões
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));

        JButton btnAdicionarTarefa = new JButton("Adicionar Tarefa");
        btnAdicionarTarefa.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnAdicionarTarefa.addActionListener(e -> {
            if (!isTabOpen("Adicionar Tarefa")) {
                JPanel adicionarPanel = new AdicionarPanel(listaDeAfazeres);
                addTabWithCloseButton("Adicionar Tarefa", adicionarPanel);
                tabbedPane.setSelectedComponent(adicionarPanel); 
            } else {
                tabbedPane.setSelectedIndex(getTabIndex("Adicionar Tarefa"));
            }
        });

        JButton btnConsultarTarefas = new JButton("Consultar Tarefas");
        btnConsultarTarefas.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnConsultarTarefas.addActionListener(e -> {
            if (!isTabOpen("Consultar Tarefas")) {
                JPanel consultarPanel = new ConsultarPanel(listaDeAfazeres);
                addTabWithCloseButton("Consultar Tarefas", consultarPanel);
                tabbedPane.setSelectedComponent(consultarPanel); 
            } else {
                tabbedPane.setSelectedIndex(getTabIndex("Consultar Tarefas"));
            }
        });

        buttonPanel.add(Box.createVerticalGlue()); // Coloca os botões no centro verticalmente
        buttonPanel.add(btnAdicionarTarefa);
        buttonPanel.add(Box.createVerticalStrut(10)); // Espaço entre os botões
        buttonPanel.add(btnConsultarTarefas);
        buttonPanel.add(Box.createVerticalGlue()); // Coloca os botões no centro verticalmente

        gbc.gridy = 1;
        gbc.weighty = 0.5;
        add(buttonPanel, gbc);
    }

    private void addTabWithCloseButton(String title, JPanel panel) {
        tabbedPane.add(panel);
        int index = tabbedPane.indexOfComponent(panel);

        JPanel tabPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        tabPanel.setOpaque(false);

        JLabel tabTitle = new JLabel(title);

        JButton closeButton = createCloseButton();
        closeButton.addActionListener(e -> {
            int closeIndex = tabbedPane.indexOfTabComponent(tabPanel);
            if (closeIndex != -1) {
                tabbedPane.remove(closeIndex);
            }
        });

        tabPanel.add(tabTitle);
        tabPanel.add(Box.createHorizontalStrut(5)); // Espaço entre o texto e o botão
        tabPanel.add(closeButton);
        tabPanel.setBorder(BorderFactory.createEmptyBorder(2, 0, 0, 0));

        tabbedPane.setTabComponentAt(index, tabPanel);
    }

    private JButton createCloseButton() {
        JButton button = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(Color.BLACK);
                int width = getWidth();
                int height = getHeight();
                g.drawLine(2, 2, width - 3, height - 3);
                g.drawLine(width - 3, 2, 2, height - 3);
            }
        };
        button.setPreferredSize(new Dimension(17, 17));
        button.setContentAreaFilled(false);
        button.setFocusable(false);
        button.setBorder(BorderFactory.createEmptyBorder());
        button.setBorderPainted(false);

        // Mudar cor ao passar o mouse
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                button.setForeground(Color.RED);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                button.setForeground(Color.BLACK);
            }
        });

        return button;
    }

    private boolean isTabOpen(String title) {
        int tabCount = tabbedPane.getTabCount();
        for (int i = 0; i < tabCount; i++) {
            Component tabComponent = tabbedPane.getTabComponentAt(i);
            if (tabComponent instanceof JPanel) {
                JPanel tabPanel = (JPanel) tabComponent;
                for (Component component : tabPanel.getComponents()) {
                    if (component instanceof JLabel) {
                        JLabel label = (JLabel) component;
                        if (title.equals(label.getText())) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    private int getTabIndex(String title) {
        int tabCount = tabbedPane.getTabCount();
        for (int i = 0; i < tabCount; i++) {
            Component tabComponent = tabbedPane.getTabComponentAt(i);
            if (tabComponent instanceof JPanel) {
                JPanel tabPanel = (JPanel) tabComponent;
                for (Component component : tabPanel.getComponents()) {
                    if (component instanceof JLabel) {
                        JLabel label = (JLabel) component;
                        if (title.equals(label.getText())) {
                            return i;
                        }
                    }
                }
            }
        }
        return -1; // Not found
    }
}
