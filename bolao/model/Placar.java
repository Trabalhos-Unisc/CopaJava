package bolao.model;

public class Placar {
    private int golsSelecaoA;
    private int golsSelecaoB;

    public Placar(int golsSelecaoA, int golsSelecaoB) {
        this.golsSelecaoA = golsSelecaoA;
        this.golsSelecaoB = golsSelecaoB;
    }

    public Selecao getVencedor(Selecao selecaoA, Selecao selecaoB) {
        if (golsSelecaoA > golsSelecaoB) return selecaoA;
        if (golsSelecaoB > golsSelecaoA) return selecaoB;
        return null; // empate
    }

    public boolean equals(Placar outro) {
        if (outro == null) return false;
        return this.golsSelecaoA == outro.golsSelecaoA
                && this.golsSelecaoB == outro.golsSelecaoB;
    }

    public int getGolsA() { return golsSelecaoA; }
    public int getGolsB() { return golsSelecaoB; }

    @Override
    public String toString() {
        return golsSelecaoA + " x " + golsSelecaoB;
    }
}
