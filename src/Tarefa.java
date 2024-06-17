import java.time.LocalDateTime;

public class Tarefa {
    private int id;
    private String descricao;
    private int prioridade;
    private int status;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataConclusao;
    private String nota;

    // Construtor
    public Tarefa(String descricao, int prioridade, int status, LocalDateTime dataCriacao, LocalDateTime dataConclusao, String nota) {
        this.descricao = descricao;
        this.prioridade = prioridade;
        this.status = status;
        this.dataCriacao = dataCriacao;
        this.dataConclusao = dataConclusao;
        this.nota = nota;
    }

    // Construtor com ID
    public Tarefa(int id, String descricao, int prioridade, int status, LocalDateTime dataCriacao, LocalDateTime dataConclusao, String nota) {
        this.id = id;
        this.descricao = descricao;
        this.prioridade = prioridade;
        this.status = status;
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

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getPrioridade() {
        return prioridade;
    }

    public void setPrioridade(int prioridade) {
        this.prioridade = prioridade;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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
