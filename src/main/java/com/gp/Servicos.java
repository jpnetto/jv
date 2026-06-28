package com.gp;

import java.util.ArrayList;
import java.util.List;

public class Servicos {

    public static Pokemon compararPokemons(Pokemon p1, Pokemon p2) {

        List<Tipo> tipo1 = p1.getTipos();
        List<Tipo> tipo2 = p2.getTipos();

        int cont1 = 0;
        int cont2 = 0;

        for (Tipo t1 : tipo1) {
            for (Tipo t2 : tipo2) {
                if (t1.getFraquezas().contains(t2)) {
                    cont1++; // p1 tem vantagem sobre p2
                }
            }
        }

        // Verifica se algum tipo de p2 é fraco contra algum tipo de p1
        for (Tipo t2 : tipo2) {
            for (Tipo t1 : tipo1) {
                if (t2.getFraquezas().contains(t1)) {
                    cont1++;
                }
            }
        }
        if (cont1 == cont2) {
            return (p1.getStats() > p2.getStats()) ? p1 : p2;

        } else {
            return (cont1 > cont2) ? p1 : p2;
        }
    }

    /**
     * Realiza uma batalha entre dois treinadores e devolve a entidade Batalha
     * já preenchida: o vencedor de cada confronto entre Pokémons é registrado
     * como um ItemBatalha (a classe intermediária da composição), e o
     * Treinador vencedor da batalha como um todo é definido ao final.
     *
     * Também atualiza o número de insígnias do treinador vencedor (efeito colateral
     * sobre o próprio objeto Treinador, que ainda precisa ser persistido por quem
     * chamar este método).
     */
    public static Batalha realizarBatalha(int id, Treinador t1, Treinador t2) {
        List<ItemBatalha> confrontos = new ArrayList<>();
        int cont1 = 0;
        int cont2 = 0;

        List<Pokemon> pokemonsT1 = t1.getPokemons();
        List<Pokemon> pokemonsT2 = t2.getPokemons();

        // Compara cada Pokémon de t1 com cada Pokémon de t2
        for (Pokemon p1 : pokemonsT1) {
            for (Pokemon p2 : pokemonsT2) {
                Pokemon vencedorConfronto = compararPokemons(p1, p2);
                confrontos.add(new ItemBatalha(p1, p2, vencedorConfronto));

                if (vencedorConfronto == p1) {
                    System.out.println(p1.getNome() + " do treinador " + t1.getNome() + " venceu contra " + p2.getNome() + " do treinador " + t2.getNome());
                    cont1++;
                } else if (vencedorConfronto == p2) {
                    System.out.println(p2.getNome() + " do treinador " + t2.getNome() + " venceu contra " + p1.getNome() + " do treinador " + t1.getNome());
                    cont2++;
                }
            }
        }

        Treinador vencedorTreinador;
        // em caso de empate, o treinador com mais insígnias vence, caso contrário, vence o treinador com mais vitórias nos combates entre os pokémons
        if (cont1 == cont2) {
            if (t1.getInsignias() > t2.getInsignias()) {
                t1.setInsignias(t1.getInsignias() + 1);
                System.out.println("Foi um empate mas o treinador " + t1.getNome() + " ganhou a batalha, por seu número de insígnias!");
            } else if (t1.getInsignias() == t2.getInsignias()) {
                System.out.println("Foi um empate total entre os treinadores " + t1.getNome() + " e " + t2.getNome() + "!");
            } else {
                t2.setInsignias(t2.getInsignias() + 1);
                System.out.println("Foi um empate, mas o treinador " + t2.getNome() + " ganhou a batalha, por seu número de insígnias!");
            }
            vencedorTreinador = (t1.getInsignias() > t2.getInsignias()) ? t1 : t2;

        } else {
            if (cont1 > cont2) {
                t1.setInsignias(t1.getInsignias() + 1);
                System.out.println("Treinador " + t1.getNome() + " ganhou a batalha!");
            } else {
                t2.setInsignias(t2.getInsignias() + 1);
                System.out.println("Treinador " + t2.getNome() + " ganhou a batalha!");
            }
            vencedorTreinador = (cont1 > cont2) ? t1 : t2;
        }

        return new Batalha(id, t1, t2, vencedorTreinador, confrontos);
    }

}