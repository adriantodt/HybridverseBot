package com.theorangehub.dmlbrowser;

import com.theorangehub.dml.DML;
import com.theorangehub.dml.DMLBuilder;

public abstract class DMLBrowser {

    protected abstract String navigate(String page);

    protected abstract DMLBuilder notFound(String page);

    protected abstract void sendMessage(DMLBuilder builder);

    protected void handle(String page) {
        String content = navigate(page);
        DMLBuilder builder = content == null ? notFound(page) : DML.parse(newBuilder(), content);
        sendMessage(builder);
    }

    protected DMLBuilder newBuilder() {
        return new DMLBuilder();
    }
}
