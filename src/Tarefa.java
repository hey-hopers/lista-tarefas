import java.time.LocalDateTime;

public class Tarefa {
    private int id;
    private String titulo;
    private int prioridade;
    private String nomePrioridade;
    private int status;
    private String nomeStatus;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataConclusao;
    private String nota;

    // Construtor
    public Tarefa(String titulo, int prioridade, int status, LocalDateTime dataCriacao, LocalDateTime dataConclusao, String nota) {
        this.titulo = titulo;
        this.prioridade = prioridade;
        this.status = status;
        this.dataCriacao = dataCriacao;
        this.dataConclusao = dataConclusao;
        this.nota = nota;
    }

    // Construtor com ID
    public Tarefa(int id, String titulo, int prioridade, String nomePrioridade, int status, String nomeStatus, LocalDateTime dataCriacao, LocalDateTime dataConclusao, String nota) {
        this.id = id;
        this.titulo = titulo;
        this.prioridade = prioridade;
        this.nomePrioridade = nomePrioridade;
        this.status = status;
        this.nomeStatus = nomeStatus;
        this.dataCriacao = dataCriacao;
        this.dataConclusao = dataConclusao;
        this.nota = nota;
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public int getPrioridade() {
        return prioridade;
    }

    public void setPrioridade(int prioridade) {
        this.prioridade = prioridade;
    }

    public String getnomePrioridade() {
        return nomePrioridade;
    }

    public void setnomePrioridade(String nomePrioridade) {
        this.nomePrioridade = nomePrioridade;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getnomeStatus() {
        return nomeStatus;
    }

    public void setnomeStatus(String nomeStatus) {
        this.nomeStatus = nomeStatus;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public LocalDateTime getDataConclusao() {
        return dataConclusao;
    }

    public void setDataConclusao(LocalDateTime dataConclusao) {
        this.dataConclusao = dataConclusao;
    }

    public String getNota() {
        return nota;
    }

    public void setNota(String nota) {
        this.nota = nota;
    }
}
