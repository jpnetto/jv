import java.util.ArrayList;
import java.util.List;

public class Pokemon extends Entidade{


    //atributos da classe
    private String nome;
    private int numeroPokedex;
    private ArrayList<Tipo> tipo;
    private double altura;
    private double peso;
    private int stats;
    private String descricao;


    //getters e setters
    public Pokemon(int id, String nome, int numeroPokedex, double altura, double peso, int stats, String descricao) {
        super(id);
        this.nome = nome;
        this.numeroPokedex = numeroPokedex;
        this.altura = altura;
        this.peso = peso;
        this.stats = stats;
        this.descricao = descricao;
        this.tipo = new ArrayList<>();
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getNumeroPokedex() {
        return numeroPokedex;
    }

    public void setNumeroPokedex(int numeroPokedex) {
        this.numeroPokedex = numeroPokedex;
    }

    public ArrayList<Tipo> getTipo() {
        return tipo;
    }

    public void setTipo(ArrayList<Tipo> tipo) {
        this.tipo = tipo;
    }

    public double getAltura() {
        return altura;
    }

    public void setAltura(double altura) {
        this.altura = altura;
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getStats() {
        return stats;
    }

    public void setStats(int stats) {
        this.stats = stats;
    }


    public void adicionarTipo(Tipo tipo) {
        this.tipo.add(tipo);
    }

    public void removerTipo(Tipo tipo) {
        this.tipo.remove(tipo);
    }

    public ArrayList<Tipo> getTipos() {
        return tipo;
    } 

    //funções
    public String toString(){
        return "id = " + id;
    }

    @Override
    public java.util.List<Pokemon> carregarTodos() {
        // lógica para buscar todos os Pokemons no json
        List<Pokemon> pokemons = new ArrayList<>();
        // preencher lista com os registros encontrados
        return pokemons;
    }

}