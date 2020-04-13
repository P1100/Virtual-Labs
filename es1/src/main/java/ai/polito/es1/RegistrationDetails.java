package ai.polito.es1;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

/**
 * "Si crei la classe RegistrationDetails che contiene i campi di RegistrationCommand (tranne la seconda
 * copia della password) e un campo di tipo Date chiamato registrationDate"
 *
 * ---> da specifica avrei dovuto mettere anche il campo privacy, ma non dovrebbe servire visto che il
 *      form non deve venire accettato senza consenso della privacy spuntato
 */
@Builder
@Value
public class RegistrationDetails {
    // NOTA: specifica chiedeva Date, ma dà warning consigliando LocalDateTime, che è stato mostrato anche a lezione
    LocalDateTime registrationDate;

    String name;
    String surname;
    String email;
    String password;
}
