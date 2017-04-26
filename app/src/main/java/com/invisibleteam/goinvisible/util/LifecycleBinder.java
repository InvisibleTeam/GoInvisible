package com.invisibleteam.goinvisible.util;

import rx.Observable;

public interface LifecycleBinder {
    <T> Observable.Transformer<T, T> bind();
}
