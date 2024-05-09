package controle;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Recepcionista extends Thread {
    private final List<Quarto> quartos;
    private final ReentrantLock lock;
    private final Condition temQuartoDisponivel;
    private final Queue<Hospede> filaEspera;
    private final List<Camareira> camareiras;

    public Recepcionista(List<Quarto> quartos) {
        this.quartos = quartos != null ? quartos : new ArrayList<>(); // Inicializa a lista de quartos
        this.lock = new ReentrantLock();
        this.temQuartoDisponivel = lock.newCondition();
        this.filaEspera = new ConcurrentLinkedQueue<>(); // Utiliza ConcurrentLinkedQueue para thread safety
        this.camareiras = new ArrayList<>();
    }

    public void adicionarCamareira(Camareira camareira) {
        camareiras.add(camareira);
    }

    public Quarto reservarQuarto(int tamanhoGrupo) {
        lock.lock();
        try {
            if (quartos.isEmpty() || !existeQuartoDisponivel(tamanhoGrupo)) {
                return null;
            }

            for (Quarto quarto : quartos) {
                if (quarto.temEspaco(tamanhoGrupo) && quarto.chaveNaRecepcao()) {
                    quarto.pegarChave();
                    return quarto;
                }
            }

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

    // Método para chamar camareiras quando necessário
    public void chamarCamareira() {
        for (Camareira camareira : camareiras) {
            synchronized (camareira) {
                camareira.notify(); // Notifica a camareira para verificar se há quartos para limpar
            }
        }
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
