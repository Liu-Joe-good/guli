package com.atguigu.msm.service;

import java.util.HashMap;

public interface MsmService {
    boolean send(HashMap<String, String> sixBitRandom, String phone);
}
