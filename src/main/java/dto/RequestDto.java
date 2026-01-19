package dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestDto {
    private String method;
    private QueryString querystring;
    private Body body;

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class QueryString {
        private Integer id;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Body {
        private Integer price;
        private String name;
        private Integer qty;
    }

    @Override
    public String toString() {
        return "RequestDto{" +
                "method='" + method + '\'' +
                ", querystring=" + querystring +
                ", body=" + body +
                '}';
    }
}
