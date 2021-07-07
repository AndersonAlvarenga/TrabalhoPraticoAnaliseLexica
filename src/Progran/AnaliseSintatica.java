package Progran;

import java.io.IOException;
import java.util.ArrayList;

public class AnaliseSintatica {
	private ArrayList<Resultado> resul;
	private ArrayList<String[]> token = new ArrayList<String[]>();
	private ArrayList<Integer> col = new ArrayList<Integer>();
	private ArrayList<Integer> linha = new ArrayList<Integer>();
	private int erros = 0;

	public AnaliseSintatica(ArrayList<Resultado> tokens) {
		this.resul = tokens;
		setToken(resul);
		
		analiseSintatica(this.token);

	}

//carrega os arrayslist de tokens vindos da analise lexica e tira as linhas em branco, deixando um token por posição
	private void setToken(ArrayList<Resultado> resultado) {
		resultado.forEach((t) -> {
			if (t.getToken().size() > 0) {
				t.getToken().forEach((x) -> {
					String s = x.substring(1, x.length() - 1);
					String[] p = new String[2];
					p = s.split(",");
					if (p.length == 1) {
						p = new String[2];
						p[0] = "SMB_COM";
						p[1] = ",";
					}
					
					this.linha.add(t.getLinha());
					this.token.add(p);
				});
				t.getColuna().forEach((c)->{
					this.col.add(c);
				});
			}
		});

	}

	// faz as verificação da analise sintatica
	private void analiseSintatica(ArrayList<String[]> token) {
		System.out.println();
		int index = 0;
		switch (token.get(index)[1]) {
		// verifica token program no inicio do texto
		case "program":
			index += 1;
			switch (token.get(index)[0]) {
			// verifica token ID no apos o token program
			case "ID":
				index += 1;
				body(index);

				break;
			default:
				System.out.println("Erro encontrado Linha: "+this.linha.get(index)+" Coluna: "+this.col.get(index));
				if(this.erros == 3) {
					exit();
				}else {
					this.erros+=1;
				}
			}
			break;
		default:
			System.out.println("Erro encontrado Linha: "+this.linha.get(index)+" Coluna: "+this.col.get(index));
			if(this.erros == 3) {
				exit();
			}else {
				this.erros+=1;
			}
		}
	}

	private int body(int posi) {
		// decl-list { stm-list }
		int index = posi;
		while (!"{".equals(token.get(index)[1])) {
			index = decliList(index);
		}
		if ("{".equals(token.get(index)[1])) {
			index += 1;
		} else {
			System.out.println("Erro encontrado Linha: "+this.linha.get(index)+" Coluna: "+this.col.get(index));
			if(this.erros == 3) {
				exit();
			}else {
				this.erros+=1;
			}
		}
		index = stmtList(index);
		if ("SMB_CBC".equals(token.get(index)[0])) {
			index += 1;
		} else {
			System.out.println("Erro encontrado Linha: "+this.linha.get(index)+" Coluna: "+this.col.get(index));
			if(this.erros == 3) {
				exit();
			}else {
				this.erros+=1;
			}
		}
		return index;
	}

//primeira parte 
	private int decliList(int posi) {
		int index = posi;
		// decl ; decl-list | vazio
		if ("{".equals(token.get(index)[1])) {
			// fazer voltar pro body
		} else {
			index = decl(index);
			if (";".equals(token.get(index)[1])) {
				index += 1;
			} else {
				System.out.println("Erro encontrado Linha: "+this.linha.get(index)+" Coluna: "+this.col.get(index));
				if(this.erros == 3) {
					exit();
				}else {
					this.erros+=1;
				}

			}
		}

		return index;
	}

	private int decl(int posi) {
		// type id_list
		int index = posi;
		if ("char".equals(token.get(index)[1]) || "num".equals(token.get(index)[1])) {
			index += 1;
			index = idList(index);
		} else {
			System.out.println("Erro encontrado Linha: "+this.linha.get(index)+" Coluna: "+this.col.get(index));
			if(this.erros == 3) {
				exit();
			}else {
				this.erros+=1;
			}
		}
		return index;
	}

