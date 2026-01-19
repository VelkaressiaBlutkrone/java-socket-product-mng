package client;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import dto.RequestDto;
import dto.ResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.Product;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;

public class MyClient {
    private static final Logger logger =
            LoggerFactory.getLogger(MyClient.class);

    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 20000);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             Scanner sc = new Scanner(System.in)) {

            logger.atInfo().log("====== 상품 관리 시스템 접속 ======");

            while (true) {
                System.out.println("\n1.목록조회 2.상세조회  3.추가  4.삭제  5.종료");
                System.out.print("번호입력 >> ");
                String menu = sc.nextLine();

                if (menu.equals("5")) {
                    socket.close();
                    logger.atInfo().log("====== 상품 관리 시스템 접속 종료 ======");
                    break;
                }

                switch (menu) {
                    case "1": {
                        String reqStr = reqDtoToJson("get", null, null);
                        out.println(reqStr);

                        selectAll(in.readLine());
                    }
                    break;
                    case "2": {
                        System.out.print("아이디 입력(숫자) >> ");
                        String prdId = sc.nextLine();
                        if (!prdId.isEmpty()) {
                            try {
                                String reqStr = reqDtoToJson("get"
                                        , new RequestDto.QueryString(Integer.parseInt(prdId))
                                        , null);
                                out.println(reqStr);

                                selectDetail(in.readLine());
                            } catch (NumberFormatException e) {
                                System.out.println("아이디가 숫자가 아닙니다.");
                            }
                        }
                    }
                    break;
                    case "3": {
                        try{
                            System.out.println("등록할 상품을 입력하세요. >>");
                            String name = sc.nextLine();
                            System.out.println("등록할 가격을 입력하세요. >>");
                            String price = sc.nextLine();
                            System.out.println("등록할 개수을 입력하세요. >>");
                            String qty = sc.nextLine();

                            String reqStr = reqDtoToJson("post", null, RequestDto.Body.builder()
                                    .name(name)
                                    .price(Integer.parseInt(price))
                                    .qty(Integer.parseInt(qty))
                                    .build());

                            out.println(reqStr);

                            System.out.println("상품이 추가 되었습니다.");
                        } catch (Exception e) {
                            logger.atTrace().setCause(e).log("Exception occurred");
                        }

                    }
                    break;
                    case "4": {
                        System.out.println("삭제할 상품의 아이디를 입력하세요. >>");
                        String id = sc.nextLine();
                        String reqStr = reqDtoToJson("delete",
                                RequestDto.QueryString.builder().id(Integer.parseInt(id)).build(),
                                null);

                        out.println(reqStr);

                        System.out.println("상품이 삭제 되었습니다.");
                    }
                    break;
                    default: {

                    }
                    break;
                }


            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void selectAll(String result) {
        ResponseDto dto = jsonToResDto(result);

        Type type = new TypeToken<List<Product>>() {
        }.getType();
        List<Product> list = new Gson().fromJson(dto.getBody(), type);

        System.out.printf("%-6s | %-10s | %-6s | %-6s%n",
                "아이디", "이름", "가격", "수량");
        System.out.println("------------------------------------");

        for (Product prd : list) {
            System.out.printf("%-6d | %-10s | %-6d | %-6d%n",
                    prd.getId(),
                    prd.getName(),
                    prd.getPrice(),
                    prd.getQty()
            );
        }
    }

    private static void selectDetail(String result) {
        ResponseDto dto = jsonToResDto(result);

        Type type = new TypeToken<Product>() {
        }.getType();
        Product prd = new Gson().fromJson(dto.getBody(), type);
        System.out.println(
                prd.getId() + " | " +
                        prd.getName() + " | " +
                        prd.getPrice() + " | " +
                        prd.getQty()
        );
    }

    private static String reqDtoToJson(String method, RequestDto.QueryString queryString, RequestDto.Body body) {
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
