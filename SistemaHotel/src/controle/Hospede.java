package controle;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Hospede extends Thread {
private final Recepcionista recepcionista;
private final int tamanhoGrupo;
private boolean conseguiuQuarto;
public Hospede(Recepcionista recepcionista, String nome, int tamanhoGrupo) {
super(nome);
this.recepcionista = recepcionista;
this.tamanhoGrupo = tamanhoGrupo;
this.conseguiuQuarto = false;}
@Override
public void run() {
int tentativas = 0;
Random random = new Random();
while (!conseguiuQuarto && tentativas < 2) {
Quarto quarto = recepcionista.reservarQuarto(tamanhoGrupo);
tentativas++;
if (quarto != null) {
System.out.println(getName() + " reservou o quarto " + quarto.getNumero());
quarto.entrar(tamanhoGrupo);
try {
TimeUnit.SECONDS.sleep(random.nextInt(5) + 2); 
 } catch (InterruptedException e) {
 Thread.currentThread().interrupt(); }
quarto.sair(tamanhoGrupo); 
System.out.println(getName() + " saiu para passear.");
try {
TimeUnit.SECONDS.sleep(random.nextInt(10) + 5); 
} catch (InterruptedException e) {
    Thread.currentThread().interrupt();}
System.out.println(getName() + " voltou para o hotel.");
quarto.esperarLiberacao(); 
quarto.devolverChave(); 
System.out.println(getName() + " deixou a chave do quarto " + quarto.getNumero() + " na recepção.");
recepcionista.notificarQuartoDisponivel(); 
conseguiuQuarto = true;
} else {
System.out.println(getName() + " não conseguiu um quarto. Tentará novamente mais tarde.");
try {
TimeUnit.SECONDS.sleep(random.nextInt(3) + 1); 
} catch (InterruptedException e) {
Thread.currentThread().interrupt();}}}
if (!conseguiuQuarto) {
System.out.println(getName() + " não conseguiu um quarto após duas tentativas e fez uma reclamação.");}}
public int getTamanhoGrupo() {
return tamanhoGrupo;}
public void setConseguiuQuarto(boolean conseguiuQuarto) {
this.conseguiuQuarto = conseguiuQuarto; }}


