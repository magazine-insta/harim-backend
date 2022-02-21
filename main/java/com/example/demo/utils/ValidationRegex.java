package com.example.demo.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidationRegex {
    public static boolean isRegexNickName(String target) {
        String regex = "^[A-Z0-9]{3,}$";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(target);
        return matcher.find();
    }


    public static boolean isRegexPassword(String target){

        String regex = "^[A-Za-z\\d~!@#$%^&*()+|=]{4,}$";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(target);
        
        return matcher.find();
    }

    //'숫자', '문자', '특수문자' 무조건 1개 이상, 비밀번호 '최소 8자에서 최대 16자'까지 허용
    // (특수문자는 정의된 특수문자만 사용 가능) ^(?=.*[A-Za-z])(?=.*\d)(?=.*[~!@#$%^&*()+|=])[A-Za-z\d~!@#$%^&*()+|=]{8,16}$
    // '숫자', '문자' 무조건 1개 이상, '최소 8자에서 최대 20자' 허용 //(특수문자는 정의된 특수문자만 사용 가능)
    // ^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d~!@#$%^&*()+|=]{8,20}$
}
