package exrf.pos.dto.responses;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

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

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }

    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }

    @Override
    public String toString() {
        return "CommonResponseDTO{" +
                "data=" + data +
                ", message=" + message +
                ", pagination=" + pagination +
                '}';
    }

    // Pagination inner class
    public static class Pagination {
        private int page;
        private int perPage;
        private int total;
        private int totalPages;

        public int getPage() {
            return page;
        }

        public void setPage(int page) {
            this.page = page;
        }

        public int getPerPage() {
            return perPage;
        }

        public void setPerPage(int perPage) {
            this.perPage = perPage;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public int getTotalPages() {
            return totalPages;
        }

        public void setTotalPages(int totalPages) {
            this.totalPages = totalPages;
        }

        @Override
        public String toString() {
            return "Pagination{" +
                    "page=" + page +
                    ", perPage=" + perPage +
                    ", total=" + total +
                    ", totalPages=" + totalPages +
                    '}';
        }
    }
}

