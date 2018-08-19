package com.longwei.umier.controller.wx;

import org.apache.commons.codec.digest.DigestUtils;

public class Test {
    public static void main(String[] args) {
        String s = "jsapi_ticket=kgt8ON7yVITDhtdwci0qefNc5jJaqdNRIeJXi1Pui-GEmGORUMxH-wV_jsVRR-IZEJ5o1qPdGc006Vy5R9glaA&noncestr=WKglLmvq&timestamp=1534673698539&url=http://exam.umier.cn";
        String sign = DigestUtils.sha1Hex(s);
        System.out.println(sign);
    }
}


