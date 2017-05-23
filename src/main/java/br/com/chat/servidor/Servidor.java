package br.com.chat.servidor;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Servidor {
	
	private int porta;
	private List<PrintStream> clientes;
	
	public static void main(String[] args) {
		new Servidor(12345).executa();
	}
	
	public Servidor(int porta){
		this.porta = porta;
		this.clientes = new ArrayList<PrintStream>();
	}
	
	public void executa(){
		try {
			ServerSocket servidor = new ServerSocket(this.porta);
			while (true) {
				Socket cliente = aceitarCliente(servidor);
				adicionarSaidaDoClienteParaListaDeStreams(cliente);
				criarNovaThreadParaCliente(cliente);
			}
			
		} catch (IOException e) {
			System.out.println("Não foi possível conectar a porta: " + e);
		}
	}

	private void criarNovaThreadParaCliente(Socket cliente) throws IOException {
		TrataCliente tc = new TrataCliente(cliente.getInputStream(), this);
		new Thread(tc).start();
	}

	private void adicionarSaidaDoClienteParaListaDeStreams(Socket cliente) throws IOException {
		PrintStream saidaDoCliente = new PrintStream(cliente.getOutputStream());
		this.clientes.add(saidaDoCliente);
	}

	private Socket aceitarCliente(ServerSocket servidor) throws IOException {
		Socket cliente = servidor.accept();
		System.out.println("Nova conexão com o cliente " + cliente.getInetAddress().getHostAddress());
		return cliente;
	}
	
	public void distribuiMensagem(String msg) {
		for (PrintStream printStream : clientes) {
			printStream.println(msg);
		}
	}
}
