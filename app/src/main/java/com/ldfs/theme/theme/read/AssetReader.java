package com.ldfs.theme.theme.read;

import android.content.Context;

import com.ldfs.theme.App;
import com.ldfs.theme.util.IOUtils;
import com.ldfs.theme.util.XmlParser;

import org.xmlpull.v1.XmlPullParser;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

public abstract class AssetReader<K, T> {

    /**
     * 初始化资源
     */
    public HashMap<K, T> read() {
        InputStream inputStream = null;
        HashMap<K, T> configs = null;
        try {
            Context context = App.getAppContext();
            configs = new HashMap<>();
            Config config = getClass().getAnnotation(Config.class);
            if (null != config) {
                String[] values = config.value();
                if (0 < values.length) {
                    inputStream = context.getResources().getAssets().open(values[0]);
                    if (null != inputStream) {
                        XmlParser.ParserListener listener = getParserListener(configs);
                        if (null != listener) {
                            XmlParser.startParser(inputStream, listener);
                        } else {
                            throw new NullPointerException("listener 不能为空!");
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeStream(inputStream);
        }
        return configs;
    }

    /**
     * 解析attr
     *
     * @param parser
     * @param listener
     */
    protected void parserAttrs(XmlPullParser parser, XmlLayoutParserListener listener) {
        for (int i = 0; i < parser.getAttributeCount(); i++) {
            String namespace = parser.getAttributeNamespace(i);
            if (null != listener) {
                listener.parser(namespace, parser.getAttributeName(i), parser.getAttributeValue(i));
            }
        }
    }

    /**
     * 读取配置项
     *
     * @return
     */
    public abstract XmlParser.ParserListener getParserListener(HashMap<K, T> configs);

    public interface XmlLayoutParserListener {
        void parser(String nameSpace, String attrName, String attrValue);
    }
}
