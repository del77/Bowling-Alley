package pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.web.dto;

public enum AccessLevelDto {
    CLIENT ("CLIENT"),
    EMPLOYEE ("EMPLOYEE"),
    ADMIN ("ADMIN");

    private String string;

    AccessLevelDto(String string) {
        this.string = string;
    }

    @Override
    public String toString() {
        return string;
    }
}
