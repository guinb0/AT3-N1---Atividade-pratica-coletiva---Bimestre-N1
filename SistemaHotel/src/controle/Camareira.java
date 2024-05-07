package controle;

import java.util.List;

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
                for (Quarto quarto : quartos) {
                    if (quarto.estaVazio() && quarto.chaveNaRecepcao() && !quarto.estaLimpo()) {
                        quarto.pegarChave();
                        System.out.println(getName() + " está limpando o quarto " + quarto.getNumero());
                        quarto.limpar(); // Simula a limpeza
                        quarto.devolverChave(); // Devolve a chave
                        System.out.println(getName() + " terminou de limpar o quarto " + quarto.getNumero());
                    }
                }
                Thread.sleep(2000); // Tempo entre limpezas
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