	private int idList(int posi) {
		int index = posi;
		if ("ID".equals(token.get(index)[0])) {
			index += 1;
			index = idList2(index);

		} else {
			System.out.println("Erro encontrado Linha: "+this.linha.get(index)+" Coluna: "+this.col.get(index));
			if(this.erros == 3) {
				exit();
			}else {
				this.erros+=1;
			}
		}

		return index;
	}

	private int idList2(int posi) {
		int index = posi;
		if (",".equals(token.get(index)[1])) {
			index += 1;
			index = idList(index);
		}
		return index;
	}

//fim primeira parte ok

	/// implementar
	private int stmtList(int posi) {

		int index = posi;
		imprime(index);
		if (!"SMB_CBC".equals(token.get(index)[0])) {
			index = stmt(index);
			imprime(index);
			if ("SMB_SEM".equals(token.get(index)[0])) {
				index += 1;
			} else {
				System.out.println("Erro encontardo Linha: "+this.linha.get(index)+" Coluna: "+this.col.get(index));
				if(this.erros == 3) {
					exit();
				}else {
					this.erros+=1;
				}
				System.exit(0);
			}
			index = stmtList(index);
		}
		return index;
	}
	// ----------------------------------------

	private int factor(int posi) {
		int index = posi;
		imprime(index);
		if ("ID".equals(token.get(index)[0])) {
			index += 1;
		} else {
			if ("CHAR_CONST".equals(token.get(index)[0]) || "NUM_CONST".equals(token.get(index)[0])) {
				index = constant(index);
			} else {
				if ("SMB_OPA".equals(token.get(index)[0])) {
					index += 1;
					index = expressao(index);
					if ("SMB_CPA".equals(token.get(index)[0])) {
						index += 1;
					} else {
						System.out.println("Erro encontrado Linha: "+this.linha.get(index)+" Coluna: "+this.col.get(index));
						if(this.erros == 3) {
							exit();
						}else {
							this.erros+=1;
						}
					}
				} else {
					System.out.println("Erro encontrado Linha: "+this.linha.get(index)+" Coluna: "+this.col.get(index));
					if(this.erros == 3) {
						exit();
					}else {
						this.erros+=1;
					}
				}
			}
		}

		return index;
	}

	private int expressao(int posi) {
		int index = posi;
		imprime(index);
		index = simpleExpr(index);
		index = expression2(index);
		return index;
	}

	private int simpleExpr(int posi) {
		int index = posi;
		imprime(index);
		index = term(index);
		index = simpleExpr2(index);
		return index;
	}

	private int term(int posi) {
		int index = posi;
		imprime(index);
		index = factorB(index);
		index = term2(index);
		return index;
	}

	private int factorB(int posi) {
		int index = posi;
		imprime(index);
		index = factorA(index);
		index = factorB2(index);
		return index;
	}

	private int factorA(int posi) {
		int index = posi;
		imprime(index);
		if ("not".equals(token.get(index)[1])) {
			index += 1;
			index = factor(index);
		} else {
			index = factor(index);
		}
		return index;
	}

	private int factorB2(int posi) {
		int index = posi;
		imprime(index);
		if ("*".equals(token.get(index)[1]) || "/".equals(token.get(index)[1])) {
			index = mulop(index);
			index = factorA(index);
			index = factorB2(index);
		}
		return index;
	}

	private int term2(int posi) {
		int index = posi;
		imprime(index);
		if ("+".equals(token.get(index)[1]) || "-".equals(token.get(index)[1])) {
			index = addop(index);
			index = factorB(index);
			index = term2(index);
		}
		return index;
	}

	private int simpleExpr2(int posi) {
		int index = posi;
		imprime(index);
		if ("==".equals(token.get(index)[1]) || ">".equals(token.get(index)[1]) || ">=".equals(token.get(index)[1])
				|| "<".equals(token.get(index)[1]) || "<=".equals(token.get(index)[1])
				|| "!=".equals(token.get(index)[1])) {
			index = relop(index);
			index = term(index);
			index = simpleExpr2(index);
		}
		return index;
	}

