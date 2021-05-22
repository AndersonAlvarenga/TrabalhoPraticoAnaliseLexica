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
		boolean texto = false;
		ArrayList<Resultado> resposta = new ArrayList<Resultado>();
		RespostaToken res = new RespostaToken();
		int linhaComent = 0;
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
		hash.put("KW_EOF", "eof");

		File path = new File("C:\\Users\\ander\\OneDrive\\Área de Trabalho\\teste.txt");
		try {
			BufferedReader buff = new BufferedReader(new FileReader(path));
			String linha = "";
			int linhaC = 0;
			int contador = 0;
			while (linha != null) {
				contador += 1;
				linha = buff.readLine();
				if (linha != null) {
					res = gerarToken(comentario, texto, linha, hash, linhaComent);
					resposta.add(new Resultado(res.getToken(), res.getErro(), resposta.size() + 1));
					if (res.getComentario() == "true") {
						comentario = true;
					} else {
						comentario = false;
					}
					if (res.getTexto() == "true") {
						texto = true;
					} else {
						texto = false;
					}
					linhaComent = res.getColunaComent();
					if (linhaComent != 0) {
						if(linhaC==0) {
							linhaC = contador;
						}
						
					} else {
						linhaC = 0;
					}
				}

			}
			// gerar token EOF
			res = gerarToken(comentario, texto, "eof", hash, linhaComent);
			resposta.add(new Resultado(res.getToken(), res.getErro(), resposta.size() + 1));
			imprimirToken(resposta);
			if(linhaC!=0) {
				System.out.println("Linha: "+linhaC);
				System.out.println("Erros: Comentario não fechado, comentário iniciado na coluna: "+linhaComent);
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


	private static RespostaToken gerarToken(boolean comentario, boolean texto, String linha,
			HashMap<String, String> tbSimbolo, int coment) {
		String erro = "";
		String[] arraylinha = linha.split("");
		ArrayList<Simbolo> simbolos = new ArrayList<Simbolo>();
		String palavra = "";
		String token = "";
		String literal = "";

		ArrayList<String> retornoToken = new ArrayList<String>();
		RespostaToken resposta = new RespostaToken();
		for (int x = 0; x < arraylinha.length; x++) {
			simbolos.add(new Simbolo(arraylinha[x]));
		}
		for (int x = 0; x < simbolos.size(); x++) {
			int cont = 0;
			if (texto != true) {
				switch (simbolos.get(x).getTipo()) {
				case "letra":
					// Forma as palavras da linguagem
					if (comentario == false) {
						palavra += simbolos.get(x).getSimbolo();

						for (int y = x + 1; y < simbolos.size(); y++) {
							if (simbolos.get(y).getTipo() != "outro") {

								palavra += simbolos.get(y).getSimbolo();
								cont += 1;
							} else {

								if (tbSimbolo.containsKey("KW_" + palavra.toUpperCase())) {
									token += "<" + "KW" + "," + tbSimbolo.get("KW_" + palavra.toUpperCase()) + ">";
									retornoToken.add(token);
									token = "";
								} else {
									if (tbSimbolo.containsKey("ID_" + palavra.toUpperCase())) {
										token += "<" + "ID" + "," + tbSimbolo.get("ID_" + palavra.toUpperCase()) + ">";
										retornoToken.add(token);
										token = "";
									} else {
										tbSimbolo.put("ID_" + palavra.toUpperCase(), palavra);
										token += "<" + "ID" + "," + tbSimbolo.get("ID_" + palavra.toUpperCase()) + ">";
										retornoToken.add(token);
										token = "";
									}
								}
								palavra = "";
								break;
							}

						}
						x += cont;
					}
					break;
				case "num":
					// forma os numerais das palavras
					if (comentario == false) {
						String number = "";
						number += simbolos.get(x).getSimbolo();
						if (0 == simbolos.size() - 1) {
							tbSimbolo.put("NUM_CONST_" + number, number);
							token += "<" + "NUM_CONST" + "," + tbSimbolo.get("NUM_CONST_" + number) + ">";
							retornoToken.add(token);
							token = "";
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
											token += "<" + "NUM_CONST" + "," + tbSimbolo.get("NUM_CONST_" + number)
													+ ">";
											retornoToken.add(token);
											token = "";

										} else {
											erro += "Caracter esperado não encontrado na coluna " + (z + 1) + " ";
											break;
										}
										cont2 += 1;
										break;
									}
									y += cont2;
								} else {
									tbSimbolo.put("NUM_CONST_" + number, number);
									token += "<" + "NUM_CONST" + "," + tbSimbolo.get("NUM_CONST_" + number) + ">";
									retornoToken.add(token);
									token = "";
									break;
								}
								break;

							}
							cont += 1;
						}
						x += cont;
					}
					break;

				// cometarios, testos entre aspas, simbolos e operadores
				case "outro":
					if (comentario == false) {
						// caso não seja comentario

						switch (simbolos.get(x).getSimbolo()) {
						case "/":
							// comentario e divisão
							if (x < simbolos.size() - 1) {
								switch (simbolos.get(x + 1).getSimbolo()) {
								case "*":
									comentario = true;
									coment = x + 1;
									x += 1;
									break;
								case "/":
									x = simbolos.size();
									break;
								default:
									if (tbSimbolo.get("OP_DIV") != "") {
										tbSimbolo.put("OP_DIV", simbolos.get(x).getSimbolo());
									}
									token += "<" + "OP_DIV" + "," + tbSimbolo.get("OP_DIV") + ">";
									retornoToken.add(token);
									token = "";
									break;
								}
							}

							break;

						// Operadores

						case "=":
							if (x == simbolos.size() - 1) {
								if (tbSimbolo.get("OP_ATRIB") != "=") {
									tbSimbolo.put("OP_ATRIB", simbolos.get(x).getSimbolo());
								}
								token += "<" + "OP_ATRIB" + "," + tbSimbolo.get("OP_ATRIB") + ">";
								retornoToken.add(token);
								token = "";
							} else {
								for (int y = x + 1; y < simbolos.size(); y++) {
									if (" ".equals(simbolos.get(y).getSimbolo())) {
										cont += 1;
									} else {
										if ("=".equals(simbolos.get(y).getSimbolo())) {
											if (tbSimbolo.get("OP_EQ") != "==") {
												String item = "=" + simbolos.get(y).getSimbolo();
												tbSimbolo.put("OP_EQ", item);

											}
											token += "<" + "OP_EQ" + "," + tbSimbolo.get("OP_EQ") + ">";
											retornoToken.add(token);
											token = "";
											cont += 1;
											break;
										} else {
											if (tbSimbolo.get("OP_ATRIB") != "=") {
												tbSimbolo.put("OP_ATRIB", simbolos.get(x).getSimbolo());
											}
											token += "<" + "OP_ATRIB" + "," + tbSimbolo.get("OP_ATRIB") + ">";
											retornoToken.add(token);
											token = "";
											break;
										}

									}

								}
								x += cont;
							}
							break;
						case "!":
							if (x == simbolos.size() - 1) {
								erro = "Caracter esperado não encontrado na coluna " + (x + 1);
								cont += 1;
							} else {
								for (int y = x + 1; y < simbolos.size(); y++) {
									if (" ".equals(simbolos.get(y).getSimbolo())) {
										cont += 1;
									} else {
										if ("=".equals(simbolos.get(y).getSimbolo())) {
											if (tbSimbolo.get("OP_NE") != "!=") {
												tbSimbolo.put("OP_NE",
														simbolos.get(x).getSimbolo() + simbolos.get(y).getSimbolo());
											}
											token += "<" + "OP_NE" + "," + tbSimbolo.get("OP_NE") + ">";
											retornoToken.add(token);
											token = "";
											cont += 1;
											break;
										} else {
											erro = "Caracter esperado não encontrado na coluna " + (x + 1);
										}

									}
								}
								x += cont;
							}

							break;
						case ">":

							if (x == simbolos.size() - 1) {
								if (tbSimbolo.get("OP_GT") != ">") {
									tbSimbolo.put("OP_GT", simbolos.get(x).getSimbolo());
								}
								token += "<" + "OP_GT" + "," + tbSimbolo.get("OP_GT") + ">";
								retornoToken.add(token);
								token = "";
							} else {
								for (int y = x + 1; y < simbolos.size(); y++) {
									if (" ".equals(simbolos.get(y).getSimbolo())) {
										cont += 1;

									} else {

										if ("=".equals(simbolos.get(y).getSimbolo())) {
											if (tbSimbolo.get("OP_GE") != ">=") {
												tbSimbolo.put("OP_GE",
														simbolos.get(x).getSimbolo() + simbolos.get(y).getSimbolo());
											}
											token += "<" + "OP_GE" + "," + tbSimbolo.get("OP_GE") + ">";
											retornoToken.add(token);
											token = "";
											cont += 1;
											break;
										}

									}
								}

								x += cont;
							}
							break;
						case "<":

							if (x == simbolos.size() - 1) {
								if (tbSimbolo.get("OP_LT") != "<") {
									tbSimbolo.put("OP_LT", simbolos.get(x).getSimbolo());
								}
								token += "<" + "OP_LT" + "," + tbSimbolo.get("OP_LT") + ">";
								retornoToken.add(token);
								token = "";
							} else {

								for (int y = x + 1; y < simbolos.size(); y++) {
									if (" ".equals(simbolos.get(y).getSimbolo())) {
										cont += 1;

									} else {

										if ("=".equals(simbolos.get(y).getSimbolo())) {
											if (tbSimbolo.get("OP_LE") != "<=") {
												tbSimbolo.put("OP_LE",
														simbolos.get(x).getSimbolo() + simbolos.get(y).getSimbolo());
											}
											token += "<" + "OP_LE" + "," + tbSimbolo.get("OP_LE") + ">";
											retornoToken.add(token);
											token = "";
											cont += 1;
											break;
										}

									}
								}

								x += cont;
							}
							break;
						case "+":
							if (tbSimbolo.get("OP_AD") != "+") {
								tbSimbolo.put("OP_AD", simbolos.get(x).getSimbolo());
							}
							token += "<" + "OP_AD" + "," + tbSimbolo.get("OP_AD") + ">";
							retornoToken.add(token);
							token = "";
							break;
						case "-":
							if (tbSimbolo.get("OP_MIN") != "-") {
								tbSimbolo.put("OP_MIN", simbolos.get(x).getSimbolo());
							}
							token += "<" + "OP_MIN" + "," + tbSimbolo.get("OP_MIN") + ">";
							retornoToken.add(token);
							token = "";
							break;
						case "*":
							if (tbSimbolo.get("OP_MUL") != "*") {
								tbSimbolo.put("OP_MUL", simbolos.get(x).getSimbolo());
							}
							token += "<" + "OP_MUL" + "," + tbSimbolo.get("OP_MUL") + ">";
							retornoToken.add(token);
							token = "";
							break;

						// Simbolos
						case "{":
							if (tbSimbolo.get("SMB_OBC") != "{") {
								tbSimbolo.put("SMB_OBC", simbolos.get(x).getSimbolo());
							}
							token += "<" + "SMB_OBC" + "," + tbSimbolo.get("SMB_OBC") + ">";
							retornoToken.add(token);
							token = "";

							break;
						case "}":
							if (tbSimbolo.get("SMB_CBC") != "}") {
								tbSimbolo.put("SMB_CBC", simbolos.get(x).getSimbolo());
							}
							token += "<" + "SMB_CBC" + "," + tbSimbolo.get("SMB_CBC") + ">";
							retornoToken.add(token);
							token = "";

							break;
						case "(":
							if (tbSimbolo.get("SMB_OPA") != "(") {
								tbSimbolo.put("SMB_OPA", simbolos.get(x).getSimbolo());
							}
							token += "<" + "SMB_OPA" + "," + tbSimbolo.get("SMB_OPA") + ">";
							retornoToken.add(token);
							token = "";

							break;
						case ")":
							if (tbSimbolo.get("SMB_CPA") != ")") {
								tbSimbolo.put("SMB_CPA", simbolos.get(x).getSimbolo());
							}
							token += "<" + "SMB_CPA" + "," + tbSimbolo.get("SMB_CPA") + ">";
							retornoToken.add(token);
							token = "";

							break;
						case ",":
							if (tbSimbolo.get("SMB_COM") != ",") {
								tbSimbolo.put("SMB_COM", simbolos.get(x).getSimbolo());
							}
							token += "<" + "SMB_COM" + "," + tbSimbolo.get("SMB_COM") + ">";
							retornoToken.add(token);
							token = "";

							break;
						case ";":
							if (tbSimbolo.get("SMB_SEM") != ";") {
								tbSimbolo.put("SMB_SEM", simbolos.get(x).getSimbolo());
							}
							token += "<" + "SMB_SEM" + "," + tbSimbolo.get("SMB_SEM") + ">";
							retornoToken.add(token);
							token = "";
							break;
						case "\"":
							if (x == simbolos.size() - 1) {
								erro += "Simbolo indevido na coluna " + x + " " + "Simbolo: "
										+ simbolos.get(x).getSimbolo();
							} else {
								texto = true;
							}

							break;
						default:

							if (!"\t".equals(simbolos.get(x).getSimbolo()) && !" ".equals(simbolos.get(x).getSimbolo())
									&& !"".equals(simbolos.get(x).getSimbolo())) {
								erro += " Erro identificado na coluna " + (x + 1) + " Simbolo: "
										+ simbolos.get(x).getSimbolo();
							}
							break;

						}
					} else {
						// caso seja comentário
						if ("*".equals(simbolos.get(x).getSimbolo())) {
							if (x < simbolos.size() - 1) {
								if ("/".equals(simbolos.get(x + 1).getSimbolo())) {
									comentario = false;
									coment = 0;
									x += 1;
								}
							}
						}
					}

					break;

				}
			} else {
				//Caso seja Char COnt
				literal += simbolos.get(x).getSimbolo();
				if(x==simbolos.size()-1&&!"\"".equals(simbolos.get(x).getSimbolo())) {
					erro = "Erro na coluna: " + x + ", aspas não foram fechadas";
				}
				if ("\"".equals(simbolos.get(x).getSimbolo())) {
					erro = "Erro na coluna: " + x + " Simbolo: " + simbolos.get(x).getSimbolo()
							+ ", não é permitido CharConst vazio!";
					texto = false;
				} else {
					for (int y = x + 1; y < simbolos.size(); y++) {
						cont += 1;

						if ("\"".equals(simbolos.get(y).getSimbolo())) {
							if (tbSimbolo.get("CHAR_CONST_" + literal) != ";") {
								tbSimbolo.put("CHAR_CONST_" + literal, literal);
							}
							token += "<" + "CHAR_CONST" + "," + tbSimbolo.get("CHAR_CONST_" + literal) + ">";
							retornoToken.add(token);
							token = "";
							texto = false;
							break;
						} else {
							literal += simbolos.get(y).getSimbolo();
							if (y == simbolos.size() - 1 && simbolos.get(y).getSimbolo() != "\"") {
								// Cheguei na ultima posição
								erro = "Erro na coluna: " + x + ", aspas não foram fechadas";
								texto = false;

							}
						}

					}
					x += cont;
				}
				texto = false;

			}

		}
		if (palavra != "") {

			if (tbSimbolo.containsKey("KW_" + palavra.toUpperCase())) {
				token += "<" + "KW" + "," + tbSimbolo.get("KW_" + palavra.toUpperCase()) + ">";
				retornoToken.add(token);
				token = "";
			} else {
				if (tbSimbolo.containsKey("ID_" + palavra.toUpperCase())) {
					token += "<" + "ID" + "," + tbSimbolo.get("ID_" + palavra.toUpperCase()) + ">";
					retornoToken.add(token);
					token = "";
				} else {
					tbSimbolo.put("ID_" + palavra.toUpperCase(), palavra);
					token += "<" + "ID" + "," + tbSimbolo.get("ID_" + palavra.toUpperCase()) + ">";
					retornoToken.add(token);
					token = "";
				}
			}
			palavra = "";
		}
		if (comentario == false) {
			resposta.setComentario("false");
		} else {
			resposta.setComentario("true");
		}
		if (texto == false) {
			resposta.setTexto("false");
		} else {
			resposta.setTexto("true");
		}
		resposta.setToken(retornoToken);
		resposta.setErro(erro);
		resposta.setColunaComent(coment);

		return resposta;
	}

	private static void imprimirToken(ArrayList<Resultado> resul) {
		resul.forEach((c) -> {
			if (c.getToken().size() != 0) {
				System.out.println("Linha: " + c.getLinha());
				System.out.print("Token: ");
				c.getToken().forEach((t) -> {
					System.out.print(t + " ");
				});
				System.out.println("");
				if (c.getErro() != "") {
					System.out.print("Erros: " + c.getErro());

				}
				System.out.println("\n");

			} else {
				if ((c.getToken().size() == 0) && (c.getErro() != "")) {
					System.out.println("Linha: " + c.getLinha());
					System.out.println("Erros: " + c.getErro());
					System.out.println("\n");
				}
			}

		});

	}
}
