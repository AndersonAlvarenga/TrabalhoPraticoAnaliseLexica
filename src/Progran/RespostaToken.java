package Progran;

import java.util.ArrayList;

public class RespostaToken {
	private String comentario;
	private String texto;
	private ArrayList<String> token;
	private ArrayList<Integer> coluna;
	private String erro;
	private int colunaComent;

	public int getColunaComent() {
		return colunaComent;
	}

	public ArrayList<Integer> getColuna() {
		return coluna;
	}

	public void setColuna(ArrayList<Integer> coluna) {
		this.coluna = coluna;
	}

	public void setColunaComent(int colunaComent) {
		this.colunaComent = colunaComent;
	}

	public String getErro() {
		return erro;
	}

	public void setErro(String erro) {
		this.erro = erro;
	}

	public RespostaToken() {
	}

	public String getComentario() {
		return comentario;
	}

	public void setComentario(String comentario) {
		this.comentario = comentario;
	}

	public ArrayList<String> getToken() {
		return token;
	}

	public void setToken(ArrayList<String> token) {
		this.token = token;
	}
	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}
	

}
