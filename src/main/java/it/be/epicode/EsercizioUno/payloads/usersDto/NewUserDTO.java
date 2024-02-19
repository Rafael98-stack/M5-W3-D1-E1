package it.be.epicode.EsercizioUno.payloads.usersDto;

public record NewUserDTO
        (String name,
        String surname,
        String email,
        String password) {
}
