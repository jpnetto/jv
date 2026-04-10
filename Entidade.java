public abstract class Entidade {

    protected int id;
    protected boolean persistido;

    // Construtor vazio
    public Entidade() {
        this.persistido = false;
    }

    // Construtor com id
    public Entidade(int id) {
        this.id = id;
        this.persistido = false;
    }

    // Getters e setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isPersistido() {
        return persistido;
    }

    public void setPersistido(boolean persistido) {
        this.persistido = persistido;
    }

    // Métodos de persistência
    public boolean salvar() {
        if (!persistido) {
            // aqui se salvara no JSON
            persistido = true;
            return true;
        }
        return false;
    }

    public boolean atualizar() {
        if (persistido) {
            // aqui se atualizara no JSON
            return true;
        }
        return false;
    }

    public boolean apagar(int id) {
        if (persistido) {
            // aqui se apagara no JSON
            persistido = false;
            return true;
        }
        return false;
    }

    public boolean carregar(int id) {
        // aqui se carregara no JSON
        return false;
    }

    // Método abstrato
    public abstract java.util.List<? extends Entidade> carregarTodos();

    public String toString() {
        return "Entidade [id=" + id + ", persistido=" + persistido + "]";
    }
}
