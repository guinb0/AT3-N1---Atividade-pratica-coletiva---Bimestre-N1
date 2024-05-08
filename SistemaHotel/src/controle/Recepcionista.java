package controle;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Recepcionista extends Thread {
    private final List<Quarto> quartos;
    private final ReentrantLock lock;
    private final Condition temQuartoDisponivel;
    private final Queue<Hospede> filaEspera;

    public Recepcionista(List<Quarto> quartos) {
        this.quartos = quartos;
        this.lock = new ReentrantLock();
        this.temQuartoDisponivel = lock.newCondition();
        this.filaEspera = new LinkedList<>();
    }

    public Quarto reservarQuarto(int tamanhoGrupo) {
        lock.lock();
        try {
            while (!existeQuartoDisponivel(tamanhoGrupo)) {
                temQuartoDisponivel.await();
            }

            for (Quarto quarto : quartos) {
                if (quarto.temEspaco(tamanhoGrupo) && quarto.chaveNaRecepcao()) {
                    quarto.pegarChave();
                    return quarto;
                }
            }

            return null;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return null;
        } finally {
            lock.unlock();
        }
    }

    private boolean existeQuartoDisponivel(int tamanhoGrupo) {
        return quartos.stream().anyMatch(quarto -> quarto.temEspaco(tamanhoGrupo) && quarto.chaveNaRecepcao());
    }

    public void notificarQuartoDisponivel() {
        lock.lock();
        try {
            temQuartoDisponivel.signalAll();
        } finally {
            lock.unlock();
        }
    }

    public void entrarNaFilaEspera(Hospede hospede) {
        filaEspera.add(hospede);
    }

    public void tentarAlocarQuarto() {
        while (!filaEspera.isEmpty()) {
            Hospede hospede = filaEspera.poll();
            Quarto quarto = reservarQuarto(hospede.getTamanhoGrupo());
            if (quarto != null) {
                System.out.println(hospede.getName() + " finalmente conseguiu um quarto após esperar na fila.");
                hospede.setConseguiuQuarto(true);
            } else {
                System.out.println(hospede.getName() + " ainda não conseguiu um quarto e continuará esperando.");
            }
        }
    }

    public void cadastrarHospede(Hospede hospede) {
        System.out.println("Recepcionista cadastrou " + hospede.getName() + " como hóspede.");
    }

    @Override
    public void run() {
        try {
            while (true) {
                tentarAlocarQuarto();
                Thread.sleep(1000); // Simular trabalho da recepção
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}