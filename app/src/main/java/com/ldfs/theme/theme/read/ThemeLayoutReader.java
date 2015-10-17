package com.ldfs.theme.theme.read;

import android.text.TextUtils;

import com.ldfs.theme.theme.bean.ResType;
import com.ldfs.theme.theme.bean.StyleElement;
import com.ldfs.theme.util.XmlParser;

import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by cz on 15/8/9.
 */
@Config("theme/layout.xml")
public class ThemeLayoutReader extends AssetReader<String, ArrayList<StyleElement>> {
    @Override
    public XmlParser.ParserListener getParserListener(HashMap<String, ArrayList<StyleElement>> configs) {
        return new XmlParser.ParserListener() {
            private ArrayList<StyleElement> styleElement;
            private String name;

            @Override
            public void startParser(XmlPullParser parser) {
                if ("layout".equals(parser.getName())) {
                    styleElement = new ArrayList<>();
                    XmlParser.runParser(parser, (String... ts) -> {
                        if ("name".equals(ts[0])) {
                            name = ts[1];
                        }
                    });
                } else if ("view".equals(parser.getName())) {
                    final StyleElement item = new StyleElement();
                    XmlParser.runParser(parser, (String... ts) -> {
                        if ("id".equals(ts[0])) {
                            item.id = ts[1];
                        } else if ("attr".equals(ts[0])) {
                            item.attrs = ts[1];
                        } else if ("type".equals(ts[0])) {
                            item.types = ts[1];
                        }
                    });
                    //初始化引用操作item
                    if (!TextUtils.isEmpty(item.attrs) && !TextUtils.isEmpty(item.types)) {
                        String[] attrsArray = item.attrs.split("\\|");
                        String[] typesArray = item.types.split("\\|");
                        if (null != attrsArray && null != typesArray && attrsArray.length == typesArray.length) {
                            int size = attrsArray.length;
                            //attr->drawable_account_subscribe_selector|drawable_white_text_selector
                            //types->setBackgroundResourceFilter|topDrawable|setTextColor
                            for (int i = 0; i < size; i++) {
                                StyleElement.Element element = new StyleElement.Element();
                                String attr = attrsArray[i];
                                String attrValue = typesArray[i];
                                element.resType = attr.substring(attr.indexOf("_") + 1);
                                element.type = ResType.getType(attrValue);
                                element.value = attr;
                                item.items.add(element);
                            }
                        }
                    }
                    styleElement.add(item);
                }
            }

            @Override
            public void endParser(XmlPullParser parser) {
                if ("layout".equals(parser.getName())) {
                    configs.put(name, styleElement);
                }
            }
        };
    }
}
