package visao;

import controle.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class HotelSimulation {
    public static void main(String[] args) {
        List<Quarto> quartos = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            quartos.add(new Quarto(i));
        }

        List<Recepcionista> recepcionistas = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            Recepcionista recepcionista = new Recepcionista(quartos);
            recepcionistas.add(recepcionista);
            recepcionista.start();
        }

        List<Camareira> camareiras = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            Camareira camareira = new Camareira(quartos, "Camareira " + i);
            camareiras.add(camareira);
            camareira.start();
        }

        List<Hospede> hospedes = new ArrayList<>();
        Random random = new Random();
        for (int i = 1; i <= 50; i++) {
            int tamanhoGrupo = random.nextInt(6) + 1;
            Hospede hospede = new Hospede(recepcionistas.get(random.nextInt(recepcionistas.size())), "Hospede " + i, tamanhoGrupo);
            hospedes.add(hospede);
            hospede.start();
        }

        try {
            Thread.sleep(30000); // Aguarda a simulação por 30 segundos
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("Fim da simulação.");
    }
}

