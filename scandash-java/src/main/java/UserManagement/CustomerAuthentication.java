package UserManagement;

import CardManagement.Card;
import CardManagement.CardScanner;

public class CustomerAuthentication {

        // Method to fetch card details from the database
    public static Boolean cardFound(Card card) {
        return card!=null;
    }
    public static Boolean isCardActive (Card card) {
        return card.isActive();
    }

    public static Boolean authenticateCard(Card card, String cardID, String enteredPIN) {
        boolean isAuthenticated = card.authenticate(cardID, enteredPIN);
        if (isAuthenticated)
            CurrentCustomerSession.getInstance().setCurrentCard(card);
        return isAuthenticated;
    }
    public static Boolean isValidCardID(String cardID) {
        return cardID.length()==8;
    }

}
