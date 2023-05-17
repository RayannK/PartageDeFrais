/* DataHelper.java                                                16 mai 2023
 * IUT Rodez, no copyright
 */
package com.example.partagedefrais.helper;

import android.widget.EditText;

/**
 * classe contenant des methodes d'aides
 * @author rayann.karon
 */
public class DataHelper {
    /**
     * extrait une chaine de caractère d'un editText
     * @param edit EditText à extraire
     * @return la chaine de caratère extraite
     */
    public static String getString(EditText edit) {
        return edit.getText().toString();
    }

    /**
     * extrait un int d'un editText
     * @param edit EditText à extraire
     * @return l'int extrait
     * @throws NumberFormatException si la string ne contient pas d'entier à parser
     */
    public static int getInt(EditText edit) throws NumberFormatException{
        String strValue = getString(edit);
        if (isStringEmptyOrNull(strValue)) {
            return 0;
        }
        return Integer.parseInt(strValue);
    }

    /**
     * Vérifie si une chaine de charactère est vide ou null
     * @param toCheck chaine de charactère a analyser
     * @return true si vide ou null sinon faux
     */
    public static boolean isStringEmptyOrNull(String toCheck) {
        return toCheck == null || toCheck.isEmpty() || toCheck.trim().isEmpty();
    }
}
