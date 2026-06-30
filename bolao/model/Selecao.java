package bolao.model;

import java.util.ArrayList;
import java.util.List;

public class Selecao {
    private static int contadorId = 1;

    private int id;
    private String nome;
    private String pais;
    private String grupoCopa;
    private List<Jogador> jogadores;

    public Selecao(String nome, String pais, String grupoCopa) {
        this.id = contadorId++;
        this.nome = nome;
        this.pais = pais;
        this.grupoCopa = grupoCopa;
        this.jogadores = new ArrayList<>();
    }

    public void addJogador(Jogador jogador) {
        jogadores.add(jogador);
    }

    public List<Jogador> getJogadores() { return new ArrayList<>(jogadores); }
    public int getId() { return id; }
    public String getNome() { return nome; }
    public String getPais() { return pais; }
    public String getGrupoCopa() { return grupoCopa; }

    @Override
    public String toString() {
        return nome + " (" + pais + ") - Grupo " + grupoCopa;
    }
}
