package com.frozendo.study.entity;

import java.io.Serializable;

public record Product(String code,
                      String name,
                      String department) implements Serializable {
}
