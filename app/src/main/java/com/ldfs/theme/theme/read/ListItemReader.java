package com.ldfs.theme.theme.read;


import com.ldfs.theme.theme.bean.ListItem;
import com.ldfs.theme.theme.util.ResUtils;
import com.ldfs.theme.util.XmlParser;

import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by cz on 15/10/14.
 */
@Config("theme/list_layout.xml")
public class ListItemReader extends AssetReader<String, ArrayList<ListItem>> {

    @Override
    public XmlParser.ParserListener getParserListener(HashMap<String, ArrayList<ListItem>> configs) {
        return new XmlParser.ParserListener() {
            private ArrayList<ListItem> items;
            private String tabName;

            @Override
            public void startParser(XmlPullParser parser) {
                String name = parser.getName();
                if ("layout".equals(name)) {
                    items = new ArrayList<>();
                    XmlParser.runParser(parser, (String... ts) -> {
                        if ("name".equals(ts[0])) {
                            tabName = ts[1];
                        }
                    });
                } else if ("item".equals(name)) {
                    ListItem listItem = new ListItem();
                    XmlParser.runParser(parser, (String... ts) -> {
                        if ("id".equals(ts[0])) {
                            listItem.id = ResUtils.id(ts[1]);
                        } else if ("view".equals(ts[0])) {
                            listItem.name = ts[1];
                        }
                    });
                    items.add(listItem);
                }
            }

            @Override
            public void endParser(XmlPullParser parser) {
                if ("layout".equals(parser.getName())) {
                    configs.put(tabName, items);
                }
            }
        };
    }
}