	private int stmt(int posi) {
		int index = posi;
		imprime(index);
		if ("ID".equals(token.get(index)[0])) {
			index += 1;
			index = assingStmt(index);
		} else {

			switch (token.get(index)[1]) {
			case "if":
				index += 1;
				index = ifStmt(index);
				break;
			case "while":
				index += 1;
				index = whileStmt(index);
				break;
			case "read":
				index += 1;
				index = read(index);
				break;
			case "write":
				index += 1;
				index = write(index);
				break;
			default:
				System.out.println("Erro encontrados Linha: "+this.linha.get(index)+" Coluna: "+this.col.get(index));
				if(this.erros == 3) {
					exit();
				}else {
					this.erros+=1;
				}
				break;
			}
		}
		return index;
	}

	private int assingStmt(int posi) {
		int index = posi;
		imprime(index);
		if ("=".equals(token.get(index)[1])) {
			index += 1;
			index = simpleExpr(index);
		}
		return index;
	}

	private int ifStmt(int posi) {
		int index = posi;
		imprime(index);
		if ("SMB_OPA".equals(token.get(index)[0])) {
			index += 1;
			index = expressao(index);
			if ("SMB_CPA".equals(token.get(index)[0])) {
				index += 1;
			} else {
				System.out.println("Erro encontrado Linha: "+this.linha.get(index)+" Coluna: "+this.col.get(index));
				if(this.erros == 3) {
					exit();
				}else {
					this.erros+=1;
				}
			}
		} else {
			System.out.println("Erro encontrado Linha: "+this.linha.get(index)+" Coluna: "+this.col.get(index));
			if(this.erros == 3) {
				exit();
			}else {
				this.erros+=1;
			}
		}

		if ("SMB_OBC".equals(token.get(index)[0])) {
			index += 1;
			index = stmtList(index);
			if ("SMB_CBC".equals(token.get(index)[0])) {
				index += 1;
			} else {
				System.out.println("Erro encontrado Linha: "+this.linha.get(index)+" Coluna: "+this.col.get(index));
				if(this.erros == 3) {
					exit();
				}else {
					this.erros+=1;
				}
			}
		} else {
			System.out.println("Erro encontrado Linha: "+this.linha.get(index)+" Coluna: "+this.col.get(index));
			if(this.erros == 3) {
				exit();
			}else {
				this.erros+=1;
			}
		}
		index = ifStmt2(index);
		return index;
	}

	private int ifStmt2(int posi) {
		int index = posi;
		imprime(index);
		if ("else".equals(token.get(index)[1])) {
			index += 1;
			if ("SMB_OBC".equals(token.get(index)[0])) {
				index += 1;
				index = stmtList(index);
				if ("SMB_CBC".equals(token.get(index)[0])) {
					index += 1;
				} else {
					System.out.println("Erro encontrado Linha: "+this.linha.get(index)+" Coluna: "+this.col.get(index));
					if(this.erros == 3) {
						exit();
					}else {
						this.erros+=1;
					}
				}
			} else {
				System.out.println("Erro encontrado Linha: "+this.linha.get(index)+" Coluna: "+this.col.get(index));
				if(this.erros == 3) {
					exit();
				}else {
					this.erros+=1;
				}
			}
		}
		return index;
	}

	private int whileStmt(int posi) {
		int index = posi;
		imprime(index);
		index = stmtPrefix(index);
		if ("SMB_OBC".equals(token.get(index)[0])) {
			index += 1;
			index = stmtList(index);
			if ("SMB_CBC".equals(token.get(index)[0])) {
				index += 1;
			} else {
				System.out.println("Erro encontrado Linha: "+this.linha.get(index)+" Coluna: "+this.col.get(index));
				if(this.erros == 3) {
					exit();
				}else {
					this.erros+=1;
				}
			}
		} else {
			System.out.println("Erro encontrado Linha: "+this.linha.get(index)+" Coluna: "+this.col.get(index));
			if(this.erros == 3) {
				exit();
			}else {
				this.erros+=1;
			}
		}
		return index;
	}

