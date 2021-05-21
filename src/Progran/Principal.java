package Progran;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Principal {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		// variaveis
		boolean comentario = false;
		ArrayList tokenLinha = new ArrayList();
		String[] resposta = new String[2];

		// Tabela de simbolos
		HashMap<String, String> hash = new HashMap<String, String>();
		hash.put("KW_PROGRAM", "program");
		hash.put("KW_IF", "if");
		hash.put("KW_ELSE", "else");
		hash.put("KW_WHILE", "while");
		hash.put("KW_WRITE", "write");
		hash.put("KW_READ", "read");
		hash.put("KW_NUM", "num");
		hash.put("KW_CHAR", "char");
		hash.put("KW_NOT", "not");
		hash.put("KW_OR", "or");
		hash.put("KW_AND", "and");

		File path = new File("C:\\Users\\ander\\OneDrive\\Área de Trabalho\\teste.txt");
		try {
			BufferedReader buff = new BufferedReader(new FileReader(path));
			String linha = "";
			while (linha != null) {
				linha = buff.readLine();
				if (linha != null) {
					resposta = gerarToken(comentario, linha, hash);

				}

			}
			buff.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static String[] gerarToken(boolean comentario, String linha, HashMap<String, String> tbSimbolo) {
		String erro = "";
		String[] arraylinha = linha.split("");
		ArrayList<Simbolo> simbolos = new ArrayList<Simbolo>();
		String palavra = "";
		String token = "";
		for (int x = 0; x < arraylinha.length; x++) {
			simbolos.add(new Simbolo(arraylinha[x]));
		}
		for (int x = 0; x < simbolos.size(); x++) {
			// System.out.println(simbolos.get(x).getTipo());
			// System.out.println(simbolos.get(x).getSimbolo());
			int cont = 0;
			switch (simbolos.get(x).getTipo()) {
			case "letra":
				palavra += simbolos.get(x).getSimbolo();

				for (int y = x + 1; y < simbolos.size(); y++) {
					// System.out.println(simbolos.get(y).getTipo());
					// System.out.println(simbolos.get(y).getSimbolo());

					if (simbolos.get(y).getTipo() != "outro") {

						palavra += simbolos.get(y).getSimbolo();
						cont += 1;
					} else {

						if (tbSimbolo.containsKey("KW_" + palavra.toUpperCase())) {
							token += "<" + "KW_" + palavra.toUpperCase() + ","
									+ tbSimbolo.get("KW_" + palavra.toUpperCase()) + ">";
						} else {
							if (tbSimbolo.containsKey("ID_" + palavra.toUpperCase())) {
								token += "<" + "ID_" + palavra.toUpperCase() + ","
										+ tbSimbolo.get("ID_" + palavra.toUpperCase()) + ">";
							} else {
								tbSimbolo.put("ID_" + palavra.toUpperCase(), palavra);
								token += "<" + "ID_" + palavra.toUpperCase() + ","
										+ tbSimbolo.get("ID_" + palavra.toUpperCase()) + ">";
							}
						}
						palavra = "";
						break;
					}

				}
				x += cont;

				break;
			case "num":
				String number = "";
				number += simbolos.get(x).getSimbolo();
				if (0 == simbolos.size() - 1) {
					tbSimbolo.put("NUM_CONST_" + number, number);
					token += "<" + "NUM_CONST_" + number + "," + tbSimbolo.get("NUM_CONST_" + number) + ">";
					break;
				}
				for (int y = x + 1; y < simbolos.size(); y++) {
					if (simbolos.get(y).getTipo() == "num") {
						number += simbolos.get(y).getSimbolo();
					} else {
						if (simbolos.get(y).getTipo() == "ponto") {
							number += simbolos.get(y).getSimbolo();
							int cont2 = 0;
							for (int z = y + 1; z < simbolos.size(); z++) {
								if (simbolos.get(z).getTipo() == "num") {
									number += simbolos.get(z).getSimbolo();
									tbSimbolo.put("NUM_CONST_" + number, number);
									token += "<" + "NUM_CONST_" + number + "," + tbSimbolo.get("NUM_CONST_" + number) + ">";
									
								} else {
									erro += "Caracter esperado não encontrado na coluna " + z + " ";
									break;
								}
								cont2 += 1;
								break;
							}
							y += cont2;
						} else {
							tbSimbolo.put("NUM_CONST_" + number, number);
							token += "<" + "NUM_CONST_" + number + "," + tbSimbolo.get("NUM_CONST_" + number) + ">";
							break;
						}
						break;

					}
					cont+=1;
				}
				x+=cont;

				break;
			case "outro":

				break;

			}

		}
		if (palavra != "") {

			if (tbSimbolo.containsKey("KW_" + palavra.toUpperCase())) {
				token += "<" + "KW_" + palavra.toUpperCase() + "," + tbSimbolo.get("KW_" + palavra.toUpperCase()) + ">";
			} else {
				if (tbSimbolo.containsKey("ID_" + palavra.toUpperCase())) {
					token += "<" + "ID_" + palavra.toUpperCase() + "," + tbSimbolo.get("ID_" + palavra.toUpperCase())
							+ ">";
				} else {
					tbSimbolo.put("ID_" + palavra.toUpperCase(), palavra);
					token += "<" + "ID_" + palavra.toUpperCase() + "," + tbSimbolo.get("ID_" + palavra.toUpperCase())
							+ ">";
				}
			}
			palavra = "";
		}
		System.out.println(token);
		return null;
	}

}
