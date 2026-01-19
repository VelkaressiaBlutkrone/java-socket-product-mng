package client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dto.RequestDto;

public class MyClient {
    private static final Logger logger = LoggerFactory.getLogger(MyClient.class);

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
                        String reqStr = ClientService.reqDtoToJson("get", null, null);
                        out.println(reqStr);

                        ClientService.selectAll(in.readLine());
                    }
                        break;
                    case "2": {
                        System.out.print("아이디 입력(숫자) >> ");
                        String prdId = sc.nextLine();
                        if (!prdId.isEmpty()) {
                            try {
                                String reqStr = ClientService.reqDtoToJson("get",
                                        new RequestDto.QueryString(Integer.parseInt(prdId)),
                                        null);
                                out.println(reqStr);

                                ClientService.selectDetail(in.readLine());
                            } catch (NumberFormatException e) {
                                logger.atTrace().setCause(e).log("아이디가 숫자가 아닙니다.");
                            }
                        }
                    }
                        break;
                    case "3": {
                        try {
                            System.out.println("등록할 상품을 입력하세요. >>");
                            String name = sc.nextLine();
                            System.out.println("등록할 가격을 입력하세요. >>");
                            String price = sc.nextLine();
                            System.out.println("등록할 개수을 입력하세요. >>");
                            String qty = sc.nextLine();

                            String reqStr = ClientService.reqDtoToJson("post", null, RequestDto.Body.builder()
                                    .name(name)
                                    .price(Integer.parseInt(price))
                                    .qty(Integer.parseInt(qty))
                                    .build());

                            out.println(reqStr);

                            logger.atInfo().log("상품이 추가 되었습니다.");
                        } catch (Exception e) {
                            logger.atTrace().setCause(e).log("Exception occurred");
                        }

                    }
                        break;
                    case "4": {
                        System.out.println("삭제할 상품의 아이디를 입력하세요. >>");
                        String id = sc.nextLine();
                        String reqStr = ClientService.reqDtoToJson("delete",
                                RequestDto.QueryString.builder().id(Integer.parseInt(id)).build(),
                                null);

                        out.println(reqStr);

                        logger.atInfo().log("상품이 삭제 되었습니다.");
                    }
                        break;
                    default: {

                    }
                        break;
                }

            }

        } catch (Exception e) {
            logger.atError().setCause(e).log("예외 발생");
        }
    }

}
