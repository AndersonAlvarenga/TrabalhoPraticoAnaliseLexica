package Progran;

public class Simbolo {

	private String simbolo;
	private String tipo;


	public Simbolo(String simbolo, boolean verificador) {
		setSimbolo(simbolo,verificador);
		setTipo();

	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo() {
		switch (getSimbolo()) {

		// letras
		case "a":
			tipo = "letra";
			break;
		case "b":
			tipo = "letra";
			break;
		case "c":
			tipo = "letra";
			break;
		case "d":
			tipo = "letra";
			break;
		case "e":
			tipo = "letra";
			break;
		case "f":
			tipo = "letra";
			break;
		case "g":
			tipo = "letra";
			break;
		case "h":
			tipo = "letra";
			break;
		case "i":
			tipo = "letra";
			break;
		case "j":
			tipo = "letra";
			break;
		case "k":
			tipo = "letra";
			break;
		case "l":
			tipo = "letra";
			break;
		case "m":
			tipo = "letra";
			break;
		case "n":
			tipo = "letra";
			break;
		case "o":
			tipo = "letra";
			break;
		case "p":
			tipo = "letra";
			break;
		case "q":
			tipo = "letra";
			break;
		case "r":
			tipo = "letra";
			break;
		case "s":
			tipo = "letra";
			break;
		case "t":
			tipo = "letra";
			break;
		case "u":
			tipo = "letra";
			break;
		case "v":
			tipo = "letra";
			break;
		case "w":
			tipo = "letra";
			break;
		case "x":
			tipo = "letra";
			break;
		case "y":
			tipo = "letra";
			break;
		case "z":
			tipo = "letra";
			break;

		case "0":
			tipo = "num";
			break;
		case "1":
			tipo = "num";
			break;
		case "2":
			tipo = "num";
			break;
		case "3":
			tipo = "num";
			break;
		case "4":
			tipo = "num";
			break;
		case "5":
			tipo = "num";
			break;
		case "6":
			tipo = "num";
			break;
		case "7":
			tipo = "num";
			break;
		case "8":
			tipo = "num";
			break;
		case "9":
			tipo = "num";
			break;
		// ponto flutuante
		case ".":
			tipo = "ponto";
			break;

		default:
			tipo = "outro";
			break;
		}
	}

	public String getSimbolo() {
		return simbolo;
	}

	public void setSimbolo(String simbolo,boolean ver) {
		
		String letra = "";
		if (ver == true) {
			letra = simbolo;
		} else {
			switch (simbolo) {
			case "A":
				letra = "a";
				break;
			case "B":
				letra = "b";
				break;
			case "C":
				letra = "c";
				break;
			case "D":
				letra = "d";
				break;
			case "E":
				letra = "e";
				break;
			case "F":
				letra = "f";
				break;
			case "G":
				letra = "g";
				break;
			case "H":
				letra = "h";
				break;
			case "I":
				letra = "i";
				break;
			case "J":
				letra = "j";
				break;
			case "K":
				letra = "k";
				break;
			case "L":
				letra = "l";
				break;
			case "M":
				letra = "m";
				break;
			case "N":
				letra = "n";
				break;
			case "O":
				letra = "o";
				break;
			case "P":
				letra = "p";
				break;
			case "Q":
				letra = "q";
				break;
			case "R":
				letra = "r";
				break;
			case "S":
				letra = "s";
				break;
			case "T":
				letra = "t";
				break;
			case "U":
				letra = "u";
				break;
			case "V":
				letra = "v";
				break;
			case "W":
				letra = "w";
				break;
			case "X":
				letra = "x";
				break;
			case "Y":
				letra = "y";
				break;
			case "Z":
				letra = "z";
				break;
			default:
				letra = simbolo;
				break;

			}
			
		}
		this.simbolo = letra;
	}
}
