package controle;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class Camareira extends Thread {
private final List<Quarto> quartos;

public Camareira(List<Quarto> quartos, String nome) {
super(nome);
this.quartos = quartos;}
@Override
public void run() {
try {
while (true) {
 boolean algumQuartoParaLimpar = false;
for (Quarto quarto : quartos) {
if (quarto.estaVazio() && quarto.chaveNaRecepcao() && !quarto.estaLimpo()) {
algumQuartoParaLimpar = true;
quarto.pegarChave();
System.out.println(getName() + " está limpando o quarto " + quarto.getNumero());
quarto.limpar();         quarto.devolverChave();        System.out.println(getName() + " terminou de limpar o quarto " + quarto.getNumero());}}
if (!algumQuartoParaLimpar) {
synchronized (this) {
wait();}}
TimeUnit.SECONDS.sleep(2); }
} catch (InterruptedException e) {
Thread.currentThread().interrupt();}}
public synchronized void acordar() {
notify();  }}
