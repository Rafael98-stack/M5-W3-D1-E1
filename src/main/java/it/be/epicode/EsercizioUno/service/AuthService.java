package it.be.epicode.EsercizioUno.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import it.be.epicode.EsercizioUno.entities.User;
import it.be.epicode.EsercizioUno.exceptions.UnauthorizedException;
import it.be.epicode.EsercizioUno.payloads.usersDto.UserLoginDTO;
import it.be.epicode.EsercizioUno.security.JWTTools;

@Service
public class AuthService {
    @Autowired
    private UsersService usersService;

    @Autowired
    private JWTTools jwtTools;

    public String authenticateUserAndGenerateToken(UserLoginDTO payload) {
        User user = usersService.findByEmail(payload.email());
        if (user.getPassword().equals(payload.password())) {

            return jwtTools.createToken(user);
        } else {
            throw new UnauthorizedException("Credenziali sbagliate!");
        }


    }
}
