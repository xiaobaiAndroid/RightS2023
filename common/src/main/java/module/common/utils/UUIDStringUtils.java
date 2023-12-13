package module.common.utils;

import java.util.UUID;

/**
 * @date： 2023/11/16
 * @author: 78495
*/
public class UUIDStringUtils {

    public static String randomUUID() {

        UUID uuid = UUID.randomUUID();
        return uuid.toString().replace("-", "").toUpperCase();
    }

}
