package walaniam.snake;

import lombok.RequiredArgsConstructor;

import java.net.URL;
import java.util.Optional;

@RequiredArgsConstructor
public enum ImageResource {

    APPLE("/images/apple.png"),
    DOT("/images/dot.png"),
    HEAD("/images/head.png");

    private final String location;

    public URL getUrl() {
        return Optional
                .ofNullable(this.getClass().getResource(location))
                .orElseThrow(() -> new IllegalStateException("Image not found: " + location));
    }
}
