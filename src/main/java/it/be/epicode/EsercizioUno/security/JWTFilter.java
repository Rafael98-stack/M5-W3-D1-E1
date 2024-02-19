package it.be.epicode.EsercizioUno.security;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import it.be.epicode.EsercizioUno.entities.User;
import it.be.epicode.EsercizioUno.exceptions.UnauthorizedException;
import it.be.epicode.EsercizioUno.service.UsersService;

import java.io.IOException;
import java.util.UUID;

@Component
public class JWTFilter extends OncePerRequestFilter {

    @Autowired
    private JWTTools jwtTools;

    @Autowired
    private UsersService usersService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Il codice di questo metodo verrà eseguito ad ogni richiesta che richieda di essere autenticati
        // Cose da fare:

        // 1. Verifichiamo se la richiesta effettivamente contiene un Authorization Header, se non c'è --> 401
        String authHeader = request.getHeader("Authorization"); // Bearer eyJhbGciOiJIUzM4NCJ9.eyJpYXQiOjE3MDgzNDAxMDgsImV4cCI6MTcwODQyNjUwOCwic3ViIjoiYzk2NGI0MGMtYjM5Yi00ODc2LWEwZGItYzQwOGI3OWQ5YTQ1In0.Kt8bZ4KdseMY9ZcKRyOpr3KDZieeLY78uE4xt4pkho4qJMn2wKVlmEQ7oENW1ptN
        if (authHeader == null || !authHeader.startsWith("Bearer "))
            throw new UnauthorizedException("Per favore metti il token nell'header");

        // 2. Se c'è estraiamo il token dall'header
        String accessToken = authHeader.substring(7);

        System.out.println("ACCESS TOKEN " + accessToken);

        // 3. Verifichiamo se il token è stato manipolato (verifica signature) e se non è scaduto (verifica Expiration Date)
        jwtTools.verifyToken(accessToken);

        // 4. Se è tutto OK proseguiamo con la catena fino ad arrivare all'endpoint

        // 4.1 Cerco l'utente nel DB (l'id sta nel token..)
        String id = jwtTools.extractIdFromToken(accessToken);
        User user = usersService.findById(UUID.fromString(id));

        // 4.2 Devo informare Spring Security che l'utente è autenticato (se non faccio questo step riceverò 403 come risposta)
        Authentication authentication = new UsernamePasswordAuthenticationToken(user, null);
        // Ci servirà domani per l'AUTORIZZAZIONE
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 4.3 Proseguo ora col prossimo elemento della catena
        filterChain.doFilter(request, response); // va al prossimo elemento della catena

        // 5. Se non è OK --> 401
    }

    // Disabilito il filtro per determinate richieste tipo Login o Register (non devono richiedere token)
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        // Uso questo metodo per specificare in che situazioni il filtro NON DEVE FILTRARE (non deve chiedere il token)
        return new AntPathMatcher().match("/auth/**", request.getServletPath());
        // Se l'URL della richiesta corrente corrisponde a /auth/qualsiasicosa, allora il filtro non entra in azione
    }
}