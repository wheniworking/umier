package com.longwei.umier.utils;



import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.util.List;

public class ValidatorUtils {

    private ValidatorUtils() {
        throw new UnsupportedOperationException();
    }

    /**
     * 获取验证结果中的错误提示信息
     * @param result : 验证结果
     * @return String : 提示信息字符串
     */
    public static String buildErrorMessage(BindingResult result) {
        StringBuilder message = new StringBuilder();
        List<ObjectError> list = result.getAllErrors();
        if (list != null && !list.isEmpty()) {
            for (ObjectError elem : list) {
                String defaultMessage = elem.getDefaultMessage();
                if (!StringUtils.isEmpty(defaultMessage)) {
                    message.append(defaultMessage).append("  ");
                }
            }
        }
        return message.toString();
    }
}
