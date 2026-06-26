package com.gp.persistencia;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.gp.Entidade;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

public class EntidadeDAO<E extends Entidade> {

    private final Set<E> storage = new HashSet<>();
    private final Class<E> clazz;
    private final ObjectMapper mapper = new ObjectMapper();

    public EntidadeDAO(Class<E> clazz) {
        this.clazz = clazz;
    }

    public synchronized void salvar(E entidade) throws PersistenceException {
        if (entidade == null) throw new PersistenceException("salvar", "Entidade nula", null);
        if (storage.contains(entidade)) {
            throw new PersistenceException("salvar", "Objeto já existe no conjunto", entidade);
        }
        storage.add(entidade);
    }

    public synchronized void atualizar(E entidade) throws PersistenceException {
        if (entidade == null) throw new PersistenceException("atualizar", "Entidade nula", null);
        boolean removed = storage.remove(entidade); // relies on equals by id
        if (!removed) {
            throw new PersistenceException("atualizar", "Nenhum objeto com mesmo id encontrado", entidade);
        }
        storage.add(entidade);
    }

    public synchronized E apagar(int id) throws PersistenceException {
        Optional<E> found = storage.stream().filter(e -> e.getId() == id).findFirst();
        if (!found.isPresent()) {
            throw new PersistenceException("apagar", "Nenhum objeto com id informado", id);
        }
        E removed = found.get();
        storage.remove(removed);
        return removed;
    }

    public synchronized E carregar(int id) throws PersistenceException {
        return storage.stream()
                .filter(e -> e.getId() == id)
                .findFirst()
                .orElseThrow(() -> new PersistenceException("carregar", "Nenhum objeto com id informado", id));
    }

    public synchronized E[] carregarTodos() throws PersistenceException {
        if (storage.isEmpty()) {
            throw new PersistenceException("carregarTodos", "Conjunto vazio", null);
        }
        // ordenar por id
        List<E> ordered = storage.stream()
                .sorted(Comparator.comparingInt(Entidade::getId))
                .collect(Collectors.toList());
        @SuppressWarnings("unchecked")
        E[] array = (E[]) java.lang.reflect.Array.newInstance(clazz, ordered.size());
        return ordered.toArray(array);
    }

    public synchronized void persistir(String arquivo) throws PersistenceException {
        try {
            File file = new File(arquivo);
            List<E> list = storage.stream()
                    .sorted(Comparator.comparingInt(Entidade::getId))
                    .collect(Collectors.toList());
            mapper.writeValue(file, list);
        } catch (Exception ex) {
            throw new PersistenceException("persistir", "Erro ao gravar arquivo: " + ex.getMessage(), arquivo, ex);
        }
    }

    public synchronized void recuperar(String arquivo) throws PersistenceException {
        try {
            File file = new File(arquivo);
            if (!file.exists() || file.length() == 0) {
                storage.clear();
                return;
            }
            CollectionType type = mapper.getTypeFactory().constructCollectionType(List.class, clazz);
            List<E> list = mapper.readValue(file, type);
            storage.clear();
            storage.addAll(list);
        } catch (Exception ex) {
            throw new PersistenceException("recuperar", "Erro ao ler arquivo: " + ex.getMessage(), arquivo, ex);
        }
    }

    // utilitários
    public synchronized boolean existsById(int id) {
        return storage.stream().anyMatch(e -> e.getId() == id);
    }

    public synchronized int size() {
        return storage.size();
    }
}
