public class Tarefa {
    private int id;
    private String descricao;
    private boolean concluido;

    // Construtor que aceita apenas a descrição da tarefa
    public Tarefa(String descricao) {
        this.descricao = descricao;
        this.concluido = false; // Por padrão, uma nova tarefa não está concluída
    }

    public int getId() {
        return id;
    }

    public String getDescricao() {
        return descricao;
    }

    public boolean estaConcluido() {
        return concluido;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setConcluido(boolean concluido) {
        this.concluido = concluido;
    }

    @Override
    public String toString() {
        return "Tarefa{" +
                "id=" + id +
                ", descricao='" + descricao + '\'' +
                ", concluido=" + concluido +
                '}';
    }
}
