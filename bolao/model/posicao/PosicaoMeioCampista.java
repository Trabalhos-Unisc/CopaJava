package bolao.model.posicao;

public class PosicaoMeioCampista implements Posicao {
    private final double multiplicador = 1.5;

    @Override
    public double getMultiplicadorPontuacao() {
        return multiplicador;
    }

    @Override
    public String getNome() {
        return "Meio-Campista";
    }
}
