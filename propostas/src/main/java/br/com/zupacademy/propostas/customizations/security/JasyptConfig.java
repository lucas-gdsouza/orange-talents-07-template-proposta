package br.com.zupacademy.propostas.customizations.security;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.exceptions.EncryptionOperationNotPossibleException;
import org.jasypt.iv.RandomIvGenerator;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class JasyptConfig {

    /**
     * JasyptConfig não é um componente do Spring, e nem precisa ser, pois não é injetado em nenhum Controller.
     * Por isso, não conseguimos utilizar a anotação @Value.
     * Sendo assim, não há valor padrão para a variável de ambiente. Por isso, ou definimos ela.
     * Ou deixamos o valor padrão hardcoded na classe.
     */
    private final String salt = getSalt();

    /**
     * @return Gera o hash. Aqui, não adicionamos nenhum salt randomizado.
     */
    public String gerarHash(String documentoAHashear) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.reset();

            String hashGerado = String.format("%0128x", new BigInteger(1, //  BigInteger é positivo.
                    md.digest((documentoAHashear + salt).getBytes(StandardCharsets.UTF_8))));

            return hashGerado;

        } catch (Exception ex) {
            throw new SecurityException("ERRO AO GERAR HASH");
        }
    }

    public String criptografar(String documentoACriptografar) {
        try {
            String documentoCriptografado = encryptor().encrypt(documentoACriptografar);
            return documentoCriptografado;
        } catch (EncryptionOperationNotPossibleException e) {
            throw new EncryptionOperationNotPossibleException("ERRO DE CRIPTOGRAFIA");
        }

    }

    public String descriptografar(String documentoADescriptografar) {
        try {
            String documentoDescriptografado = encryptor().decrypt(documentoADescriptografar);
            return documentoDescriptografado;
        } catch (EncryptionOperationNotPossibleException ex) {
            throw new EncryptionOperationNotPossibleException(
                    "O salt da criptografia deste registro é diferente do salt vigente");
        }
    }

    /**
     * @return criptografador configurado para ser usado nos métodos criptografar e descriptografar.
     */
    private StandardPBEStringEncryptor encryptor() {
        StandardPBEStringEncryptor textEncryptor = new StandardPBEStringEncryptor();
        textEncryptor.setPassword(salt);
        textEncryptor.setAlgorithm("PBEWithHMACSHA512AndAES_256");
        textEncryptor.setIvGenerator(new RandomIvGenerator());

        return textEncryptor;
    }

    private String getSalt() {
            return "rm'!@N=Ke!~p8VTA2ZRK~nMDQX5Uvm!m'D&]{@Vr?G;2?XhbC:Qa#9#eMLN}x3?JR3.2zr~v)gYF^8:8>:XfB:Ww75N/emt9Yj[bQMNCWwWJ?N,nvH.<2.r~w]*e~vgak)Xv8H`MH/72E`,^k@n<vE-wD3g9JWPy;CrY*.Kd2_D])=><D?YhBaSua5hW%{2]_FVXzb9`8FH^b[X3jzVER&:jw2<=c38=>L/zBq`}C6tT*cCSVC^c]-L}&/";
    }
}