package entities;

import java.util.Map;
import java.util.UUID;
import java.util.HashMap;

public class MdpReset {

        private Map<String, String> tokenMap;

        public MdpReset() {
            tokenMap = new HashMap<>();
        }

        public String generateToken(String email) {
            String token = generateUniqueToken();
            tokenMap.put(token, email);
            return token;
        }

        public String getEmailForToken(String token) {
            return tokenMap.get(token);
        }



        public boolean isValidToken(String token) {
            return tokenMap.containsKey(token);
        }

        public void updatePassword(String token, String newPassword) {
            String email = tokenMap.get(token);
            // Update the password for the user with the given email address
            // You need to implement your own logic to update the password in your database
            updatePasswordInDatabase(email, newPassword);
            tokenMap.remove(token);
        }

        private void updatePasswordInDatabase(String email, String newPassword) {
            // Simulate updating the password in the database
            System.out.println("Updating password for email: " + email + " to: " + newPassword);
        }

        private String generateUniqueToken() {
            return UUID.randomUUID().toString();
        }
    }

