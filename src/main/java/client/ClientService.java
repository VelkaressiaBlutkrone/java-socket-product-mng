package client;

import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import dto.RequestDto;
import dto.ResponseDto;
import server.Product;

public class ClientService {
    public static void selectAll(String result) {
        ResponseDto dto = jsonToResDto(result);

        Type type = new TypeToken<List<Product>>() {}.getType();
        List<Product> list = new Gson().fromJson(dto.getBody(), type);

        System.out.printf("%-6s | %-10s | %-6s | %-6s%n",
                "아이디", "이름", "가격", "수량");
        System.out.println("------------------------------------");

        for (Product prd : list) {
            System.out.printf("%-6d | %-10s | %-6d | %-6d%n",
                    prd.getId(),
                    prd.getName(),
                    prd.getPrice(),
                    prd.getQty());
        }
    }

    public static void selectDetail(String result) {
        ResponseDto dto = jsonToResDto(result);

        Type type = new TypeToken<Product>() {
        }.getType();
        Product prd = new Gson().fromJson(dto.getBody(), type);
        System.out.println(
                prd.getId() + " | " +
                        prd.getName() + " | " +
                        prd.getPrice() + " | " +
                        prd.getQty());
    }

    public static String reqDtoToJson(String method, RequestDto.QueryString queryString, RequestDto.Body body) {
        RequestDto reqDto = RequestDto.builder()
                .method(method)
                .querystring(queryString)
                .body(body)
                .build();

        return new Gson().toJson(reqDto);
    }

    private static ResponseDto jsonToResDto(String json) {
        return new Gson().fromJson(json, ResponseDto.class);
    }
}
