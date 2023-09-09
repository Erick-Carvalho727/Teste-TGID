package com.teste.tgid.util;

public class ValidacaoUtil {
	
	private static final int TAMANHO_CPF = 11;
    private static final int TAMANHO_CNPJ = 14;

    private static final int[] PESOS_CPF = { 11, 10, 9, 8, 7, 6, 5, 4, 3, 2 };
    private static final int[] PESOS_CNPJ = { 6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2 };
	
    public static boolean cnpjValido(final String cnpj) {
        if (cnpj == null) {
            return false;
        }
        if (cnpj.length() != TAMANHO_CNPJ) {
            return false;
        }
        if (cnpj.matches(cnpj.charAt(0) + "{" + TAMANHO_CNPJ + "}")) {
            return false;
        }
        String digitos = cnpj.substring(0, TAMANHO_CNPJ - 2);
        String verificador1 = verificador(digitos, PESOS_CNPJ);
        String verificador2 = verificador(digitos + verificador1, PESOS_CNPJ);
        return (digitos + verificador1 + verificador2).equals(cnpj);
    }
    
    public static boolean cpfValido(final String cpf) {
        if (cpf == null) {
            return false;
        }
        if (cpf.length() != TAMANHO_CPF) {
            return false;
        }
        if (cpf.matches(cpf.charAt(0) + "{" + TAMANHO_CPF + "}")) {
            return false;
        }
        String digitos = cpf.substring(0, TAMANHO_CPF - 2);
        String verificador1 = verificador(digitos, PESOS_CPF);
        String verificador2 = verificador(digitos + verificador1, PESOS_CPF);
        return (digitos + verificador1 + verificador2).equals(cpf);
    }
    
    private static String verificador(String digitos, int[] pesos) {
        int soma = 0;
        int qtdPesos = pesos.length;
        int qtdDigitos = digitos.length();
        for (int posicao = qtdDigitos - 1; posicao >= 0; posicao--) {
            int digito = Character.getNumericValue(digitos.charAt(posicao));
            soma += digito * pesos[qtdPesos - qtdDigitos + posicao];
        }
        soma = 11 - soma % 11;
        return String.valueOf(soma > 9 ? 0 : soma);
    }
}
