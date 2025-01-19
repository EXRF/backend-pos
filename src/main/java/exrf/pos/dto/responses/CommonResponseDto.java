package exrf.pos.dto.responses;

import lombok.*;
import org.springframework.data.domain.Page;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CommonResponseDto<T> {

    private T data;
    private Object message;
    private Metadata metadata = new Metadata();

    public CommonResponseDto(Class<T> clazz) {
        try {
            Constructor<T> constructor = clazz.getDeclaredConstructor();
            this.data = constructor.newInstance();
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    // Pagination inner class
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class Metadata {
        private int page;
        private int perPage;
        private long total;
        private int totalPages;
    }
}

