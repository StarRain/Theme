package com.ldfs.theme.theme.read;


import com.ldfs.theme.theme.bean.AttrType;
import com.ldfs.theme.theme.bean.Style;
import com.ldfs.theme.util.XmlParser;

import org.xmlpull.v1.XmlPullParser;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by cz on 15/8/9.
 */
@Config("theme/style.xml")
public class ThemeStyleReader extends AssetReader<String, Style> {
    @Override
    public XmlParser.ParserListener getParserListener(HashMap<String, Style> configs) {
        return new XmlParser.ParserListener() {
            private Pattern compile = Pattern.compile("(@\\+|@|\\?)(.+)/(.+)");
            private Style style;

            @Override
            public void startParser(XmlPullParser parser) {
                if ("style".equals(parser.getName())) {
                    style = new Style();
                    XmlParser.runParser(parser, (String... ts) -> {
                        if ("name".equals(ts[0])) {
                            style.name = ts[1];
                        } else if ("value".equals(ts[0])) {
                            style.value = Integer.valueOf(ts[1]);
                        }
                    });
                } else if ("attr".equals(parser.getName())) {
                    final Style.AttrItem item = new Style.AttrItem();
                    XmlParser.runParser(parser, (String... ts) -> {
                        if ("name".equals(ts[0])) {
                            String name = ts[1];
                            item.attr = name;
                            //标记资源类型
                            String[] array = name.split("_");
                            if (null != array && 0 < array.length) {
                                item.type = AttrType.valueOf(array[0]);
                            }
                        } else if ("value".equals(ts[0])) {
                            Matcher matcher = compile.matcher(ts[1]);
                            if (matcher.find()) {
                                int count = matcher.groupCount();
                                if (3 <= count) {
                                    item.value = matcher.group(3);
                                }
                            }
                        }
                    });
                    style.items.add(item);
                }
            }

            @Override
            public void endParser(XmlPullParser parser) {
                if ("style".equals(parser.getName())) {
                    configs.put(style.name, style);
                }
            }
        };
    }
}
