package jv;

public class Entidade {


    //atributos da classe
    private int id;
    private boolean persistido;

    //getters e setters
    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }   

    public boolean getPersistido(){
        return persistido;
    }

    public void setPersistido(boolean persistido){
        this.persistido = persistido;
    }

    //funções
    public String toString(){
        return "id = " + id;
    }

    public void salvar(int id){

    }
}
