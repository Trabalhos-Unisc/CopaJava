package bolao.model.posicao;

public class PosicaoGoleiro implements Posicao {
    private final double multiplicador = 3.0;

    @Override
    public double getMultiplicadorPontuacao() {
        return multiplicador;
    }

    @Override
    public String getNome() {
        return "Goleiro";
    }
}
