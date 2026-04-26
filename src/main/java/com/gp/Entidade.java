package com.gp;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

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
    public boolean salvar(String arquivo, Class<? extends Entidade> clazz) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            File file = new File(arquivo);

            List<Entidade> entidades = new ArrayList<>();

            // Se já existe arquivo, carrega lista existente
            if (file.exists() && file.length() > 0) {
                JsonNode root = mapper.readTree(file);

                if (root.isArray()) {
                    entidades = mapper.readValue(
                        file,
                        mapper.getTypeFactory().constructCollectionType(List.class, clazz)
                    );
                } else if (root.isObject()) {
                    // Se o arquivo contém apenas um objeto, transforma em lista
                    Entidade entidade = mapper.treeToValue(root, clazz);
                    entidades.add(entidade);
                }
            }

            // Adiciona o objeto atual
            entidades.add(this);

            // Grava lista completa (sempre como array)
            mapper.writeValue(file, entidades);

            persistido = true;
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }





    // Atualizar objeto no JSON (sobrescreve o arquivo com os dados atuais)
    public boolean atualizar(String arquivo, Class<? extends Entidade> clazz) {
        if (persistido) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                File file = new File(arquivo);

                List<Entidade> entidades = new ArrayList<>();
                if (file.exists()) {
                    entidades = mapper.readValue(
                        file,
                        mapper.getTypeFactory().constructCollectionType(List.class, clazz)
                    );
                }

                // Remove o objeto antigo com o mesmo id
                entidades.removeIf(e -> e.getId() == this.getId());

                // Adiciona o objeto atualizado
                entidades.add(this);

                // Grava lista completa
                mapper.writeValue(file, entidades);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }


    // Apagar arquivo JSON
    public boolean apagar(String arquivo, Class<? extends Entidade> clazz) {
        File file = new File(arquivo);
        if (file.exists()) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                List<Entidade> entidades = mapper.readValue(
                    file,
                    mapper.getTypeFactory().constructCollectionType(List.class, clazz)
                );

                // Remove o objeto com o mesmo id
                entidades.removeIf(e -> e.getId() == this.getId());

                // Grava lista atualizada
                mapper.writeValue(file, entidades);

                persistido = false;
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }


    // Carregar objeto do JSON
    public static <T> T carregar(String arquivo, Class<T> clazz) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(new File(arquivo), clazz);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Método abstrato
    public abstract java.util.List<? extends Entidade> carregarTodos();

    public String toString() {
        return "Entidade [id=" + id + ", persistido=" + persistido + "]";
    }
}
