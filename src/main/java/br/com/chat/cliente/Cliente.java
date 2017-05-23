package br.com.chat.cliente;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Cliente {
	
	private String host;
	private int porta;
	
	public static void main(String[] args) throws IOException {
		new Cliente("127.0.0.1", 12345).executa();
	}

	public Cliente(String host, int porta) {
		super();
		this.host = host;
		this.porta = porta;
	}
	
	public void executa() throws IOException{
		Socket cliente = null;
		Recebedor r = null;
		cliente = criarCliente(cliente);
		
		if(cliente != null) {
			r = new Recebedor(cliente.getInputStream());
			new Thread(r).start();
		}else {
			return;
		}
		
		Scanner teclado = new Scanner(System.in);
		PrintStream saida = new PrintStream(cliente.getOutputStream());
		while (teclado.hasNextLine()) {
			saida.println(teclado.nextLine());
			
		}
		
		saida.close();
		teclado.close();
		cliente.close();
	}

	private Socket criarCliente(Socket cliente) {
		try {
			cliente = new Socket(this.host, this.porta);
			System.out.println("Cliente conectado ao servidor");
		} catch (UnknownHostException e) {
			System.out.println("Não foi possível conexão ao servidor: " + e);
		} catch (IOException e) {
			System.out.println("Não foi possível conexão ao servidor: " + e);
		}
		return cliente;
	}
}
