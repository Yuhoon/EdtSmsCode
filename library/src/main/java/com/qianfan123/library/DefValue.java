package com.qianfan123.library;

/**
 * Created by wangcong on 2017/2/14.
 */

enum DefValue {
    TXT_SIZE(16),
    TXT_COLOR(R.color.light_black),
    SIZE(40),
    MAX_LEN(6);

    int value;

    DefValue(int value) {
        this.value = value;
    }
}
