package server;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

import dto.RequestDto;
import dto.ResponseDto;

public class MyServer {
    private static final Logger logger = LoggerFactory.getLogger(MyServer.class);

    public static void main(String[] args) {

        try (ServerSocket serverSocket = new ServerSocket(20000)) {
            logger.atInfo().log("[Server] 상품 관리 서버 시작 (Port: 20000)");

            while (true) {
                Socket socket = serverSocket.accept();
                logger.atInfo().log("[Server] 클라이언트 접속: " + socket.getInetAddress());

                handler(socket);
            }
        } catch (Exception e) {
            logger.atTrace().setCause(e).log(e.getMessage());
        }
    }

    private static void handler(Socket socket) {
        try (Scanner in = new Scanner(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
            ResponseDto resDto = null;

            while (in.hasNextLine()) {
                String line = in.nextLine();
                logger.atInfo().log(line);

                RequestDto reqDto = jsonToRequestDto(line);

                if (reqDto.getMethod().equals("get")) {
                    if (reqDto.getQuerystring() == null) {
                        resDto = selectAll();
                    } else {
                        resDto = select(reqDto);
                    }
                } else if (reqDto.getMethod().equals("delete")) {
                    if (reqDto.getQuerystring() != null) {
                        resDto = delete(reqDto);
                    } else {

                    }
                } else if (reqDto.getMethod().equals("post")) {
                    if (reqDto.getBody() != null) {
                        resDto = insert(reqDto);
                    } else {

                    }
                } else {

                }

                String res = responseDtoToJson(resDto);
                out.println(res);
            }
        } catch (IOException e) {
            logger.atTrace().setCause(e).log(e.getMessage());
        }
    }

    private static ResponseDto insert(RequestDto dto) {
        try {
            ProductService service = new ProductService();
            service.ProductAdd(dto.getBody().getName(), dto.getBody().getPrice(), dto.getBody().getQty());
            return ResponseDto.builder()
                    .msg("ok")
                    .body(null)
                    .build();
        } catch (RuntimeException e) {
            logger.atTrace().setCause(e).log(e.getMessage());
        }

        return ResponseDto.builder()
                .msg("error")
                .body(null)
                .build();
    }

    private static ResponseDto select(RequestDto dto) {
        ProductService service = new ProductService();
        Product prod = service.ProductDetail(dto.getQuerystring().getId());
        String body = new Gson().toJson(prod);
        return ResponseDto.builder()
                .msg("ok")
                .body(body)
                .build();
    }

    private static ResponseDto selectAll() {
        try {
            ProductService service = new ProductService();
            List<Product> list = service.ProductList();
            String array = new Gson().toJson(list);

            return ResponseDto.builder()
                    .msg("ok")
                    .body(array)
                    .build();
        } catch (RuntimeException e) {
            logger.atTrace().setCause(e).log(e.getMessage());
        }

        return ResponseDto.builder()
                .msg("error")
                .body(null)
                .build();
    }

    private static ResponseDto delete(RequestDto dto) {
        ProductService service = new ProductService();
        service.ProductDelete(dto.getQuerystring().getId());
        return ResponseDto.builder()
                .msg("ok")
                .body("")
                .build();
    }

    private static RequestDto jsonToRequestDto(String json) {
        return new Gson().fromJson(json, RequestDto.class);
    }

    private static String responseDtoToJson(ResponseDto dto) {
        return new Gson().toJson(dto);
    }
}
