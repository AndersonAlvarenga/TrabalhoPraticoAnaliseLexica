package Progran;

import java.util.ArrayList;

public class Resultado {
	private ArrayList<String> token;
	private String erro;
	private int linha;

	public Resultado(ArrayList<String> token, String erro, int linha) {
		this.token = token;
		this.erro = erro;
		this.linha = linha;
	}

	public ArrayList<String> getToken() {
		return token;
	}

	public void setToken(ArrayList<String> token) {
		this.token = token;
	}

	public String getErro() {
		return erro;
	}

	public void setErro(String erro) {
		this.erro = erro;
	}

	public int getLinha() {
		return linha;
	}

	public void setLinha(int linha) {
		this.linha = linha;
	}

	

}
