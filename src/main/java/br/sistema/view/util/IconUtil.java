package br.sistema.view.util;
import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class IconUtil {

    public static Icon carregarIcone(String caminho, int largura, int altura){
        URL url = IconUtil.class.getResource(caminho);
        if (url == null) {
            throw new IllegalArgumentException(
                    "ICONE NAO ENCONTRADO: " + caminho
            );
        }
        ImageIcon iconOriginal = new ImageIcon(url);
        Image imagemRedimensionada =
                iconOriginal
                        .getImage()
                        .getScaledInstance(
                                largura,
                                altura,
                                Image.SCALE_SMOOTH
                        );


        return new ImageIcon(imagemRedimensionada);
    }
}
