package com.szfp.ss.retrofit.interfaces;

import java.util.Map;

/**
 * Created by 戴尔 on 2017/11/9.
 */

@FunctionalInterface
public interface HeadersInterceptor {
    Map checkHeaders(Map headers);
}