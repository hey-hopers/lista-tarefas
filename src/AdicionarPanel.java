import org.jdesktop.swingx.JXDatePicker;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class AdicionarPanel extends JPanel {
    private ListaDeAfazeres listaDeAfazeres;
    private LocalDateTime dataHoraInicio;

    public AdicionarPanel(ListaDeAfazeres listaDeAfazeres) {
        this.listaDeAfazeres = listaDeAfazeres;
        this.dataHoraInicio = LocalDateTime.now();
        setBackground(new Color(200, 200, 200));
        initComponents();
    }

    private void initComponents() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 10, 15, 10); // Ajustar layout principal
    
        // Ajustes linha Titulo
        JLabel titulo_texto = new JLabel("Título:");
        titulo_texto.setFont(titulo_texto.getFont().deriveFont(Font.BOLD, 40f)); 
        JTextField titulo = new JTextField(30);
        titulo.setFont(titulo.getFont().deriveFont(Font.PLAIN, 22f));
        titulo.setPreferredSize(new Dimension(titulo.getPreferredSize().width, 40));
    
        // Ajustes linha Data de Inicio
        JLabel data_criacao_texto = new JLabel("Data de Criação:");
        data_criacao_texto.setFont(data_criacao_texto.getFont().deriveFont(Font.BOLD, 20f));
        JLabel data_criacao = new JLabel(dataHoraInicio.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm")));
        data_criacao.setFont(data_criacao.getFont().deriveFont(Font.BOLD, 16f));
        data_criacao.setPreferredSize(new Dimension(200,40));

        // Ajustes linha Prioridade
        JLabel prioridade_texto = new JLabel("Prioridade:");
        prioridade_texto.setFont(prioridade_texto.getFont().deriveFont(Font.BOLD, 20f));
        JComboBox<String> prioridade = new JComboBox<>(new String[]{"Baixa", "Média", "Alta"});
        prioridade.setSelectedIndex(0);
        prioridade.setFont(prioridade.getFont().deriveFont(Font.PLAIN, 16f));
        prioridade.setPreferredSize(new Dimension(150, prioridade.getPreferredSize().height));
    
        // Ajustes linha Data de Conclusão
        JLabel data_conclusao_texto = new JLabel("Data de Conclusão:");
        data_conclusao_texto.setFont(data_conclusao_texto.getFont().deriveFont(Font.BOLD, 20f));
        JXDatePicker data_conclusao = CustomizarData();
        CustomizarBotaoData(data_conclusao);
        JSpinner hora_conclusao = CustomizarHora();      
    
        JPanel data_conclusao_panel = new JPanel(new GridBagLayout());
        data_conclusao_panel.setBackground(new Color(200, 200, 200));
        GridBagConstraints lay_conclusao = new GridBagConstraints();
        lay_conclusao.insets = new Insets(0, 0, 0, 10);
    
        lay_conclusao.gridx = 0;
        data_conclusao_panel.add(data_conclusao, lay_conclusao);
    
        lay_conclusao.gridx = 1;
        data_conclusao_panel.add(hora_conclusao, lay_conclusao);

        // Ajustes linha Nota
        JLabel nota_texto = new JLabel("Nota:");
        nota_texto.setFont(nota_texto.getFont().deriveFont(Font.BOLD, 20f));
        JTextArea nota_area = new JTextArea(10, 45);
        nota_area.setLineWrap(true);
        nota_area.setWrapStyleWord(true);
        nota_area.setFont(nota_area.getFont().deriveFont(Font.PLAIN, 16f));
        nota_area.setPreferredSize(new Dimension(150, nota_area.getPreferredSize().height));
        JScrollPane nota = new JScrollPane(nota_area);
    
        // Ajustes linha Botão Adicionar
        JButton botao_adicionar = new JButton("Adicionar");
        botao_adicionar.setFont(botao_adicionar.getFont().deriveFont(Font.BOLD, 16f));
        botao_adicionar.setPreferredSize(new Dimension(botao_adicionar.getPreferredSize().width, 30));
    
        // Layout dos componentes
        // Titulo
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.LINE_END;
        add(titulo_texto, gbc);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        add(titulo, gbc);

        // Data de Criação
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.LINE_END;
        add(data_criacao_texto, gbc);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        add(data_criacao, gbc);
    
        // Prioridade
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.LINE_END;
        add(prioridade_texto, gbc);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        add(prioridade, gbc);
    
        // Data de Conclusão
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.LINE_END;
        add(data_conclusao_texto, gbc);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        add(data_conclusao_panel, gbc);
    
        // Nota
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.LINE_END;
        add(nota_texto, gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        gbc.gridheight = 2;
        gbc.anchor = GridBagConstraints.LINE_START;
        add(nota, gbc);
    
        // Botão de Adicionar Tarefa
        gbc.gridx = 1;
        gbc.gridy = 6;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        add(botao_adicionar, gbc);

        // Evento do botão Adicionar
        botao_adicionar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String tituloString = titulo.getText();
                int prioridadeInt = prioridade.getSelectedIndex() + 1;
                Date dataConclusaoDate = data_conclusao.getDate();
                Date horaConclusaoDate = (Date) hora_conclusao.getValue();
                String nota = nota_area.getText();
    
                if (tituloString.isEmpty() || dataConclusaoDate == null) {
                    JOptionPane.showMessageDialog(null, "Preencha todos os campos obrigatórios.");
                    return;
                }
    
                LocalDateTime dataConclusao = LocalDateTime.ofInstant(dataConclusaoDate.toInstant(), ZoneId.systemDefault());
                LocalDateTime horaConclusao = LocalDateTime.ofInstant(horaConclusaoDate.toInstant(), ZoneId.systemDefault());
    
                LocalDateTime dataHoraConclusao = dataConclusao.withHour(horaConclusao.getHour()).withMinute(horaConclusao.getMinute());
    
                Tarefa tarefa = new Tarefa(tituloString, prioridadeInt, 1, dataHoraInicio, dataHoraConclusao, nota);
                boolean sucesso = listaDeAfazeres.adicionarTarefa(tarefa);
    
                if (sucesso) {
                    JOptionPane.showMessageDialog(null, "Tarefa adicionada com sucesso.");
                    titulo.setText("");
                    prioridade.setSelectedIndex(0);
                    data_conclusao.setDate(null);
                    hora_conclusao.setValue(new Date());
                    nota_area.setText("");
                } else {
                    JOptionPane.showMessageDialog(null, "Erro ao adicionar tarefa.");
                }
            }
        });
    }
    
    // Metodos para auxiliar na interface de Data e Hora
    private JXDatePicker CustomizarData() {
        JXDatePicker data_campo = new JXDatePicker();
        data_campo.setFormats(new SimpleDateFormat("dd/MM/yyyy"));
        data_campo.getEditor().setColumns(10);
        data_campo.setFont(data_campo.getFont().deriveFont(Font.PLAIN, 16f));
        data_campo.setPreferredSize(new Dimension(150, data_campo.getPreferredSize().height));
        return data_campo;
    }
    
    private JSpinner CustomizarHora() {
        SpinnerDateModel modelo = new SpinnerDateModel();
        JSpinner rotacao = new JSpinner(modelo);
        rotacao.setEditor(new JSpinner.DateEditor(rotacao, "HH:mm"));
        rotacao.setValue(new Date());
        rotacao.setFont(rotacao.getFont().deriveFont(Font.PLAIN, 16f));
        rotacao.setPreferredSize(new Dimension(80, rotacao.getPreferredSize().height));
        return rotacao;
    }
    
    private void CustomizarBotaoData(JXDatePicker data_botao) {
        JButton botao = (JButton) data_botao.getComponent(1);
        ImageIcon icone = new ImageIcon("img/calendar-icon.png");
        Image imagem_ajustada = icone.getImage().getScaledInstance(18, 18, Image.SCALE_SMOOTH);
        botao.setIcon(new ImageIcon(imagem_ajustada));
        botao.setPreferredSize(new Dimension(30, botao.getPreferredSize().height));
    }
}
