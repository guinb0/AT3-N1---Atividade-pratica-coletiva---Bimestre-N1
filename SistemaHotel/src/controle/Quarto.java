package controle;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Quarto {
private final int numero;
private final int capacidade;
private int ocupacao;
private final ReentrantLock lock;
private final Condition quartoLivre;
private boolean estaLimpo;
private boolean chaveNaRecepcao;
public Quarto(int numero) {
this.numero = numero;
this.capacidade = 4;
this.ocupacao = 0;
this.lock = new ReentrantLock();
this.quartoLivre = lock.newCondition();
this.estaLimpo = true;
this.chaveNaRecepcao = true;}
public int getNumero() {
return numero;}
public boolean temEspaco(int tamanhoGrupo) {
return ocupacao + tamanhoGrupo <= capacidade;}
public boolean estaVazio() {
return ocupacao == 0;}
public boolean chaveNaRecepcao() {
return chaveNaRecepcao;}
public void pegarChave() {
lock.lock();
try {
while (!chaveNaRecepcao) {
quartoLivre.await();}
chaveNaRecepcao = false;} 
catch (InterruptedException e) {
Thread.currentThread().interrupt();} 
finally {
lock.unlock();}}
public void devolverChave() {
lock.lock();
try {
chaveNaRecepcao = true;
quartoLivre.signalAll(); } 
finally {
lock.unlock();}}
public void entrar(int tamanhoGrupo) {
ocupacao += tamanhoGrupo;}
public void sair(int tamanhoGrupo) {
ocupacao -= tamanhoGrupo;
if (estaVazio()) {
devolverChave();}}
public void limpar() {
lock.lock();
try {
estaLimpo = true;}
finally {
lock.unlock();}}
public boolean estaLimpo() {
return estaLimpo;}
public void sujar() {
estaLimpo = false;}
public void solicitarLimpeza() {
Recepcionista recepcionista = new Recepcionista(null);
recepcionista.chamarCamareira(); }
public void esperarLiberacao() {
lock.lock();
try {
while (!estaVazio()) {
quartoLivre.await(); }} 
catch (InterruptedException e) {
Thread.currentThread().interrupt();}
finally {
lock.unlock();}}}
