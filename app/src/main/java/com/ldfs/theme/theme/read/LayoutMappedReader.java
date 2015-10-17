package com.ldfs.theme.theme.read;


import com.ldfs.theme.theme.bean.LayoutMapped;
import com.ldfs.theme.theme.util.ResUtils;
import com.ldfs.theme.util.XmlParser;

import org.xmlpull.v1.XmlPullParser;

import java.util.HashMap;


/**
 * Created by cz on 15/8/9.
 * 布局映射解析对象
 */
@Config("theme/mapped.xml")
public class LayoutMappedReader extends AssetReader<String, LayoutMapped> {

    @Override
    public XmlParser.ParserListener getParserListener(HashMap<String, LayoutMapped> configs) {
        return new XmlParser.ParserListener() {
            private LayoutMapped mapped;

            @Override
            public void startParser(XmlPullParser parser) {
                String name = parser.getName();
                if ("layout".equals(name)) {
                    mapped = new LayoutMapped();
                    XmlParser.runParser(parser, (String... ts) -> {
                        if ("name".equals(ts[0])) {
                            mapped.name = ts[1];
                        } else if ("enable".equals(ts[0])) {
                            mapped.enable = Boolean.valueOf(ts[1]);
                        }
                    });
                } else if ("item".equals(name)) {
                    final LayoutMapped.Item item = new LayoutMapped.Item();
                    XmlParser.runParser(parser, (String... ts) -> {
                        if ("name".equals(ts[0])) {
                            item.layout = ts[1];
                        } else if ("id".equals(ts[0])) {
                            item.id = ResUtils.id(ts[1]);
                        }
                    });
                    mapped.layout.add(item);
                }
            }

            @Override
            public void endParser(XmlPullParser parser) {
                if ("layout".equals(parser.getName())) {
                    configs.put(mapped.name, mapped);
                }
            }
        };
    }
}
