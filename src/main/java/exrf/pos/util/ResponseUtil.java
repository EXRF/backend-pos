package exrf.pos.util;

import exrf.pos.dto.responses.CommonResponseDto;

import java.util.List;

public class ResponseUtil {

    public static <T> CommonResponseDto<T> responseSuccess(Class<T> clazz, String message) {
        CommonResponseDto<T> response = new CommonResponseDto<>(clazz);
        response.setMessage(message);
        return response;
    }

    public static <T> CommonResponseDto<T> responseSuccess(Class<T> clazz, T data, String message) {
        CommonResponseDto<T> response = new CommonResponseDto<>(clazz);
        response.setData(data);
        response.setMessage(message);
        return response;
    }

    public static <T> CommonResponseDto<T> responseSuccess(Class<T> clazz, T data, String message, CommonResponseDto.Pagination pagination) {
        CommonResponseDto<T> response = new CommonResponseDto<>(clazz);
        response.setData(data);
        response.setMessage(message);
        response.setPagination(pagination);
        return response;
    }

    public static <T> CommonResponseDto<T> responseError(Class<T> clazz, List<String> message) {
        CommonResponseDto<T> response = new CommonResponseDto<>(clazz);
        response.setMessage(message.getFirst());
        return response;
    }

    public static <T> CommonResponseDto<T> responseError(Class<T> clazz, String message) {
        CommonResponseDto<T> response = new CommonResponseDto<>(clazz);
        response.setMessage(message);
        return response;
    }
}
