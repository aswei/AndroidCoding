package cn.com.coding.androidcodinglibrary.utils.base;

import java.util.Collection;
import java.util.Map;

/**
 * Created by
 * 作者：luoluo on 2017-2-23.
 *
 * 判断是否为空的操作
 */

public class CheckUtils {

    public static boolean isEmpty(CharSequence str) {
        return isNull(str) || str.length() == 0;
    }

    public static boolean isEmpty(Object[] obj) {
        return isNull(obj) || obj.length == 0;
    }

    public static boolean isEmpty(Collection<?> list) {
        return isNull(list) || list.isEmpty();
    }

    public static boolean isEmpty(Map<?, ?> map) {
        return isNull(map) || map.isEmpty();
    }

    public static boolean isNull(Object o) {
        return o == null;
    }

}
