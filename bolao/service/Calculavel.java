package bolao.service;

import bolao.model.Jogo;
import bolao.model.Palpite;
import bolao.model.PontuacaoJogo;

public interface Calculavel {
    PontuacaoJogo calcular(Palpite palpite, Jogo jogo);
}
