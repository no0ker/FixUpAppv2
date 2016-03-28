package orcsoft.todo.fixupappv2.Exceptions;

import java.util.HashMap;
import java.util.Map;

public class NetException extends Exception {
    private Integer errorCode;
    private String additionalInfo;
    private final Map<Integer, String> messages = new HashMap<Integer, String>() {{
        put(0, "Ok");
        put(400, "400 - Переданы некорректные данные");
        put(401, "401 - Требуется авторизация");
        put(500, "500 - Произошла ошибка на стороне сервера");
    }};

    public String getMessage() {
        StringBuilder sb = new StringBuilder();
        if (errorCode != null) {
            if (messages.containsKey(errorCode)) {
                sb.append(messages.get(errorCode));
            } else {
                sb.append("Error code: ");
                sb.append(errorCode);
            }
        } else {
            sb.append("Error code is Null.");
        }
        if (additionalInfo != null) {
            sb.append(" (");
            sb.append(this.additionalInfo);
            sb.append(").");
        }
        return sb.toString();
    }

    public NetException(Integer errorCode) {
        this(errorCode, null);
    }

    public NetException(Integer errorCode, String additionalInfo) {
        super();
        this.errorCode = errorCode;
        this.additionalInfo = additionalInfo;
    }
}
