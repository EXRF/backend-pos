package exrf.pos.dto.responses;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

@Setter
@Getter
@ToString
public class CommonResponseDto<T> {

    private T data;
    private Object message;
    private Pagination pagination = new Pagination();

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
    @ToString
    public static class Pagination {
        private int page;
        private int perPage;
        private int total;
        private int totalPages;
    }
}

