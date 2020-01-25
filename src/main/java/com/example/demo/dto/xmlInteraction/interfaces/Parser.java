package com.example.demo.dto.xmlInteraction.interfaces;

import java.util.List;

public interface Parser<T, T1> {

    T1 parse(List<T> t);
}
