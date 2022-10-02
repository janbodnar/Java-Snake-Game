package walaniam.snake;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class GameParams {

    public static final int DEFAULT_SPEED = 140;
    public static final int DEFAULT_SNAKE_SIZE = 3;

    private Integer speed;
    private Integer snakeSize;
}
