package pl.lodz.p.it.ssbd2019.ssbd03.exceptions.entity;

public class AlleyNumberLessThanOneException extends DataAccessException {

        private static String code = "validate.alleyNumberConstraint";

        public AlleyNumberLessThanOneException() {
            super();
        }

        public AlleyNumberLessThanOneException(String message) {
            super(message);
        }

        public AlleyNumberLessThanOneException(String message, Throwable cause) {
            super(message, cause);
        }

        public AlleyNumberLessThanOneException(Throwable cause) {
            super(cause);
        }

        @Override
        public String getCode(){
            return code;
        }

}
