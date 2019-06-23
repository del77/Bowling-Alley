package pl.lodz.p.it.ssbd2019.ssbd03.exceptions.entity;

public class AlleyScoreConstraintViolationException extends DataAccessException {

        private static String code = "validate.alleyScoreConstraint";

        public AlleyScoreConstraintViolationException() {
            super();
        }

        public AlleyScoreConstraintViolationException(String message) {
            super(message);
        }

        public AlleyScoreConstraintViolationException(String message, Throwable cause) {
            super(message, cause);
        }

        public AlleyScoreConstraintViolationException(Throwable cause) {
            super(cause);
        }

        @Override
        public String getCode(){
            return code;
        }

}
