import java.util.ArrayList;
import java.util.List;

public class Treinador extends Entidade {

    private String nome;
    private String regiao;
    private List<Pokemon> pokemons;
    private int insignias;
    
    public Treinador(int id, String nome, String regiao, int insignias) {
        super(id);
        this.nome = nome;
        this.regiao = regiao;
        this.pokemons = new ArrayList<>();
        this.insignias = insignias;
    }

    public void addPokemon(Pokemon pokemon) {
        pokemons.add(pokemon);
    }

    public void removerPokemon(Pokemon pokemon) {
        pokemons.remove(pokemon);
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getRegiao() {
        return regiao;
    }

    public void setRegiao(String regiao) {
        this.regiao = regiao;
    }

    public List<Pokemon> getPokemons() {
        return pokemons;
    }

    public int getInsignias() {
        return insignias;
    }

    public void setInsignias(int insignias) {
        this.insignias = insignias;
    }

    @Override
    public java.util.List<Treinador> carregarTodos() {
        // lógica para buscar todos os Treinadores no json
        List<Treinador> treinadores = new ArrayList<>();
        // preencher lista com os registros encontrados
        return treinadores;
    }

    

}
