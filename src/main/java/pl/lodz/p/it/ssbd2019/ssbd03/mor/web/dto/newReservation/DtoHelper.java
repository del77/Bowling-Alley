package pl.lodz.p.it.ssbd2019.ssbd03.mor.web.dto.newReservation;

import java.util.ArrayList;
import java.util.List;

public class DtoHelper {
    public static void postProcess(ClientNewReservationDto dto) {
        List<Integer> shoesSize = dto.getShoesSize();
        List<Integer> shoesNumber = dto.getShoesNumber();
        if (shoesSize != null && shoesNumber != null) {
            List<ShoesDto> shoes = new ArrayList<>();
            for (int i=0; i < shoesSize.size(); i++) {
                shoes.add(new ShoesDto(shoesSize.get(i), shoesNumber.get(i)));
            }
            dto.setShoes(shoes);
        }

        List<Integer> ballSize = dto.getBallSize();
        List<Integer> ballNumber = dto.getBallNumber();
        if (ballSize != null && ballNumber != null) {
            List<BallsDto> balls = new ArrayList<>();
            for (int i=0; i < ballSize.size(); i++) {
                balls.add(new BallsDto(ballSize.get(i), ballNumber.get(i)));
            }
            dto.setBalls(balls);
        }
    }
}
