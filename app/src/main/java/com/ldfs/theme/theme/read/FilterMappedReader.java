package com.ldfs.theme.theme.read;


import com.ldfs.theme.theme.bean.DrawableFilterAttrItem;
import com.ldfs.theme.util.XmlParser;

import org.xmlpull.v1.XmlPullParser;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by cz on 15/8/9.
 * 布局映射解析对象
 */
@Config("theme/filter_mapped.xml")
public class FilterMappedReader extends AssetReader<String, DrawableFilterAttrItem> {

    @Override
    public XmlParser.ParserListener getParserListener(HashMap<String, DrawableFilterAttrItem> configs) {
        return new XmlParser.ParserListener() {
            private Pattern compile = Pattern.compile("(@\\+|@|\\?)(.+)/(.+)");
            private DrawableFilterAttrItem mapped;

            @Override
            public void startParser(XmlPullParser parser) {
                String name = parser.getName();
                if ("style".equals(name)) {
                    mapped = new DrawableFilterAttrItem();
                    XmlParser.runParser(parser, (String... ts) -> {
                        if ("name".equals(ts[0])) {
                            mapped.name = ts[1];
                        } else if ("value".equals(ts[0])) {
                            mapped.value = ts[1];
                        }
                    });
                } else if ("item".equals(name)) {
                    DrawableFilterAttrItem.FilterAttrItem item = new DrawableFilterAttrItem.FilterAttrItem();
                    mapped.items.add(item);
                    XmlParser.runParser(parser, (String... ts) -> {
                        if ("attr".equals(ts[0])) {
                            item.attr = ts[1];
                        } else if ("color".equals(ts[0])) {
                            Matcher matcher = compile.matcher(ts[1]);
                            if (matcher.find()) {
                                int count = matcher.groupCount();
                                if (3 <= count) {
                                    item.color = matcher.group(3);
                                }
                            }
                        }
                    });
                }
            }

            @Override
            public void endParser(XmlPullParser parser) {
                if ("style".equals(parser.getName())) {
                    configs.put(mapped.name, mapped);
                }
            }
        };
    }
}
