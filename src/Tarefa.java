import java.sql.Timestamp;

public class Tarefa {
    private int id;
    private int status_id;
    private String descricao;
    private Timestamp dataConclusao;

    public Tarefa(String descricao) {
        this.descricao = descricao;
        this.status_id = 1; // Por padrão, uma nova tarefa não está concluída
    }

    // Getters e setters para os campos id, status_id, descricao e prioridade

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStatus() {
        return status_id;
    }

    public void setStatus(int status_id) {
        this.status_id = status_id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Timestamp getDataConclusao() {
        return dataConclusao;
    }

    public void setDataConclusao(Timestamp dataConclusao) {
        this.dataConclusao = dataConclusao;
    }

    @Override
    public String toString() {
        return "Tarefa{" +
                "id=" + id +
                ", status_id=" + status_id +
                ", descricao='" + descricao + '\'' +
                ", dataConclusao=" + dataConclusao +
                '}';
    }
}
