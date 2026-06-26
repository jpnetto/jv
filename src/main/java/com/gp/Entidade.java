package com.gp;

public abstract class Entidade implements Comparable<Entidade> {

    protected int id;

    // Construtor vazio
    public Entidade() {
    }

    // Construtor com id
    public Entidade(int id) {
        this.id = id;
    }

    // Getter e setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // Igualdade baseada no id
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Entidade other = (Entidade) obj;
        return this.id == other.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }

    
    @Override
    public int compareTo(Entidade outro) {
        return Integer.compare(this.id, outro.id);
    }

    @Override
    public String toString() {
        return "Entidade [id=" + id + "]";
    }
}