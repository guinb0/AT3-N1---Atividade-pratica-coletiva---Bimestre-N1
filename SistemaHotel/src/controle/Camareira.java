package controle;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class Camareira extends Thread {
    private final List<Quarto> quartos;

    public Camareira(List<Quarto> quartos, String nome) {
        super(nome);
        this.quartos = quartos;
    }

    @Override
    public void run() {
        try {
            while (true) {
                boolean algumQuartoParaLimpar = false;

                // Verifica se há quartos para limpar
                for (Quarto quarto : quartos) {
                    if (quarto.estaVazio() && quarto.chaveNaRecepcao() && !quarto.estaLimpo()) {
                        algumQuartoParaLimpar = true;
                        quarto.pegarChave();
                        System.out.println(getName() + " está limpando o quarto " + quarto.getNumero());
                        quarto.limpar(); // Simula a limpeza
                        quarto.devolverChave(); // Devolve a chave
                        System.out.println(getName() + " terminou de limpar o quarto " + quarto.getNumero());
                    }
                }

                if (!algumQuartoParaLimpar) {
                    synchronized (this) {
                        wait(); // Aguarda até que um novo quarto precise de limpeza
                    }
                }

                TimeUnit.SECONDS.sleep(2); // Tempo entre limpezas
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    // Método para acordar a camareira quando um novo quarto precisa ser limpo
    public synchronized void acordar() {
        notify(); // Notifica a camareira para acordar e verificar se há quartos para limpar
    }
}
