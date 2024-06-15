package utility;

import java.util.AbstractMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Package di utility, esterno al modello BCED, implementa delle funzioni di utilità.
 */
public final class Utilities {

    /**
     * Costruttore privato per impedire la creazione di istanze della classe.
     */
    private Utilities(){}

    /**
     * Funzione che utilizza dei regex e limiti dimensionali per validare gli ingressi forniti dall'utente.
     * @param nome il nome da validare.
     * @param cognome il cognome da validare.
     * @param email l'email da validare.
     * @param password la password da validare.
     * @param telefono il contatto telefonico da validare.
     * @param automobile l'automobile da validare.
     * @param postiDisponibili il numero dei posti disponibili da validare.
     * @return una tupla <code>(booleano, messaggio)</code>: il booleano contiene l'esito della verifica, il messaggio
     * contiene l'eventuale motivazione di fallimento.
     */
    public static AbstractMap.SimpleEntry<Boolean, String> validateDatiPersonali(String nome,
                                                                                 String cognome,
                                                                                 String email,
                                                                                 char[] password,
                                                                                 String telefono,
                                                                                 String automobile,
                                                                                 int postiDisponibili) {
        if (nome.isBlank() || cognome.isBlank() || email.isBlank() || password.length == 0 || telefono.isBlank()) {
            return new AbstractMap.SimpleEntry<>(false, "Riempi i campi obbligatori.");
        }

        if (!automobile.isBlank() && postiDisponibili == 0 || (automobile.isBlank() && postiDisponibili != 0)) {
            return new AbstractMap.SimpleEntry<>(false, "Il valore dei posti disponibili è incorretto.");
        }

        Pattern specialCharRegex = Pattern.compile("[^a-zA-Z]", Pattern.CASE_INSENSITIVE);
        Pattern numberRegex = Pattern.compile("[0-9]", Pattern.CASE_INSENSITIVE);
        Pattern emailRegex = Pattern.compile("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$", Pattern.CASE_INSENSITIVE);
        Pattern telRegex = Pattern.compile("^[+]?[0-9]*$", Pattern.CASE_INSENSITIVE);
        Matcher nomeMatcher = specialCharRegex.matcher(nome);
        Matcher nomeMatcherNumber = numberRegex.matcher(nome);
        Matcher cognomeMatcher = specialCharRegex.matcher(cognome);
        Matcher cognomeMatcherNumber = numberRegex.matcher(cognome);
        Matcher emailMatcher = emailRegex.matcher(email);
        Matcher telefonoMatcher = telRegex.matcher(telefono);


        if (nome.length() > 50) {
            return new AbstractMap.SimpleEntry<>(false, "Il nome supera la lunghezza massima di 50 caratteri.");
        }

        if (nomeMatcher.find() || nomeMatcherNumber.find()) {
            return new AbstractMap.SimpleEntry<>(false, "Il nome contiene caratteri non validi.");
        }

        if (cognome.length() > 50) {
            return new AbstractMap.SimpleEntry<>(false, "Il cognome supera la lunghezza massima di 50 caratteri.");
        }

        if (cognomeMatcher.find() || cognomeMatcherNumber.find()) {
            return new AbstractMap.SimpleEntry<>(false, "Il cognome contiene caratteri non validi.");
        }

        if (email.length() > 50) {
            return new AbstractMap.SimpleEntry<>(false, "L'email supera la lunghezza massima di 50 caratteri.");
        }

        if (!emailMatcher.find()) {
            return new AbstractMap.SimpleEntry<>(false, "L'email non è valida.");
        }

        if (telefono.length() > 15) {
            return new AbstractMap.SimpleEntry<>(false, "Il contatto telefonico supera la lunghezza massima di 15 caratteri.");
        }

        if (!telefonoMatcher.find()) {
            return new AbstractMap.SimpleEntry<>(false, "Il contatto telefonico contiene caratteri non validi.");
        }

        if (password.length > 50 || password.length < 4) {
            return new AbstractMap.SimpleEntry<>(false, "La password deve essere compresa tra 4 e 50 caratteri.");
        }

        if (automobile.length() > 50) {
            return new AbstractMap.SimpleEntry<>(false, "Il modello dell'automobile supera la lunghezza massima di 50 caratteri.");
        }

        if (postiDisponibili < 0) {
            return new AbstractMap.SimpleEntry<>(false, "Il numero di posti disponibili non può essere negativo.");
        }

        return new AbstractMap.SimpleEntry<>(true, "OK");
    }
}
