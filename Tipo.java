import java.util.ArrayList;
import java.util.List;

public class Tipo extends Entidade {
    private String nome;
    private String descricao;

    public Tipo(int id, String nome, String descricao) {
        super(id);
        this.nome = nome;
        this.descricao = descricao;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public String toString() {
        return "Tipo: " + nome + " - " + descricao;
    }


    @Override
    public java.util.List<Tipo> carregarTodos() {
        // lógica para buscar todos os tipos no json
        List<Tipo> tipos = new ArrayList<>();
        // preencher lista com os registros encontrados
        return tipos;
    }


    // Aqui você sobrescreve salvar, atualizar, apagar, carregar, etc.
}
