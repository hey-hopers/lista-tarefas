public class Tarefa {
    private int id;
    private int status_id;
    private String descricao;
    private String nome_prioridade;

    public int getId() {
        return id;
    }

    // Construtor que aceita apenas a descrição da tarefa
    public Tarefa(String descricao) {
        this.descricao = descricao;
        this.status_id = 1; // Por padrão, uma nova tarefa não está concluída
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getStatus() {
        return status_id;
    }

    public void setStatus(int status_id) {
        this.status_id = status_id;
    }

    public String getPrioridade() {
        return nome_prioridade;
    }

    public void setPrioridade(String nome_prioridade) {
        this.nome_prioridade = nome_prioridade;
    }

    @Override
    public String toString() {
        return "Tarefa{" +
                "id=" + id +
                "status=" + status_id +
                "prioridade=" + nome_prioridade +
                ", descricao='" + descricao + '\'' +
                '}';
    }
}
