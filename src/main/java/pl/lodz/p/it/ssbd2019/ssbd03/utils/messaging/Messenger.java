package pl.lodz.p.it.ssbd2019.ssbd03.utils.messaging;

import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.MessageNotSentException;

/**
 * Uogólniony interfejs dla klas, które służą do wysyłania wiadomości.
 */
public interface Messenger {
    /**
     * Metoda odpowiedzialna za wysyłanie wiadomości.
     * @param message obiekt wiadomości.
     * @throws MessageNotSentException w przypadku, gdy nie udało się wysłać wiadomości.
     */
    void sendMessage(ClassicMessage message) throws MessageNotSentException;
}
