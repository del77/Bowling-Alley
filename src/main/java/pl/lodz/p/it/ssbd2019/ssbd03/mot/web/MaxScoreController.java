package pl.lodz.p.it.ssbd2019.ssbd03.mot.web;

import pl.lodz.p.it.ssbd2019.ssbd03.mot.service.AlleyService;
import pl.lodz.p.it.ssbd2019.ssbd03.mot.web.dto.AlleyMaxScoreDto;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.roles.MotRoles;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("alleys")
@RequestScoped
public class MaxScoreController {

    @EJB(beanName = "MOTAlleyService")
    private AlleyService alleyService;

    /**
     * Zwraca najlepszy wynik dla tory w postaci obektu JSON.
     *
     *   1. Użytkownik jest zalogowany na koncie o roli "Employee" lub "Client".
     *   2. Wyświetla listę torów.
     *   3. Z poziomu listy torów klika na przycisk "Menu" rozwijając menu kontekstowe.
     *   4. Wybiera opcję "Najlepszy wynik".
     *   5. Otwiera się okno typu modal z wynikiem.
     *
     * @param id Identyfikator toru
     * @return JSON String z informacjami dotyczącymi najlepszego wyniku dla toru.
     */
    @GET
    @RolesAllowed(MotRoles.GET_BEST_SCORE_FOR_ALLEY)
    @Path("{id}/best-score")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBestScoreForAlley(@PathParam("id") Long id) {
        Integer score;
        try {
            score = this.alleyService.getById(id).getMaxScore();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.serverError().build();
        }
        return Response.ok(new AlleyMaxScoreDto(score)).build();
    }

}