	private int stmtPrefix(int posi) {
		int index = posi;
		imprime(index);
		if ("SMB_OPA".equals(token.get(index)[0])) {
			index += 1;
			index = expressao(index);
			if ("SMB_CPA".equals(token.get(index)[0])) {
				index += 1;
			} else {
				System.out.println("Erro encontrado Linha: "+this.linha.get(index)+" Coluna: "+this.col.get(index));
				if(this.erros == 3) {
					exit();
				}else {
					this.erros+=1;
				}
				System.exit(0);
				try {
					int c = System.in.read();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} else {
			System.out.println("Erro encontrado Linha: "+this.linha.get(index)+" Coluna: "+this.col.get(index));
			if(this.erros == 3) {
				exit();
			}else {
				this.erros+=1;
			}
		}
		return index;
	}

	private int read(int posi) {
		int index = posi;
		imprime(index);
		if ("ID".equals(token.get(index)[0])) {
			index += 1;
		} else {
			System.out.println("Erro encontrado Linha: "+this.linha.get(index)+" Coluna: "+this.col.get(index));
			if(this.erros == 3) {
				exit();
			}else {
				this.erros+=1;
			}
		}
		return index;
	}

	private int write(int posi) {
		int index = posi;
		imprime(index);
		index = simpleExpr(index);
		return index;
	}

	private int expression2(int posi) {
		int index = posi;
		imprime(index);
		if ("or".equals(token.get(index)[1]) || "and".equals(token.get(index)[1])) {
			index = logop(index);
			index = simpleExpr(index);
			index = expression2(index);
		}
		return index;
	}

	private int logop(int posi) {
		int index = posi;
		imprime(index);
		if ("or".equals(token.get(index)[1]) || "and".equals(token.get(index)[1])) {
			index += 1;
		} else {
			System.out.println("Erro encontrado");
		}
		return index;
	}

	private int relop(int posi) {
		int index = posi;
		imprime(index);
		if ("==".equals(token.get(index)[1]) || ">".equals(token.get(index)[1]) || ">=".equals(token.get(index)[1])
				|| "<".equals(token.get(index)[1]) || "<=".equals(token.get(index)[1])
				|| "!=".equals(token.get(index)[1])) {
			index += 1;
		} else {
			System.out.println("Erro encontrado Linha: "+this.linha.get(index)+" Coluna: "+this.col.get(index));
			if(this.erros == 3) {
				exit();
			}else {
				this.erros+=1;
			}
		}
		return index;
	}

	private int addop(int posi) {
		int index = posi;
		imprime(index);
		if ("+".equals(token.get(index)[1]) || "-".equals(token.get(index)[1])) {
			index += 1;
		} else {
			System.out.println("Erro encontrado Linha: "+this.linha.get(index)+" Coluna: "+this.col.get(index));
			if(this.erros == 3) {
				exit();
			}else {
				this.erros+=1;
			}
		}
		return index;
	}

	private int mulop(int posi) {
		int index = posi;
		imprime(index);
		if ("*".equals(token.get(index)[1]) || "/".equals(token.get(index)[1])) {
			index += 1;
		} else {
			System.out.println("Erro encontrado Linha: "+this.linha.get(index)+" Coluna: "+this.col.get(index));
			if(this.erros == 3) {
				exit();
			}else {
				this.erros+=1;
			}
		}
		return index;
	}

	private int constant(int posi) {
		int index = posi;
		imprime(index);
		if ("CHAR_CONST".equals(token.get(index)[0]) || "NUM_CONST".equals(token.get(index)[0])) {
			index += 1;
		} else {
			System.out.println("Erro encontrado Linha: "+this.linha.get(index)+" Coluna: "+this.col.get(index));
			if(this.erros == 3) {
				exit();
			}else {
				this.erros+=1;
			}
		}
		return index;
	}

	private void imprime(int id) {
		// System.out.println(token.get(id)[1]);
	}
	private void exit() {
		System.exit(0);
	}
}
